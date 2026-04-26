package com.traveler.search.domain.like.dto.message;

public class LikeSearchMessage {
    public record AddedDTO(Long likeId, Long postId, Long memberId) {}

    public record RemovedDTO(Long likeId, Long postId, Long memberId) {}
}
