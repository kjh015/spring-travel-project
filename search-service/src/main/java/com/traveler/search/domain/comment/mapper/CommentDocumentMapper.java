package com.traveler.search.domain.comment.mapper;

import com.traveler.search.domain.comment.document.CommentDocument;
import com.traveler.search.domain.comment.dto.message.CommentSearchMessage;
import com.traveler.search.domain.comment.dto.response.CommentSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentDocumentMapper {
    @Mapping(source = "commentId", target = "id")
    CommentDocument toCommentDocument(CommentSearchMessage.CreatedDTO dto);

    @Mapping(source = "id", target = "commentId")
    CommentSearchResponse.ListDTO toListDTO(CommentDocument comment);

    @Mapping(source = "id", target = "commentId")
    CommentSearchResponse.MyDTO toMyDTO(CommentDocument comment);
}
