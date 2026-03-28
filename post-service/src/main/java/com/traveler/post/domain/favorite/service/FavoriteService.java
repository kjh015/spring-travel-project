package com.traveler.post.domain.favorite.service;

import com.traveler.post.domain.favorite.dto.req.FavoriteReqDTO;
import com.traveler.post.domain.favorite.entity.Favorite;
import com.traveler.post.domain.favorite.mapper.FavoriteMapper;
import com.traveler.post.domain.favorite.repository.FavoriteRepository;
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
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;
    private final FavoriteMapper favoriteMapper;
    private final ApplicationEventPublisher eventPublisher;

    public void addFavorite(FavoriteReqDTO.AddDTO dto, Long memberId) {
        if (favoriteRepository.existsByPostIdAndMemberId(dto.postId(), memberId)) {
            return;
        }

        Post post = postRepository
                .findById(dto.postId())
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        try {
            Favorite savedFavorite = favoriteRepository.save(favoriteMapper.toAddFavorite(post, memberId));
            eventPublisher.publishEvent(favoriteMapper.toAddedMessage(savedFavorite));
        } catch (DataIntegrityViolationException e) {
            log.info("Concurrent favorite request ignored for memberId: {}, postId: {}", memberId, dto.postId());
        }
    }

    public void removeFavorite(Long postId, Long memberId) {
        favoriteRepository.findByPostIdAndMemberId(postId, memberId).ifPresent(favorite -> {
            favoriteRepository.delete(favorite);
            eventPublisher.publishEvent(favoriteMapper.toRemovedMessage(favorite));
        });
    }
}
