package com.traveler.web.domain.search.mapper;

import com.traveler.web.domain.search.client.dto.request.PostSearchClientRequest;
import com.traveler.web.domain.search.client.dto.response.PostSearchClientResponse;
import com.traveler.web.domain.search.dto.request.PostSearchRequest;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostSearchMapper {
    PostSearchClientRequest.SearchDTO toSearchClientRequest(PostSearchRequest.SearchDTO dto);

    PostSearchResponse.ListDTO toSearchListResponse(PostSearchClientResponse.SearchDTO result, String memberNickname);

    PostSearchResponse.DetailDTO toDetailResponse(PostSearchClientResponse.DetailDTO result, String memberNickname);

    PostSearchResponse.AutocompleteDTO toAutocompleteResponse(PostSearchClientResponse.AutocompleteDTO result);

    PostSearchResponse.ListDTO toMyListResponse(PostSearchClientResponse.MyDTO result, String memberNickname);
}
