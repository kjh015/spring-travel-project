package com.traveler.post.domain.like.dto.message;

public class LikeMessage {

    public record AddedDTO(Long postId, Long memberId) {}

    public record RemovedDTO(Long postId, Long memberId) {}
}
