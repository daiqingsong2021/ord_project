package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.PageInfoUtiil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.form.rygl.SpecialWorkerAddForm;
import com.wisdom.acm.szxm.form.rygl.SpecialWorkerUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.SpecialWorkerMapper;
import com.wisdom.acm.szxm.po.rygl.SpecialWorkCertPo;
import com.wisdom.acm.szxm.po.rygl.SpecialWorkerPo;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkCertService;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkerService;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkCertVo;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkerVo;
import com.wisdom.acm.szxm.vo.rygl.WarnList;
import com.wisdom.acm.szxm.vo.rygl.WarnVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommFileService;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
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
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpecialWorkerServiceImpl extends BaseService<SpecialWorkerMapper, SpecialWorkerPo>
        implements SpecialWorkerService {
    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @Autowired
    private CommFileService commFileService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private CommDictService commDictService;

    @Autowired
    private SpecialWorkCertService specialWorkCertService;

    @Override
    public PageInfo<SpecialWorkerVo> selectSpecialWorkerList(Map<String, Object> mapWhere, List<String> sectionList,
                                                             Integer pageSize, Integer currentPageNum) {
        mapWhere.put("sectionList", sectionList);
        PageHelper.startPage(currentPageNum, pageSize);
        mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
        mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        List<SpecialWorkerVo> specialWorkerVos = mapper.selectSpecialWorker(mapWhere);
        PageInfo<SpecialWorkerVo> pageInfo = new PageInfo<SpecialWorkerVo>(specialWorkerVos);
        //获取分页后的集合
        List<SpecialWorkerVo> specialWorkerVoList = pageInfo.getList();

        if (!ObjectUtils.isEmpty(specialWorkerVoList)) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            Map<String, DictionaryVo> workTypeDictMap = commDictService.getDictMapByTypeCode("szxm.rygl.worktype");
            Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");

            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());

            for (SpecialWorkerVo specialWorkerVo : specialWorkerVoList) {
                //根据标段ID查询 标段Bean
                ProjectTeamVo sectionVO = sectionMap.get(specialWorkerVo.getSectionId());
                specialWorkerVo.setSectionCode(sectionVO.getCode());
                specialWorkerVo.setSectionName(sectionVO.getName());
                specialWorkerVo.setProjectName(projectVo.getName());

                specialWorkerVo.setJob(szxmCommonUtil.getDictionaryName(psotionDictMap, specialWorkerVo.getJob()));
                String[] workTypes = specialWorkerVo.getWorkType().split(",");
                for (String s : workTypes) {
                    String workTypeName = szxmCommonUtil.getDictionaryName(workTypeDictMap, s);
                    GeneralVo generalVo = new GeneralVo();
                    generalVo.setCode(s);
                    generalVo.setName(workTypeName);
                    specialWorkerVo.getWorkTypeVoList().add(generalVo);
                }

                //设置流程
                specialWorkerVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(specialWorkerVo.getStatusVo().getCode()).getName());

            }

        }
        return pageInfo;
    }

    @Override
    public WarnVo getWarningInformation(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        List<SpecialWorkerVo> specialWorkerVos = mapper.selectSpecialWorker(mapWhere);
        if (!ObjectUtils.isEmpty(specialWorkerVos)) {
            String projectId = String.valueOf(mapWhere.get("projectId"));
            if(StringHelper.isNullAndEmpty(projectId))
            {
                throw new BaseException("项目ID不能为空!");
            }
            PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(projectId));
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            WarnVo warnVo = new WarnVo();
            List<WarnList> warnLists = Lists.newArrayList();
            int warnNums = 0;
            int dDateNums = 0;
            for (SpecialWorkerVo specialWorkerVo : specialWorkerVos) {
                //根据标段ID查询 标段Bean
                ProjectTeamVo sectionVO = sectionMap.get(specialWorkerVo.getSectionId());
                specialWorkerVo.setSectionCode(sectionVO.getCode());
                specialWorkerVo.setSectionName(sectionVO.getName());
                specialWorkerVo.setProjectName(projectVo.getName());

                warnNums += specialWorkerVo.getWarnNums();
                dDateNums += specialWorkerVo.getDDateNums();
                if (0 != specialWorkerVo.getWarnNums() || 0 != specialWorkerVo.getDDateNums()) {
                    mapWhere.put("specialWorkerId", specialWorkerVo.getId());
                    List<SpecialWorkCertVo> specialWorkCertVoList = specialWorkCertService.selectSpecialWorkCert(mapWhere);
                    if (!ObjectUtils.isEmpty(specialWorkCertVoList)) {
                        for (SpecialWorkCertVo specialWorkCertVo : specialWorkCertVoList) {
                            if (!"0".equals(specialWorkCertVo.getWarnStatusVo().getCode())) {
                                WarnList warnList = new WarnList();
                                warnList.setSectionId(specialWorkerVo.getSectionId());
                                warnList.setSectionCode(specialWorkerVo.getSectionCode());
                                warnList.setName(specialWorkerVo.getName());
                                warnList.setCertificateName(specialWorkCertVo.getCertName());
                                warnList.setCertificateState(specialWorkCertVo.getWarnStatusVo().getCode());

                                warnLists.add(warnList);
                            }
                        }
                    }
                }
            }
            Collections.sort(warnLists);

            PageInfo<WarnList> pageInfo = new PageInfo<>();
            pageInfo.setPageNum(currentPageNum);
            pageInfo.setPageSize(pageSize);
            Page<WarnList> newPage = new PageInfoUtiil<WarnList>().generatePageList(pageInfo, warnLists);
            PageInfo<WarnList> newPageInfo = new PageInfo<>(newPage);
            newPageInfo.setTotal(pageInfo.getTotal());

            warnVo.setExpiringNumber(dDateNums);
            warnVo.setWillExpiringNumber(warnNums);
            warnVo.setWarnLists(new TableResultResponse(newPageInfo));
            return warnVo;
        }
        return null;
    }

    @Override
    public List<SpecialWorkerVo> getFlowSpecialWorkerList(Map<String, Object> mapWhere, List<String> sectionList) {
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("ids")))) {//如果主键Ids 不为空
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids", ids);
        } else {
            mapWhere.put("sectionList", sectionList);
            mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
            mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        }
        List<SpecialWorkerVo> specialWorkerVos = mapper.selectSpecialWorker(mapWhere);
        if (!ObjectUtils.isEmpty(specialWorkerVos)) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(specialWorkerVos.get(0).getProjectId());
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            Map<String, DictionaryVo> workTypeDictMap = commDictService.getDictMapByTypeCode("szxm.rygl.worktype");
            Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
            for (SpecialWorkerVo specialWorkerVo : specialWorkerVos) {
                ProjectTeamVo sectionVo = sectionMap.get(specialWorkerVo.getSectionId());
                specialWorkerVo.setSectionCode(sectionVo.getCode());
                specialWorkerVo.setSectionName(sectionVo.getName());
                specialWorkerVo.setProjectName(projectVo.getName());

                specialWorkerVo.setJob(szxmCommonUtil.getDictionaryName(psotionDictMap, specialWorkerVo.getJob()));
                String[] workTypes = specialWorkerVo.getWorkType().split(",");
                for (String s : workTypes) {
                    String workTypeName = szxmCommonUtil.getDictionaryName(workTypeDictMap, s);
                    GeneralVo generalVo = new GeneralVo();
                    generalVo.setCode(s);
                    generalVo.setName(workTypeName);
                    specialWorkerVo.getWorkTypeVoList().add(generalVo);
                }

                //设置流程
                specialWorkerVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(specialWorkerVo.getStatusVo().getCode()).getName());
            }
        }
        return specialWorkerVos;
    }

    @Override
    public SpecialWorkerVo addSpecialWorker(SpecialWorkerAddForm specialWorkerAddForm) {
        //查询是否相同人的特殊工种
        Example example = new Example(SpecialWorkerPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("peopleId", specialWorkerAddForm.getPeopleId());
        SpecialWorkerPo existPo = this.selectOneByExample(example);
        if (!ObjectUtils.isEmpty(existPo))
            throw new BaseException("已存在该人员的记录!");
        SpecialWorkerPo specialWorkerPo = dozerMapper.map(specialWorkerAddForm, SpecialWorkerPo.class);
        specialWorkerPo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.toString());

        this.insert(specialWorkerPo);

        SpecialWorkerVo specialWorkerVo = this.selectBySpecialWorkerId(specialWorkerPo.getId());
        return specialWorkerVo;
    }

    @Override
    public void deleteSpecialWorker(List<Integer> ids) {
        //删除文件
        commFileService.deleteDocFileRelationByBiz(ids, "STAFF-SPECIALTYPE");//项目基础信息bizCode 这点前端需要注意

        //删除特殊工种证书
        Map<String, Object> mapWhere = Maps.newHashMap();
        for (Integer specialWorkerId : ids) {
            Example example = new Example(SpecialWorkCertPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("specialWorkerId", specialWorkerId);
            List<SpecialWorkCertPo> specialWorkCertPoList = specialWorkCertService.selectByExample(example);
            List<Integer> specialWorkCertIds = ListUtil.toValueList(specialWorkCertPoList, "id", Integer.class);
            specialWorkCertService.deleteSpecialWorkCert(specialWorkCertIds);
        }
        this.deleteByIds(ids);
    }

    @Override
    public SpecialWorkerVo updateSpecialWorker(SpecialWorkerUpdateForm specialWorkerUpdateForm) {
        SpecialWorkerPo updateSpecialWorkerPo = dozerMapper.map(specialWorkerUpdateForm, SpecialWorkerPo.class);
        super.updateSelectiveById(updateSpecialWorkerPo);//根据ID更新po，值为null的不更新，只更新不为null的值

        SpecialWorkerVo specialWorkerVo = this.selectBySpecialWorkerId(updateSpecialWorkerPo.getId());
        return specialWorkerVo;
    }

    @Override
    public SpecialWorkerVo selectBySpecialWorkerId(Integer id) {
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("id", id);
        List<SpecialWorkerVo> specialWorkerVos = mapper.selectSpecialWorker(mapWhere);
        SpecialWorkerVo specialWorkerVo = specialWorkerVos.get(0);
        //根据标段ID查询 标段Bean
        ProjectTeamVo sectionVO = commProjectTeamService.getProjectTeamById(specialWorkerVo.getSectionId());
        specialWorkerVo.setSectionCode(sectionVO.getCode());
        specialWorkerVo.setSectionName(sectionVO.getName());
        PlanProjectVo projectVo = commPlanProjectService.getProject(specialWorkerVo.getProjectId());
        specialWorkerVo.setProjectName(projectVo.getName());

        String[] workTypes = specialWorkerVo.getWorkType().split(",");
        for (String s : workTypes) {
            String workTypeName = szxmCommonUtil.getDictionaryName("szxm.rygl.worktype", s);
            GeneralVo generalVo = new GeneralVo();
            generalVo.setCode(s);
            generalVo.setName(workTypeName);
            specialWorkerVo.getWorkTypeVoList().add(generalVo);
        }
        specialWorkerVo.setJob(szxmCommonUtil.getDictionaryName("base.position.type", specialWorkerVo.getJob()));
        //设置流程
        specialWorkerVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(specialWorkerVo.getStatusVo().getCode()).getName());

        return specialWorkerVo;
    }

    @Override
    public int querySpecialWorkerCount(String sectionId) {
        Integer count = mapper.querySpecialWorkerCount(sectionId);
        return ObjectUtils.isEmpty(count) ? 0 : count;
    }

    public static void main(String args[]) {
        System.out.println(SzxmEnumsUtil.StatusEnum.valueOf("INIT").getName());
        ;
    }
}
