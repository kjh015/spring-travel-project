package com.traveler.post.domain.like.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LikeRequest {
    public record AddDTO(
            @NotNull(message = "게시글 ID는 필수입니다.") @Schema(description = "좋아요를 누를 게시글 ID", example = "101")
                    Long postId) {}
}
