package com.traveler.web.domain.search.mapper;

import com.traveler.web.domain.search.client.dto.request.PostSearchClientRequest;
import com.traveler.web.domain.search.client.dto.response.PostSearchClientResponse;
import com.traveler.web.domain.search.dto.request.PostSearchRequest;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostSearchMapper {
    PostSearchClientRequest.SearchDTO toSearchClientRequest(PostSearchRequest.SearchDTO dto);

    @Mapping(source = "memberNickname", target = "memberNickname")
    PostSearchResponse.ListDTO toSearchListResponse(PostSearchClientResponse.SearchDTO response, String memberNickname);

    @Mapping(source = "memberNickname", target = "memberNickname")
    PostSearchResponse.DetailDTO toDetailResponse(PostSearchClientResponse.DetailDTO response, String memberNickname);

    PostSearchResponse.AutocompleteDTO toAutocompleteResponse(PostSearchClientResponse.AutocompleteDTO response);

    @Mapping(source = "memberNickname", target = "memberNickname")
    PostSearchResponse.ListDTO toMyListResponse(PostSearchClientResponse.MyDTO response, String memberNickname);
}
