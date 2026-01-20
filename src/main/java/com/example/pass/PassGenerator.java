package com.example.pass;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PassGenerator {

    public static void main(String[] args) throws Exception {
        Path passDir = Paths.get("pass");

        ManifestGenerator.createManifest(passDir);

        PK7Signer.generateSign();

        ZipUtil.zipPass(passDir, Paths.get("card.pkpass"));

    }
}