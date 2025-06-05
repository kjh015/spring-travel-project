package com.traveler.board.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service("localImageStorageService")
public class LocalImageStorageService implements ImageStorageService {
    private static final String UPLOAD_DIR = "C:/develop/project/spring/spring-travel-project/images";

    @Override
    public String store(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String ext = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = uuid + ext;
        File dest = new File(UPLOAD_DIR, fileName);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        // 예: "/images/uuid.jpg"로 리턴
        return "/images/" + fileName;
    }

    @Override
    public void delete(String path) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR, path.replaceFirst("^/images/", "")); // 경로 조정
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            // 필요시 로그 남기기, 실패해도 무시 가능
            System.err.println("이미지 삭제 실패: " + path);
        }
    }
}
