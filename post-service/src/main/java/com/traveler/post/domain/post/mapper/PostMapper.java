package com.traveler.post.domain.post.mapper;

import com.traveler.post.domain.post.dto.msg.PostMsgDTO;
import com.traveler.post.domain.post.dto.req.PostReqDTO;
import com.traveler.post.domain.post.dto.res.PostResDTO;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.entity.PostImage;
import com.traveler.post.domain.post.entity.TravelPlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "travelPlace", source = "travelPlace")
    @Mapping(target = "images", ignore = true)
    Post toCreateEntity(PostReqDTO.CreateDTO dto, TravelPlace travelPlace, Long memberId);

    @Mapping(source = "id", target = "postId")
    PostResDTO.CreateDTO toCreateDTO(Post post);

    @Mapping(source = "id", target = "postId")
    PostResDTO.UpdateDTO toUpdateDTO(Post post);

    @Mapping(source = "id", target = "postId")
    PostResDTO.DeleteDTO toDeleteDTO(Post post);

    // Kafka Message
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "travelPlace.name", target = "travelPlace")
    @Mapping(source = "travelPlace.category", target = "category")
    @Mapping(source = "travelPlace.region", target = "region")
    @Mapping(source = "travelPlace.address", target = "address")
    PostMsgDTO.CreatedMessage toCreatedMsgDTO(Post post);

    PostMsgDTO.ImageInfo toImageInfo(PostImage postImage);

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "travelPlace.name", target = "travelPlace")
    @Mapping(source = "travelPlace.category", target = "category")
    @Mapping(source = "travelPlace.region", target = "region")
    @Mapping(source = "travelPlace.address", target = "address")
    PostMsgDTO.UpdatedMessage toUpdatedMsgDTO(Post post);

    @Mapping(source = "id", target = "postId")
    PostMsgDTO.DeletedMessage toDeletedMsgDTO(Post post);

    default PostMsgDTO.ImagesDeleteMessage toDeleteImagesMsgDTO(List<String> imageKeys) {
        if (imageKeys == null) return null;
        return new PostMsgDTO.ImagesDeleteMessage(imageKeys);
    }
}
