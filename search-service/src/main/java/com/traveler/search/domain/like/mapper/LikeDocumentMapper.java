package com.traveler.search.domain.like.mapper;

import com.traveler.search.domain.like.document.LikeDocument;
import com.traveler.search.domain.like.dto.message.LikeSearchMessage;
import com.traveler.search.domain.like.dto.response.LikeSearchResponse;
import com.traveler.search.domain.post.document.PostDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeDocumentMapper {
    @Mapping(source = "likeId", target = "id")
    LikeDocument toLikeDocument(LikeSearchMessage.AddedDTO addedDTO);

    @Mapping(source = "id", target = "postId")
    LikeSearchResponse.MyDTO toMyDTO(PostDocument post);
}
