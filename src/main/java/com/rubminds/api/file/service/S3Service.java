package com.rubminds.api.file.service;

import com.rubminds.api.file.dto.FileRequest;
import com.rubminds.api.file.dto.S3Component;
import com.rubminds.api.file.exception.FileExtensionException;
import com.rubminds.api.file.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final S3Component component;

    public FileRequest.Upload uploadFile(MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);
        String fileName = createFileName(extension);
        String publicUrl = component.getPublicUrl() + "/" + fileName;
        Integer width = null;
        Integer height = null;
        try{
            PutObjectResponse response = s3Client.putObject(
                    PutObjectRequest.builder()
                            .key(fileName)
                            .bucket(component.getBucket())
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
        } catch (IOException e) {
            throw new FileUploadException();
        }
        return FileRequest.Upload.builder()
                    .originalFileName(originalFileName)
                    .name(fileName)
                    .extension(extension)
                    .size(file.getSize())
                    .publicUrl(publicUrl)
                    .width(width)
                    .height(height)
                    .build();
    }


    private String createFileName(String extension) {
        return UUID.randomUUID().toString().concat(extension);
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new FileExtensionException(fileName);
        }
    }
}
