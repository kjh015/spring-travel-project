package com.traveler.post.domain.favorite.mapper;

import com.traveler.post.domain.favorite.dto.msg.FavoriteMsgDTO;
import com.traveler.post.domain.favorite.entity.Favorite;
import com.traveler.post.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoriteMapper {

    Favorite toAddFavorite(Post post, Long memberId);

    // Kafka
    @Mapping(source = "post.id", target = "postId")
    FavoriteMsgDTO.AddedMessage toAddedMessage(Favorite favorite);

    @Mapping(source = "post.id", target = "postId")
    FavoriteMsgDTO.RemovedMessage toRemovedMessage(Favorite favorite);
}
