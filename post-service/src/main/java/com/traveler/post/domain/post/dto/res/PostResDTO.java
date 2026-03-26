package com.traveler.post.domain.post.dto.res;

import java.time.LocalDateTime;

public class PostResDTO {

    public record CreateDTO(Long postId, LocalDateTime createdAt) {}

    public record UpdateDTO(Long postId, LocalDateTime updatedAt) {}

    public record DeleteDTO(Long postId, LocalDateTime deletedAt) {}
}
