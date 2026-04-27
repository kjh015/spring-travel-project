package com.traveler.post.domain.like.dto.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LikeMessage {

    public record AddedDTO(Long likeId, Long postId, Long memberId) {}

    public record RemovedDTO(Long likeId) {}
}
