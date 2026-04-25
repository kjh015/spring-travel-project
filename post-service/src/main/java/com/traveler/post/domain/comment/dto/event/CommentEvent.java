package com.traveler.post.domain.comment.dto.event;

import com.traveler.post.domain.comment.dto.message.CommentMessage;
import com.traveler.post.domain.post.dto.event.PostEvent;
import com.traveler.post.domain.post.dto.message.PostMessage;

public class CommentEvent {
    public record Created(CommentMessage.CreatedDTO commentMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}

    public record Updated(CommentMessage.UpdatedDTO commentMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}

    public record Deleted(CommentMessage.DeletedDTO commentMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}
}
