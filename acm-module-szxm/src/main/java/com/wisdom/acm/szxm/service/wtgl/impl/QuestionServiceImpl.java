package com.wisdom.acm.szxm.service.wtgl.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.PageInfoUtiil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.form.wtgl.QuestionAddForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionForwardForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionHandleForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionPublishForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionUpdateForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionVerifyForm;
import com.wisdom.acm.szxm.mapper.wtgl.QuestionMapper;
import com.wisdom.acm.szxm.po.pfe.StationPo;
import com.wisdom.acm.szxm.po.wtgl.QuestionPo;
import com.wisdom.acm.szxm.po.wtgl.QuestionRecordPo;
import com.wisdom.acm.szxm.service.pfe.StationService;
import com.wisdom.acm.szxm.service.wtgl.QuestionRecordService;
import com.wisdom.acm.szxm.service.wtgl.QuestionService;
import com.wisdom.acm.szxm.vo.pfe.StationVo;
import com.wisdom.acm.szxm.vo.rygl.WarnList;
import com.wisdom.acm.szxm.vo.wtgl.FltjQuestionVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionClassVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionMonthVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionVo;
import com.wisdom.acm.szxm.vo.wtgl.TJQuestionVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommFileService;
import com.wisdom.base.common.feign.CommOrgService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.SysMessageAddForm;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl extends BaseService<QuestionMapper, QuestionPo> implements QuestionService {
    private static Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    @Autowired
    private CommUserService commUserService;

    @Autowired
    private CommOrgService commOrgService;

    @Autowired
    private CommFileService commFileService;

    @Autowired
    private QuestionRecordService questionRecordService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private StationService stationService;

    /**
     * 判断日期格式和范围
     */
    private final String DATE_REXP = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

    @Override
    public PageInfo<QuestionVo> selectQuestions(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        String sectionIds = String.valueOf(ObjectUtils.isEmpty(mapWhere.get("sectionIds")) ? "" : mapWhere.get("sectionIds"));//获取标段ID集合
        if (StringHelper.isNotNullAndEmpty(sectionIds)) {
            sectionIds = StringHelper.formattString(sectionIds);
            String[] sectionIdsArray = sectionIds.split(",");
            List<String> sectionList = new ArrayList<String>(Arrays.asList(sectionIdsArray));
            mapWhere.put("sectionList", sectionList);
        }
        mapWhere.put("projectId", StringHelper.formattString(mapWhere.get("projectId")));
        mapWhere.put("bizType", StringHelper.formattString(mapWhere.get("bizType")));
        mapWhere.put("title", StringHelper.formattString(mapWhere.get("title")));

        String status = String.valueOf(ObjectUtils.isEmpty(mapWhere.get("status")) ? "" : mapWhere.get("status"));//获取标段ID集合
        if (StringHelper.isNotNullAndEmpty(status)) {
            status = StringHelper.formattString(status);
            String[] statusArray = status.split(",");
            List<String> statusList = new ArrayList<String>(Arrays.asList(statusArray));
            mapWhere.remove("status");
            mapWhere.put("statusList", statusList);
        }
        mapWhere.put("type", StringHelper.formattString(mapWhere.get("type")));
        mapWhere.put("priority", StringHelper.formattString(mapWhere.get("priority")));
        mapWhere.put("createrId", StringHelper.formattString(mapWhere.get("createrId")));
        mapWhere.put("currentUserId", StringHelper.formattString(mapWhere.get("currentUserId")));
        mapWhere.put("startTime", StringHelper.formattString(mapWhere.get("startTime")));
        if (!ObjectUtils.isEmpty((mapWhere.get("endTime")))) {
            Date dayAfter = DateUtil.getDayAfter(DateUtil.getDateFormat(String.valueOf(mapWhere.get("endTime"))), 1);
            mapWhere.put("endTime", DateUtil.getDateFormat(dayAfter));
        }
        mapWhere.put("bizId", StringHelper.formattString(mapWhere.get("bizId")));
        mapWhere.put("loginUserId", commUserService.getLoginUser().getId());
        mapWhere.put("id", StringHelper.formattString(mapWhere.get("id")));
        PageHelper.startPage(currentPageNum, pageSize);
        List<QuestionVo> questionVoList = Lists.newArrayList();
        if ("1".equals(String.valueOf(mapWhere.get("isYq"))))
            questionVoList = mapper.selectYqQuestionList(mapWhere);
        else
            questionVoList = mapper.selectQuestionList(mapWhere);
        if (!ObjectUtils.isEmpty(questionVoList)) {
            installStation(questionVoList);
        }
        PageInfo<QuestionVo> pageInfo = new PageInfo<QuestionVo>(questionVoList);
        return pageInfo;
    }

    @Override
    public PageInfo<QuestionVo> securityCheckQuestions(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        mapWhere.put("projectId", StringHelper.formattString(mapWhere.get("projectId")));
        mapWhere.put("bizType", StringHelper.formattString(mapWhere.get("bizType")));

        String status = String.valueOf(ObjectUtils.isEmpty(mapWhere.get("status")) ? "" : mapWhere.get("status"));
        if (StringHelper.isNullAndEmpty(status)) {
            throw new BaseException("status不能为空");
        }
        String[] statusArray = status.split(",");
        List<String> statusList = new ArrayList<String>(Arrays.asList(statusArray));
        mapWhere.remove("status");
        mapWhere.put("statusList", statusList);
        PageHelper.startPage(currentPageNum, pageSize);
        PageInfo<QuestionVo> pageInfo = new PageInfo<>(mapper.secIssueList(mapWhere));
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            installStation(pageInfo.getList());
        }
        return pageInfo;
    }

    /**
     * 设置站点名
     *
     * @param questionVoList
     */
    private void installStation(List<QuestionVo> questionVoList) {
        //查出所有站点
        List<StationPo> stationPoList = stationService.selectListAll();
        for (QuestionVo questionVo : questionVoList) {
            if (StringHelper.isNotNullAndEmpty(questionVo.getStation())) {
                String[] stations = questionVo.getStation().split(",");
                for (String s : stations) {
                    for (StationPo stationPo : stationPoList) {
                        if (stationPo.getProjectId().equals(questionVo.getProjectId()) && s.equals(stationPo.getCode())) {
                            GeneralVo generalVo = new GeneralVo();
                            generalVo.setId(stationPo.getId());
                            generalVo.setCode(stationPo.getCode());
                            generalVo.setName(stationPo.getName());
                            questionVo.getStationVo().add(generalVo);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<TJQuestionVo> secIssueQuantity(Map<String, Object> mapWhere) {
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        if (StringHelper.isNullAndEmpty(projectId)) {
            throw new BaseException("项目id不能为空");
        }
        mapWhere.put("projectId", projectId);
        String date = StringHelper.formattString(mapWhere.get("date"));
        if (StringHelper.isNullAndEmpty(date) || !date.matches(DATE_REXP)) {
            //不传data或传入格式错误 取当前时间
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(new Date(), 1)));
        } else {
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(DateUtil.getDateFormat(date), 1)));
        }
        String status = String.valueOf(ObjectUtils.isEmpty(mapWhere.get("status")) ? "" : mapWhere.get("status"));//获取标段ID集合
        String viewType = StringHelper.formattString(mapWhere.get("viewType"));
        if (StringHelper.isNullAndEmpty(viewType)) {
            throw new BaseException("统计类型不能为空");
        }
        List<QuestionVo> questionVos = mapper.selectQuestionQuantity(mapWhere);
        if (ObjectUtils.isEmpty(questionVos)) {
            logger.info("{}暂无问题记录", date);
            return null;
        }
        installStation(questionVos);
        Map<String, List<QuestionVo>> statisticsMap = Maps.newHashMap();
        List<TJQuestionVo> tjQuestionVos = Lists.newArrayList();
        getTjQuestionVos(viewType, questionVos, statisticsMap, tjQuestionVos);
        Collections.sort(tjQuestionVos);

        return tjQuestionVos;
    }

    /**
     * 获取统计问题列表
     *
     * @param viewType
     * @param questionVos
     * @param statisticsMap
     * @param tjQuestionVos
     */
    private void getTjQuestionVos(String viewType, List<QuestionVo> questionVos, Map<String, List<QuestionVo>> statisticsMap, List<TJQuestionVo> tjQuestionVos) {
        if ("section".equals(viewType)) {
            for (QuestionVo questionVo : questionVos) {
                if (ObjectUtils.isEmpty(statisticsMap.get(questionVo.getSectionCode()))) {
                    if (StringHelper.isNullAndEmpty(questionVo.getSectionCode())) {
                        logger.info("问题编号：{}, 问题标题：{} 未关联标段", questionVo.getId(), questionVo.getTitle());
                        continue;
                    }
                    List<QuestionVo> question = Lists.newArrayList();
                    question.add(questionVo);
                    statisticsMap.put(questionVo.getSectionCode(), question);
                } else {
                    statisticsMap.get(questionVo.getSectionCode()).add(questionVo);
                }
            }
            if (!ObjectUtils.isEmpty(statisticsMap)) {
                for (Map.Entry<String, List<QuestionVo>> entry : statisticsMap.entrySet()) {
                    List<QuestionVo> value = entry.getValue();
                    if (!ObjectUtils.isEmpty(value)) {
                        TJQuestionVo tjQuestionVo = new TJQuestionVo();
                        tjQuestionVo.setTotalQuantity(value.size());
                        tjQuestionVo.setProjectId(value.get(0).getProjectId());
                        tjQuestionVo.setSectionCode(value.get(0).getSectionCode());
                        tjQuestionVo.setSectionId(value.get(0).getSectionId());
                        int total = 0;
                        for (QuestionVo questionVo : value) {
                            if (!SzxmEnumsUtil.QuestionStatusEnum.CLOSED.getCode().equals(questionVo.getStatusVo().getCode())) {
                                total++;
                            }
                        }
                        tjQuestionVo.setUnclosedQUantity(total);

                        tjQuestionVos.add(tjQuestionVo);
                    }
                }
            }
        } else if ("station".equals(viewType)) {
            for (QuestionVo questionVo : questionVos) {
                List<GeneralVo> stationVo = questionVo.getStationVo();
                if (!ObjectUtils.isEmpty(stationVo)) {
                    for (GeneralVo generalVo : stationVo) {
                        if (ObjectUtils.isEmpty(statisticsMap.get(generalVo.getCode()))) {
                            List<QuestionVo> question = Lists.newArrayList();
                            question.add(questionVo);
                            statisticsMap.put(generalVo.getCode(), question);
                        } else {
                            statisticsMap.get(generalVo.getCode()).add(questionVo);
                        }
                    }
                } else {
                    logger.info("问题编号：{}, 问题标题：{} 未关联站点", questionVo.getId(), questionVo.getTitle());
                }
            }
            if (!ObjectUtils.isEmpty(statisticsMap)) {
                for (Map.Entry<String, List<QuestionVo>> entry : statisticsMap.entrySet()) {
                    List<QuestionVo> value = entry.getValue();
                    if (!ObjectUtils.isEmpty(value)) {
                        TJQuestionVo tjQuestionVo = new TJQuestionVo();
                        tjQuestionVo.setProjectId(value.get(0).getProjectId());
                        tjQuestionVo.setTotalQuantity(value.size());
                        List<GeneralVo> stationVo = value.get(0).getStationVo();
                        for (GeneralVo generalVo : stationVo) {
                            if (entry.getKey().equals(generalVo.getCode())) {
                                tjQuestionVo.setStationId(generalVo.getId());
                                tjQuestionVo.setStationCode(generalVo.getCode());
                                tjQuestionVo.setStationName(generalVo.getName());
                                break;
                            }
                        }
                        int total = 0;
                        for (QuestionVo questionVo : value) {
                            if (!SzxmEnumsUtil.QuestionStatusEnum.CLOSED.getCode().equals(questionVo.getStatusVo().getCode())) {
                                total++;
                            }
                        }
                        tjQuestionVo.setUnclosedQUantity(total);

                        tjQuestionVos.add(tjQuestionVo);
                    }
                }
            }
        }
    }

    @Override
    public PageInfo<QuestionVo> secIssueList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        String viewType = StringHelper.formattString(mapWhere.get("viewType"));
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        //获取状态集合
        String status = StringHelper.formattString(mapWhere.get("status"));
        if (StringHelper.isNullAndEmpty(status) || StringHelper.isNullAndEmpty(projectId) || StringHelper.isNullAndEmpty(viewType)) {
            throw new BaseException("项目id不能为空,或者统计类型不能为空，或者status不能为空");
        }
        String[] statusArray = status.split(",");
        List<String> statusList = new ArrayList<String>(Arrays.asList(statusArray));
        mapWhere.remove("status");
        mapWhere.put("statusList", statusList);
        mapWhere.put("projectId", projectId);

        String date = StringHelper.formattString(mapWhere.get("date"));
        if (StringHelper.isNullAndEmpty(date) || !date.matches(DATE_REXP)) {
            //不传data或传入格式错误 取当前时间
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(new Date(), 1)));
        } else {
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(DateUtil.getDateFormat(date), 1)));
        }

        if ("section".equals(viewType)) {
            String sectionId = String.valueOf(ObjectUtils.isEmpty(mapWhere.get("sectionId")) ? "" : mapWhere.get("sectionId"));//获取标段ID集合
            if (StringHelper.isNullAndEmpty(sectionId)) {
                throw new BaseException("根据标段查问题时，标段id不能为空");
            }
            mapWhere.put("sectionId", sectionId);
        } else if ("station".equals(viewType)) {
            String stationIdStr = StringHelper.formattString(mapWhere.get("stationId"));
            Integer stationId;
            try {
                stationId = Integer.parseInt(stationIdStr);
            } catch (NumberFormatException e) {
                throw new BaseException("根据站点查问题时，站点id为数字");
            }
            Example example = new Example(StationPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", stationId);
            StationPo stationPo = stationService.selectOneByExample(example);
            if (ObjectUtils.isEmpty(stationPo) || StringHelper.isNullAndEmpty(stationPo.getCode())) {
                throw new BaseException("根据站点查问题时，站点为空或者站点编码为空");
            }
            mapWhere.put("station", stationPo.getCode());
        }
        if ("station".equals(viewType)) {
            List<QuestionVo> questionVoList = mapper.secIssueList(mapWhere);
            if (!ObjectUtils.isEmpty(questionVoList)) {
                installStation(questionVoList);
                List<QuestionVo> questionVoList1 = checkStation(mapWhere, viewType, questionVoList);

                PageInfo<QuestionVo> pageInfo = new PageInfo<>();
                pageInfo.setPageNum(currentPageNum);
                pageInfo.setPageSize(pageSize);
                Page<QuestionVo> newPage = new PageInfoUtiil<QuestionVo>().generatePageList(pageInfo, questionVoList1);
                PageInfo<QuestionVo> newPageInfo = new PageInfo<>(newPage);
                newPageInfo.setTotal(pageInfo.getTotal());
                return newPageInfo;
            }
            return new PageInfo<QuestionVo>();
        } else {
            PageHelper.startPage(currentPageNum, pageSize);
            PageInfo<QuestionVo> pageInfo = new PageInfo<>(mapper.secIssueList(mapWhere));
            if (!ObjectUtils.isEmpty(pageInfo.getList())) {
                installStation(pageInfo.getList());
            }
            return pageInfo;
        }
    }

    /**
     * 判断通过站点查询是否包含该站点编码
     *
     * @param mapWhere
     * @param viewType
     * @param questionVos
     * @return
     */
    private List<QuestionVo> checkStation(Map<String, Object> mapWhere, String viewType, List<QuestionVo> questionVos) {
        if ("station".equals(viewType)) {
            String station = (String) mapWhere.get("station");
            List<QuestionVo> questionVoList = Lists.newArrayList();
            for (QuestionVo questionVo : questionVos) {
                String[] stations = questionVo.getStation().split(",");
                for (String s : stations) {
                    if (station.equals(s)) {
                        questionVoList.add(questionVo);
                        break;
                    }
                }
            }
            return questionVoList;
        }
        return questionVos;
    }

    @Override
    public FltjQuestionVo issueClassificationStatistic(Map<String, Object> mapWhere) {
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        if (StringHelper.isNullAndEmpty(projectId)) {
            throw new BaseException("项目id不能为空");
        }
        mapWhere.put("projectId", projectId);
        String date = StringHelper.formattString(mapWhere.get("date"));
        if (StringHelper.isNullAndEmpty(date) || !date.matches(DATE_REXP)) {
            //不传data或传入格式错误 取当前时间
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(new Date(), 1)));
        } else {
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(DateUtil.getDateFormat(date), 1)));
        }

        List<QuestionClassVo> questionClassVos = mapper.selectQuestionType(mapWhere);
        if (!ObjectUtils.isEmpty(questionClassVos)) {
            for (QuestionClassVo questionClassVo : questionClassVos) {
                int unclousedQua = questionClassVo.getIssueQuantity() - questionClassVo.getUnclosedQuantity();
                questionClassVo.setUnclosedQuantity(unclousedQua);
            }
        }
        List<QuestionMonthVo> questionMonthVos = mapper.selectQuestionMonth(mapWhere);
        FltjQuestionVo fltjQuestionVo = new FltjQuestionVo();
        fltjQuestionVo.setQuestionClassVos(questionClassVos);
        fltjQuestionVo.setQuestionMonthVos(questionMonthVos);
        return fltjQuestionVo;
    }

    @Override
    public PageInfo<QuestionVo> secIssueClassList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        String type = StringHelper.formattString(mapWhere.get("type"));
        //获取状态集合
        String status = StringHelper.formattString(mapWhere.get("status"));
        if (StringHelper.isNullAndEmpty(projectId) || StringHelper.isNullAndEmpty(type) || StringHelper.isNullAndEmpty(status)) {
            throw new BaseException("项目id不能为空， 问题类型不能为空， 状态不能为空");
        }
        mapWhere.put("projectId", projectId);
        String date = StringHelper.formattString(mapWhere.get("date"));
        if (StringHelper.isNullAndEmpty(date) || !date.matches(DATE_REXP)) {
            //不传data或传入格式错误 取当前时间
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(new Date(), 1)));
        } else {
            mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.getDayAfter(DateUtil.getDateFormat(date), 1)));
        }
        mapWhere.put("type", type);
        String[] statusArray = status.split(",");
        List<String> statusList = new ArrayList<String>(Arrays.asList(statusArray));
        mapWhere.remove("status");
        mapWhere.put("statusList", statusList);
        PageHelper.startPage(currentPageNum, pageSize);
        List<QuestionVo> questionVos = mapper.secIssueList(mapWhere);
        PageInfo<QuestionVo> pageInfo = new PageInfo<>(questionVos);
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            installStation(pageInfo.getList());
        }
        return pageInfo;
    }

    /**
     * 根据各个模块的主键id（biz_id）查询问题表数据
     *
     * @param id
     * @return
     */
    @Override
    public List<QuestionVo> queryQuestionList(Integer id) {
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("bizId", StringHelper.formattString(String.valueOf(id)));
        List<QuestionVo> queryQuestionList = mapper.queryQuestionList(id);
        if (!ObjectUtils.isEmpty(queryQuestionList)) {
            //查出所有站点
            installStation(queryQuestionList);
        }
        return queryQuestionList;
    }

    /**
     * 查询单个问题
     *
     * @param id
     * @return
     */
    @Override
    public QuestionVo selectQuestionsById(Integer id) {
        QuestionVo questionVo = mapper.selectQuestion(id);
        if (StringHelper.isNotNullAndEmpty(questionVo.getStation())) {
            List<StationVo> stationVoList = stationService.queryStationByProjectId(questionVo.getProjectId());
            String[] stations = questionVo.getStation().split(",");
            for (String s : stations) {
                for (StationVo stationVo : stationVoList) {
                    if (s.equals(stationVo.getCode())) {
                        GeneralVo generalVo = new GeneralVo();
                        generalVo.setCode(stationVo.getCode());
                        generalVo.setName(stationVo.getName());
                        questionVo.getStationVo().add(generalVo);
                    }
                }
            }
        }
        return questionVo;
    }

    /**
     * 增加问题
     *
     * @param questionAddForm
     */
    @Override
    public Integer addQuestion(QuestionAddForm questionAddForm) {
        QuestionPo questionPo = dozerMapper.map(questionAddForm, QuestionPo.class);
        //设置状态,问题创建人及其orgID，初始当前处理人，当前处理人orgId
        questionPo.setStatus(SzxmEnumsUtil.QuestionStatusEnum.NEW.getCode());
        UserInfo userInfo = commUserService.getLoginUser();
        Integer cteaterOrgId = 0;//当前登录用户的部门ID
        if (!ObjectUtils.isEmpty(questionAddForm.getProjectId())) {//如果不为空，取当前创建人所处的项目团队
            List<Integer> orgIds = mapper.selectProjectTeamOrg(questionAddForm.getProjectId(), userInfo.getId());
            if (ObjectUtils.isEmpty(orgIds))
                throw new BaseException("当前用户在项目团队中未被分配,无法创建问题");
            cteaterOrgId = orgIds.get(0);
        } else
            cteaterOrgId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID
        questionPo.setCreaterOrgId(cteaterOrgId);
        questionPo.setCurrentUserId(userInfo.getId());
        questionPo.setCurrentUserOrgId(cteaterOrgId);
        this.insert(questionPo);

        //新建问题记录表
        QuestionRecordPo questionRecordPo = new QuestionRecordPo();
        questionRecordPo.setProjectId(questionPo.getProjectId());
        questionRecordPo.setSectionId(questionPo.getSectionId());
        questionRecordPo.setQuestionId(questionPo.getId());
        questionRecordPo.setAction(SzxmEnumsUtil.QuestionRecordActionEnum.NEW.getCode());
        questionRecordPo.setUserId(questionPo.getCreator());//下一步处理人初始化为创建人
        questionRecordPo.setOrgId(cteaterOrgId);//下一步处理人组织OrgID初始化
        questionRecordPo.setStatus(SzxmEnumsUtil.QuestionStatusEnum.NEW.getCode());
        questionRecordPo.setRemark("新建标题为：" + questionPo.getTitle() + "的问题");
        questionRecordPo.setCreaterOrgId(cteaterOrgId);
        questionRecordService.insert(questionRecordPo);

        //去除文件ID集合
        List<Integer> fileIds = questionAddForm.getFileIds();
        //保存文件关联信息
        if (!ObjectUtils.isEmpty(fileIds)) {
            commFileService.addFileRelation(questionPo.getId(), "question", fileIds);//文件关联
            commFileService.addFileRelation(questionRecordPo.getId(), "question-record", fileIds);//record也关联
        }
        return questionPo.getId();
    }

    /**
     * 修改问题
     *
     * @param questionUpdateForm
     */
    @Override
    public void updateQuestion(QuestionUpdateForm questionUpdateForm) {
        QuestionPo updatePo = dozerMapper.map(questionUpdateForm, QuestionPo.class);
        this.updateSelectiveById(updatePo);//根据ID更新po，值为null的不更新，只更新不为null的值
        //将问题的record查询出来，此时只会有一个
        Example example = new Example(QuestionRecordPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("questionId", questionUpdateForm.getId());
        QuestionRecordPo questionRecordPo = questionRecordService.selectOneByExample(example);
        //去除文件ID集合
        List<Integer> fileIds = questionUpdateForm.getFileIds();
        //修改文件关联信息
        if (!ObjectUtils.isEmpty(fileIds)) {
            commFileService.updateFileRelation(updatePo.getId(), "question", fileIds);
            if (!ObjectUtils.isEmpty(questionRecordPo))
                commFileService.updateFileRelation(questionRecordPo.getId(), "question-record", fileIds);
        }

    }

    /**
     * 删除问题
     *
     * @param ids
     */
    @Override
    public void deleteQuestion(List<Integer> ids) {
        //删除附件
        commFileService.deleteDocFileRelationByBiz(ids, "question");
        //删除问题记录表
        questionRecordService.deleteByQuestionId(ids);
        //删除本表
        this.deleteByIds(ids);
    }

    /**
     * 发布问题
     *
     * @param questionPublishForm
     */
    @Override
    public void updatePublishQuestion(QuestionPublishForm questionPublishForm) {
        QuestionPo questionPo = this.selectById(questionPublishForm.getQuestionId());
        UserInfo userInfo = commUserService.getLoginUser();
        Integer cteaterOrgId = 0;//
        if (!ObjectUtils.isEmpty(questionPo.getProjectId())) {//如果不为空，取当前创建人所处的项目团队
            List<Integer> orgIds = mapper.selectProjectTeamOrg(questionPo.getProjectId(), userInfo.getId());
            if (ObjectUtils.isEmpty(orgIds))
                throw new BaseException("当前用户在项目团队中未被分配,无法发布问题");
            cteaterOrgId = orgIds.get(0);
        } else
            cteaterOrgId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID
        //1 问题记录表增加一条记录，状态位待处理，动作为发布
        QuestionRecordPo questionRecordPo = new QuestionRecordPo();
        questionRecordPo.setProjectId(questionPo.getProjectId());
        questionRecordPo.setSectionId(questionPo.getSectionId());
        questionRecordPo.setQuestionId(questionPo.getId());
        questionRecordPo.setAction(SzxmEnumsUtil.QuestionRecordActionEnum.RELEASE.getCode());
        questionRecordPo.setStatus(SzxmEnumsUtil.QuestionStatusEnum.DCL.getCode());
        questionRecordPo.setOrgId(questionPublishForm.getOrgId());
        questionRecordPo.setUserId(questionPublishForm.getUserId());
        questionRecordPo.setCreaterOrgId(cteaterOrgId);
        questionRecordPo.setRemark("发布标题为：" + questionPo.getTitle() + "的问题");
        questionRecordService.insert(questionRecordPo);
        //2，更新问题状态，设置问题上一步处理人以及orgId以及状态,设置问题当前处理人以及当前处理人orgID以及状态
        questionPo.setLastUserOrgId(questionPo.getCurrentUserOrgId());
        questionPo.setLastUserId(questionPo.getCurrentUserId());
        questionPo.setLastStatus(questionPo.getStatus());

        questionPo.setCurrentUserOrgId(questionRecordPo.getOrgId());
        questionPo.setCurrentUserId(questionRecordPo.getUserId());
        questionPo.setStatus(questionRecordPo.getStatus());
        this.updateSelectiveById(questionPo);
        //3 对问题当前处理人，发送推送消息
        UserVo receiver = commUserService.getUserVoByUserId(questionPo.getCurrentUserId());
        SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
        sysMessageAddForm.setTitle("智慧工程项目系统-问题处理提醒");
        sysMessageAddForm.setPcContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时处理!");
        sysMessageAddForm.setContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时到工作台-工程管理-问题汇总页面处理。");
        List<UserVo> recvUsers = Lists.newArrayList();
        recvUsers.add(receiver);
        sysMessageAddForm.setRecvUser(recvUsers);
        sysMessageAddForm.setClaimDealTime(new Date());
        szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
    }

    /**
     * 处理问题
     *
     * @param questionHandleForm
     */
    @Override
    public void addHandleQuestion(QuestionHandleForm questionHandleForm) {
        QuestionPo questionPo = this.selectById(questionHandleForm.getQuestionId());
        UserInfo userInfo = commUserService.getLoginUser();
        Integer cteaterOrgId = 0;//
        if (!ObjectUtils.isEmpty(questionPo.getProjectId())) {//如果不为空，取当前创建人所处的项目团队
            List<Integer> orgIds = mapper.selectProjectTeamOrg(questionPo.getProjectId(), userInfo.getId());
            if (ObjectUtils.isEmpty(orgIds))
                throw new BaseException("当前用户在项目团队中未被分配,无法处理问题");
            cteaterOrgId = orgIds.get(0);
        } else
            cteaterOrgId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID
        //1 问题记录表增加一条记录，状态位待审核，动作为处理
        QuestionRecordPo questionRecordPo = dozerMapper.map(questionHandleForm, QuestionRecordPo.class);
        questionRecordPo.setAction(SzxmEnumsUtil.QuestionRecordActionEnum.HANDLE.getCode());
        questionRecordPo.setStatus(SzxmEnumsUtil.QuestionStatusEnum.DSH.getCode());
        questionRecordPo.setCreaterOrgId(cteaterOrgId);
        questionRecordService.insert(questionRecordPo);
        //去除文件ID集合
        List<Integer> fileIds = questionHandleForm.getFileIds();
        //保存文件关联信息
        if (!ObjectUtils.isEmpty(fileIds))
            commFileService.addFileRelation(questionRecordPo.getId(), "question-record", fileIds);
        //2，更新问题状态，设置问题上一步处理人以及orgId以及状态,设置问题当前处理人以及当前处理人orgID以及状态
        questionPo.setLastUserOrgId(questionPo.getCurrentUserOrgId());
        questionPo.setLastUserId(questionPo.getCurrentUserId());
        questionPo.setLastStatus(questionPo.getStatus());

        questionPo.setCurrentUserOrgId(questionRecordPo.getOrgId());
        questionPo.setCurrentUserId(questionRecordPo.getUserId());
        questionPo.setStatus(questionRecordPo.getStatus());
        this.updateSelectiveById(questionPo);
        //3 对问题当前处理人，发送推送消息
        UserVo receiver = commUserService.getUserVoByUserId(questionPo.getCurrentUserId());
        SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
        sysMessageAddForm.setTitle("智慧工程项目系统-问题审核提醒");
        sysMessageAddForm.setPcContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来审核，请及时审核!");
        sysMessageAddForm.setContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来审核，请及时到工作台-工程管理-问题汇总页面审核。");
        List<UserVo> recvUsers = Lists.newArrayList();
        recvUsers.add(receiver);
        sysMessageAddForm.setRecvUser(recvUsers);
        sysMessageAddForm.setClaimDealTime(new Date());
        szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
    }

    /**
     * 转发问题
     *
     * @param questionForwardForm
     */
    @Override
    public void addForwardQuestion(QuestionForwardForm questionForwardForm) {
        QuestionPo questionPo = this.selectById(questionForwardForm.getQuestionId());
        UserInfo userInfo = commUserService.getLoginUser();
        Integer cteaterOrgId = 0;//
        if (!ObjectUtils.isEmpty(questionPo.getProjectId())) {//如果不为空，取当前创建人所处的项目团队
            List<Integer> orgIds = mapper.selectProjectTeamOrg(questionPo.getProjectId(), userInfo.getId());
            if (ObjectUtils.isEmpty(orgIds))
                throw new BaseException("当前用户在项目团队中未被分配,无法转发问题");
            cteaterOrgId = orgIds.get(0);
        } else
            cteaterOrgId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID
        //1 问题记录表增加一条记录，状态位待处理，动作为转发
        QuestionRecordPo questionRecordPo = dozerMapper.map(questionForwardForm, QuestionRecordPo.class);
        questionRecordPo.setAction(SzxmEnumsUtil.QuestionRecordActionEnum.FORWARD.getCode());
        questionRecordPo.setStatus(SzxmEnumsUtil.QuestionStatusEnum.DCL.getCode());
        questionRecordPo.setCreaterOrgId(cteaterOrgId);
        questionRecordService.insert(questionRecordPo);
        //2，更新问题状态，设置问题上一步处理人以及orgId以及状态,设置问题当前处理人以及当前处理人orgID以及状态
        questionPo.setLastUserOrgId(questionPo.getCurrentUserOrgId());
        questionPo.setLastUserId(questionPo.getCurrentUserId());
        questionPo.setLastStatus(questionPo.getStatus());

        questionPo.setCurrentUserOrgId(questionRecordPo.getOrgId());
        questionPo.setCurrentUserId(questionRecordPo.getUserId());
        questionPo.setStatus(questionRecordPo.getStatus());
        this.updateSelectiveById(questionPo);
        //3 对问题当前处理人，推送消息
        UserVo receiver = commUserService.getUserVoByUserId(questionPo.getCurrentUserId());
        SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
        sysMessageAddForm.setTitle("智慧工程项目系统-问题处理提醒");
        sysMessageAddForm.setPcContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时处理!");
        sysMessageAddForm.setContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时到工作台-工程管理-问题汇总页面处理。");
        List<UserVo> recvUsers = Lists.newArrayList();
        recvUsers.add(receiver);
        sysMessageAddForm.setRecvUser(recvUsers);
        sysMessageAddForm.setClaimDealTime(new Date());
        szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
    }

    /**
     * 审核问题
     *
     * @param questionVerifyForm
     */
    @Override
    public void addVerifyQuestion(QuestionVerifyForm questionVerifyForm) {
        QuestionPo questionPo = this.selectById(questionVerifyForm.getQuestionId());
        UserInfo userInfo = commUserService.getLoginUser();
        Integer cteaterOrgId = 0;//
        if (!ObjectUtils.isEmpty(questionPo.getProjectId())) {//如果不为空，取当前创建人所处的项目团队
            List<Integer> orgIds = mapper.selectProjectTeamOrg(questionPo.getProjectId(), userInfo.getId());
            if (ObjectUtils.isEmpty(orgIds))
                throw new BaseException("当前用户在项目团队中未被分配,无法审核问题");
            cteaterOrgId = orgIds.get(0);
        } else
            cteaterOrgId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID
        //1  问题记录表增加一条记录
        // 1.1 状态位：若审核通过 且下一步审核人为空 则为关闭状态,如果下一步审核人不为空 则为待审核状态。若审核不通过，则为待处理状态
        // 1.2  动作：若审核通过 且下一步审核人为空 则为关闭,如果下一步审核人不为空 则为确认 若审核不通过，则为驳回
        String status = "";
        String action = "";
        if ("1".equals(questionVerifyForm.getIsPass())) {
            if (ObjectUtils.isEmpty(questionVerifyForm.getUserId())) {
                status = SzxmEnumsUtil.QuestionStatusEnum.CLOSED.getCode();
                action = SzxmEnumsUtil.QuestionRecordActionEnum.CLOSED.getCode();
            } else {
                status = SzxmEnumsUtil.QuestionStatusEnum.DSH.getCode();
                action = SzxmEnumsUtil.QuestionRecordActionEnum.CONFIRM.getCode();
            }
        } else if ("0".equals(questionVerifyForm.getIsPass())) {
            status = SzxmEnumsUtil.QuestionStatusEnum.DCL.getCode();
            action = SzxmEnumsUtil.QuestionRecordActionEnum.REJECT.getCode();
        }
        QuestionRecordPo questionRecordPo = dozerMapper.map(questionVerifyForm, QuestionRecordPo.class);
        questionRecordPo.setAction(action);
        questionRecordPo.setStatus(status);
        questionRecordPo.setCreaterOrgId(cteaterOrgId);
        questionRecordService.insert(questionRecordPo);
        //去除文件ID集合
        List<Integer> fileIds = questionVerifyForm.getFileIds();
        //保存文件关联信息
        if (!ObjectUtils.isEmpty(fileIds))
            commFileService.addFileRelation(questionRecordPo.getId(), "question-record", fileIds);
        //2，更新问题状态，设置问题上一步处理人以及orgId以及状态,设置问题当前处理人以及当前处理人orgID以及状态
        questionPo.setLastUserOrgId(questionPo.getCurrentUserOrgId());
        questionPo.setLastUserId(questionPo.getCurrentUserId());
        questionPo.setLastStatus(questionPo.getStatus());

        questionPo.setCurrentUserOrgId(questionRecordPo.getOrgId());
        questionPo.setCurrentUserId(questionRecordPo.getUserId());
        questionPo.setStatus(questionRecordPo.getStatus());
        if (SzxmEnumsUtil.QuestionStatusEnum.CLOSED.getCode().equals(questionPo.getStatus()))
            questionPo.setEndTime(new Date());//设定问题关闭时间
        this.updateSelectiveById(questionPo);
        //3.1 如果问题为关闭状态 则推送该问题的处理记录的所有创建人，说明问题已关闭，推送消息
        //3.2 如果问题为待处理状态或者待审核状态，对问题当前处理人，推送消息
        if (SzxmEnumsUtil.QuestionStatusEnum.CLOSED.getCode().equals(questionPo.getStatus())) {//问题已经关闭
            Example example = new Example(QuestionRecordPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("questionId", questionPo.getId());
            List<QuestionRecordPo> questionRecordPos = questionRecordService.selectByExample(example);

            List<Integer> userIds = Lists.newArrayList();
            userIds.add(questionPo.getCreator());
            for (QuestionRecordPo historyRecord : questionRecordPos) {
                userIds.add(historyRecord.getCreator());
            }
            Map<Integer, UserVo> userVoMap = commUserService.getUserVoMapByUserIds(userIds);

            List<UserVo> recvUsers = Lists.newArrayList();
            for (Map.Entry<Integer, UserVo> entry : userVoMap.entrySet()) {
                recvUsers.add(entry.getValue());
            }
            SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
            sysMessageAddForm.setTitle("智慧工程项目系统-问题关闭提醒");
            sysMessageAddForm.setContent("来自" + userVoMap.get(questionPo.getCreator()).getName() + "创建的，标题为\"" + questionPo.getTitle() + "\"的问题已关闭。");
            sysMessageAddForm.setRecvUser(recvUsers);
            sysMessageAddForm.setClaimDealTime(new Date());
            szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
        } else if (SzxmEnumsUtil.QuestionStatusEnum.DSH.getCode().equals(questionPo.getStatus())) {//问题状态位待审核
            UserVo receiver = commUserService.getUserVoByUserId(questionPo.getCurrentUserId());
            SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
            sysMessageAddForm.setTitle("智慧工程项目系统-问题审核提醒");
            sysMessageAddForm.setPcContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来审核，请及时审核!");
            sysMessageAddForm.setContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来审核，请及时到工作台-工程管理-问题汇总页面审核。");
            List<UserVo> recvUsers = Lists.newArrayList();
            recvUsers.add(receiver);
            sysMessageAddForm.setRecvUser(recvUsers);
            sysMessageAddForm.setClaimDealTime(new Date());
            szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
        } else if (SzxmEnumsUtil.QuestionStatusEnum.DCL.getCode().equals(questionPo.getStatus())) {//问题状态位待处理
            UserVo receiver = commUserService.getUserVoByUserId(questionPo.getCurrentUserId());
            SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
            sysMessageAddForm.setTitle("智慧工程项目系统-问题处理提醒");
            sysMessageAddForm.setPcContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时处理!");
            sysMessageAddForm.setContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时到工作台-工程管理-问题汇总页面处理。");
            List<UserVo> recvUsers = Lists.newArrayList();
            recvUsers.add(receiver);
            sysMessageAddForm.setRecvUser(recvUsers);
            sysMessageAddForm.setClaimDealTime(new Date());
            szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
        }
    }

    /**
     * 挂起问题
     *
     * @param id
     */
    @Override
    public void addHandUpQuestion(Integer id) {
        QuestionPo questionPo = this.selectById(id);
        UserInfo userInfo = commUserService.getLoginUser();
        Integer cteaterOrgId = 0;//
        if (!ObjectUtils.isEmpty(questionPo.getProjectId())) {//如果不为空，取当前创建人所处的项目团队
            List<Integer> orgIds = mapper.selectProjectTeamOrg(questionPo.getProjectId(), userInfo.getId());
            if (ObjectUtils.isEmpty(orgIds))
                throw new BaseException("当前用户在项目团队中未被分配,无法挂起问题");
            cteaterOrgId = orgIds.get(0);
        } else
            cteaterOrgId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID

        //1 问题记录表增加一条记录，状态为已挂起，动作为挂起
        QuestionRecordPo questionRecordPo = new QuestionRecordPo();
        questionRecordPo.setProjectId(questionPo.getProjectId());
        questionRecordPo.setSectionId(questionPo.getSectionId());
        questionRecordPo.setQuestionId(questionPo.getId());
        questionRecordPo.setAction(SzxmEnumsUtil.QuestionRecordActionEnum.HANDUP.getCode());
        questionRecordPo.setStatus(SzxmEnumsUtil.QuestionStatusEnum.HANDUP.getCode());
        questionRecordPo.setOrgId(cteaterOrgId);//下一步处理人是当前挂起的这个人及其组织
        questionRecordPo.setUserId(userInfo.getId());
        questionRecordPo.setCreaterOrgId(cteaterOrgId);
        questionRecordPo.setRemark("挂起标题为：" + questionPo.getTitle() + "的问题");
        questionRecordService.insert(questionRecordPo);
        //2，更新问题状态，设置问题上一步处理人以及orgId以及状态,设置问题当前处理人以及当前处理人orgID以及状态
        questionPo.setLastUserOrgId(questionPo.getCurrentUserOrgId());//更新上一步状态
        questionPo.setLastUserId(questionPo.getCurrentUserId());
        questionPo.setLastStatus(questionPo.getStatus());

        questionPo.setCurrentUserOrgId(questionRecordPo.getOrgId());
        questionPo.setCurrentUserId(questionRecordPo.getUserId());
        questionPo.setStatus(questionRecordPo.getStatus());
        this.updateSelectiveById(questionPo);
        //3,推送消息给 问题上一步处理人，告知该问题已挂起
        UserVo receiver = commUserService.getUserVoByUserId(questionPo.getLastUserId());
        SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
        sysMessageAddForm.setTitle("智慧工程项目系统-问题挂起提醒");
        sysMessageAddForm.setContent("标题为\"" + questionPo.getTitle() + "\"的问题已被:" + userInfo.getActuName() + "挂起,暂时无法对其进行处理或审核。");
        List<UserVo> recvUsers = Lists.newArrayList();
        recvUsers.add(receiver);
        sysMessageAddForm.setRecvUser(recvUsers);
        sysMessageAddForm.setClaimDealTime(new Date());
        szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
        //3.2 推送消息给问题记录所有的创建者，此问题已被挂起
        Example example = new Example(QuestionRecordPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("questionId", questionPo.getId());
        List<QuestionRecordPo> questionRecordPos = questionRecordService.selectByExample(example);

        List<Integer> userIds = Lists.newArrayList();
        for (QuestionRecordPo historyRecord : questionRecordPos) {
            userIds.add(historyRecord.getCreator());
        }
        Map<Integer, UserVo> userVoMap = commUserService.getUserVoMapByUserIds(userIds);

        recvUsers = Lists.newArrayList();
        for (Map.Entry<Integer, UserVo> entry : userVoMap.entrySet()) {
            recvUsers.add(entry.getValue());
        }
        sysMessageAddForm = new SysMessageAddForm();
        sysMessageAddForm.setTitle("智慧工程项目系统-问题挂起提醒");
        sysMessageAddForm.setContent("标题为\"" + questionPo.getTitle() + "\"的问题已被:" + userInfo.getActuName() + "挂起，请知悉。");
        sysMessageAddForm.setRecvUser(recvUsers);
        sysMessageAddForm.setClaimDealTime(new Date());
        szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
    }

    /**
     * 取消挂起问题
     *
     * @param id
     */
    @Override
    public void addCancelHandUpQuestion(Integer id) {
        QuestionPo questionPo = this.selectById(id);
        UserInfo userInfo = commUserService.getLoginUser();
        Integer cteaterOrgId = 0;//
        if (!ObjectUtils.isEmpty(questionPo.getProjectId())) {//如果不为空，取当前创建人所处的项目团队
            List<Integer> orgIds = mapper.selectProjectTeamOrg(questionPo.getProjectId(), userInfo.getId());
            if (ObjectUtils.isEmpty(orgIds))
                throw new BaseException("当前用户在项目团队中未被分配,无法取消挂起问题");
            cteaterOrgId = orgIds.get(0);
        } else
            cteaterOrgId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID
        //1 问题记录表增加一条记录，状态取问题的上一步状态（相当于还原），动作为取消挂起
        QuestionRecordPo questionRecordPo = new QuestionRecordPo();
        questionRecordPo.setProjectId(questionPo.getProjectId());
        questionRecordPo.setSectionId(questionPo.getSectionId());
        questionRecordPo.setQuestionId(questionPo.getId());
        questionRecordPo.setAction(SzxmEnumsUtil.QuestionRecordActionEnum.CANCELHANDUP.getCode());
        questionRecordPo.setStatus(questionPo.getLastStatus());//还原
        questionRecordPo.setOrgId(questionPo.getLastUserOrgId());//还原
        questionRecordPo.setUserId(questionPo.getLastUserId());//还原
        questionRecordPo.setCreaterOrgId(cteaterOrgId);
        questionRecordPo.setRemark("取消挂起标题为：" + questionPo.getTitle() + "的问题");
        questionRecordService.insert(questionRecordPo);
        //2，更新问题状态，设置问题上一步处理人以及orgId以及状态,设置问题当前处理人以及当前处理人orgID以及状态
        questionPo.setLastUserOrgId(questionPo.getCurrentUserOrgId());//更新上一步状态
        questionPo.setLastUserId(questionPo.getCurrentUserId());
        questionPo.setLastStatus(questionPo.getStatus());

        questionPo.setCurrentUserOrgId(questionRecordPo.getOrgId());
        questionPo.setCurrentUserId(questionRecordPo.getUserId());
        questionPo.setStatus(questionRecordPo.getStatus());
        this.updateSelectiveById(questionPo);
        //3.1 推送消息给问题记录的所有创建人，问题已取消挂起
        Example example = new Example(QuestionRecordPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("questionId", questionPo.getId());
        List<QuestionRecordPo> questionRecordPos = questionRecordService.selectByExample(example);

        List<Integer> userIds = Lists.newArrayList();
        for (QuestionRecordPo historyRecord : questionRecordPos) {
            userIds.add(historyRecord.getCreator());
        }
        Map<Integer, UserVo> userVoMap = commUserService.getUserVoMapByUserIds(userIds);

        List<UserVo> recvUsers = Lists.newArrayList();
        for (Map.Entry<Integer, UserVo> entry : userVoMap.entrySet()) {
            recvUsers.add(entry.getValue());
        }
        SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
        sysMessageAddForm.setTitle("智慧工程项目系统-问题取消挂起提醒");
        sysMessageAddForm.setContent("标题为\"" + questionPo.getTitle() + "\"的问题已被:" + userInfo.getActuName() + "取消挂起，恢复状态：\"" + SzxmEnumsUtil.QuestionStatusEnum.getEnumByCode(questionPo.getStatus()).getName() + "\",请知悉。");
        sysMessageAddForm.setRecvUser(recvUsers);
        sysMessageAddForm.setClaimDealTime(new Date());
        szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
        //3.2 对问题当前处理人或者审核人，推送消息
        if (SzxmEnumsUtil.QuestionStatusEnum.DCL.getCode().equals(questionPo.getStatus())) {
            UserVo receiver = commUserService.getUserVoByUserId(questionPo.getCurrentUserId());
            sysMessageAddForm = new SysMessageAddForm();
            sysMessageAddForm.setTitle("智慧工程项目系统-问题处理提醒");
            sysMessageAddForm.setPcContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时处理!");
            sysMessageAddForm.setContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来处理，请及时到工作台-工程管理-问题汇总页面处理。");
            recvUsers = Lists.newArrayList();
            recvUsers.add(receiver);
            sysMessageAddForm.setRecvUser(recvUsers);
            sysMessageAddForm.setClaimDealTime(new Date());
            szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
        } else if (SzxmEnumsUtil.QuestionStatusEnum.DSH.getCode().equals(questionPo.getStatus())) {

            UserVo receiver = commUserService.getUserVoByUserId(questionPo.getCurrentUserId());
            sysMessageAddForm = new SysMessageAddForm();
            sysMessageAddForm.setTitle("智慧工程项目系统-问题审核提醒");
            sysMessageAddForm.setPcContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来审核，请及时审核!");
            sysMessageAddForm.setContent("来自" + userInfo.getActuName() + "，标题为\"" + questionPo.getTitle() + "\"的问题需要您来审核，请及时到工作台-工程管理-问题汇总页面审核。");
            recvUsers = Lists.newArrayList();
            recvUsers.add(receiver);
            sysMessageAddForm.setRecvUser(recvUsers);
            sysMessageAddForm.setClaimDealTime(new Date());
            szxmCommonUtil.sendMessageRecv(sysMessageAddForm);
        }
    }

    @Override
    public int queryQuestionCount(String sectionId) {
        Integer count = mapper.queryQuestionCount(sectionId);
        return ObjectUtils.isEmpty(count) ? 0 : count;
    }


    public static void main(String args[]) {
        Integer xx = null;
        System.out.println(StringHelper.formattString(xx));
    }
}
