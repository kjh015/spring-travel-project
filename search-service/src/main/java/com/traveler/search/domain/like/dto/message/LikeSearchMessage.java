package com.traveler.search.domain.like.dto.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LikeSearchMessage {
    public record AddedDTO(Long likeId, Long postId, Long memberId) {}

    public record RemovedDTO(Long likeId) {}
}
