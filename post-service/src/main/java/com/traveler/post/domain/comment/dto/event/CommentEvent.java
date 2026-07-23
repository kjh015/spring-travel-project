package com.traveler.post.domain.comment.dto.event;

import com.traveler.post.domain.comment.dto.message.CommentMessage;
import com.traveler.post.domain.post.dto.event.PostEvent;
import com.traveler.post.domain.post.dto.message.PostMessage;

public final class CommentEvent {

    private CommentEvent() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record Created(CommentMessage.CreatedDTO commentMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}

    public record Updated(CommentMessage.UpdatedDTO commentMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}

    public record Deleted(CommentMessage.DeletedDTO commentMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}

    // 부모 게시물이 이미 삭제되어 통계 갱신 없이 댓글 삭제 메시지만 발행하는 어드민 전용 이벤트
    public record AdminDeleted(CommentMessage.DeletedDTO commentMsg) {}
}
