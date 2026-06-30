package com.traveler.post.domain.post.service;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.post.domain.post.dto.request.PostRequest;
import com.traveler.post.domain.post.dto.response.PostResponse;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.entity.TravelPlace;
import com.traveler.post.domain.post.mapper.PostMapper;
import com.traveler.post.domain.post.mapper.TravelPlaceMapper;
import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.exception.code.PostServiceErrorCode;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    public PostResponse.CreateDTO createPost(Long memberId, PostRequest.CreateDTO dto) {
        TravelPlace travelPlace = travelPlaceMapper.toCreateEntity(dto);

        Post post = postMapper.toCreateEntity(dto, travelPlace, memberId);

        post.setImages(dto.images());

        Post savedPost = postRepository.save(post);

        eventPublisher.publishEvent(postMapper.toCreatedEvent(savedPost));

        return postMapper.toCreateDTO(savedPost);
    }

    public PostResponse.UpdateDTO updatePost(Long postId, Long memberId, PostRequest.UpdateDTO dto) {
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
                eventPublisher.publishEvent(postMapper.toImageDeleteEvent(post.getId(), keysToDelete));
            }
        }

        eventPublisher.publishEvent(postMapper.toUpdatedEvent(post));

        return postMapper.toUpdateDTO(post);
    }

    public PostResponse.DeleteDTO deletePost(Long postId, Long memberId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new PostServiceException(ErrorCode.FORBIDDEN);
        }

        post.delete();
        eventPublisher.publishEvent(postMapper.toDeletedEvent(post));

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
            eventPublisher.publishEvent(postMapper.toImageDeleteBatchEvent(ids.getFirst(), imageUrls));
        }
    }

    public void flushViewCountsToDB(Map<Object, Object> viewCountMap) {
        if (viewCountMap == null || viewCountMap.isEmpty()) return;

        for (Map.Entry<Object, Object> entry : viewCountMap.entrySet()) {
            Long postId = Long.valueOf((String) entry.getKey());
            Long increment = Long.valueOf((String) entry.getValue());

            // DB 벌크 업데이트
            postRepository.incrementViewCount(postId, increment);

            // 검색 서버(Search-Service)에 최신 상태를 동기화하기 위해 다시 조회
            postRepository
                    .findById(postId)
                    .ifPresent(post -> eventPublisher.publishEvent(postMapper.toStatUpdatedEvent(post)));
        }
    }
}
