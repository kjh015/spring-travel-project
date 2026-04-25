package com.traveler.post.domain.like.dto.event;

import com.traveler.post.domain.like.dto.message.LikeMessage;
import com.traveler.post.domain.post.dto.event.PostEvent;
import com.traveler.post.domain.post.dto.message.PostMessage;

public class LikeEvent {
    public record Added(LikeMessage.AddedDTO likeMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}

    public record Removed(LikeMessage.RemovedDTO likeMsg, PostMessage.UpdateStatDTO postMsg)
            implements PostEvent.StatUpdate {}
}
