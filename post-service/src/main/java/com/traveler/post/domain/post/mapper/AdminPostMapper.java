package com.traveler.post.domain.post.mapper;

import com.traveler.post.domain.post.dto.response.AdminPostResponse;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.entity.PostImage;
import java.util.Comparator;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminPostMapper {

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "deleted", target = "isDeleted")
    AdminPostResponse.ListDTO toListDTO(Post post);

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "deleted", target = "isDeleted")
    @Mapping(source = "travelPlace.category", target = "category")
    @Mapping(source = "travelPlace.region", target = "region")
    @Mapping(source = "travelPlace.name", target = "travelPlace")
    @Mapping(source = "travelPlace.address", target = "address")
    AdminPostResponse.DetailDTO toDetailDTO(Post post);

    @Mapping(source = "id", target = "postId")
    AdminPostResponse.DeleteDTO toDeleteDTO(Post post);

    @Mapping(source = "id", target = "postId")
    AdminPostResponse.RestoreDTO toRestoreDTO(Post post);

    default List<String> toImageKeys(List<PostImage> images) {
        if (images == null) return List.of();
        return images.stream()
                .sorted(Comparator.comparingInt(PostImage::getSortOrder))
                .map(PostImage::getImageKey)
                .toList();
    }
}
