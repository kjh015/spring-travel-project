package com.traveler.search.domain.comment.repository;

import com.traveler.search.domain.comment.dto.message.CommentSearchMessage;
import lombok.SneakyThrows;

public interface CommentDocumentRepositoryCustom {
    @SneakyThrows
    void updatePartial(CommentSearchMessage.UpdatedDTO dto);
}
