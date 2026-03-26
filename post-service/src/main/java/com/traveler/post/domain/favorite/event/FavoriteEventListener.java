package com.traveler.post.domain.favorite.event;

import com.traveler.post.domain.favorite.dto.msg.FavoriteMsgDTO;
import com.traveler.post.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class FavoriteEventListener {

    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFavoriteAdded(FavoriteMsgDTO.AddedMessage msg) {
        log.info("Favorite added Kafka Event triggered for PostID: {} and MemberID: {}", msg.postId(), msg.memberId());
        kafkaProducer.send("favorite-add-topic", msg);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFavoriteRemoved(FavoriteMsgDTO.RemovedMessage msg) {
        log.info(
                "Favorite removed Kafka Event triggered for PostID: {} and MemberID: {}", msg.postId(), msg.memberId());
        kafkaProducer.send("favorite-remove-topic", msg);
    }
}
