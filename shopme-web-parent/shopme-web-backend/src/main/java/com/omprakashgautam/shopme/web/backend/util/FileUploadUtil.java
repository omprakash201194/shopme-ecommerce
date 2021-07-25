package com.omprakashgautam.shopme.web.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author omprakash gautam
 * Created on 14-Jul-21 at 8:49 PM.
 */
@Slf4j
public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex){
            throw new IOException("Could not save file:" + fileName, ex);
        }
    }

    public static void cleanDirectory(String dir){
        Path dirPath = Paths.get(dir);
        try {
            Files.list(dirPath).filter(file -> !Files.isDirectory(file)).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    log.error("Could not delete file:" + file);
                }
            });
        } catch (IOException e) {
            log.error("Could not list directory:" + dirPath);
        }
    }

    public static void removeDir(String directory) {
        cleanDirectory(directory);
        try {
            Files.delete(Paths.get(directory));
        } catch (IOException e) {
            log.error("Could not remove directory: " + directory);
        }
    }
}
