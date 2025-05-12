package com.traveler.logpipeline.service;

import com.traveler.logpipeline.entity.Format;
import com.traveler.logpipeline.repository.FormatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatService {
    private final FormatRepository formatRepository;

    public FormatService(FormatRepository formatRepository) {
        this.formatRepository = formatRepository;
    }

    public List<Format> listFormats(String processId){
        return formatRepository.findAllByProcessId(processId);
    }

    public Format viewFormat(String formatId){
        return formatRepository.findById(formatId).orElse(null);
    }

    public void addFormat(Format format){
        formatRepository.save(format);
    }

    public void updateFormat(Format inFormat){
        Format format = formatRepository.findById(inFormat.getId()).orElse(null);
        if(format != null){
            format.setName(inFormat.getName());
            format.setFormatJson(inFormat.getFormatJson());
            formatRepository.save(format);
        }
    }
    public void removeFormat(String formatId){
        formatRepository.deleteById(formatId);
    }


}
