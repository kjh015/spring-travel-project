package com.traveler.post.domain.post.event;

import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.enums.PostEventType;
import com.traveler.post.domain.post.mapper.PostMapper;
import com.traveler.post.global.kafka.KafkaTopicProperties;
import com.traveler.post.global.outbox.event.BaseEventPublisher;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PostEventPublisher extends BaseEventPublisher {
    private final PostMapper postMapper;

    public PostEventPublisher(
            ApplicationEventPublisher eventPublisher, PostMapper postMapper, KafkaTopicProperties topicProperties) {
        super(eventPublisher, topicProperties);
        this.postMapper = postMapper;
    }

    public void publishCreated(Post post) {
        publish(post.getId(), PostEventType.CREATED, postMapper.toCreatedMsgDTO(post));
    }

    public void publishUpdated(Post post) {
        publish(post.getId(), PostEventType.UPDATED, postMapper.toUpdatedMsgDTO(post));
    }

    public void publishDeleted(Post post) {
        publish(post.getId(), PostEventType.DELETED, postMapper.toDeletedMsgDTO(post));
    }

    public void publishImagesDelete(Long aggregateId, List<String> imageKeys) {
        if (imageKeys == null || imageKeys.isEmpty()) return;

        publish(aggregateId, PostEventType.IMAGE_DELETE, postMapper.toDeleteImagesMsgDTO(imageKeys));
    }

    public void publishImagesDelete(List<Long> postIds, List<String> imageKeys) {
        if (imageKeys == null || imageKeys.isEmpty() || postIds == null || postIds.isEmpty()) return;

        // 대표 ID로 첫 번째 ID 사용
        publish(postIds.getFirst(), PostEventType.IMAGE_DELETE_BATCH, postMapper.toDeleteImagesMsgDTO(imageKeys));
    }
}
