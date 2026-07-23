package com.traveler.web.domain.search.facade;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.member.adaptor.MemberClientAdaptor;
import com.traveler.web.domain.search.client.CommentSearchClient;
import com.traveler.web.domain.search.client.dto.response.CommentSearchClientResponse;
import com.traveler.web.domain.search.dto.response.CommentSearchResponse;
import com.traveler.web.domain.search.mapper.CommentSearchMapper;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentSearchFacade {
    private final CommentSearchClient commentSearchClient;
    private final CommentSearchMapper commentSearchMapper;
    private final MemberClientAdaptor memberClientAdaptor;

    public PageResponse<CommentSearchResponse.ListDTO> getComments(Long postId, Pageable pageable) {
        PageResponse<CommentSearchClientResponse.ListDTO> result =
                commentSearchClient.getComments(postId, pageable).result();

        Set<Long> memberIds = result.content().stream()
                .map(CommentSearchClientResponse.ListDTO::memberId)
                .collect(Collectors.toSet());

        Map<Long, String> nicknameMap = memberClientAdaptor.getNicknameMap(memberIds);

        // 3. 데이터 조합
        return result.map(clientDto -> {
            String nickname = nicknameMap.getOrDefault(clientDto.memberId(), "알 수 없음");
            return commentSearchMapper.toListResponse(clientDto, nickname);
        });
    }

    public PageResponse<CommentSearchResponse.ListDTO> getMyComments(Pageable pageable) {
        PageResponse<CommentSearchClientResponse.MyDTO> result =
                commentSearchClient.getMyComments(pageable).result();

        String nickname = memberClientAdaptor.getMyNickname();

        return result.map(clientDto -> commentSearchMapper.toMyListResponse(clientDto, nickname));
    }
}
