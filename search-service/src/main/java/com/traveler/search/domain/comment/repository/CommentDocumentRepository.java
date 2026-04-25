package com.traveler.search.domain.comment.repository;

import com.traveler.search.domain.comment.document.CommentDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentDocumentRepository
        extends ElasticsearchRepository<CommentDocument, Long>, CommentDocumentRepositoryCustom {}
