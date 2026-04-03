package com.traveler.post.domain.post.message;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.post.domain.post.dto.msg.PostMsgDTO;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.s3.S3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostMessageConsumer {

    private final S3Client s3Client;

    @KafkaListener(topics = "${spring.kafka.topics.post-commands}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeS3Delete(PostMsgDTO.ImagesDeleteMessage event, Acknowledgment ack) {
        try {

            log.info("Kafka Consumer: S3 파일 삭제 시작 - {}건", event.imageKeys().size());
            s3Client.deleteFilesByUrls(event.imageKeys());
            ack.acknowledge();

        } catch (Exception e) {
            log.error("S3 삭제 메시지 처리 중 오류 발생", e);
            throw new PostServiceException(ErrorCode.S3_DELETE_ERROR);
        }
    }
}
