package com.traveler.web.domain.post.facade;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.post.client.AdminPostClient;
import com.traveler.web.domain.post.client.dto.response.AdminPostClientResponse;
import com.traveler.web.domain.post.dto.response.AdminPostResponse;
import com.traveler.web.domain.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminPostFacade {
    private final AdminPostClient adminPostClient;
    private final PostMapper postMapper;

    public PageResponse<AdminPostResponse.ListDTO> getPosts(Boolean deleted, Pageable pageable) {
        PageResponse<AdminPostClientResponse.ListDTO> clientResponse =
                adminPostClient.getPosts(deleted, pageable).result();
        return clientResponse.map(postMapper::toAdminListResponse);
    }

    public AdminPostResponse.DetailDTO getPost(Long postId) {
        return postMapper.toAdminDetailResponse(adminPostClient.getPost(postId).result());
    }

    public AdminPostResponse.DeleteDTO deletePost(Long postId) {
        return postMapper.toAdminDeleteResponse(
                adminPostClient.deletePost(postId).result());
    }

    public AdminPostResponse.RestoreDTO restorePost(Long postId) {
        return postMapper.toAdminRestoreResponse(
                adminPostClient.restorePost(postId).result());
    }

    public AdminPostResponse.PermanentDeleteDTO permanentlyDeletePost(Long postId) {
        return postMapper.toAdminPermanentDeleteResponse(
                adminPostClient.permanentlyDeletePost(postId).result());
    }
}
