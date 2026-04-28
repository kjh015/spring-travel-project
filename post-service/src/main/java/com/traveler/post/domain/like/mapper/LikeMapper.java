package com.traveler.post.domain.like.mapper;

import com.traveler.post.domain.like.dto.event.LikeEvent;
import com.traveler.post.domain.like.dto.message.LikeMessage;
import com.traveler.post.domain.like.entity.Like;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.mapper.PostMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PostMapper.class})
public interface LikeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "memberId", source = "memberId")
    Like toAddEntity(Post post, Long memberId);

    // Kafka
    @Mapping(source = "id", target = "likeId")
    @Mapping(source = "post.id", target = "postId")
    LikeMessage.AddedDTO toAddedMessage(Like like);

    @Mapping(source = "id", target = "likeId")
    LikeMessage.RemovedDTO toRemovedMessage(Like like);

    // Event
    @Mapping(target = "likeMsg", source = "like")
    @Mapping(target = "postMsg", source = "post")
    LikeEvent.Added toAddedEvent(Like like, Post post);

    @Mapping(target = "likeMsg", source = "like")
    @Mapping(target = "postMsg", source = "post")
    LikeEvent.Removed toRemovedEvent(Like like, Post post);
}
