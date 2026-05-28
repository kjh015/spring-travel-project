package com.traveler.web.domain.search.facade;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.member.adaptor.MemberClientAdaptor;
import com.traveler.web.domain.search.client.LikeSearchClient;
import com.traveler.web.domain.search.client.dto.response.LikeSearchClientResponse;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.mapper.LikeSearchMapper;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeSearchFacade {
    private final LikeSearchClient likeSearchClient;
    private final LikeSearchMapper likeSearchMapper;
    private final MemberClientAdaptor memberClientAdaptor;

    public PageResponse<PostSearchResponse.ListDTO> getMyLikePosts(Pageable pageable) {
        PageResponse<LikeSearchClientResponse.MyDTO> result =
                likeSearchClient.getMyLikePosts(pageable).result();

        Set<Long> memberIds = result.content().stream()
                .map(LikeSearchClientResponse.MyDTO::memberId)
                .collect(Collectors.toSet());

        Map<Long, String> nicknameMap = memberClientAdaptor.getNicknameMap(memberIds);

        // 3. 데이터 조합
        return result.map(clientDto -> {
            String nickname = nicknameMap.getOrDefault(clientDto.memberId(), "알 수 없음");
            return likeSearchMapper.toMyListResponse(clientDto, nickname);
        });
    }
}
