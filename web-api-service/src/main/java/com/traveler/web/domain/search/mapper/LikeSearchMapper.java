package com.traveler.web.domain.search.mapper;

import com.traveler.web.domain.search.client.dto.response.LikeSearchClientResponse;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeSearchMapper {
    @Mapping(source = "memberNickname", target = "memberNickname")
    PostSearchResponse.ListDTO toMyListResponse(LikeSearchClientResponse.MyDTO response, String memberNickname);
}
