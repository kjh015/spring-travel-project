package com.traveler.useractivity.domain.history.document;

import com.traveler.useractivity.domain.history.document.vo.HistoryFailInfo;
import com.traveler.useractivity.global.document.BaseDocument;
import java.util.Map;
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
@Document(indexName = "#{@environment.getProperty('app.elasticsearch.indices.history')}")
@Setting(settingPath = "/elasticsearch/history-settings.json")
@Mapping(mappingPath = "/elasticsearch/history-mappings.json")
public class HistoryDocument extends BaseDocument {

    // 추적 ID
    @Field(type = FieldType.Keyword)
    private String traceId;

    // 프로세스 식별자
    @Field(name = "log_process_id", type = FieldType.Keyword)
    private Long logProcessId;

    @Field(name = "log_process_name", type = FieldType.Keyword)
    private String logProcessName;

    // 성공 여부
    @Field(type = FieldType.Boolean)
    private boolean success;

    // 에러 상세 정보
    @Field(name = "fail_info", type = FieldType.Object)
    private HistoryFailInfo failInfo;

    // 상세 로그 데이터
    @Field(name = "log_data", type = FieldType.Object)
    private Map<String, Object> logData;

    public static final class Fields {
        public static final String TRACE_ID = "traceId";
        public static final String LOG_PROCESS_ID = "log_process_id";
        public static final String SUCCESS = "success";
        public static final String FAIL_INFO = "fail_info";
        public static final String LOG_DATA = "log_data";

        public static final String FAIL_INFO_CODE = "fail_info.code";
        public static final String FAIL_INFO_STAGE = "fail_info.stage";
        public static final String FAIL_INFO_RULE_ID = "fail_info.fail_rule_id";
        public static final String FAIL_INFO_DETAIL = "fail_info.detail";

        public static final String ID = "id";
        public static final String CREATED_AT = "createdAt";
        public static final String UPDATED_AT = "updatedAt";
    }
}
