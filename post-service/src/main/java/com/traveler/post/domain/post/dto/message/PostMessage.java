package com.traveler.post.domain.post.dto.message;

import com.traveler.post.domain.post.enums.Category;
import com.traveler.post.domain.post.enums.Region;
import java.time.Instant;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PostMessage {

    public record ImageInfo(String imageKey, int sortOrder) {}

    public record CreatedDTO(
            Long postId,
            Long memberId,
            String title,
            String content,
            Category category,
            Region region,
            String travelPlace,
            String address,
            Instant createdAt,
            List<ImageInfo> images) {}

    public record UpdatedDTO(
            Long postId,
            String title,
            String content,
            Category category,
            Region region,
            String travelPlace,
            String address,
            Instant updatedAt,
            List<ImageInfo> images) {}

    public record DeletedDTO(Long postId) {}

    public record ImagesDeleteDTO(Long postId, List<String> imageKeys) {
        public ImagesDeleteDTO {
            imageKeys = (imageKeys == null) ? java.util.List.of() : List.copyOf(imageKeys);
        }
    }

    public record UpdateStatDTO(Long postId, Double starAvg, Long commentCount, Long likeCount, Long viewCount) {}
}
