package com.wisdom.acm.szxm.service.rygl.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.rygl.ProjInfoAddForm;
import com.wisdom.acm.szxm.form.rygl.ProjInfoUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.PeopleEntryDetailMapper;
import com.wisdom.acm.szxm.mapper.rygl.ProjInfoMapper;
import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.ProjInfoService;
import com.wisdom.acm.szxm.service.rygl.ZkService;
import com.wisdom.acm.szxm.vo.rygl.*;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.SortUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjInfoServiceImpl extends BaseService<ProjInfoMapper, ProjInfoPo>
        implements ProjInfoService {
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommDocService commDocService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;


    @Autowired
    protected org.dozer.Mapper dozerMapper;
    @Autowired
    protected PeopleEntryDetailMapper peopleEntryDetailMapper;


    /**
     * 项目组织信息Service
     */
    @Autowired
    private ProjInfoService projInfoService;
    /**
     * 用户基本信息服务
     */
    @Autowired
    private CommUserService commUserService;

    @Autowired
    private ZkService zkService;

    @Autowired
    private CommDictService commDictService;

    private static final Logger logger = LoggerFactory.getLogger(ProjInfoServiceImpl.class);

    @Override
    public List<ProjInfoVo> selectProjInfoList(Map<String, Object> mapWhere, List<String> sectionList) {
        Example example = new Example(ProjInfoPo.class);
        Example.Criteria criteria = example.createCriteria();
        if (!sectionList.isEmpty())
            criteria.andIn("sectionId", sectionList);//标段IN
        String projectId = String.valueOf(mapWhere.get("projectId"));//获取项目ID
        if (StringHelper.isNotNullAndEmpty(projectId))
            criteria.andEqualTo("projectId", projectId);//项目ID
        example.setOrderByClause("PARENT_ID,SORT_NUM asc");
        List<ProjInfoPo> projInfoList = this.selectByExample(example);
        List<ProjInfoVo> projInfoVoList = Lists.newArrayList();
        /**
         * 查询数据处理
         */
        Map<String, DictionaryVo> orgTypeDictMap = commDictService.getDictMapByTypeCode("base.org.type");
        Map<String, DictionaryVo> orgClassDictMap = commDictService.getDictMapByTypeCode("base.org.classification");
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(Integer.valueOf(projectId));

        for (ProjInfoPo projInfoPo : projInfoList) {
            ProjInfoVo projInfoVo = new ProjInfoVo();
            dozerMapper.map(projInfoPo, projInfoVo);//po对象转换为Vo对象
            projInfoVo.getOrgCategoryVo().setCode(projInfoPo.getOrgCategory());
            projInfoVo.getOrgCategoryVo().setName(szxmCommonUtil
                    .getDictionaryName(orgClassDictMap, projInfoPo.getOrgCategory()));
            projInfoVo.getOrgTypeVo().setCode(projInfoPo.getOrgType());
            projInfoVo.getOrgTypeVo().setName(szxmCommonUtil.getDictionaryName(orgTypeDictMap, projInfoPo.getOrgType()));
            //根据标段ID查询 标段Bean
            ProjectTeamVo sectionVO = sectionMap.get(projInfoPo.getSectionId());
            projInfoVo.setSectionCode(sectionVO.getCode());
            projInfoVo.setSectionName(sectionVO.getName());
            projInfoVo.setSectionType(sectionVO.getTypeName());
            projInfoVo.setIsProject("0");
            projInfoVo.setSort(projInfoPo.getSort());
            projInfoVoList.add(projInfoVo);
        }
        Collections.sort(projInfoVoList);

        String projView = String.valueOf(mapWhere.get("projView"));//获取项目视图

        List<ProjInfoVo> projInfoVos = szxmCommonUtil.generateTree(projInfoVoList);
        if (StringHelper.isNotNullAndEmpty(projView)) {//如果是项目团队的视图
            ProjInfoVo projInfo = new ProjInfoVo();
            projInfo.setId(Integer.valueOf(projectId));
            projInfo.setProjectId(Integer.valueOf(projectId));
            PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(projectId));
            projInfo.setProjectName(projectVo.getName());
            projInfo.setOrgName(projectVo.getName());
            projInfo.setIsProject("1");
            projInfo.setSort(0);
            if (!ObjectUtils.isEmpty(projInfoVos))
                projInfo.setChildren(projInfoVos);
            List<ProjInfoVo> allResult = Lists.newArrayList();
            allResult.add(projInfo);
            return allResult;
        }
        // resloveTreeToSort(projInfoVos);
        return projInfoVos;
    }

    /**
     * 递归排序
     *
     * @param tree
     * @return
     */
    private List<ProjInfoVo> resloveTreeToSort(List<ProjInfoVo> tree) {
        if (!ObjectUtils.isEmpty(tree)) {
            for (ProjInfoVo projInfoVo : tree) {
                List<ProjInfoVo> projInfoVoChildren = projInfoVo.getChildren();
                if (!ObjectUtils.isEmpty(projInfoVoChildren)) {
                    List<ProjInfoVo> new_childList = this.resloveTreeToSort(projInfoVoChildren);
                    projInfoVo.setChildren(new_childList);
                }
            }
            tree = SortUtil.sortObj(tree, "sort", "desc");
        }
        return tree;
    }

    @Override
    public ProjInfoVo addPorjInfo(ProjInfoAddForm projInfoAddForm) {
        ProjInfoPo projInfoPo = dozerMapper.map(projInfoAddForm, ProjInfoPo.class);
        projInfoPo.setOrgCode(DateUtil.getDateFormat(new Date(), "yyyyMMddHHmmss"));
        if (ObjectUtils.isEmpty(projInfoPo.getSort())) {
            projInfoPo.setSort(this.getNextSortByParentId(projInfoPo.getParentId()));
        }
        super.insert(projInfoPo);
        ProjInfoVo projInfoVo = dozerMapper.map(projInfoPo, ProjInfoVo.class);
        projInfoVo.getOrgCategoryVo().setCode(projInfoPo.getOrgCategory());
        projInfoVo.getOrgCategoryVo().setName(szxmCommonUtil
                .getDictionaryName("base.org.classification", projInfoPo.getOrgCategory()));
        projInfoVo.getOrgTypeVo().setCode(projInfoPo.getOrgType());
        projInfoVo.getOrgTypeVo().setName(szxmCommonUtil.getDictionaryName("base.org.type", projInfoPo.getOrgType()));
        //根据标段ID查询 标段Bean
        ProjectTeamVo sectionVO = commProjectTeamService.getProjectTeamById(projInfoPo.getSectionId());
        projInfoVo.setSectionCode(sectionVO.getCode());
        projInfoVo.setSectionName(sectionVO.getName());
        projInfoVo.setSectionType(sectionVO.getTypeName());
        try {
            //更新考勤信息
            zkService.addOrUpdateZkOrg(projInfoVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return projInfoVo;
    }

    @Override
    public ApiResult addPorjInfos(List<ProjInfoPo> listProjInfoPo) {
        if (ObjectUtils.isEmpty(listProjInfoPo)) {
            return ApiResult.error("传入同步分包单位为空");
        }
        ArrayList<ProjInfoVo> projInfoVos = Lists.newArrayList();
        int i = 1;

        Integer sort = this.getNextSortByParentId(listProjInfoPo.get(0).getParentId());
        for (ProjInfoPo projInfoPo : listProjInfoPo) {
            if (ObjectUtils.isEmpty(projInfoPo.getSort())) {
                projInfoPo.setSort(sort + i);
                i++;
            }
        }
        super.insert(listProjInfoPo);

        for (ProjInfoPo projInfoPo : listProjInfoPo) {
            ProjInfoVo projInfoVo = dozerMapper.map(projInfoPo, ProjInfoVo.class);
            projInfoVos.add(projInfoVo);
        }
        if (ObjectUtils.isEmpty(projInfoVos)) {
            logger.info("增加或更新考勤系统组织为空");
            return ApiResult.error("更新考勤信息为空");
        }
        try {
            List<String> names = ListUtil.toValueList(projInfoVos,"orgName",String.class);
            logger.info("增加或更新系统组织机构为：{}", names);
            //更新考勤信息
            int zkOrgCount = zkService.addOrUpdateZkOrg(projInfoVos);
            String result = 1 == zkOrgCount ? "成功" : "失败";
            logger.info("增加或更新考勤系统组织机构结果：{}", result);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ApiResult.success();
    }

    @Override
    public ProjInfoVo updatePorjInfo(ProjInfoUpdateForm projInfoUpdateForm) {
        ProjInfoPo projInfouUpdatePo = dozerMapper.map(projInfoUpdateForm, ProjInfoPo.class);//form对象转为PO对象
        super.updateSelectiveById(projInfouUpdatePo);//根据ID更新po，值为null的不更新，只更新不为null的值

        ProjInfoPo projInfoPo = this.selectById(projInfouUpdatePo.getId());//将数据查询出来
        ProjInfoVo projInfoVo = dozerMapper.map(projInfoPo, ProjInfoVo.class);
        projInfoVo.getOrgCategoryVo().setCode(projInfoPo.getOrgCategory());
        projInfoVo.getOrgCategoryVo().setName(szxmCommonUtil
                .getDictionaryName("base.org.classification", projInfoPo.getOrgCategory()));
        projInfoVo.getOrgTypeVo().setCode(projInfoPo.getOrgType());
        projInfoVo.getOrgTypeVo().setName(szxmCommonUtil.getDictionaryName("base.org.type", projInfoPo.getOrgType()));
        //根据标段ID查询 标段Bean
        ProjectTeamVo sectionVO = commProjectTeamService.getProjectTeamById(projInfoPo.getSectionId());
        projInfoVo.setSectionCode(sectionVO.getCode());
        projInfoVo.setSectionName(sectionVO.getName());
        projInfoVo.setSectionType(sectionVO.getTypeName());
        try {
            //更新考勤信息
            zkService.addOrUpdateZkOrg(projInfoVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return projInfoVo;
    }

    @Override
    public void deletePorjInfo(Integer id) {
        //此处ID其实就一个
        List<ProjInfoPo> allProjInfoPos = this.queryChildrenAndMePos(id);
        List<Integer> allIds = Lists.newArrayList();
        for (ProjInfoPo projInfoPo : allProjInfoPos) {
            allIds.add(projInfoPo.getId());
        }
        if (!ObjectUtils.isEmpty(allIds)) {
            //删除人员表信息
            peopleService.deletePeopleByInfoIds(allIds);
            //删除项目仓库信息
            mapper.deleteWarnHouseByInfoIds(allIds);
            //删除调试平台信息
            mapper.deleteTsPlatByInfoIds(allIds);
            //删除文件
            //删除文件
            String bizIds = "";
            for (Integer id_ : allIds) {
                bizIds += (id_ + ",");
            }
            bizIds = bizIds.substring(0, bizIds.lastIndexOf(","));
            commDocService.deleteDocByBizTypeAndBizIds("STAFF-PROJINFO", bizIds);

            try {
                //删除考勤信息//此处ID其实就一个
                ProjInfoVo projInfoVo = dozerMapper.map(this.selectById(id), ProjInfoVo.class);
                zkService.deleteZkOrg(projInfoVo);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            //删除自身及其子表
            this.deleteByIds(allIds);
        }
    }

    @Override
    public Integer updatePorjInfoMsg(ProjectTeamVo sgPojectTeamVo) {
        Example example = new Example(ProjInfoPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sysOrgId", sgPojectTeamVo.getId());

        ProjInfoPo projInfoUpdatePo = new ProjInfoPo();//部分更新值
        projInfoUpdatePo.setOrgName(sgPojectTeamVo.getName());
        projInfoUpdatePo.setProjUnitName(sgPojectTeamVo.getName());
        projInfoUpdatePo.setOrgCategory(sgPojectTeamVo.getOrgClassification());//单位分类
        projInfoUpdatePo.setOrgType(sgPojectTeamVo.getTypeCoe());//单位类型

        int res = this.updateByExampleSelective(projInfoUpdatePo, example);//根据条件更新;
        try {
            //更新考勤信息
            ProjInfoPo projInfoPo = this.selectOneByExample(example);
            if (!ObjectUtils.isEmpty(projInfoPo)) {
                ProjInfoVo projInfoVo = dozerMapper.map(projInfoPo, ProjInfoVo.class);
                zkService.addOrUpdateZkOrg(projInfoVo);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return res;

    }

    @Override
    public Integer deletePorjInfoMsg(Integer sysOrgid) {

        Example example = new Example(ProjInfoPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sysOrgId", sysOrgid);
        ProjInfoPo projInfoPo = this.selectOneByExample(example);
        this.deletePorjInfo(projInfoPo.getId());
        return projInfoPo.getId();
    }

    @Override
    public ProjInfoVo selectByProjInfoId(Integer id) {
        ProjInfoPo projInfoPo = this.selectById(id);
        if (ObjectUtils.isEmpty(projInfoPo))
            return new ProjInfoVo();
        ProjInfoVo projInfoVo = dozerMapper.map(projInfoPo, ProjInfoVo.class);
        //根据标段ID查询 标段Bean
        projInfoVo.getOrgCategoryVo().setCode(projInfoPo.getOrgCategory());
        projInfoVo.getOrgCategoryVo().setName(szxmCommonUtil
                .getDictionaryName("base.org.classification", projInfoPo.getOrgCategory()));
        projInfoVo.getOrgTypeVo().setCode(projInfoPo.getOrgType());
        projInfoVo.getOrgTypeVo().setName(szxmCommonUtil.getDictionaryName("base.org.type", projInfoPo.getOrgType()));

        ProjectTeamVo sectionVO = commProjectTeamService.getProjectTeamById(projInfoPo.getSectionId());
        projInfoVo.setSectionCode(sectionVO.getCode());
        projInfoVo.setSectionName(sectionVO.getName());
        projInfoVo.setSectionType(sectionVO.getTypeName());
        return projInfoVo;
    }

    @Override
    public Object getOrgPeopleList(Map<String, Object> mapWhere, List<String> sectionList) {
        //查询所有标段下的组织机构
        mapWhere.put("projectId", StringHelper.formattString(String.valueOf(mapWhere.get("projectId"))));
        mapWhere.put("sectionList", sectionList);
        List<OrgPeopleVo> orgPeopleVos = mapper.selectOrgInfoList(mapWhere);

        //查询符合条件的人员
        List<String> typeList = Lists.newArrayList();
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("type")))) {
            String[] typeArray = String.valueOf(mapWhere.get("type")).split(",");
            typeList = new ArrayList<String>(Arrays.asList(typeArray));
        }
        if (ObjectUtils.isEmpty(typeList))
            typeList.add("");
        mapWhere.put("typeList", typeList);
        mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
        List<PeopleVo> peopleVoList = peopleService.selectOrgPeople(mapWhere);

        //将组织机构拼装为树
        orgPeopleVos = szxmCommonUtil.generateTree(orgPeopleVos);//组装成树
        Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
        Map<String, DictionaryVo> workTypeDictMap = commDictService.getDictMapByTypeCode("szxm.rygl.worktype");
        buildTreeChildren(orgPeopleVos, peopleVoList, psotionDictMap, workTypeDictMap);
        return orgPeopleVos;
    }

    @Override
    public List<AddressBookOrgVo> selectAddressBookOrg(Map<String, Object> mapWhere) {
        List<AddressBookOrgVo> addressBookOrgVos = mapper.selectAddressBookOrg(mapWhere);
        //标段需要排序，将标段取出后进行排序操作
        List<AddressBookOrgVo> sectionList = Lists.newArrayList();
        List<AddressBookOrgVo> otherList = Lists.newArrayList();

        if (ObjectUtils.isEmpty(addressBookOrgVos)) {
            return addressBookOrgVos;
        }
        for (AddressBookOrgVo addressBookOrgVo : addressBookOrgVos) {
            if (!"section".equals(addressBookOrgVo.getType())) {
                addressBookOrgVo.setSectionCode(null);
                otherList.add(addressBookOrgVo);
            } else {
                sectionList.add(addressBookOrgVo);
            }
        }
        if (ObjectUtils.isEmpty(sectionList)) {
            return addressBookOrgVos;
        }
        Collections.sort(sectionList);
        otherList.addAll(sectionList);
        return otherList;
    }

    @Override
    public List<PeopleVo> selectAddressBookPeopleFyz(Integer projectId, Integer userId, String searcher) {
        List<PeopleVo> peopleVoList = mapper.selectAddressBookPeopleFyz(projectId, userId, searcher);
        return peopleVoList;
    }

    @Override
    public List<PeopleVo> selectAddressBookPeople2(Integer projectId, Integer orgId, Integer userId, String searcher) {
        List<PeopleVo> peopleVoList = mapper.selectAddressBookPeople2(projectId, orgId, userId, searcher);
        return peopleVoList;
    }

    @Override
    public List<PeopleVo> selectAddressBookPeopleYz(Integer projectId, String searcher) {
        List<PeopleVo> peopleVoList = mapper.selectAddressBookPeopleYz(projectId, searcher);
        return peopleVoList;
    }

    @Override
    public List<PeopleVo> queryUserListByProjectTeamId(Integer orgId, String searcher) {
        return mapper.queryUserListByProjectTeamId(orgId, searcher);
    }

    @Override
    public Integer queryProjInfoNotUpdate(String sectionId) {
        return mapper.queryProjInfoNotUpdate(sectionId);
    }

    private void buildTreeChildren(List<OrgPeopleVo> orgPeopleVos, List<PeopleVo> peopleVoList, Map<String, DictionaryVo> psotionDictMap, Map<String, DictionaryVo> workTypeDictMap) {
        for (OrgPeopleVo orgPeopleVo : orgPeopleVos) {
            if ("org".equals(orgPeopleVo.getType())) {
                addNewChildren(orgPeopleVo, peopleVoList, psotionDictMap, workTypeDictMap);
                if (!ObjectUtils.isEmpty(orgPeopleVo.getChildren()))
                    buildTreeChildren(orgPeopleVo.getChildren(), peopleVoList, psotionDictMap, workTypeDictMap);
            }
        }
    }

    private void addNewChildren(OrgPeopleVo root, List<PeopleVo> peopleVoList, Map<String, DictionaryVo> psotionDictMap, Map<String, DictionaryVo> workTypeDictMap) {
        List<OrgPeopleVo> newChildren = Lists.newArrayList();
        for (PeopleVo peopleVo : peopleVoList) {
            if (peopleVo.getProjInfoId().equals(root.getId())) {
                OrgPeopleVo orgPeopleVo = new OrgPeopleVo();
                orgPeopleVo.setId(peopleVo.getId());
                orgPeopleVo.setType("people");
                orgPeopleVo.setPositionName(szxmCommonUtil.getDictionaryName(psotionDictMap, peopleVo.getJobVo().getCode()));
                orgPeopleVo.setRyType(peopleVo.getTypeVo().getName());
                String workTypeNames = "";
                if (StringHelper.isNotNullAndEmpty(peopleVo.getWorkTypeVo().getCode())) {
                    String[] workTypes = peopleVo.getWorkTypeVo().getCode().split(",");
                    for (String s : workTypes) {
                        String workTypeName = szxmCommonUtil.getDictionaryName(workTypeDictMap, s);
                        workTypeNames += workTypeName + ",";
                    }
                }

                if (StringHelper.isNotNullAndEmpty(workTypeNames))
                    workTypeNames = workTypeNames.substring(0, workTypeNames.lastIndexOf(","));
                orgPeopleVo.setWorkTypeName(workTypeNames);
                orgPeopleVo.setName(peopleVo.getName());
                orgPeopleVo.setProjInfoId(root.getId());
                orgPeopleVo.setOrgName(root.getName());
                newChildren.add(orgPeopleVo);
            }
        }
        if (!ObjectUtils.isEmpty(newChildren))
            root.addChildrens(newChildren);
    }

    @Override
    public Integer porjInfoMsg(ProjectTeamVo sgPojectTeamVo) {
        Integer parentId = 0;
        ProjInfoPo insertPo = this.addPorjInfoMsg(parentId, sgPojectTeamVo);
        return insertPo.getId();
    }

    private ProjInfoPo addPorjInfoMsg(Integer parnetId, ProjectTeamVo projectTeamVo) {
        Example example = new Example(ProjInfoPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sysOrgId", projectTeamVo.getId());
        ProjInfoPo exitsPo = this.selectOneByExample(example);
        if (ObjectUtils.isEmpty(exitsPo)) {
            ProjInfoPo insertPo = new ProjInfoPo();
            insertPo.setParentId(parnetId);
            insertPo.setOrgCode(DateUtil.getDateFormat(new Date(), "yyyyMMddHHmmss"));
            if (ObjectUtils.isEmpty(insertPo.getSort())) {
                insertPo.setSort(this.getNextSortByParentId(insertPo.getParentId()));
            }
            insertPo.setProjectId(projectTeamVo.getProjectId());
            insertPo.setSectionId(projectTeamVo.getParentId());
            insertPo.setOrgName(projectTeamVo.getName());
            insertPo.setProjUnitName(projectTeamVo.getName());
            insertPo.setOrgCategory(projectTeamVo.getOrgClassification());//单位分类Code
            insertPo.setOrgType(projectTeamVo.getTypeCoe());//单位类型Code
            insertPo.setSysOrgId(projectTeamVo.getId());
            super.insert(insertPo);
            //更新考勤，记录此处捕获异常，不抛出，以免影响事务
            try {
                //更新考勤信息
                ProjInfoVo projInfoVo = dozerMapper.map(insertPo, ProjInfoVo.class);
                zkService.addOrUpdateZkOrg(projInfoVo);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            return insertPo;
        } else {
            return exitsPo;
        }

    }

    private Integer getNextSortByParentId(Integer parentId) {
        Example example = new Example(ProjInfoPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        return mapper.selectCountByExample(example);
    }

    @Override
    public List<SysOrgTreeVo> getOrgInfoByProjectId(Integer projectId) {
        //根据条件查询所有的组织信息
        List<SysOrgTreeVo> orgAll = mapper.getAllOrgInfoByProjectId(projectId);
        //去除掉外部单位
        orgAll = orgAll.stream().filter(item-> !StringUtils.equals(item.getOrgName(),"外部单位")).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(orgAll)){
            return Lists.newArrayList();
        }
        List<SysOrgTreeVo> retList;

        List<Integer> orgIds = orgAll.stream().map(SysOrgTreeVo :: getId).distinct().collect(Collectors.toList());
        List<SysOrgTreeVo> orgAllTree = TreeUtil.bulid(orgAll, 0);
        //设置标识
        boolean auth = false;
        //递归返回符合条件的树形
        retList = this.querySearchOrgTree(orgIds,orgAllTree,auth);
        return retList;
    }
    /**
     * 递归查询符合条件的org结果集
     *
     * @param retIds
     * @param orgAllTree
     * @param auth
     * @return
     */
    private List<SysOrgTreeVo> querySearchOrgTree(List<Integer> retIds, List<SysOrgTreeVo> orgAllTree, boolean auth) {
        List<SysOrgTreeVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(orgAllTree)) {
            //判断当前id是否包含在查询范围内
            for (SysOrgTreeVo sysOrgVo : orgAllTree) {
                boolean thisAuth = auth;
                if (!thisAuth && retIds.contains(sysOrgVo.getId())) {
                    thisAuth = true;
                }
                //递归查询子节点
                List<SysOrgTreeVo> childrenList = this.querySearchOrgTree(retIds, sysOrgVo.getChildren(), thisAuth);

                if (thisAuth || !ObjectUtils.isEmpty(childrenList)) {
                    SysOrgTreeVo retOrgVo = new SysOrgTreeVo();
                    this.dozerMapper.map(sysOrgVo, retOrgVo);
                    if (!ObjectUtils.isEmpty(childrenList))
                        retOrgVo.setChildren(childrenList);
                    retList.add(retOrgVo);
                }
            }
        }

        return retList;
    }








    @Override
    public List<ParticipateUnitVo> getParticipateUnitInfo(Map<String, Object> mapWhere) {
        Integer projectId= Integer.valueOf( mapWhere.get("projectId").toString());
        String sectionIds = mapWhere.get("sectionIds").toString();//获取标段ID集合
       // String orgCategory = mapWhere.get("orgCategory").toString();//获取标段ID集合
        List<ProjInfoPo> projInfoList= new ArrayList<ProjInfoPo>();
        //标段 不为空
        if(StringHelper.isNotNullAndEmpty(sectionIds) )
        {
            String[] sectionIdsArray = sectionIds.split(",");
            List<String> sectionList = new ArrayList<String>(Arrays.asList(sectionIdsArray));
            Example example = new Example(ProjInfoPo.class);
            Example.Criteria criteria = example.createCriteria();
            if (!sectionList.isEmpty())
                criteria.andIn("sectionId", sectionList);//标段IN
            //String projectId = String.valueOf(mapWhere.get("projectId"));//获取项目ID
            if (StringHelper.isNotNullAndEmpty(projectId.toString()))
                criteria.andEqualTo("projectId", projectId);//项目ID
            example.setOrderByClause("PARENT_ID,SORT_NUM asc");
            projInfoList = this.selectByExample(example);

        }
        else
        {
            //标段id 为空
            projInfoList= mapper.selectParticipartUnitProInfo(mapWhere);
        }
        List<ParticipateUnitVo> ParticipateUnitVoList = Lists.newArrayList();
        /**
         * 查询数据处理
         */
        Map<String, DictionaryVo> orgTypeDictMap = commDictService.getDictMapByTypeCode("base.org.type");
        Map<String, DictionaryVo> orgClassDictMap = commDictService.getDictMapByTypeCode("base.org.classification");
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(Integer.valueOf(projectId));

        for (ProjInfoPo projInfoPo : projInfoList) {
            ParticipateUnitVo participateUnitVo = new ParticipateUnitVo();
            //根据标段ID查询 标段Bean
            ProjectTeamVo sectionVO = sectionMap.get(projInfoPo.getSectionId());
            //项目id
            participateUnitVo.setProjectId(projInfoPo.getProjectId());
            //项目名称
            //participateUnitVo.setProjectName(projInfoPo.getp());
            //单位名称
            participateUnitVo.setOrgName(projInfoPo.getOrgName());
            //标段id
            participateUnitVo.setId(sectionVO.getId());
            //标段code
            participateUnitVo.setSectionCode(sectionVO.getCode());
            //标段名称
            participateUnitVo.setSectionName(sectionVO.getName());
            //标段基础信息id
            participateUnitVo.setProjInfoId (projInfoPo.getId().toString());
            //sort
            participateUnitVo.setSort(projInfoPo.getSort());
            //项目名称
            if (!ObjectUtils.isEmpty(sectionVO.getCuList())) {
                //项目名称
                participateUnitVo.setProjectName(sectionVO.getCuList().get(0).getName());
            }
            else
            {
                //项目名称
                participateUnitVo.setProjectName("");
            }

            Map<String, Object> map=new HashMap();
            map.put("sectionId",projInfoPo.getSectionId());
            map.put("job","2-1");
            List<PeopleEntryDetailVo> peopleEntryDetailVoList = peopleEntryDetailMapper.selectPeopleEntryDetail(map);
            if (!ObjectUtils.isEmpty(peopleEntryDetailVoList)) {
                //项目经理
                participateUnitVo.setProjectLeader(peopleEntryDetailVoList.get(0).getName());
            }
            else
            {
                //项目经理
                participateUnitVo.setProjectLeader("");
            }
            //监理单位
            if(!ObjectUtils.isEmpty(sectionVO.getCcuList()))
            {
                participateUnitVo.setSupervisionUnit(sectionVO.getCcuList().get(0).getName());
            }
            else
            {
                participateUnitVo.setSupervisionUnit("");
            }
            //总监代表
            map.put("job","3-2");
            peopleEntryDetailVoList = peopleEntryDetailMapper.selectPeopleEntryDetail(map);
            if (!ObjectUtils.isEmpty(peopleEntryDetailVoList)) {
                //总监代表
                participateUnitVo.setDirector(peopleEntryDetailVoList.get(0).getName());
            }
            else
            {
                //总监代表
                participateUnitVo.setDirector("");
            }
            //根据标段id获取业主userID
            List<GeneralVo> Users=sectionVO.getOwnerList();
         //   ImmutableMap<Integer, GeneralVo> userId2User = Maps.uniqueIndex(Users, sysUser -> sysUser.getId());
            //分管业主
            if(!ObjectUtils.isEmpty(Users))
            {
                StringBuffer branchManage=new StringBuffer();
                for (GeneralVo  user  :Users)
                {
                    branchManage.append(user.getName()).append("、");
                }
                participateUnitVo.setBranchManage(branchManage.toString().substring(0,branchManage.length()-1));
            }
            else
            {
                participateUnitVo.setBranchManage("");
            }
            ParticipateUnitVoList.add(participateUnitVo);
        }

        return ParticipateUnitVoList;
    }

    @Override
    public List<ParticipateUnitVo> getProjectPeopleInfo(Map<String, Object> mapWhere) {
        Integer projectId= Integer.valueOf( mapWhere.get("projectId").toString());
       // String sectionId = mapWhere.get("sectionIds").toString();//获取标段ID集合
       // String orgCategory = mapWhere.get("orgCategory").toString();//获取标段ID集合
        //根据sessionid  查找施工作业队
        List<ProjInfoPo> projInfoList=mapper.selectOrgCategoryBySessionId(mapWhere);

        if(!ObjectUtils.isEmpty(projInfoList))
        {
            //遍历施工作业队公司查找
            for (ProjInfoPo projInfoPo : projInfoList)
            {

            }
        }




        List<ParticipateUnitVo> ParticipateUnitVoList = Lists.newArrayList();
        /**
         * 查询数据处理
         */
        Map<String, DictionaryVo> orgTypeDictMap = commDictService.getDictMapByTypeCode("base.org.type");
        Map<String, DictionaryVo> orgClassDictMap = commDictService.getDictMapByTypeCode("base.org.classification");
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(Integer.valueOf(projectId));

        for (ProjInfoPo projInfoPo : projInfoList) {
            ParticipateUnitVo participateUnitVo = new ParticipateUnitVo();
            //根据标段ID查询 标段Bean
            ProjectTeamVo sectionVO = sectionMap.get(projInfoPo.getSectionId());
            //项目id
            participateUnitVo.setProjectId(projInfoPo.getProjectId());
            //项目名称
            //participateUnitVo.setProjectName(projInfoPo.getp());
            //单位名称
            participateUnitVo.setOrgName(projInfoPo.getOrgName());
            //标段id
            participateUnitVo.setId(sectionVO.getId());
            //标段code
            participateUnitVo.setSectionCode(sectionVO.getCode());
            //标段名称
            participateUnitVo.setSectionName(sectionVO.getName());
            //标段基础信息id
            participateUnitVo.setProjInfoId (projInfoPo.getId().toString());
            //sort
            participateUnitVo.setSort(projInfoPo.getSort());
            //项目名称
            if (!ObjectUtils.isEmpty(sectionVO.getCuList())) {
                //项目名称
                participateUnitVo.setProjectName(sectionVO.getCuList().get(0).getName());
            }
            else
            {
                //项目名称
                participateUnitVo.setProjectName("");
            }

            Map<String, Object> map=new HashMap();
            map.put("sectionId",projInfoPo.getSectionId());
            map.put("job","2-1");
            List<PeopleEntryDetailVo> peopleEntryDetailVoList = peopleEntryDetailMapper.selectPeopleEntryDetail(map);
            if (!ObjectUtils.isEmpty(peopleEntryDetailVoList)) {
                //项目经理
                participateUnitVo.setProjectLeader(peopleEntryDetailVoList.get(0).getName());
            }
            else
            {
                //项目经理
                participateUnitVo.setProjectLeader("");
            }
            //监理单位
            if(!ObjectUtils.isEmpty(sectionVO.getCcuList()))
            {
                participateUnitVo.setSupervisionUnit(sectionVO.getCcuList().get(0).getName());
            }
            else
            {
                participateUnitVo.setSupervisionUnit("");
            }
            //总监代表
            map.put("job","3-2");
            peopleEntryDetailVoList = peopleEntryDetailMapper.selectPeopleEntryDetail(map);
            if (!ObjectUtils.isEmpty(peopleEntryDetailVoList)) {
                //总监代表
                participateUnitVo.setDirector(peopleEntryDetailVoList.get(0).getName());
            }
            else
            {
                //总监代表
                participateUnitVo.setDirector("");
            }
            //根据标段id获取业主userID
            List<GeneralVo> Users=sectionVO.getOwnerList();
            //   ImmutableMap<Integer, GeneralVo> userId2User = Maps.uniqueIndex(Users, sysUser -> sysUser.getId());
            //分管业主
            if(!ObjectUtils.isEmpty(Users))
            {
                StringBuffer branchManage=new StringBuffer();
                for (GeneralVo  user  :Users)
                {
                    branchManage.append(user.getName()).append("、");
                }
                participateUnitVo.setBranchManage(branchManage.toString().substring(0,branchManage.length()-1));
            }
            else
            {
                participateUnitVo.setBranchManage("");
            }
            ParticipateUnitVoList.add(participateUnitVo);
        }

        return ParticipateUnitVoList;
    }
}
