package com.example.pass;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PassGenerator {

    public static void main(String[] args) throws Exception {
        Path passDir = Paths.get("pass");

        ManifestGenerator.createManifest(passDir);

        SignatureGenerator.createSignature(
                passDir,
                new File("certs/pass.p12"),
                "liya@2020",
                new File("certs/AppleWWDRCAG3.pem")
        );

        ZipUtil.zipPass(passDir, Paths.get("card.pkpass"));

    }
}