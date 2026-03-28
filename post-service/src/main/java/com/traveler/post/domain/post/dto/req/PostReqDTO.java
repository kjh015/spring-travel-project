package com.traveler.post.domain.post.dto.req;

import com.traveler.post.domain.post.enums.Category;
import com.traveler.post.domain.post.enums.Region;
import java.util.List;

public class PostReqDTO {

    public record CreateDTO(
            String title,
            String content,
            String travelPlace,
            String address,
            Category category,
            Region region,
            List<String> images) {}

    public record UpdateDTO(
            String title,
            String content,
            String travelPlace,
            String address,
            Category category,
            Region region,
            List<String> images) {}
}
