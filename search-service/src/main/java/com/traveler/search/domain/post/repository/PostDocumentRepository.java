package com.traveler.search.domain.post.repository;

import com.traveler.search.domain.post.document.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostDocumentRepository
        extends ElasticsearchRepository<PostDocument, Long>, PostDocumentRepositoryCustom {

    Page<PostDocument> findByMemberId(Long memberId, Pageable pageable);
}
