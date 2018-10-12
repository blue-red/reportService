package com.cwn.wizbank.report.repository;

import com.cwn.wizbank.report.entity.ReportTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReportTemplateRepository extends CrudRepository<ReportTemplate,Long>{
}
