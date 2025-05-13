package com.traveler.logpipeline.service;

import com.traveler.logpipeline.entity.Format;
import com.traveler.logpipeline.repository.FormatRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatService {
    private final FormatRepository formatRepository;
    private final ProcessRepository processRepository;

    public FormatService(FormatRepository formatRepository, ProcessRepository processRepository) {
        this.formatRepository = formatRepository;
        this.processRepository = processRepository;
    }

    public List<Format> listFormats(Long processId){
        return formatRepository.findAllByProcess_Id(processId);
    }

    public Format viewFormat(Long formatId){
        return formatRepository.findById(formatId).orElse(null);
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


}
