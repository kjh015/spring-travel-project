package com.traveler.post.domain.like.dto.message;

import java.time.Instant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LikeMessage {

    public record AddedDTO(Long likeId, Long postId, Long memberId, Instant createdAt) {}

    public record RemovedDTO(Long likeId) {}
}
