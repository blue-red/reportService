package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.common.ExportReportUtil;
import com.cwn.wizbank.report.config.DbContextHolder;
import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.entity.ReportHistory;
import com.cwn.wizbank.report.enums.DBTypeEnum;
import com.cwn.wizbank.report.enums.GenerationTypeEnum;
import com.cwn.wizbank.report.event.EventPublisher;
import com.cwn.wizbank.report.repository.ReportHistoryRepository;
import com.github.abel533.echarts.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 报表抽象实现类
 * @author Andrew.xiao 2018/5/2
 */
public abstract class AbstractReportService implements IReportService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReportConditionService reportConditionService;

    @Autowired
    private ReportHistoryRepository reportHistoryRepository;

    @Autowired
    private ReportHistoryService reportHistoryService;

    @Autowired
    private ReportArchiveService reportArchiveService;

    @Autowired
    private QueryParamsService queryParamsService;

    private Integer executeThreadCount = 30;

    /**
     * 线程池
     */
    private ExecutorService fixedThreadPool;

    /**
     * 初始化线程池
     */
    @PostConstruct
    private void initThreadPool() {
        fixedThreadPool = Executors.newFixedThreadPool(executeThreadCount);
    }

    @Override
    public List<Map<String,Object>> execute(ReportCondition reportCondition,ExecuteCallback executeCallback) {
        //验证参数
        queryParamsService.validationParams(reportCondition);
        //解析参数
        Map<String, Object> paramMap = queryParamsService.getParamMap(reportCondition);
        //返回结果的
        List<Map<String,Object>> data = new ArrayList<>();
        //是否后台生成
        boolean isBack = reportCondition.getIsBack();
        if (!isBack) {
            //立即生成，获取数据
            DbContextHolder.setDbType(DBTypeEnum.core);//数据源切换到wizbank核心库
            data = fetchData(reportCondition,paramMap);
            //导出
            export(data, reportCondition, queryParamsService.mergeQueryParam(reportCondition.getParams(),paramMap));
            if (executeCallback != null) {
                executeCallback.onFinish(data,"excel file");
            }
            DbContextHolder.setDbType(DBTypeEnum.report);//数据源切换到报表数据库
            return data;
        }
        if(reportCondition.getId() == null || 0 == reportCondition.getId()){
            //保存报表查询条件
            reportCondition = reportConditionService.saveReportCondition(reportCondition);
        }
        //后台生成，导出报表
        runExportThread(reportCondition,paramMap,executeCallback);
        return data;
    }

    /***
     * 根据报表条件，导出excel文档
     * @param reportCondition
     */
    public void runExportThread(final ReportCondition reportCondition, final Map<String, Object> paramMap, final ExecuteCallback executeCallback){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                //生成历史记录，状态为生成中
                ReportHistory reportHistory = reportHistoryService.saveReportHistoryInfo(reportCondition);
                reportHistory.setParams(queryParamsService.mergeQueryParam(reportCondition.getParams(),paramMap));
                reportHistory.setStatus(GenerationTypeEnum.SUCCESS);
                try {
                    //根据条件获取数据
                    DbContextHolder.setDbType(DBTypeEnum.core);//数据源切换到wizbank核心库
                    List<Map<String,Object>> data = fetchData(reportCondition,paramMap);

                    DbContextHolder.setDbType(DBTypeEnum.report);//数据源切换到报表数据库
                    //归档信息保存
                    //reportArchiveService.saveReportArchiveInfo(reportHistory, data);
                    //导出
                    export(data, reportCondition, reportHistory.getParams());
                    if (executeCallback != null) {
                        executeCallback.onFinish(data,"excel file");
                    }
                }catch (Exception e){
                    DbContextHolder.setDbType(DBTypeEnum.report);//数据源切换到报表数据库
                    reportHistory.setStatus(GenerationTypeEnum.FAILE);
                    reportHistoryRepository.save(reportHistory);
                    logger.error(e.getMessage());
                }
                //更新历史记录，状态，成功或失败
                reportHistory.setCompleteTime(new Date());
                reportHistoryRepository.save(reportHistory);
                //TODO  站内信
            }
        });
    }

    private void export(List<Map<String, Object>> data, ReportCondition reportCondition, Map<String,Object> exportParams) {
        //导出必填的列
        List<String> showFieldRequires = (List<String>) reportCondition.getParams().get("showFieldRequire");
        //导出可选的列
        List<String> showFieldOptionals = (List<String>) reportCondition.getParams().get("showFieldOptional");
        showFieldRequires.addAll(showFieldOptionals);

        //获取（拼装）要显示在eChart图表上的数据
        Map<String, Object> chartData = getChartData(data, reportCondition);
        Option option = null;
        if(!CollectionUtils.isEmpty(chartData)){
            //获取eChart统计图表的option
            option = getChartOption(chartData);
        }

        //导出报表
        ExportReportUtil.exportExcel(reportCondition.getType(),exportParams,showFieldRequires,data,option);
    }

    /**
     * 获取报表数据
     * @param reportCondition 查询条件
     * @return
     */
    public abstract List<Map<String,Object>> fetchData(ReportCondition reportCondition, Map<String,Object> paramMap);

    /**
     * 获取要显示在eChart图表上的数据
     * @param data 源数据
     * @return
     */
    public abstract Map<String, Object> getChartData(List<Map<String, Object>> data, ReportCondition reportCondition);

    /**
     * 获取生成部门学习统计报表统计图的数据结构
     * @param chartData 要显示在eChart图表上的数据
     * @return
     */
    public abstract Option getChartOption(Map<String, Object> chartData);

}
