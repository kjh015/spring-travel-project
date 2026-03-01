package com.traveler.board.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageStorageService {
    // 이미지 저장, 반환값은 접근 가능한 경로(URL 또는 로컬 경로)
    String store(MultipartFile file) throws IOException;

    void delete(String path) throws IOException;
}
