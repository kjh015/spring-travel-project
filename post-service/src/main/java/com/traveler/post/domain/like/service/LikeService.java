package com.traveler.post.domain.like.service;

import com.traveler.post.domain.like.dto.request.LikeRequest;
import com.traveler.post.domain.like.entity.Like;
import com.traveler.post.domain.like.mapper.LikeMapper;
import com.traveler.post.domain.like.repository.LikeRepository;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final LikeMapper likeMapper;
    private final ApplicationEventPublisher eventPublisher;

    public void addLike(LikeRequest.AddDTO dto, Long memberId) {
        if (likeRepository.existsByPostIdAndMemberId(dto.postId(), memberId)) {
            return;
        }

        Post post = postRepository
                .findByIdWithLock(dto.postId())
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        try {
            Like savedLike = likeRepository.save(likeMapper.toAddEntity(post, memberId));
            post.addLike();

            eventPublisher.publishEvent(likeMapper.toAddedEvent(savedLike, post));

        } catch (DataIntegrityViolationException e) {
            log.info("Concurrent like request ignored for memberId: {}, postId: {}", memberId, dto.postId());
        }
    }

    public void removeLike(Long postId, Long memberId) {
        likeRepository.findByPostIdAndMemberId(postId, memberId).ifPresent(like -> {
            Post post = postRepository
                    .findByIdWithLock(postId)
                    .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

            likeRepository.delete(like);
            post.removeLike();

            eventPublisher.publishEvent(likeMapper.toRemovedEvent(like, post));
        });
    }
}
