package com.cwn.wizbank.report.repository;

import com.cwn.wizbank.report.entity.ReportHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReportHistoryRepository extends CrudRepository<ReportHistory,Long>{
}
