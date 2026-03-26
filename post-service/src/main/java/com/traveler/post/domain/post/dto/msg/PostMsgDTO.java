package com.traveler.post.domain.post.dto.msg;

import com.traveler.post.domain.post.enums.Category;
import com.traveler.post.domain.post.enums.Region;

import java.time.LocalDateTime;
import java.util.List;

public class PostMsgDTO {

    public record ImageInfo(
            String imageKey,
            int sortOrder
    ) {}

    public record CreatedMessage(
            Long postId,
            Long memberId,
            String title,
            String content,
            Category category,
            Region region,
            String travelPlace,
            String address,
            List<ImageInfo> images
    ){}

    public record UpdatedMessage(
            Long postId,
            String title,
            String content,
            Category category,
            Region region,
            String travelPlace,
            String address,
            List<ImageInfo> images
    ){}

    public record DeletedMessage(
            Long postId,
            LocalDateTime deletedAt
    ) {}
}
