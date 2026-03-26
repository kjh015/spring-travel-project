package com.traveler.post.domain.post.event;

import com.traveler.post.domain.post.dto.event.PostEventDTO;
import com.traveler.post.domain.post.dto.msg.PostMsgDTO;
import com.traveler.post.global.kafka.KafkaProducer;
import com.traveler.post.global.s3.S3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostEventListener {
    // 입력 파라미터 기준으로 Event가 실행됨. 같은 파라미터가 여러 개라면 전부 실행됨. 파라미터를 구독 중인 상태
    // Outbox Pattern 적용 고려

    private final S3Client s3Client;
    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostMsgDTO.CreatedMessage msg) {
        log.info("Post created Kafka Event triggered for ID: {}", msg.postId());
        kafkaProducer.send("post-create-topic", msg);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdated(PostMsgDTO.UpdatedMessage msg) {
        log.info("Post updated Kafka Event triggered for ID: {}", msg.postId());
        kafkaProducer.send("post-update-topic", msg);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostDeleted(PostMsgDTO.DeletedMessage msg) {
        log.info("Post deleted Kafka Event triggered for ID: {}", msg.postId());
        kafkaProducer.send("post-delete-topic", msg);
    }

    // S3 이미지 삭제
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleImagesDelete(PostEventDTO.ImagesDeleteEvent event) {
        log.info("Starting S3 file deletion for updated post");
        s3Client.deleteFilesByUrls(event.imageKeys());
    }
}
