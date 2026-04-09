package com.traveler.search.domain.post.listener;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.search.domain.post.dto.msg.PostMsgDTO;
import com.traveler.search.domain.post.service.PostSearchService;
import com.traveler.search.global.exception.SearchServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostDocumentMessageConsumer {
    private final PostSearchService postSearchService;

    @KafkaListener(topics = "${spring.kafka.topics.post-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTest(PostMsgDTO.CreatedMessage msg, Acknowledgment ack) {
        try {

            log.info("Kafka Consumer: Post Service to Search Service");
            postSearchService.create(msg);
            ack.acknowledge();

        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생", e);
            throw new SearchServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
