package com.example.pass;

import org.bouncycastle.cms.*;
import org.bouncycastle.cert.jcajce.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class SignatureGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void createSignature(
            Path passDir,
            File p12File,
            String p12Password,
            File wwdrCertFile
    ) throws Exception {

        // Load manifest
        byte[] manifestData = Files.readAllBytes(passDir.resolve("manifest.json"));

        // Load keystore
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(p12File), p12Password.toCharArray());

        String alias = ks.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, p12Password.toCharArray());
        X509Certificate signingCert = (X509Certificate) ks.getCertificate(alias);

        // Load WWDR certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate wwdrCert =
                (X509Certificate) cf.generateCertificate(new FileInputStream(wwdrCertFile));

        List<X509Certificate> certList = List.of(signingCert, wwdrCert);
        Store<?> certStore = new JcaCertStore(certList);

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA")
                .setProvider("BC")
                .build(privateKey);

        generator.addSignerInfoGenerator(
                new JcaSignerInfoGeneratorBuilder(
                        new JcaDigestCalculatorProviderBuilder()
                                .setProvider("BC")
                                .build())
                        .setSignedAttributeGenerator(
                                new DefaultSignedAttributeTableGenerator())
                        .build(signer, signingCert)
        );

        generator.addCertificates(certStore);

        CMSTypedData cmsData = new CMSProcessableByteArray(manifestData);

        // Detached signature
        CMSSignedData signedData = generator.generate(cmsData, false);

        Files.write(passDir.resolve("signature"), signedData.getEncoded());
    }
}

