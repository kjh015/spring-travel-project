package com.traveler.search.domain.comment.document;

import com.traveler.search.global.document.BaseDocument;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@Document(indexName = "${elasticsearch.indices.comment}")
@Setting(settingPath = "/elasticsearch/comment-settings.json")
@Mapping(mappingPath = "/elasticsearch/comment-mappings.json")
public class CommentDocument extends BaseDocument {

    public static final class Fields {
        public static final String POST_ID = "postId";
        public static final String MEMBER_ID = "memberId";
        public static final String CONTENT = "content";
        public static final String STAR = "star";
    }

    @Field(type = FieldType.Long)
    private Long postId;

    @Field(type = FieldType.Long)
    private Long memberId;

    @Field(type = FieldType.Text, analyzer = "korean_nori")
    private String content;

    @Field(type = FieldType.Integer)
    private Integer star;
}
