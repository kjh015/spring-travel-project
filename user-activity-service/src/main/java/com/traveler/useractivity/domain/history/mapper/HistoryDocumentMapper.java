package com.traveler.useractivity.domain.history.mapper;

import com.traveler.useractivity.domain.history.document.HistoryDocument;
import com.traveler.useractivity.domain.history.document.vo.HistoryFailInfo;
import com.traveler.useractivity.domain.history.dto.response.HistoryResponse;
import com.traveler.useractivity.domain.process.core.message.FailInfo;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistoryDocumentMapper {
    // LogPayload -> HistoryDocument 변환
    @Mapping(target = "id", ignore = true) // ES가 자동 생성하도록
    HistoryDocument toDocument(LogPayload<?> payload);

    // Core의 ErrorInfo -> ES의 HistoryErrorInfo 변환
    @Mapping(target = "code", source = "code.name") // Enum -> String 변환
    @Mapping(target = "stage", source = "code.stage") // Enum 내부 값 추출
    HistoryFailInfo toHistoryErrorInfo(FailInfo failInfo);

    @Mapping(target = "historyId", source = "id")
    HistoryResponse.SuccessDTO toSuccessDTO(HistoryDocument document);

    @Mapping(target = "historyId", source = "id")
    @Mapping(target = "failStage", source = "failInfo.stage")
    @Mapping(target = "failRuleName", source = "failInfo.failRuleName")
    HistoryResponse.FailDTO toFailDTO(HistoryDocument document);

    HistoryResponse.DetailDTO toDetailDTO(HistoryDocument historyDocument);
}
