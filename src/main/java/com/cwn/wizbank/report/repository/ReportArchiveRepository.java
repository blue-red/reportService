package com.cwn.wizbank.report.repository;

import com.cwn.wizbank.report.entity.ReportArchive;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReportArchiveRepository extends CrudRepository<ReportArchive,Long>{

    ReportArchive getByReportHistoryId(Long reportHistoryId);

}
