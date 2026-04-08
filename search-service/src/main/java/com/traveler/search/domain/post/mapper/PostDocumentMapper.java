package com.traveler.search.domain.post.mapper;

import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.dto.msg.PostMsgDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostDocumentMapper {

    @Mapping(source = "postId", target = "id")
    PostDocument toDocument(PostMsgDTO.CreatedMessage msg);
}
