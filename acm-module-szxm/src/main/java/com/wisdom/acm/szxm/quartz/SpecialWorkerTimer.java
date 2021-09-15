package com.wisdom.acm.szxm.quartz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkCertService;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkCertVo;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.form.SysMessageAddForm;
import com.wisdom.base.common.vo.ProjectTeamUserVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.UserVo;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 特殊工种 定时执行Job
 * 每天凌晨跑任务，如果预警或者过期。提醒施工单位项目经理，监理单位的总监，以及相关业主代表
 */
public class SpecialWorkerTimer extends QuartzJobBean
{
    /**
     * logback
     */
    private static  final Logger logger = LoggerFactory.getLogger(SpecialWorkerTimer.class);

    @Autowired private SpecialWorkCertService specialWorkCertService;

    @Autowired private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    /**
     * 定时任务逻辑实现方法
     * 每当触发器触发时会执行该方法逻辑
     *
     * @param jobExecutionContext 任务执行上下文
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        logger.info("特殊工种定时扫描任务开始执行，开始时间：{}", new Date());
        List<SpecialWorkCertVo> specialWorkCertVoList = specialWorkCertService.selectSwWorkWithWorker();
        List<Map<String, Object>> warnList = Lists.newArrayList();

        //项目-标段 缓存Map
        Map<Integer,List<ProjectTeamVo>> projectTeamMap=Maps.newHashMap();

        for (SpecialWorkCertVo specialWorkCertVo : specialWorkCertVoList)
        {
            if(ObjectUtils.isEmpty(projectTeamMap.get(specialWorkCertVo.getProjectId())))
            {
                List<ProjectTeamVo> sectionVos = commProjectTeamService.querySectioinListByProjectId(specialWorkCertVo.getProjectId());
                projectTeamMap.put(specialWorkCertVo.getProjectId(),sectionVos);
            }

            Date certExpirationTime = specialWorkCertVo.getCertExpirationTime();
            Date warnDate = specialWorkCertVo.getWarnDate();
            Date nowDate = new Date();
            if (nowDate.equals(warnDate) || (nowDate.after(warnDate) && nowDate.before(certExpirationTime)) || nowDate.equals(certExpirationTime) )
            {//预警
                Map<String, Object> warnMap = Maps.newHashMap();
                warnMap.put("content",
                        "请注意：姓名：" + specialWorkCertVo.getPeopleName() + "(" + specialWorkCertVo.getOrgName() + ")" + "的特殊工种证书即将到期！" + "证书编号："
                                + specialWorkCertVo.getCertNum() + " 证书有效期：" + DateUtil.getDateFormat(specialWorkCertVo.getCertExpirationTime()));
                warnMap.put("sectionId",specialWorkCertVo.getSectionId());
                warnMap.put("projectId",specialWorkCertVo.getProjectId());
                warnMap.put("title","智慧工程项目系统-特殊工种证书预警提醒");
                warnList.add(warnMap);
            }
            else if (nowDate.after(certExpirationTime))
            {//过期
                Map<String, Object> warnMap = Maps.newHashMap();
                warnMap.put("content",
                        "请注意：姓名：" + specialWorkCertVo.getPeopleName() + "(" + specialWorkCertVo.getOrgName() + ")" + "的特殊工种证书已过期！" + "证书编号："
                                + specialWorkCertVo.getCertNum() + " 证书有效期：" + DateUtil.getDateFormat(specialWorkCertVo.getCertExpirationTime()));
                warnMap.put("sectionId",specialWorkCertVo.getSectionId());
                warnMap.put("projectId",specialWorkCertVo.getProjectId());
                warnMap.put("title","智慧工程项目系统-特殊工种证书到期提醒");
                warnList.add(warnMap);
            }

        }
        for (Map<String, Object> warnMap : warnList)
        {
            String projectId=String.valueOf(warnMap.get("projectId"));
            String sectionId=String.valueOf(warnMap.get("sectionId"));
            String title=String.valueOf(warnMap.get("title"));
            String content=String.valueOf(warnMap.get("content"));

            List<ProjectTeamUserVo> sgdwXmjls= szxmCommonUtil.getSgdwRoleUser(projectTeamMap.get(Integer.valueOf(projectId)),Integer.valueOf(sectionId),"SG_XMJL");//根据标段获取施工单位项目经理
            List<ProjectTeamUserVo> jldwZjs= szxmCommonUtil.getJldwRoleUser(projectTeamMap.get(Integer.valueOf(projectId)),Integer.valueOf(sectionId),"GL_ZJ");//根据标段获取监理单位总监

            List<ProjectTeamUserVo> owners=szxmCommonUtil.getOwnerList(Integer.valueOf(sectionId));//根据标段ID获取业主代表
            List<UserVo> recvUsers=Lists.newArrayList();
            for(ProjectTeamUserVo projectTeamUserVo:sgdwXmjls)
            {
                recvUsers.add(projectTeamUserVo.getUser());
            }
            for(ProjectTeamUserVo projectTeamUserVo:jldwZjs)
            {
                recvUsers.add(projectTeamUserVo.getUser());
            }
            for(ProjectTeamUserVo projectTeamUserVo:owners)
            {
                recvUsers.add(projectTeamUserVo.getUser());
            }
            if(!ObjectUtils.isEmpty(recvUsers))
            {
                SysMessageAddForm sysMessageAddForm=new SysMessageAddForm();
                sysMessageAddForm.setTitle(title);
                sysMessageAddForm.setContent(content);
                sysMessageAddForm.setRecvUser(recvUsers);
                sysMessageAddForm.setClaimDealTime(new Date());
                szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
            }
        }
        logger.info("特殊工种定时扫描任务结束，结束时间：{}", new Date());
    }

}
