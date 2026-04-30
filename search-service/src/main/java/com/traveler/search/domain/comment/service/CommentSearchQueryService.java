package com.traveler.search.domain.comment.service;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.comment.document.CommentDocument;
import com.traveler.search.domain.comment.dto.response.CommentSearchResponse;
import com.traveler.search.domain.comment.mapper.CommentDocumentMapper;
import com.traveler.search.domain.comment.repository.CommentDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentSearchQueryService {
    private final CommentDocumentRepository commentDocumentRepository;
    private final CommentDocumentMapper commentDocumentMapper;

    public PageResponse<CommentSearchResponse.ListDTO> getComments(Long postId, Pageable pageable) {
        Page<CommentDocument> comments = commentDocumentRepository.findByPostId(postId, pageable);

        return PageConverter.toPageResponse(comments, commentDocumentMapper::toListDTO);
    }

    public PageResponse<CommentSearchResponse.MyDTO> getMyComments(Long memberId, Pageable pageable) {
        Page<CommentDocument> comments = commentDocumentRepository.findByMemberId(memberId, pageable);

        return PageConverter.toPageResponse(comments, commentDocumentMapper::toMyDTO);
    }
}
