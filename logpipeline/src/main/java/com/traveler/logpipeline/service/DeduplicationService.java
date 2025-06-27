package com.traveler.logpipeline.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.dto.DeduplicationDto;
import com.traveler.logpipeline.entity.Deduplication;
import com.traveler.logpipeline.entity.LogPassHistory;
import com.traveler.logpipeline.repository.DeduplicationRepository;
import com.traveler.logpipeline.repository.LogPassHistoryRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeduplicationService {
    private final DeduplicationRepository deduplicationRepository;
    private final LogPassHistoryRepository logPassHistoryRepository;
    private final ProcessRepository processRepository;
    private final ObjectMapper objectMapper;


    @Transactional
    public List<DeduplicationDto> getDeduplicationList(Long processId){
        List<Deduplication> ddpList = deduplicationRepository.findAllByProcess_Id(processId);

        return ddpList.stream()
                .map(ddp -> DeduplicationDto.builder()
                        .id(String.valueOf(ddp.getId()))
                        .name(ddp.getName())
                        .active(ddp.isActive())
                        .createdTime(ddp.getCreatedTime())
                        .updatedTime(ddp.getUpdatedTime())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public DeduplicationDto viewDeduplication(Long deduplicationId){
        Deduplication ddp = deduplicationRepository.findById(deduplicationId).orElseThrow(() -> new CustomLogException("존재하지 않는 중복제거 설정입니다."));
        List<DeduplicationDto.RowDto> rows; // RowType은 rows의 실제 타입으로 수정
        try {
            rows = objectMapper.readValue(ddp.getDeduplicationJson(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new CustomLogException("JSON 파싱 오류: " + e.getMessage());
        }

        return DeduplicationDto.builder()
                .id(String.valueOf(ddp.getId()))
                .name(ddp.getName())
                .active(ddp.isActive())
                .rows(rows)
                .build();
    }

    @Transactional
    public void addDeduplication(DeduplicationDto data){
        Deduplication deduplication = new Deduplication();
        try {
            deduplication.setName(data.getName());
            deduplication.setProcess(processRepository.findById(Long.valueOf(data.getProcessId())).orElseThrow(() -> new CustomLogException("존재하지 않는 프로세스입니다.")));
            deduplication.setDeduplicationJson(objectMapper.writeValueAsString(data.getRows()));
            deduplication.setActive(data.isActive());
            deduplicationRepository.save(deduplication);
        } catch (JsonProcessingException e) {
            throw new CustomLogException("JSON Parsing Error");
        }

    }
    @Transactional
    public void updateDeduplication(DeduplicationDto data){
        try {
            Deduplication deduplication = deduplicationRepository.findById(Long.valueOf(data.getId())).orElseThrow(() -> new CustomLogException("존재하지 않는 중복제거 설정입니다."));
            deduplication.setName(data.getName());
            deduplication.setActive(data.isActive());
            deduplication.setDeduplicationJson(objectMapper.writeValueAsString(data.getRows()));
        } catch (JsonProcessingException e) {
            throw new CustomLogException("JSON Parsing Error");
        }
    }
    @Transactional
    public void removeDeduplication(Long deduplicationId){
        deduplicationRepository.deleteById(deduplicationId);
    }

    public List<Deduplication> getActiveDeduplication(Long processId){
        return deduplicationRepository.findAllByIsActiveTrueAndProcess_Id(processId);
    }

    @Transactional
    public void addPassHistory(Deduplication ddp, Long processId, LocalDateTime expT, String userId, String logJson){
        LogPassHistory history = new LogPassHistory();
        history.setDeduplication(ddp);
        history.setProcess(processRepository.findById(processId).orElse(null));
        history.setLogJson(logJson);
        history.setExpiredTime(expT);
        history.setUserId(userId);
        logPassHistoryRepository.save(history);
    }

    @Transactional
    public void updatePassHistory(LogPassHistory history, LocalDateTime expT){
        history.setExpiredTime(expT);
        logPassHistoryRepository.save(history);
    }

    @Transactional
    public List<LogPassHistory> getPassHistory(Long ddpId, String userId){
        return logPassHistoryRepository.findAllByDeduplication_idAndUserId(ddpId, userId);

    }




}
