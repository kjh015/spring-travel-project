package com.traveler.post.domain.comment.entity;

import com.traveler.common.db.entity.BaseEntity;
import com.traveler.post.domain.post.entity.Post;
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
        this.content = content;
        this.star = star;
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
