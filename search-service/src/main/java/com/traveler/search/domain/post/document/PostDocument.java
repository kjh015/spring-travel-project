package com.traveler.search.domain.post.document;

import com.traveler.search.global.entity.BaseDocument;
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
@Document(indexName = "posts")
@Setting(settingPath = "/elasticsearch/post-settings.json")
@Mapping(mappingPath = "/elasticsearch/post-mappings.json")
public class PostDocument extends BaseDocument {

    @Field(type = FieldType.Text, analyzer = "korean_nori")
    private String title;

    @Field(type = FieldType.Text, analyzer = "chosung_ngram_analyzer")
    private String titleChosung;

    @Field(type = FieldType.Text, analyzer = "korean_nori")
    private String content;

    @Field(type = FieldType.Long)
    private Long memberId;

    @Field(type = FieldType.Text, analyzer = "korean_nori")
    private String travelPlace;

    @Field(type = FieldType.Text, analyzer = "korean_nori")
    private String address;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private String region;

    @Field(type = FieldType.Double)
    private Double starAvg;

    @Field(type = FieldType.Long)
    private Long viewCount;

    @Field(type = FieldType.Long)
    private Long likeCount;

    @Field(type = FieldType.Long)
    private Long commentCount;
}
