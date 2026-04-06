package com.traveler.post.domain.post.service;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.post.domain.post.dto.req.PostReqDTO;
import com.traveler.post.domain.post.dto.res.PostResDTO;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.entity.TravelPlace;
import com.traveler.post.domain.post.mapper.PostMapper;
import com.traveler.post.domain.post.mapper.TravelPlaceMapper;
import com.traveler.post.domain.post.publisher.PostOutboxPublisher;
import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final TravelPlaceMapper travelPlaceMapper;
    private final PostOutboxPublisher postOutboxPublisher;

    public PostResDTO.CreateDTO createPost(Long memberId, PostReqDTO.CreateDTO dto) {
        TravelPlace travelPlace = travelPlaceMapper.toCreateEntity(dto);

        Post post = postMapper.toCreateEntity(dto, travelPlace, memberId);

        post.setImages(dto.images());

        Post savedPost = postRepository.save(post);

        postOutboxPublisher.publishCreated(savedPost);

        return postMapper.toCreateDTO(savedPost);
    }

    public PostResDTO.UpdateDTO updatePost(Long postId, Long memberId, PostReqDTO.UpdateDTO dto) {
        Post post = postRepository
                .findByIdWithDetails(postId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new PostServiceException(ErrorCode.FORBIDDEN);
        }

        post.getTravelPlace().update(dto.category(), dto.region(), dto.travelPlace(), dto.address());
        post.update(dto.title(), dto.content());

        if (dto.images() != null) {
            List<String> keysToDelete = post.setImages(dto.images());

            if (!keysToDelete.isEmpty()) {
                postOutboxPublisher.publishImagesDelete(post.getId(), keysToDelete);
            }
        }

        postOutboxPublisher.publishUpdated(post);

        return postMapper.toUpdateDTO(post);
    }

    public PostResDTO.DeleteDTO deletePost(Long postId, Long memberId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new PostServiceException(ErrorCode.FORBIDDEN);
        }

        post.delete();
        postOutboxPublisher.publishDeleted(post);

        return postMapper.toDeleteDTO(post);
    }

    // Batch Delete
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        // S3 이미지 조회
        List<String> imageUrls = postRepository.findImageKeysByPostIds(ids);

        // DB 벌크 삭제
        postRepository.hardDeletePostsByIds(ids);

        // S3 이미지 삭제 이벤트 발행
        if (!imageUrls.isEmpty()) {
            postOutboxPublisher.publishImagesDelete(ids, imageUrls);
        }
    }
}
