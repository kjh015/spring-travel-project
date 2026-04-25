package com.traveler.post.domain.like.mapper;

import com.traveler.post.domain.like.dto.event.LikeEvent;
import com.traveler.post.domain.like.dto.message.LikeMessage;
import com.traveler.post.domain.like.entity.Like;
import com.traveler.post.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {

    Like toAddEntity(Post post, Long memberId);

    // Kafka
    @Mapping(source = "post.id", target = "postId")
    LikeMessage.AddedDTO toAddedMessage(Like like);

    @Mapping(source = "post.id", target = "postId")
    LikeMessage.RemovedDTO toRemovedMessage(Like like);

    // Event
    @Mapping(target = "likeMsg", source = "like")
    @Mapping(target = "postMsg", source = "post")
    LikeEvent.Added toAddedEvent(Like like, Post post);

    @Mapping(target = "likeMsg", source = "like")
    @Mapping(target = "postMsg", source = "post")
    LikeEvent.Removed toRemovedEvent(Like like, Post post);
}
