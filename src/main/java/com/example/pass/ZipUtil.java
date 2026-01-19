package com.example.pass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.*;

public class ZipUtil {

    public static void zipPass(Path sourceDir, Path outputZip) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(outputZip))) {
            Files.walk(sourceDir).filter(Files::isRegularFile).forEach(path -> {
                ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                try {
                    zs.putNextEntry(zipEntry);
                    Files.copy(path, zs);
                    zs.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}

