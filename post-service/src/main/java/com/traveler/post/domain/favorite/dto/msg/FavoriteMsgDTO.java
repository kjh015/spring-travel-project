package com.traveler.post.domain.favorite.dto.msg;

public class FavoriteMsgDTO {

    public record AddedMessage(Long postId, Long memberId) {}

    public record RemovedMessage(Long postId, Long memberId) {}
}
