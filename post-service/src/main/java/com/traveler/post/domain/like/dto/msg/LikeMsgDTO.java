package com.traveler.post.domain.like.dto.msg;

public class LikeMsgDTO {

    public record AddedMessage(Long postId, Long memberId) {}

    public record RemovedMessage(Long postId, Long memberId) {}
}
