package com.traveler.post.domain.post.dto.event;

import com.traveler.post.domain.post.dto.message.PostMessage;

public class PostEvent {
    public record Created(PostMessage.CreatedDTO postMsg) {}

    public record Updated(PostMessage.UpdatedDTO postMsg) {}

    public record Deleted(PostMessage.DeletedDTO postMsg) {}

    public record ImagesDelete(PostMessage.ImagesDeleteDTO postMsg) {}

    public record ImagesDeleteBatch(PostMessage.ImagesDeleteDTO postMsg) {}

    public interface StatUpdate {
        PostMessage.UpdateStatDTO postMsg();
    }
}
