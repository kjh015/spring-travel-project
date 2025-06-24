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
    private static final String UPLOAD_DIR = "/home/ubuntu/work/images";

    @Override
    public String store(MultipartFile file) throws IOException {
        System.out.println("image store proceed... file = " + file.getOriginalFilename());
        System.out.println("upload_dir: " + UPLOAD_DIR);
        String uuid = UUID.randomUUID().toString();
        String ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = uuid + ext;
        File dest = new File(UPLOAD_DIR, fileName);
        try {
            dest.getParentFile().mkdirs();
            file.transferTo(dest);
        } catch (Exception e) {
            System.err.println("파일 저장 에러: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
