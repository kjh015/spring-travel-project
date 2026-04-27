package com.traveler.search.domain.like.document;

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
@Document(indexName = "${elasticsearch.indices.like}")
@Setting(settingPath = "/elasticsearch/like-settings.json")
@Mapping(mappingPath = "/elasticsearch/like-mappings.json")
public class LikeDocument extends BaseDocument {
    public static final class Fields {
        public static final String MEMBER_ID = "memberId";
        public static final String POST_ID = "postId";
    }

    @Field(type = FieldType.Long)
    private Long memberId;

    @Field(type = FieldType.Long)
    private Long postId;
}
