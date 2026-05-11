package com.traveler.web.domain.post.facade;

import com.traveler.web.domain.post.client.LikeClient;
import com.traveler.web.domain.post.dto.request.LikeRequest;
import com.traveler.web.domain.post.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeFacade {
    private final LikeClient likeClient;
    private final LikeMapper likeMapper;

    public void addLike(LikeRequest.AddDTO dto) {
        likeClient.addLike(likeMapper.toAddClientRequest(dto));
    }

    public void removeLike(Long postId) {
        likeClient.removeLike(postId);
    }
}
