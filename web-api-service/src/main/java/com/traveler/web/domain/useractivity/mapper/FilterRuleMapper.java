package com.traveler.web.domain.useractivity.mapper;

import com.traveler.web.domain.useractivity.client.dto.FilterClientNode;
import com.traveler.web.domain.useractivity.client.dto.request.FilterRuleClientRequest;
import com.traveler.web.domain.useractivity.client.dto.response.FilterRuleClientResponse;
import com.traveler.web.domain.useractivity.dto.FilterNode;
import com.traveler.web.domain.useractivity.dto.request.FilterRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.FilterRuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilterRuleMapper {
    FilterRuleClientRequest.CreateDTO toClientCreateDTO(FilterRuleRequest.CreateDTO dto);

    FilterRuleClientRequest.UpdateDTO toClientUpdateDTO(FilterRuleRequest.UpdateDTO dto);

    @SubclassMapping(source = FilterNode.Condition.class, target = FilterClientNode.Condition.class)
    @SubclassMapping(source = FilterNode.Operator.class, target = FilterClientNode.Operator.class)
    @SubclassMapping(source = FilterNode.Paren.class, target = FilterClientNode.Paren.class)
    FilterClientNode.Element toClientElement(FilterNode.Element element);

    FilterClientNode.Condition toClientCondition(FilterNode.Condition condition);

    FilterClientNode.Operator toClientOperator(FilterNode.Operator operator);

    FilterClientNode.Paren toClientParen(FilterNode.Paren paren);

    FilterRuleResponse.CreateDTO toResponseCreateDTO(FilterRuleClientResponse.CreateDTO clientResponse);

    FilterRuleResponse.UpdateDTO toResponseUpdateDTO(FilterRuleClientResponse.UpdateDTO clientResponse);

    FilterRuleResponse.DeleteDTO toResponseDeleteDTO(FilterRuleClientResponse.DeleteDTO clientResponse);

    FilterRuleResponse.ListDTO toResponseListDTO(FilterRuleClientResponse.ListDTO listDTO);

    FilterRuleResponse.DetailDTO toResponseDetailDTO(FilterRuleClientResponse.DetailDTO clientResponse);

    @SubclassMapping(source = FilterClientNode.Condition.class, target = FilterNode.Condition.class)
    @SubclassMapping(source = FilterClientNode.Operator.class, target = FilterNode.Operator.class)
    @SubclassMapping(source = FilterClientNode.Paren.class, target = FilterNode.Paren.class)
    FilterNode.Element toResponseElement(FilterClientNode.Element element);

    FilterNode.Condition toResponseCondition(FilterClientNode.Condition condition);

    FilterNode.Operator toResponseOperator(FilterClientNode.Operator operator);

    FilterNode.Paren toResponseParen(FilterClientNode.Paren paren);
}
