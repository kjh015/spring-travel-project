package com.traveler.search.domain.post.service;

import com.traveler.search.domain.post.dto.message.PostSearchMessage;
import com.traveler.search.domain.post.mapper.PostDocumentMapper;
import com.traveler.search.domain.post.repository.PostDocumentRepository;
import com.traveler.search.global.util.HangulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSearchCommandService {
    private final PostDocumentRepository postDocumentRepository;
    private final PostDocumentMapper postDocumentMapper;

    public void createDocument(PostSearchMessage.CreatedDTO dto) {
        String titleChosung = HangulUtils.extractChosung(dto.title());
        postDocumentRepository.save(postDocumentMapper.toCreateDocument(dto, titleChosung));
    }

    public void updateDocument(PostSearchMessage.UpdatedDTO dto) {
        postDocumentRepository.updatePartial(dto);
    }

    public void deleteDocument(PostSearchMessage.DeletedDTO dto) {
        postDocumentRepository.deleteById(dto.postId());
    }

    public void updateStatistics(PostSearchMessage.StatUpdatedDTO dto) {
        postDocumentRepository.updateStatistics(postDocumentMapper.toStatUpdateDoc(dto));
    }
}
