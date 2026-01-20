package com.example.pass;

import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

public class PK7Signer {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void generateSign() throws Exception {
        byte[] manifest = Files.readAllBytes(new File("pass/manifest.json").toPath());

        X509Certificate passCert = loadCert("certs/passcertificate.pem");
        PrivateKey key = loadPrivateKey("certs/passkey.pem");
        X509Certificate wwdrCert = loadCert("certs/AppleWWDRCAG3.pem");

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

        ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA")
                .setProvider("BC")
                .build(key);

        gen.addSignerInfoGenerator(
                new org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder(
                        new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()
                ).build(sha1Signer, passCert)
        );

        gen.addCertificates(
                new JcaCertStore(Arrays.asList(passCert, wwdrCert))
        );

        CMSProcessableByteArray content = new CMSProcessableByteArray(manifest);

        // 'false' => Detached signature
        CMSSignedData signedData = gen.generate(content, false);

        Files.write(new File("pass/signature").toPath(), signedData.getEncoded());

        System.out.println("PKCS#7 signature created as 'signature' file");
    }

    static X509Certificate loadCert(String file) throws Exception {
        return (X509Certificate) CertificateFactory.getInstance("X.509")
                .generateCertificate(Files.newInputStream(new File(file).toPath()));
    }

    static PrivateKey loadPrivateKey(String file) throws Exception {
        byte[] bytes = Files.readAllBytes(new File(file).toPath());
        String pem = new String(bytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] decoded = java.util.Base64.getDecoder().decode(pem);
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }
}

