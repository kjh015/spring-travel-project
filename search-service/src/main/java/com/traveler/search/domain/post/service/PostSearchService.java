package com.traveler.search.domain.post.service;

import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.dto.msg.PostMsgDTO;
import com.traveler.search.domain.post.mapper.PostDocumentMapper;
import com.traveler.search.domain.post.repository.PostDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostSearchService {
    private final PostDocumentRepository postDocumentRepository;
    private final PostDocumentMapper postDocumentMapper;

    @Transactional
    public PostDocument test(Long postId) {
        return postDocumentRepository.findById(postId).orElse(null);
    }

    @Transactional
    public void create(PostMsgDTO.CreatedMessage msg) {
        PostDocument postDocument = postDocumentMapper.toDocument(msg);
        postDocumentRepository.save(postDocument);
    }

}
