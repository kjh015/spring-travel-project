package com.traveler.logpipeline.service;

import com.traveler.logpipeline.entity.Filter;
import com.traveler.logpipeline.repository.FilterRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterService {
    private final FilterRepository filterRepository;

    public FilterService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    public List<Filter> listFilters(){
        return filterRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
    public Filter viewFilter(Long filterId){
        return filterRepository.findById(filterId).orElse(null);
    }
    public void addFilter(Filter filter){
        filterRepository.save(filter);
    }
    public void updateFilter(Filter inFilter){
        Filter filter = filterRepository.findById(inFilter.getId()).orElse(null);
        if(filter != null){
            filter.setName(inFilter.getName());
            filter.setActive(inFilter.isActive());
            filter.setSourceCode(inFilter.getSourceCode());
            filterRepository.save(filter);
        }
    }
    public void removeFilter(Long filterId){
        filterRepository.deleteById(filterId);
    }
}
