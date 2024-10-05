package com.project.pastebinsimple.feature.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.project.pastebinsimple.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${cloud.aws.bucket}")
    private String bucket;

    private final AmazonS3 s3Client;

    public String uploadText(String text) {
        File file = createTempFileFromString(text);
        String fileName = UUID.randomUUID().toString() + ".txt"; // Используем UUID для уникальности
        try {
            s3Client.putObject(bucket, fileName, file);
        } catch (Exception e) {
            throw new StorageException("Ошибка при загрузке файла в S3", e);
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
        return fileName;
    }

    public CompletableFuture<String> uploadFileAsync(String text) {
        File file = createTempFileFromString(text);
        String fileName = System.currentTimeMillis() + "_" + file.getName();

        return CompletableFuture.supplyAsync(() -> {
            try {
                s3Client.putObject(bucket, fileName, file);
            } catch (Exception e) {
                throw new StorageException("Ошибка при загрузке файла в S3 асинхронно", e);
            } finally {
                if (file.exists()) {
                    file.delete();
                }
            }
            return fileName;
        });
    }

    private File createTempFileFromString(String text) {
        File file = new File(UUID.randomUUID().toString().replace("-", "") + ".txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(text.getBytes());
            fos.flush();
        } catch (IOException exc) {
            throw new RuntimeException("Ошибка при создании временного файла", exc);
        }
        return file;
    }

    public byte[] downloadFile(String fileName) {
        try (S3Object s3Object = s3Client.getObject(bucket, fileName);
             S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new StorageException("Ошибка при загрузке файла из S3", e);
        }
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucket, fileName);
    }
}
