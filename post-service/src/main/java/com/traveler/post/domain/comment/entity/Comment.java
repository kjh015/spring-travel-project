package com.traveler.post.domain.comment.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@Table(
        name = "comment",
        indexes = {@Index(name = "idx_comment_deleted_at_status", columnList = "isDeleted, deletedAt")})
public class Comment extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(length = 500)
    private String content;

    private Integer star;

    @Builder.Default
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    public void update(String content, Integer star) {
        validateNotDeleted();
        this.content = content;
        this.star = star;
    }

    public void delete() {
        if (this.isDeleted) {
            return;
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    private void validateNotDeleted() {
        if (this.isDeleted) {
            throw new PostServiceException(PostServiceErrorCode.COMMENT_ALREADY_DELETED);
        }
    }
}
