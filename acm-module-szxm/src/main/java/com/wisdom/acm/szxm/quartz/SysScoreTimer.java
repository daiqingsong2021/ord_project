package com.wisdom.acm.szxm.quartz;

import com.wisdom.acm.szxm.service.sysscore.SysScoreService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 系统评分 每月25号
 */
public class SysScoreTimer extends QuartzJobBean {

    /**
     * logback
     */
    private static final Logger logger = LoggerFactory.getLogger(SysScoreTimer.class);

    /**
     * 系统评分Service
     */
    @Autowired
    private SysScoreService sysScoreService;

    /**
     * 每月25号 系统评分
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String projectIdStr = String.valueOf(jobExecutionContext.getJobDetail().getJobDataMap().get("projectId"));
        Integer projectId = null;
        try {
            projectId = Integer.parseInt(projectIdStr);
        } catch (NumberFormatException e) {
            logger.error("项目id转换错误，项目id为{}", projectIdStr);
        }
        List<String> sectionIds = sysScoreService.selectEvaluationSections(projectId);
        if (ObjectUtils.isEmpty(sectionIds)) {
            logger.info("当前暂无需要评分的标段");
            return;
        }
        for (String sectionId : sectionIds) {
            //生成评分
            sysScoreService.createSysScores(projectId, sectionId);
        }
    }
}
