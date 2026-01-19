package com.example.pass;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManifestGenerator {

    public static void createManifest(Path passDir) throws Exception {
        Map<String, String> manifest = new LinkedHashMap<>();

        Files.walk(passDir)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    String fileName = path.getFileName().toString();
                    if (fileName.equals("manifest.json") || fileName.equals("signature")) {
                        return;
                    }
                    try {
                        byte[] data = Files.readAllBytes(path);
                        String sha1 = sha1Hex(data);
                        String relativePath = passDir.relativize(path).toString().replace("\\", "/");
                        manifest.put(relativePath, sha1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        ObjectMapper mapper = new ObjectMapper();
        File manifestFile = passDir.resolve("manifest.json").toFile();
        mapper.writeValue(manifestFile, manifest);
    }

    private static String sha1Hex(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
