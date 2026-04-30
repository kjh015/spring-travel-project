package com.traveler.search.domain.like.repository;

import com.traveler.search.domain.like.document.LikeDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LikeDocumentRepository
        extends ElasticsearchRepository<LikeDocument, Long>, LikeDocumentRepositoryCustom {
    Page<LikeDocument> findByMemberId(Long memberId, Pageable pageable);
}
