package com.traveler.search.domain.comment.repository;

import com.traveler.search.domain.comment.document.CommentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentDocumentRepository
        extends ElasticsearchRepository<CommentDocument, Long>, CommentDocumentRepositoryCustom {

    Page<CommentDocument> findByPostId(Long postId, Pageable pageable);

    Page<CommentDocument> findByMemberId(Long memberId, Pageable pageable);
}
