package com.traveler.web.domain.post.client.dto.request;

import com.traveler.web.domain.post.enums.PostCategory;
import com.traveler.web.domain.post.enums.PostRegion;
import java.util.List;

public final class PostClientRequest {

    private PostClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record CreateDTO(
            String title,
            String content,
            String travelPlace,
            String address,
            PostCategory category,
            PostRegion region,
            List<String> images) {}

    public record UpdateDTO(
            String title,
            String content,
            String travelPlace,
            String address,
            PostCategory category,
            PostRegion region,
            List<String> images) {}
}
