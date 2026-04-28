package com.traveler.search.domain.like.dto.message;

import java.time.Instant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LikeSearchMessage {
    public record AddedDTO(Long likeId, Long postId, Long memberId, Instant createdAt) {}

    public record RemovedDTO(Long likeId) {}
}
