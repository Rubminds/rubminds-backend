package com.rubminds.api.file.service;

import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.file.domain.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    private final AvatarRepository avatarRepository;
    private final S3Service s3Service;

    @Transactional
    public Avatar uploadAvatar(MultipartFile file){
        return avatarRepository.save(Avatar.create(s3Service.uploadFile(file)));
    }

}