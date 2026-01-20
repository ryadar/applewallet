package com.example.pass;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class ExtractAppleWWDRCAG3 {

    public static void main(String[] args) throws Exception {

        String p12File = ".p12";
        char[] password = "".toCharArray();

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(p12File), password);

        X509Certificate appleWWDR = null;

        Enumeration<String> aliases = ks.aliases();
        while (aliases.hasMoreElements()) {

            String alias = aliases.nextElement();

            // We care about private-key entries (they have chains)
            if (!ks.isKeyEntry(alias)) {
                continue;
            }

            var chain = ks.getCertificateChain(alias);
            if (chain == null) {
                continue;
            }

            for (var cert : chain) {
                X509Certificate x509 = (X509Certificate) cert;

                String subject = x509.getSubjectX500Principal().getName();

                if (subject.contains("Apple Worldwide Developer Relations")) {
                    appleWWDR = x509;
                    break;
                }
            }
        }

        if (appleWWDR == null) {
            throw new RuntimeException("AppleWWDRCAG3 certificate NOT found in p12");
        }

        // ✅ Successfully extracted
        System.out.println("AppleWWDRCAG3 FOUND");
        System.out.println("Subject : " + appleWWDR.getSubjectX500Principal());
        System.out.println("Issuer  : " + appleWWDR.getIssuerX500Principal());
        System.out.println("Serial  : " + appleWWDR.getSerialNumber());
        System.out.println("Valid   : " +
                appleWWDR.getNotBefore() + " → " + appleWWDR.getNotAfter());
    }
}

