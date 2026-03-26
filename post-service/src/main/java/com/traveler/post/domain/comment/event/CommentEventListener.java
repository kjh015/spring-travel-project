package com.traveler.post.domain.comment.event;

import com.traveler.post.domain.comment.dto.msg.CommentMsgDTO;
import com.traveler.post.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener {

    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCreated(CommentMsgDTO.CreatedMessage msg) {
        log.info("Comment created Kafka Event triggered for ID: {}", msg.commentId());
        kafkaProducer.send("comment-create-topic", msg);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentUpdated(CommentMsgDTO.UpdatedMessage msg) {
        log.info("Comment updated Kafka Event triggered for ID: {}", msg.commentId());
        kafkaProducer.send("comment-update-topic", msg);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentDeleted(CommentMsgDTO.DeletedMessage msg) {
        log.info("Comment deleted Kafka Event triggered for ID: {}", msg.commentId());
        kafkaProducer.send("comment-delete-topic", msg);
    }
}
