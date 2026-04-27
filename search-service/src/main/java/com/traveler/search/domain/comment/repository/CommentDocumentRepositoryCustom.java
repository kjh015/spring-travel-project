package com.traveler.search.domain.comment.repository;

import com.traveler.search.domain.comment.dto.message.CommentSearchMessage;

public interface CommentDocumentRepositoryCustom {

    void updatePartial(CommentSearchMessage.UpdatedDTO dto);
}
