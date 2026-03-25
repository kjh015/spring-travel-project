package com.traveler.post.domain.post.dto.event;

import java.util.List;

public class PostEventDTO {
    // S3 이미지 삭제를 위한 이벤트
    public record ImagesDeleteEvent(
            List<String> imageKeys
    ) {}


}
