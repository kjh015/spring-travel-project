package com.traveler.logpipeline.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.dto.FormatResponseDto;
import com.traveler.logpipeline.entity.Format;
import com.traveler.logpipeline.repository.FormatRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormatService {
    private final FormatRepository formatRepository;
    private final ProcessRepository processRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FormatService(FormatRepository formatRepository, ProcessRepository processRepository) {
        this.formatRepository = formatRepository;
        this.processRepository = processRepository;
    }

    public List<FormatResponseDto> listFormats(Long processId){
        List<Format> formatList = formatRepository.findAllByProcess_Id(processId);
        return formatList.stream().map(format -> FormatResponseDto.builder()
                .id(format.getId())
                .name(format.getName())
                .isActive(format.isActive())
                .updatedTime(format.getUpdatedTime())
                .createdTime(format.getCreatedTime()).build()
        ).collect(Collectors.toList());
    }

    public FormatResponseDto viewFormat(Long formatId){
        Format format = formatRepository.findById(formatId).orElse(null);
        if(format != null){
            return FormatResponseDto.builder()
                    .id(format.getId())
                    .name(format.getName())
                    .formatJson(format.getFormatJson())
                    .defaultJson(format.getDefaultJson())
                    .isActive(format.isActive())
                    .updatedTime(format.getUpdatedTime())
                    .createdTime(format.getCreatedTime()).build();
        }
        return null;
    }

    public void addFormat(Format format, Long processId){
        format.setProcess(processRepository.findById(processId).orElse(null));        ;
        formatRepository.save(format);
    }

    public void updateFormat(Format inFormat){
        Format format = formatRepository.findById(inFormat.getId()).orElse(null);
        if(format != null){
            format.setName(inFormat.getName());
            format.setDefaultJson(inFormat.getDefaultJson());
            format.setFormatJson(inFormat.getFormatJson());
            format.setActive(inFormat.isActive());
            formatRepository.save(format);
        }
    }
    public void removeFormat(Long formatId){
        formatRepository.deleteById(formatId);
    }
    public List<Format> activeFormats(Long processId){
        return formatRepository.findAllByIsActiveTrueAndProcess_Id(processId);
    }
    public List<String> activeFormatKeys(Long processId){
        Set<String> keys = new HashSet<>();
        List<Format> activeFormats = formatRepository.findAllByIsActiveTrueAndProcess_Id(processId);
        try{
            for(Format format : activeFormats){
                Map<String, String> formatInfo = objectMapper.readValue(format.getFormatJson(), new TypeReference<>() {});
                Map<String, String> defaultInfo = objectMapper.readValue(format.getDefaultJson(), new TypeReference<>() {});
                keys.addAll(formatInfo.keySet());
                keys.addAll(defaultInfo.keySet());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(keys);
    }


}
