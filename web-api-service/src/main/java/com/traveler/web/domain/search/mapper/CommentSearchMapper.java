package com.traveler.web.domain.search.mapper;

import com.traveler.web.domain.search.client.dto.response.CommentSearchClientResponse;
import com.traveler.web.domain.search.dto.response.CommentSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentSearchMapper {

    @Mapping(source = "memberNickname", target = "memberNickname")
    CommentSearchResponse.ListDTO toListResponse(CommentSearchClientResponse.ListDTO result, String memberNickname);

    @Mapping(source = "memberNickname", target = "memberNickname")
    CommentSearchResponse.ListDTO toMyListResponse(CommentSearchClientResponse.MyDTO result, String memberNickname);
}
