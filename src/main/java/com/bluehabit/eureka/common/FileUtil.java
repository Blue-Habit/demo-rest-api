/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

public class FileUtil {
    private static final String folderUpload = "uploads";
    private static final String folderProfile = "uploads/profile";

    public static Optional<String> saveFile(MultipartFile multipartFile, String fileName) {
        final Path uploadPath = Paths.get(folderUpload);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            final Optional<String> ext = org.assertj.core.util.Files
                .getFileNameExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            final String finalFileName = fileName + "." + ext.get();
            final Path filePath = uploadPath.resolve(finalFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return Optional.of(finalFileName);
        } catch (IOException ioe) {
            return Optional.empty();
        }
    }

    public static Optional<String> saveProfilePicture(MultipartFile multipartFile, String fileName) {
        final Path uploadPath = Paths.get(folderProfile);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            final Optional<String> ext = org.assertj.core.util.Files
                .getFileNameExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            final String finalFileName = fileName + "." + ext.get();
            final Path filePath = uploadPath.resolve(finalFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return Optional.of(finalFileName);
        } catch (IOException ioe) {
            return Optional.empty();
        }
    }
}
