package com.traveler.useractivity.global.document;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class BaseDocument {

    @Id
    private String id;

    @Field(type = FieldType.Date, format = DateFormat.strict_date_optional_time_nanos)
    private Instant createdAt;

    @Field(type = FieldType.Date, format = DateFormat.strict_date_optional_time_nanos)
    private Instant updatedAt;
}
