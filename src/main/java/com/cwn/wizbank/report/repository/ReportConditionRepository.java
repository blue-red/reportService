package com.cwn.wizbank.report.repository;

import com.cwn.wizbank.report.entity.ReportCondition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReportConditionRepository extends CrudRepository<ReportCondition,Long>{
}
