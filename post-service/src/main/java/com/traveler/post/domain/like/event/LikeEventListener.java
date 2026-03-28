package com.traveler.post.domain.like.event;

import com.traveler.post.domain.like.dto.msg.LikeMsgDTO;
import com.traveler.post.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener {

    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeAdded(LikeMsgDTO.AddedMessage msg) {
        log.info("Like added Kafka Event triggered for PostID: {} and MemberID: {}", msg.postId(), msg.memberId());
        kafkaProducer.send("like-add-topic", msg);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeRemoved(LikeMsgDTO.RemovedMessage msg) {
        log.info("Like removed Kafka Event triggered for PostID: {} and MemberID: {}", msg.postId(), msg.memberId());
        kafkaProducer.send("like-remove-topic", msg);
    }
}
