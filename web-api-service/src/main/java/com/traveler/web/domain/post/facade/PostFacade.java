package com.traveler.web.domain.post.facade;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.post.client.PostClient;
import com.traveler.web.domain.post.client.dto.response.PostClientResponse;
import com.traveler.web.domain.post.dto.request.PostRequest;
import com.traveler.web.domain.post.dto.response.PostResponse;
import com.traveler.web.domain.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostFacade {
    private final PostClient postClient;
    private final PostMapper postMapper;

    public PostResponse.CreateDTO createPost(PostRequest.CreateDTO dto) {
        ApiResponse<PostClientResponse.CreateDTO> response =
                postClient.createPost(postMapper.toCreateClientRequest(dto));
        return postMapper.toCreateResponse(response.result());
    }

    public PostResponse.UpdateDTO updatePost(Long postId, PostRequest.UpdateDTO dto) {
        ApiResponse<PostClientResponse.UpdateDTO> response =
                postClient.updatePost(postId, postMapper.toUpdateClientRequest(dto));
        return postMapper.toUpdateResponse(response.result());
    }

    public PostResponse.DeleteDTO deletePost(Long postId) {
        ApiResponse<PostClientResponse.DeleteDTO> response = postClient.deletePost(postId);
        return postMapper.toDeleteResponse(response.result());
    }
}
