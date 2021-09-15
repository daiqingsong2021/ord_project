package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.PeopleEntryMapper;
import com.wisdom.acm.szxm.po.rygl.PeopleEntryDetailPo;
import com.wisdom.acm.szxm.po.rygl.PeopleEntryPo;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.po.rygl.SpecialWorkerPo;
import com.wisdom.acm.szxm.service.rygl.PeopleEntryDetailService;
import com.wisdom.acm.szxm.service.rygl.PeopleEntryService;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkerService;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryVo;
import com.wisdom.acm.szxm.vo.rygl.WorkListMonthVo;
import com.wisdom.acm.szxm.vo.rygl.WorkListsVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.EntityUtils;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PeopleEntryServiceImpl extends BaseService<PeopleEntryMapper, PeopleEntryPo> implements
        PeopleEntryService {

    @Autowired
    private CommDocService commDocService;

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommDictService commDictService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private SpecialWorkerService specialWorkerService;

    @Autowired
    private PeopleEntryDetailService peopleEntryDetailService;

    @Autowired
    private LeafService leafService;

    @Override
    public PageInfo<PeopleEntryVo> selectPeopleEntryList(Map<String, Object> mapWhere, List<String> sectionList,
                                                         Integer pageSize, Integer currentPageNum) {
        mapWhere.put("sectionList", sectionList);
        mapWhere.put("type", StringHelper.formattString(String.valueOf(mapWhere.get("type"))));
        mapWhere.put("peoEntryType", StringHelper.formattString(String.valueOf(mapWhere.get("peoEntryType"))));
        mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
        mapWhere.put("code", StringHelper.formattString(String.valueOf(mapWhere.get("code"))));
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        if (!ObjectUtils.isEmpty((mapWhere.get("endTime")))) {
            Date dayAfter = DateUtil.getDayAfter(DateUtil.getDateFormat(String.valueOf(mapWhere.get("endTime"))), 1);
            mapWhere.put("endTime", DateUtil.getDateFormat(dayAfter));
        }
        PageHelper.startPage(currentPageNum, pageSize);
        List<PeopleEntryVo> peopleEntryVoList = mapper.selectPeopleEntry(mapWhere);

        PageInfo<PeopleEntryVo> pageInfo = new PageInfo<PeopleEntryVo>(peopleEntryVoList);
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for (PeopleEntryVo peopleEntryVo : pageInfo.getList()) {
                ProjectTeamVo sectionVo = sectionMap.get(peopleEntryVo.getSectionId());
                peopleEntryVo.setSectionCode(sectionVo.getCode());
                peopleEntryVo.setSectionName(sectionVo.getName());
                peopleEntryVo.setSectionTypeName(sectionVo.getTypeName());
                peopleEntryVo.setSectionTypeCode(sectionVo.getTypeCoe());
                peopleEntryVo.setProjectName(projectVo.getName());
                //设置流程
                peopleEntryVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleEntryVo.getStatusVo().getCode()).getName());
            }
        }
        return pageInfo;
    }

    @Override
    public WorkListsVo getWorkersList(Map<String, Object> mapWhere) {
        String viewType = StringHelper.formattString(mapWhere.get("viewType"));
         if ("managers".equals(viewType)) {
            //管理人员
            mapWhere.put("peoEntryType", "1");
        } else if ("contractWorkers".equals(viewType)) {
            //劳务人员
            mapWhere.put("peoEntryType", "0");
        }
        mapWhere.put("status", SzxmEnumsUtil.StatusEnum.APPROVED.toString());
        List<PeopleEntryVo> peopleEntryVoList = mapper.selectPeopleEntry(mapWhere);
        if (!ObjectUtils.isEmpty(peopleEntryVoList)) {
            WorkListsVo workListsVo = new WorkListsVo();
            int enteringNumber = 0;
            int exitNumber = 0;
            Map<String, WorkListMonthVo> entryMap = Maps.newHashMap();
            for (PeopleEntryVo peopleEntryVo : peopleEntryVoList) {
                if("0".equals(peopleEntryVo.getTypeVo().getCode())){
                    //进场
                    enteringNumber += peopleEntryVo.getPeoNums();
                    String entryDay = DateUtil.getDateFormat(peopleEntryVo.getEntryTime(), "yyyy-MM");
                    if (ObjectUtils.isEmpty(entryMap.get(entryDay))) {
                        WorkListMonthVo workListMonthVo = new WorkListMonthVo();
                        workListMonthVo.setMonth(entryDay);
                        workListMonthVo.setEnteringNumber(peopleEntryVo.getPeoNums());
                        entryMap.put(entryDay, workListMonthVo);
                    } else if(ObjectUtils.isEmpty(entryMap.get(entryDay).getEnteringNumber())){
                        WorkListMonthVo workListMonthVo = entryMap.get(entryDay);
                        workListMonthVo.setEnteringNumber(peopleEntryVo.getPeoNums());
                        entryMap.put(entryDay, workListMonthVo);
                    }else{
                        WorkListMonthVo workListMonthVo = entryMap.get(entryDay);
                        workListMonthVo.setEnteringNumber(workListMonthVo.getEnteringNumber() + peopleEntryVo.getPeoNums());
                        entryMap.put(entryDay, workListMonthVo);
                    }
                }else if("1".equals(peopleEntryVo.getTypeVo().getCode())){
                    //退场
                    exitNumber += peopleEntryVo.getPeoNums();
                    String entryDay = DateUtil.getDateFormat(peopleEntryVo.getEntryTime(), "yyyy-MM");
                    if (ObjectUtils.isEmpty(entryMap.get(entryDay))) {
                        WorkListMonthVo workListMonthVo = new WorkListMonthVo();
                        workListMonthVo.setMonth(entryDay);
                        workListMonthVo.setExitNumber(peopleEntryVo.getPeoNums());
                        entryMap.put(entryDay, workListMonthVo);
                    } else if(ObjectUtils.isEmpty(entryMap.get(entryDay).getExitNumber())){
                        WorkListMonthVo workListMonthVo = entryMap.get(entryDay);
                        workListMonthVo.setExitNumber(peopleEntryVo.getPeoNums());
                        entryMap.put(entryDay, workListMonthVo);
                    } else {
                        WorkListMonthVo workListMonthVo = entryMap.get(entryDay);
                        workListMonthVo.setExitNumber(workListMonthVo.getExitNumber() + peopleEntryVo.getPeoNums());
                        entryMap.put(entryDay, workListMonthVo);
                    }
                }
            }
            workListsVo.setEnteringNumber(enteringNumber);
            workListsVo.setExitNumber(exitNumber);
            workListsVo.setPresenceNumber(enteringNumber - exitNumber);
            if(!ObjectUtils.isEmpty(entryMap)){
                List<WorkListMonthVo> workListMonthVos = Lists.newArrayList();
                for (Map.Entry<String, WorkListMonthVo> entry: entryMap.entrySet()) {
                    workListMonthVos.add(entry.getValue());
                }
                Collections.sort(workListMonthVos);
                workListsVo.setWorkListMonthVos(workListMonthVos);
            }
            return workListsVo;
        }
        return null;
    }

    @Override
    public List<PeopleEntryVo> selectFlowPeopleEntryList(Map<String, Object> mapWhere, List<String> sectionList) {
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("ids")))) {//如果主键Ids 不为空
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids", ids);
        } else {
            mapWhere.put("sectionList", sectionList);
            mapWhere.put("type", StringHelper.formattString(String.valueOf(mapWhere.get("type"))));
            mapWhere.put("peoEntryType", StringHelper.formattString(String.valueOf(mapWhere.get("peoEntryType"))));
            mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
            mapWhere.put("code", StringHelper.formattString(String.valueOf(mapWhere.get("code"))));
            mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
            if (!ObjectUtils.isEmpty((mapWhere.get("endTime")))) {
                Date dayAfter = DateUtil.getDayAfter(DateUtil.getDateFormat(String.valueOf(mapWhere.get("endTime"))), 1);
                mapWhere.put("endTime", DateUtil.getDateFormat(dayAfter));
            }

        }
        List<PeopleEntryVo> peopleEntryVoList = mapper.selectPeopleEntry(mapWhere);
        if (!ObjectUtils.isEmpty(peopleEntryVoList)) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(peopleEntryVoList.get(0).getProjectId());
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for (PeopleEntryVo peopleEntryVo : peopleEntryVoList) {
                ProjectTeamVo sectionVo = sectionMap.get(peopleEntryVo.getSectionId());
                peopleEntryVo.setSectionCode(sectionVo.getCode());
                peopleEntryVo.setSectionName(sectionVo.getName());
                peopleEntryVo.setProjectName(projectVo.getName());
                //设置流程
                peopleEntryVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleEntryVo.getStatusVo().getCode()).getName());
            }
        }
        return peopleEntryVoList;
    }

    @Override
    public PeopleEntryVo addPeopleEntry(PeopleEntryAddForm peopleEntryAddForm) {
        PeopleEntryPo peopleEntryPo = dozerMapper.map(peopleEntryAddForm, PeopleEntryPo.class);
        peopleEntryPo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.toString());
        peopleEntryPo.setCode("JTC" + DateUtil.getDateFormat(new Date(), "yyyyMMddHHmmss"));
        super.insert(peopleEntryPo);

        PeopleEntryVo peopleEntryVo = dozerMapper.map(peopleEntryPo, PeopleEntryVo.class);//po对象转换为Vo对象
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(peopleEntryPo.getSectionId());
        peopleEntryVo.setSectionCode(sectionVo.getCode());
        peopleEntryVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo = commPlanProjectService.getProject(peopleEntryPo.getProjectId());
        peopleEntryVo.setProjectName(projectVo.getName());
        peopleEntryVo.getStatusVo().setCode(peopleEntryPo.getStatus());
        peopleEntryVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleEntryPo.getStatus()).getName());
        peopleEntryVo.getTypeVo().setCode(peopleEntryPo.getType());
        peopleEntryVo.getTypeVo().setName("1".equals(peopleEntryPo.getType()) ? "退场" : "进场");
        peopleEntryVo.getPeoEntryTypeVo().setCode(peopleEntryPo.getPeoEntryType());
        peopleEntryVo.getPeoEntryTypeVo().setName("1".equals(peopleEntryPo.getPeoEntryType()) ? "管理人员" : "普通人员");
        peopleEntryVo.setPeoNums(0);

        return peopleEntryVo;
    }

    @Override
    public void deletePeopleEntry(List<Integer> ids) {
        //删除文件
        String bizIds = "";
        for (Integer id : ids) {
            bizIds += (id + ",");
        }
        bizIds = bizIds.substring(0, bizIds.lastIndexOf(","));
        commDocService.deleteDocByBizTypeAndBizIds("STAFF-ENTRYAEXIT", bizIds);
        //删除人员明细
        mapper.deletePeopEntryDetByeIds(ids);
        this.deleteByIds(ids);
    }

    @Override
    public PeopleEntryVo updatePeopleEntry(PeopleEntryUpdateForm peopleEntryUpdateForm) {
        PeopleEntryPo updatePeopleEntryPo = dozerMapper.map(peopleEntryUpdateForm, PeopleEntryPo.class);
        super.updateSelectiveById(updatePeopleEntryPo);//根据ID更新po，值为null的不更新，只更新不为null的值

        PeopleEntryVo peopleEntryVo = this.selectByPeopleEntryId(updatePeopleEntryPo.getId());
        return peopleEntryVo;
    }

    @Override
    public PeopleEntryVo selectByPeopleEntryId(Integer id) {
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("id", id);
        List<PeopleEntryVo> peopleEntryVos = mapper.selectPeopleEntry(mapWhere);
        PeopleEntryVo peopleEntryVo = peopleEntryVos.get(0);
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(peopleEntryVo.getSectionId());
        peopleEntryVo.setSectionCode(sectionVo.getCode());
        peopleEntryVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo = commPlanProjectService.getProject(peopleEntryVo.getProjectId());
        peopleEntryVo.setProjectName(projectVo.getName());
        //设置流程
        peopleEntryVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleEntryVo.getStatusVo().getCode()).getName());
        return peopleEntryVo;
    }

    @Override
    public void rejectPeopleEntryFlow(List<Integer> ids) {
        //1 把业务数据的状态变更为驳回
        PeopleEntryPo updatePeopleEntryPo = new PeopleEntryPo();
        updatePeopleEntryPo.setStatus(SzxmEnumsUtil.StatusEnum.REJECT.getCode());
        this.updateSelectiveByIds(updatePeopleEntryPo, ids);
    }

    @Override
    public void terminatePeopleEntryFlow(List<Integer> ids) {
        //1 把业务数据的状态变更为已批准.
        PeopleEntryPo updatePeopleEntryPo = new PeopleEntryPo();
        updatePeopleEntryPo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        this.updateSelectiveByIds(updatePeopleEntryPo, ids);
    }

    @Override
    public void deletePeopleEntryFlow(List<Integer> ids) {
        //1 把业务数据的状态变更为已批准.
        PeopleEntryPo updatePeopleEntryPo = new PeopleEntryPo();
        updatePeopleEntryPo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        this.updateSelectiveByIds(updatePeopleEntryPo, ids);
    }

    @Override
    public int queryPeopleEntryCount(String sectionId) {
        Integer count = mapper.queryPeopleEntryCount(sectionId);
        return ObjectUtils.isEmpty(count) ? 0 : count;
    }

    public static void main(String args[]) {
        String bizIds = "xxx,xxx,xxxx,";
        System.out.println(bizIds.substring(0, bizIds.lastIndexOf(",")));
    }

    @Override
    public PageInfo<PeopleEntryVo> selectSectionPeopleEntryList(Map<String, Object> mapWhere,Integer pageSize, Integer currentPageNum) {

        PageHelper.startPage(currentPageNum, pageSize);
        List<PeopleEntryVo> peopleEntryVoList = mapper.selectSectionPeopleEntry(mapWhere);

        PageInfo<PeopleEntryVo> pageInfo = new PageInfo<PeopleEntryVo>(peopleEntryVoList);
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
           // PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(Integer.valueOf(mapWhere.get("projectId").toString()));
            for (PeopleEntryVo peopleEntryVo : pageInfo.getList()) {
                ProjectTeamVo sectionVo = sectionMap.get(peopleEntryVo.getSectionId());
                peopleEntryVo.setSectionCode(sectionVo.getCode());
                peopleEntryVo.setSectionName(sectionVo.getName());
                peopleEntryVo.setSectionTypeName(sectionVo.getTypeName());
                peopleEntryVo.setSectionTypeCode(sectionVo.getTypeCoe());
               // peopleEntryVo.setProjectName(projectVo.getName());
                //设置流程
                peopleEntryVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleEntryVo.getStatusVo().getCode()).getName());
            }
        }
        return pageInfo;
    }
}
