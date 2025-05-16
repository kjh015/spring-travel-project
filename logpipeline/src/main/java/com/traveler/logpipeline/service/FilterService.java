package com.traveler.logpipeline.service;

import com.traveler.logpipeline.entity.Filter;
import com.traveler.logpipeline.repository.FilterRepository;
import com.traveler.logpipeline.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterService {
    private final FilterRepository filterRepository;
    private final ProcessRepository processRepository;

    public FilterService(FilterRepository filterRepository, ProcessRepository processRepository) {
        this.filterRepository = filterRepository;
        this.processRepository = processRepository;
    }

    public List<Filter> listFilters(Long processId){
        return filterRepository.findAllByProcess_Id(processId);
    }
    public Filter viewFilter(Long filterId){
        return filterRepository.findById(filterId).orElse(null);
    }
    public void addFilter(Filter filter, Long processId){
        filter.setProcess(processRepository.findById(processId).orElse(null));
        filterRepository.save(filter);
    }
    public void updateFilter(Filter inFilter){
        Filter filter = filterRepository.findById(inFilter.getId()).orElse(null);
        if(filter != null){
            filter.setName(inFilter.getName());
            filter.setActive(inFilter.isActive());
            filter.setSourceCode(inFilter.getSourceCode());
            filter.setTokensJson(inFilter.getTokensJson());
            filter.setUsedField(inFilter.getUsedField());
            filterRepository.save(filter);
        }
    }
    public void removeFilter(Long filterId){
        filterRepository.deleteById(filterId);
    }
    public List<Filter> activeFilters(Long processId){
        return filterRepository.findAllByIsActiveTrueAndProcess_Id(processId);
    }

}
