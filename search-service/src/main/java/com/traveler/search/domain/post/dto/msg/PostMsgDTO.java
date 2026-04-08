package com.traveler.search.domain.post.dto.msg;

import java.util.List;

public class PostMsgDTO {

    public record ImageInfo(String imageKey, int sortOrder) {}

    public record CreatedMessage(
            Long postId,
            Long memberId,
            String title,
            String content,
            String category,
            String region,
            String travelPlace,
            String address,
            List<ImageInfo> images) {}
}
