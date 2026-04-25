package com.traveler.search.domain.post.service;

import com.traveler.search.domain.post.dto.message.PostSearchMessage;
import com.traveler.search.domain.post.mapper.PostDocumentMapper;
import com.traveler.search.domain.post.repository.PostDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSearchCommandService {
    private final PostDocumentRepository postDocumentRepository;
    private final PostDocumentMapper postDocumentMapper;

    public void createDocument(PostSearchMessage.CreatedDTO dto) {
        postDocumentRepository.save(postDocumentMapper.toCreateDocument(dto));
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
