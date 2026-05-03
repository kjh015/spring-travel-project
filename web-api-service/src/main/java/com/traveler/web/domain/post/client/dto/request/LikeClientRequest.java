package com.traveler.web.domain.post.client.dto.request;

public final class LikeClientRequest {

    private LikeClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record AddDTO(Long postId) {}
}
