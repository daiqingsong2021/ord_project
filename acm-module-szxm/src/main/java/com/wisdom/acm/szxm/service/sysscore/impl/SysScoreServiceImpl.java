package com.wisdom.acm.szxm.service.sysscore.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.mapper.sysscore.SysScoreMapper;
import com.wisdom.acm.szxm.po.sysscore.ObjectScoreDetailPo;
import com.wisdom.acm.szxm.po.sysscore.ObjectTemplatePo;
import com.wisdom.acm.szxm.po.sysscore.SysScorePo;
import com.wisdom.acm.szxm.service.rygl.HolidayService;
import com.wisdom.acm.szxm.service.rygl.PeopleChangeService;
import com.wisdom.acm.szxm.service.rygl.PeopleEntryService;
import com.wisdom.acm.szxm.service.rygl.ProjInfoService;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkerService;
import com.wisdom.acm.szxm.service.sysscore.ObjectScoreDetailService;
import com.wisdom.acm.szxm.service.sysscore.ObjectTemplateService;
import com.wisdom.acm.szxm.service.sysscore.SubjectScoreDetailService;
import com.wisdom.acm.szxm.service.sysscore.SysScoreService;
import com.wisdom.acm.szxm.service.wtgl.QuestionService;
import com.wisdom.acm.szxm.vo.sysscore.ObjectScoreItemVo;
import com.wisdom.acm.szxm.vo.sysscore.ObjectScoreVo;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreVo;
import com.wisdom.acm.szxm.vo.sysscore.SysScoreVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-17 16:19
 * Description：<描述>
 */
@Service
@Slf4j
/**
 * 系统评分实现类
 */
public class SysScoreServiceImpl extends BaseService<SysScoreMapper, SysScorePo> implements SysScoreService {
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;
    @Autowired
    private CommPlanProjectService commPlanProjectService;

    //客观模板
    @Autowired
    private ObjectTemplateService objectTemplateService;
    //客观评分
    @Autowired
    private ObjectScoreDetailService objectScoreDetailService;

    //主观评分
    @Autowired
    private SubjectScoreDetailService subjectScoreDetailService;

    //人员管理
    @Autowired
    private ProjInfoService projInfoService;
    @Autowired
    private PeopleEntryService peopleEntryService;
    @Autowired
    private PeopleChangeService peopleChangeService;
    @Autowired
    private SpecialWorkerService specialWorkerService;
    @Autowired
    private HolidayService holidayService;

    //问题
    @Autowired
    private QuestionService questionService;

    @Override
    public ObjectScoreVo getSysObjectScores(Map<String, Object> mapWhere) {
        List<ObjectScoreItemVo> objectScoreItemVos = objectTemplateService.selectMainItemObjectTemplates();
        if (ObjectUtils.isEmpty(objectScoreItemVos)) {
            log.error("当前暂无客观模板数据");
            return null;
        }
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        String sectionId = StringHelper.formattString(mapWhere.get("sectionId"));
        if (StringHelper.isNullAndEmpty(projectId) || StringHelper.isNullAndEmpty(sectionId)) {
            throw new BaseException("项目id或标段id不能为空");
        }
        String year = StringHelper.formattString(mapWhere.get("year"));
        String month = StringHelper.formattString(mapWhere.get("month"));
        if (StringHelper.isNullAndEmpty(year) || StringHelper.isNullAndEmpty(month)) {
            throw new BaseException("查询客观评分年、月不能为空");
        }

        Example example = new Example(ObjectScoreDetailPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("projectId", projectId);
        List<ObjectScoreDetailPo> scoreDetailPos = objectScoreDetailService.selectByExample(example);
        if (ObjectUtils.isEmpty(scoreDetailPos)) {
            log.info("当前暂无标段id为[{}],{}年{}月客观系统评分记录", sectionId, year, month);
            throw new BaseException("当前暂无" + year + "年" + month + "月客观系统评分记录");
        }
        Map<String, ObjectScoreDetailPo> detailPoMap = ListUtil.listToMap(scoreDetailPos, "itemCode", String.class);

        ObjectScoreVo objectScoreVo = getObjectScoreVo(objectScoreItemVos, detailPoMap);
        objectScoreVo.setSectionId(Integer.parseInt(sectionId));
        objectScoreVo.setProjectId(Integer.parseInt(projectId));
        objectScoreVo.setCreatTime(scoreDetailPos.get(0).getCreatTime());

        //项目名称
        PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.parseInt(projectId));
        objectScoreVo.setProjectName(projectVo.getName());
        //标段缓存
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
        ProjectTeamVo sectionVo = sectionMap.get(sectionId);
        if (!ObjectUtils.isEmpty(sectionVo)) {
            objectScoreVo.setSectionCode(sectionVo.getCode());
            objectScoreVo.setSectionName(sectionVo.getName());

            objectScoreVo.setSgdw("");
            if (!ObjectUtils.isEmpty(sectionVo.getCuList())) {
                objectScoreVo.setSgdw(sectionVo.getCuList().get(0).getName());
            }
        }
        return objectScoreVo;
    }

    @Override
    public PageInfo<SysScoreVo>  getSysScores(Map<String, Object> mapWhere, List<String> sectionList, Integer pageSize, Integer currentPageNum) {
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        mapWhere.put("sectionList", sectionList);

        PageHelper.startPage(currentPageNum, pageSize);
        List<SysScoreVo> sysScoreVos = mapper.selectSysScoreList(mapWhere);
        //Collections.sort(sysScoreVos);
        PageInfo<SysScoreVo> pageInfo = new PageInfo<>(sysScoreVos);

        if (ObjectUtils.isEmpty(pageInfo.getList())) {
            log.info("当前暂无标段id为[{}],系统评分记录", sectionList);
            return pageInfo;
        }
        getSysScoreVo(projectId, pageInfo.getList());
        return pageInfo;
    }

    private void getSysScoreVo(String projectId, List<SysScoreVo> sysScoreVos) {
        getSubjectScore(projectId, sysScoreVos);

        PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.parseInt(projectId));
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
        for (SysScoreVo sysScoreVo : sysScoreVos) {
            //项目名称
            sysScoreVo.setProjectName(projectVo.getName());
            //标段缓存
            ProjectTeamVo sectionVo = sectionMap.get(sysScoreVo.getSectionId());
            if (!ObjectUtils.isEmpty(sectionVo)) {
                sysScoreVo.setSectionCode(sectionVo.getCode());
                sysScoreVo.setSectionName(sectionVo.getName());

                sysScoreVo.setSgdw("");
                if (!ObjectUtils.isEmpty(sectionVo.getCuList())) {
                    sysScoreVo.setSgdw(sectionVo.getCuList().get(0).getName());
                }
            }
            //设置流程
            sysScoreVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(sysScoreVo.getStatusVo().getCode()).getName());
        }
    }

    @Override
    public List<SysScoreVo> getAllSysScores(Map<String, Object> mapWhere, List<String> sectionList) {
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        mapWhere.put("sectionList", sectionList);

        List<SysScoreVo> sysScoreVos = mapper.selectSysScoreList(mapWhere);
        // Collections.sort(sysScoreVos);
        if (ObjectUtils.isEmpty(sysScoreVos)) {
            log.info("当前暂无标段id为[{}],系统评分记录", sectionList);
            return sysScoreVos;
        }
        getSysScoreVo(projectId, sysScoreVos);
        return sysScoreVos;
    }

    @Override
    public List<SysScoreVo> getFlowSysScores(Map<String, Object> mapWhere) {
        String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
        List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
        mapWhere.put("ids", ids);

        List<SysScoreVo> sysScoreVos = mapper.selectSysScoreList(mapWhere);
        // Collections.sort(sysScoreVos); TODO
        if (ObjectUtils.isEmpty(sysScoreVos)) {
            log.info("当前暂无id为[{}]系统评分记录", ids);
            throw new BaseException("当前暂无系统评分记录");
        }
        Integer projectId = sysScoreVos.get(0).getProjectId();
        PlanProjectVo projectVo = commPlanProjectService.getProject(projectId);
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
        for (SysScoreVo sysScoreVo : sysScoreVos) {
            //项目名称
            sysScoreVo.setProjectName(projectVo.getName());
            //标段缓存
            ProjectTeamVo sectionVo = sectionMap.get(sysScoreVo.getSectionId());
            if (!ObjectUtils.isEmpty(sectionVo)) {
                sysScoreVo.setSectionCode(sectionVo.getCode());
                sysScoreVo.setSectionName(sectionVo.getName());

                sysScoreVo.setSgdw("");
                if (!ObjectUtils.isEmpty(sectionVo.getCuList())) {
                    sysScoreVo.setSgdw(sectionVo.getCuList().get(0).getName());
                }
            }
            //设置流程
            sysScoreVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(sysScoreVo.getStatusVo().getCode()).getName());
        }
        return sysScoreVos;
    }

    /**
     * 获取最新主观得分（从主观明细表中查询，判断主观评分是否有更新，若有则更新系统综合得分）
     *
     * @param projectId
     * @param sysScoreVos
     */
    private void getSubjectScore(String projectId, List<SysScoreVo> sysScoreVos) {
        for (SysScoreVo sysScoreVo : sysScoreVos) {
            // 若状态不为新建，主观分不再更新
            if(!SzxmEnumsUtil.StatusEnum.INIT.getCode().equals(sysScoreVo.getStatusVo().getCode())){
                continue;
            }
            Integer sectionId = sysScoreVo.getSectionId();
            Integer year = sysScoreVo.getYear();
            Integer month = sysScoreVo.getMonth();
            //获取主观得分
            Map<String, Object> subjectMap = Maps.newHashMap();
            subjectMap.put("projectId", projectId);
            subjectMap.put("sectionId", sectionId);
            subjectMap.put("year", year);
            subjectMap.put("month", month);
            SubjectScoreVo subjectScoreVo = subjectScoreDetailService.selectSubjectScore(subjectMap, 10, 1);
            BigDecimal subjectiveScore;
            if (!ObjectUtils.isEmpty(subjectScoreVo)) {
                subjectiveScore = subjectScoreVo.getActualScore();
            } else {
                subjectiveScore = new BigDecimal(80);
                log.info("{}年{}月标段id：{} 主观得分为空，取默认值80分", year, month, sectionId);
            }
            if (subjectiveScore.compareTo(sysScoreVo.getSubjectiveScore()) != 0) {
                sysScoreVo.setSubjectiveScore(subjectiveScore);
                BigDecimal totalScore = sysScoreVo.getObjectiveScore().multiply(new BigDecimal(0.7)).add(subjectiveScore.multiply(new BigDecimal(0.3)));
                totalScore = totalScore.setScale(1, BigDecimal.ROUND_HALF_UP);
                sysScoreVo.setTotalScore(totalScore);
                if (totalScore.compareTo(new BigDecimal(80)) == -1) {
                    sysScoreVo.setIsPass("1");
                }else{
                    sysScoreVo.setIsPass("0");
                }

                SysScorePo sysScorePo = new SysScorePo();
                sysScorePo.setId(sysScoreVo.getId());
                sysScorePo.setSubjectiveScore(subjectiveScore);
                sysScorePo.setTotalScore(totalScore);
                sysScorePo.setIsPass(sysScoreVo.getIsPass());
                super.updateSelectiveById(sysScorePo);
                log.info("更新主观得分，id为{}，主观得分为{}，综合得分为{}", sysScoreVo.getId(), sysScoreVo.getSubjectiveScore(),
                        sysScoreVo.getTotalScore());
            }
        }
    }

    @Override
    public List<String> selectEvaluationSections(Integer projectId) {
        return mapper.selectEvaluationSections(projectId);
    }

    /**
     * 获取客观评分项明细
     *
     * @param objectScoreItemVoList 客观评分主项
     * @param detailPoMap           客观扣分表
     * @return
     */
    private ObjectScoreVo getObjectScoreVo(List<ObjectScoreItemVo> objectScoreItemVoList, Map<String, ObjectScoreDetailPo> detailPoMap) {
        List<ObjectScoreItemVo> scoreItemVos = Lists.newArrayList();
        //总分
        BigDecimal totalScore = new BigDecimal(0);
        //实际得分
        BigDecimal actualScore = new BigDecimal(0);

        for (ObjectScoreItemVo itemVo : objectScoreItemVoList) {
            Integer maxScore = itemVo.getMaxScore();
            totalScore = totalScore.add(new BigDecimal(maxScore));
            List<ObjectScoreItemVo> objectDetailItemVos = objectTemplateService.selectDetailItemObjectTemplates(itemVo.getId());
            if (ObjectUtils.isEmpty(objectDetailItemVos)) {
                log.error("当前考核项：{} 暂无客观模板数据", itemVo.getCheckTitle());
                return null;
            }
            BigDecimal score = new BigDecimal(0);
            for (ObjectScoreItemVo objectScoreItemVo : objectDetailItemVos) {
                ObjectScoreDetailPo objectScoreDetailPo = detailPoMap.get(objectScoreItemVo.getItemCode());
                if (!ObjectUtils.isEmpty(objectScoreDetailPo)) {
                    score = score.add(objectScoreDetailPo.getDeduction());
                    objectScoreItemVo.setViolateCount(objectScoreDetailPo.getDeductionCount());
                    objectScoreItemVo.setScore(objectScoreDetailPo.getDeduction());
                }
            }
            ObjectTemplatePo templatePo = new ObjectTemplatePo();
            templatePo.setMaxScore(itemVo.getMaxScore());
            templatePo.setMinScore(itemVo.getMinScore());
            BigDecimal realScore = getRealScore(score, templatePo);
            actualScore = actualScore.add(realScore);
            itemVo.setScore(realScore);
            scoreItemVos.add(itemVo);
            scoreItemVos.addAll(objectDetailItemVos);
        }
        if (ObjectUtils.isEmpty(scoreItemVos)) {
            throw new BaseException("获取客观评分项失败");
        }

        ObjectScoreVo objectScoreVo = new ObjectScoreVo();
        objectScoreVo.setTotalScore(totalScore);
        objectScoreVo.setActualScore(actualScore);
        objectScoreVo.setScoreItemVos(scoreItemVos);
        return objectScoreVo;
    }

    @Override
    public void createSysScores(Integer projectId, String sectionId) {
        BigDecimal objectScore = new BigDecimal(0);
        BigDecimal peoManageScores = getPeoManageScores(projectId, sectionId);
        BigDecimal deviceManageScores = getDeviceManageScores(projectId, sectionId);
        BigDecimal materialManageScores = getMaterialManageScores(projectId, sectionId);
        BigDecimal planManageScores = getPlanManageScores(projectId, sectionId);
        BigDecimal qualityManageScores = getQualityManageScores(projectId, sectionId);
        BigDecimal securityManageScores = getSecurityManageScores(projectId, sectionId);
        BigDecimal problemManageScores = getProblemManageScores(projectId, sectionId);

        objectScore = objectScore.add(deviceManageScores);
        objectScore = objectScore.add(peoManageScores);
        objectScore = objectScore.add(materialManageScores);
        objectScore = objectScore.add(planManageScores);
        objectScore = objectScore.add(qualityManageScores);
        objectScore = objectScore.add(securityManageScores);
        objectScore = objectScore.add(problemManageScores);

        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;
        //获取主观得分
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("projectId", projectId);
        mapWhere.put("sectionId", sectionId);
        mapWhere.put("year", year);
        mapWhere.put("month", month);
        SubjectScoreVo subjectScoreVo = subjectScoreDetailService.selectSubjectScore(mapWhere, 10, 1);
        BigDecimal subjectiveScore;
        if (!ObjectUtils.isEmpty(subjectScoreVo)) {
            subjectiveScore = subjectScoreVo.getActualScore();
        } else {
            subjectiveScore = new BigDecimal(80);
            log.info("{}年{}月标段id：{} 主观得分为空，取默认值80分", year, month, sectionId);
        }

        BigDecimal totalScore;
        //总分为 0.7*客观分 + 0.3 * 主观分
        totalScore = objectScore.multiply(new BigDecimal(0.7)).add(subjectiveScore.multiply(new BigDecimal(0.3)));

        Example example = new Example(SysScorePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        List<SysScorePo> sysScorePos = this.selectByExample(example);
        if (!ObjectUtils.isEmpty(sysScorePos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月考评得分已存在", projectId, sectionId, year, month);
            return;
        }

        SysScorePo sysScorePo = new SysScorePo();
        sysScorePo.setProjectId(projectId);
        sysScorePo.setSectionId(Integer.parseInt(sectionId));
        sysScorePo.setYear(year);
        sysScorePo.setMonth(month);
        sysScorePo.setObjectiveScore(objectScore);
        sysScorePo.setSubjectiveScore(subjectiveScore);
        totalScore = totalScore.setScale(1, BigDecimal.ROUND_HALF_UP);
        sysScorePo.setTotalScore(totalScore);
        sysScorePo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        log.info("项目id:[{}],标段id:[{}]，{}年{}月，考评综合得分为{}", projectId, sectionId, year, month, totalScore);
        // 0 合格 1 不合格
        sysScorePo.setIsPass("0");
        //小于80为不合格
        if (totalScore.compareTo(new BigDecimal(80)) == -1) {
            sysScorePo.setIsPass("1");
        }
        super.insert(sysScorePo);
    }

    /**
     * 人员管理分数
     *
     * @return
     */
    private BigDecimal getPeoManageScores(Integer projectId, String sectionId) {
        Integer projCount = projInfoService.queryProjInfoNotUpdate(sectionId);
        //检查施工单位对基本信息 不是按条扣分
        int projScore = ObjectUtils.isEmpty(projCount) ? 0 : 1;
        int peopleEntryCount = peopleEntryService.queryPeopleEntryCount(sectionId);
        int peopleChangeCount = peopleChangeService.queryPeopleChangeCount(sectionId);
        int specialWorkerCount = specialWorkerService.querySpecialWorkerCount(sectionId);
        int holidayCount = holidayService.queryHolidayCount(sectionId);

        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;

        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("itemCode", "RY%");
        List<ObjectTemplatePo> objectTemplatePos = objectTemplateService.selectByExample(example);
        //考核编码 --》 数量
        Map<String, Integer> itemCodeCount = Maps.newHashMap();
        //人员基本信息
        itemCodeCount.put("RYPROJ", projScore);
        //人员进退场
        itemCodeCount.put("RYENTRY", peopleEntryCount);
        //人员变更
        itemCodeCount.put("RYBG", peopleChangeCount);
        //特殊工种
        itemCodeCount.put("RYTSGZ", specialWorkerCount);
        //人员请假
        itemCodeCount.put("RYQJ", holidayCount);

        example = new Example(ObjectScoreDetailPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andLike("itemCode", "RY%");
        List<ObjectScoreDetailPo> detailPos = objectScoreDetailService.selectByExample(example);
        if (!ObjectUtils.isEmpty(detailPos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月人员模块考评得分已存在", projectId, sectionId, year, month);
            throw new BaseException("客观考评一个月仅生成一次");
        }

        BigDecimal score = insertObjectScore(projectId, sectionId, year, month, objectTemplatePos, itemCodeCount);
        Map<String, ObjectTemplatePo> templatePoMap = ListUtil.listToMap(objectTemplatePos, "itemCode", String.class);
        ObjectTemplatePo templatePo = templatePoMap.get("RY");
        BigDecimal realScore = getRealScore(score, templatePo);
        log.info("人员模块客观评分完毕,得分为{}，{}年{}月", realScore, year, month);
        return realScore;
    }

    private BigDecimal getRealScore(BigDecimal score, ObjectTemplatePo templatePo) {
        BigDecimal maxScore = new BigDecimal(templatePo.getMaxScore());
        BigDecimal minScore = new BigDecimal(templatePo.getMinScore());
        BigDecimal realScore = maxScore.subtract(score);
        if (realScore.compareTo(minScore) == -1) {
            realScore = minScore;
        }
        return realScore;
    }

    /**
     * 生成分项客观评分
     *
     * @param projectId
     * @param sectionId
     * @param year
     * @param month
     * @param objectTemplatePos
     * @param itemCodeCount
     */
    private BigDecimal insertObjectScore(Integer projectId, String sectionId, int year, int month,
                                         List<ObjectTemplatePo> objectTemplatePos, Map<String, Integer> itemCodeCount) {
        if (ObjectUtils.isEmpty(objectTemplatePos)) {
            log.info("未匹配上客观考评模板");
            throw new BaseException("未匹配上客观考评模板");
        }
        BigDecimal score = new BigDecimal(0);
        List<ObjectScoreDetailPo> scoreDetailPos = Lists.newArrayList();
        for (ObjectTemplatePo templatePo : objectTemplatePos) {
            ObjectScoreDetailPo objectScoreDetailPo = new ObjectScoreDetailPo();
            objectScoreDetailPo.setProjectId(projectId);
            objectScoreDetailPo.setSectionId(Integer.valueOf(sectionId));
            objectScoreDetailPo.setYear(year);
            objectScoreDetailPo.setMonth(month);
            for (Map.Entry<String, Integer> entry : itemCodeCount.entrySet()) {
                if (entry.getKey().equals(templatePo.getItemCode())) {
                    objectScoreDetailPo.setItemCode(entry.getKey());
                    objectScoreDetailPo.setDeductionCount(entry.getValue());
                    BigDecimal multiply = templatePo.getDeductionStandard().multiply(BigDecimal.valueOf(entry.getValue()));
                    score = score.add(multiply);
                    objectScoreDetailPo.setDeduction(multiply);
                    scoreDetailPos.add(objectScoreDetailPo);
                    break;
                }
            }
        }

        objectScoreDetailService.insert(scoreDetailPos);
        return score;
    }

    /**
     * 设备管理分数
     *
     * @return
     */
    private BigDecimal getDeviceManageScores(Integer projectId, String sectionId) {

        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;

        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("itemCode", "SB%");
        List<ObjectTemplatePo> objectTemplatePos = objectTemplateService.selectByExample(example);
        //考核编码 --》 数量
        Map<String, Integer> itemCodeCount = Maps.newHashMap();

        example = new Example(ObjectScoreDetailPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andLike("itemCode", "SB%");
        List<ObjectScoreDetailPo> detailPos = objectScoreDetailService.selectByExample(example);
        if (!ObjectUtils.isEmpty(detailPos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月设备模块考评得分已存在", projectId, sectionId, year, month);
            throw new BaseException("客观考评一个月仅生成一次");
        }
        BigDecimal score = insertObjectScore(projectId, sectionId, year, month, objectTemplatePos, itemCodeCount);
        Map<String, ObjectTemplatePo> templatePoMap = ListUtil.listToMap(objectTemplatePos, "itemCode", String.class);
        ObjectTemplatePo templatePo = templatePoMap.get("SB");
        BigDecimal realScore = getRealScore(score, templatePo);
        log.info("设备模块客观评分完毕,得分为{}，{}年{}月", realScore, year, month);
        return realScore;
    }

    /**
     * 物料管理分数
     *
     * @return
     */
    private BigDecimal getMaterialManageScores(Integer projectId, String sectionId) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;

        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("itemCode", "WL%");
        List<ObjectTemplatePo> objectTemplatePos = objectTemplateService.selectByExample(example);
        //考核编码 --》 数量
        Map<String, Integer> itemCodeCount = Maps.newHashMap();
        example = new Example(ObjectScoreDetailPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andLike("itemCode", "WL%");
        List<ObjectScoreDetailPo> detailPos = objectScoreDetailService.selectByExample(example);
        if (!ObjectUtils.isEmpty(detailPos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月物料模块考评得分已存在", projectId, sectionId, year, month);
            throw new BaseException("客观考评一个月仅生成一次");
        }
        BigDecimal score = insertObjectScore(projectId, sectionId, year, month, objectTemplatePos, itemCodeCount);

        Map<String, ObjectTemplatePo> templatePoMap = ListUtil.listToMap(objectTemplatePos, "itemCode", String.class);
        ObjectTemplatePo templatePo = templatePoMap.get("WL");
        BigDecimal realScore = getRealScore(score, templatePo);
        log.info("物料模块客观评分完毕,得分为{}，{}年{}月", realScore, year, month);
        return realScore;
    }

    /**
     * 计划管理分数
     *
     * @return
     */
    private BigDecimal getPlanManageScores(Integer projectId, String sectionId) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;

        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("itemCode", "JH%");
        List<ObjectTemplatePo> objectTemplatePos = objectTemplateService.selectByExample(example);
        //考核编码 --》 数量
        Map<String, Integer> itemCodeCount = Maps.newHashMap();
        example = new Example(ObjectScoreDetailPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andLike("itemCode", "JH%");
        List<ObjectScoreDetailPo> detailPos = objectScoreDetailService.selectByExample(example);
        if (!ObjectUtils.isEmpty(detailPos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月计划模块考评得分已存在", projectId, sectionId, year, month);
            throw new BaseException("客观考评一个月仅生成一次");
        }
        BigDecimal score = insertObjectScore(projectId, sectionId, year, month, objectTemplatePos, itemCodeCount);
        Map<String, ObjectTemplatePo> templatePoMap = ListUtil.listToMap(objectTemplatePos, "itemCode", String.class);
        ObjectTemplatePo templatePo = templatePoMap.get("JH");
        BigDecimal realScore = getRealScore(score, templatePo);
        log.info("计划模块客观评分完毕,得分为{}，{}年{}月", realScore, year, month);
        return realScore;
    }

    /**
     * 质量管理分数
     *
     * @return
     */
    private BigDecimal getQualityManageScores(Integer projectId, String sectionId) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;

        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("itemCode", "ZL%");
        List<ObjectTemplatePo> objectTemplatePos = objectTemplateService.selectByExample(example);
        //考核编码 --》 数量
        Map<String, Integer> itemCodeCount = Maps.newHashMap();

        example = new Example(ObjectScoreDetailPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andLike("itemCode", "ZL%");
        List<ObjectScoreDetailPo> detailPos = objectScoreDetailService.selectByExample(example);
        if (!ObjectUtils.isEmpty(detailPos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月质量模块考评得分已存在", projectId, sectionId, year, month);
            throw new BaseException("客观考评一个月仅生成一次");
        }
        BigDecimal score = insertObjectScore(projectId, sectionId, year, month, objectTemplatePos, itemCodeCount);
        Map<String, ObjectTemplatePo> templatePoMap = ListUtil.listToMap(objectTemplatePos, "itemCode", String.class);
        ObjectTemplatePo templatePo = templatePoMap.get("ZL");
        BigDecimal realScore = getRealScore(score, templatePo);
        log.info("质量模块客观评分完毕,得分为{}，{}年{}月", realScore, year, month);
        return realScore;
    }

    /**
     * 安全管理分数
     *
     * @return
     */
    private BigDecimal getSecurityManageScores(Integer projectId, String sectionId) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;

        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("itemCode", "AQ%");
        List<ObjectTemplatePo> objectTemplatePos = objectTemplateService.selectByExample(example);
        //考核编码 --》 数量
        Map<String, Integer> itemCodeCount = Maps.newHashMap();
        example = new Example(ObjectScoreDetailPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andLike("itemCode", "AQ%");
        List<ObjectScoreDetailPo> detailPos = objectScoreDetailService.selectByExample(example);
        if (!ObjectUtils.isEmpty(detailPos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月安全模块考评得分已存在", projectId, sectionId, year, month);
            throw new BaseException("客观考评一个月仅生成一次");
        }
        BigDecimal score = insertObjectScore(projectId, sectionId, year, month, objectTemplatePos, itemCodeCount);
        Map<String, ObjectTemplatePo> templatePoMap = ListUtil.listToMap(objectTemplatePos, "itemCode", String.class);
        ObjectTemplatePo templatePo = templatePoMap.get("AQ");
        BigDecimal realScore = getRealScore(score, templatePo);
        log.info("安全模块客观评分完毕,得分为{}，{}年{}月", realScore, year, month);
        return realScore;
    }

    /**
     * 问题管理分数
     *
     * @return
     */
    private BigDecimal getProblemManageScores(Integer projectId, String sectionId) {
        int questionCount = questionService.queryQuestionCount(sectionId);

        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;

        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("itemCode", "WT%");
        List<ObjectTemplatePo> objectTemplatePos = objectTemplateService.selectByExample(example);
        //考核编码 --》 数量
        Map<String, Integer> itemCodeCount = Maps.newHashMap();
        //问题响应
        itemCodeCount.put("WTXY", questionCount);

        example = new Example(ObjectScoreDetailPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("month", month);
        criteria.andLike("itemCode", "WT%");
        List<ObjectScoreDetailPo> detailPos = objectScoreDetailService.selectByExample(example);
        if (!ObjectUtils.isEmpty(detailPos)) {
            log.error("项目id:[{}],标段id:[{}]，{}年{}月问题模块考评得分已存在", projectId, sectionId, year, month);
            throw new BaseException("客观考评一个月仅生成一次");
        }
        BigDecimal score = insertObjectScore(projectId, sectionId, year, month, objectTemplatePos, itemCodeCount);
        Map<String, ObjectTemplatePo> templatePoMap = ListUtil.listToMap(objectTemplatePos, "itemCode", String.class);
        ObjectTemplatePo templatePo = templatePoMap.get("WT");
        BigDecimal realScore = getRealScore(score, templatePo);
        log.info("问题模块客观评分完毕,得分为{}，{}年{}月", realScore, year, month);
        return realScore;
    }

    @Override
    public void approveQuaConceFlow(String bizType, List<Integer> ids) {
        //1 把业务数据的状态变更为已批准.
        SysScorePo sysScorePo = new SysScorePo();
        sysScorePo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVED.getCode());
        this.updateSelectiveByIds(sysScorePo, ids);

        //推送通知
        List<SysScorePo> sysScorePos = super.selectByIds(ids);
        szxmCommonUtil.approveFlowAndSendMessage(bizType, sysScorePos);
    }

    @Override
    public void deleteSysObjectScore(List<Integer> ids) {
        List<SysScorePo> sysScorePos = this.selectByIds(ids);
        if(!ObjectUtils.isEmpty(sysScorePos)){
            for (SysScorePo sysScorePo:sysScorePos) {
                Integer projectId = sysScorePo.getProjectId();
                Integer sectionId = sysScorePo.getSectionId();
                Integer year = sysScorePo.getYear();
                Integer month = sysScorePo.getMonth();
                if(ObjectUtils.isEmpty(projectId) || ObjectUtils.isEmpty(sectionId) || ObjectUtils.isEmpty(year) || ObjectUtils.isEmpty(month)){
                    log.info("无法删除标段id为：{}的{}年{}月的客观考评细项，项目id，标段id，年、月都不能为空", sectionId, year, month);
                    continue;
                }
                objectScoreDetailService.deleteObjectDetailScore(projectId, sectionId, year, month);
                log.info("删除标段id为：{}的{}年{}月的客观考评细项", sectionId, year, month);
            }
        }
        this.deleteByIds(ids);
        log.info("删除主键id为：{}的系统考评得分成功", ids);
    }

    public static void main(String[] args) throws ParseException {
        Calendar gregorianCalendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        gregorianCalendar.setTime(simpleDateFormat.parse("2019-12-11"));
        int year = gregorianCalendar.get(Calendar.YEAR);
        int month = gregorianCalendar.get(Calendar.MONTH);

    }
}
