package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.*;
import com.wisdom.acm.sys.enums.SysEnum;
import com.wisdom.acm.sys.mapper.SectionMapper;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.po.SysUserOrgRolePo;
import com.wisdom.acm.sys.service.*;
import com.wisdom.acm.sys.vo.ProjectTeamUserVo;
import com.wisdom.acm.sys.vo.SectionTreeVo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.SzxmCommonService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.PageUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.feign.plan.CommPlanService;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import com.wisdom.base.common.vo.sys.SectionProjectVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标段-逻辑处理
 */
@Service
public class SectionServiceImpl extends BaseService<SectionMapper, SysOrgPo> implements SectionService {

    @Autowired
    private CommUserService userService;

    @Autowired
    private SzxmCommonService szxmCommonService;

    @Autowired
    private CommPlanService commPlanService;

    @Autowired
    private ProjectTeamService projectTeamService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserOrgRoleService userOrgRoleService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private SysUserOrgService sysUserOrgService;

    @Autowired
    private CommDictService commDictService;

    /**
     * 根据父子关系获取所属标段
     * @param sectionMap
     * @param cpMap
     * @param id
     * @return
     */
    private SectionProjectVo getSectionProjectVo(Map<Integer,SectionProjectVo> sectionMap, Map<Integer,Integer> cpMap, Integer id){
        SectionProjectVo vo = null;
        Integer parentId = cpMap.get(id);
        if (parentId != null){
            vo = sectionMap.get(parentId);
            if (vo == null){
                vo = this.getSectionProjectVo(sectionMap, cpMap ,parentId);
            }
        }
        return vo;
    }

    private List<SectionProjectVo> getChildrenSectionProjectVo(Map<Integer,SectionProjectVo> sectionMap,Map<Integer,List<Integer>> pcMap, Integer id){
        List<SectionProjectVo> vos = null;
        SectionProjectVo vo;
        List<Integer> ids = pcMap.get(id);
        if (ids != null){
            for(Integer cid : ids){
                vo = sectionMap.get(cid);
                if (vo != null){
                    if (vos == null){
                        vos = new ArrayList<>();
                    }
                    vos.add(vo);
                }
            }
        }
        return vos;
    }

    @Override
    public List<SectionProjectVo> querySectionProjectListByUserId(Integer userId){
        List<SectionProjectVo> sectionProjectVos = null;
        //获取用户部门/单位信息
        List<UserOrgVo> userOrgVos = this.sysUserOrgService.queryListByUserId(ParamEnum.PROJECT.getCode(),userId);
        if (!ObjectUtils.isEmpty(userOrgVos)){
            sectionProjectVos = new ArrayList<>();
            //项目IDS
            List<Integer> projectIds = new ArrayList<>();
            //业主用户没有专业Map<项目ID,项目ID>
            Map<Integer,Object> pMap = new HashMap<>();
            //用户是业主且有专业   Map<项目ID,List<专业>>
            Map<Integer,List<String>> ppMap = new HashMap<>();
            List<String> professionalList;//专业List
            //用户所属的项目团队以项目ID分组Map<项目ID,List<部门/单位ID>>
            Map<Integer,List<Integer>> projOrgMap = new HashMap<>();
            List<Integer> ids;
            String professional;//专业
            for(UserOrgVo vo : userOrgVos){
                //记录项目IDS
                projectIds.add(vo.getBizId());
                //用户所属的项目团队以项目ID分组
                ids = projOrgMap.get(vo.getBizId());
                if (ids == null){
                    ids = new ArrayList<>();
                    projOrgMap.put(vo.getBizId(),ids);
                }
                //ids.add(vo.getId());
                ids.add(vo.getOrgId());
                //施工单位 下的用户则代表是业主
                if (SysEnum.DEVELOPMENT_ORGANIZATION.getCode().equals(vo.getOrgType())){
                    professional = vo.getProfessional();
                    if (ObjectUtils.isEmpty(professional)){
                        pMap.put(vo.getBizId(),vo.getBizId());
                    }
                    else{
                        professionalList = ppMap.get(vo.getBizId());
                        if (professionalList == null){
                            professionalList = new ArrayList<>();
                            ppMap.put(vo.getBizId(),professionalList);
                        }
                        String [] professionalArr = professional.split(",");
                        for(String p : professionalArr){
                            professionalList.add(p);
                        }
                    }
                }
            }
            //获取标段信息Map<标段ID,SectionProjectVo>
            Map<Integer,SectionProjectVo> sectionMap = new HashMap<>();//标段集合
            //获取标段信息Map<项目ID,SectionProjectVo>
            Map<Integer,List<SectionProjectVo>> sectionMap_ = new HashMap<>();//标段集合
            //根据项目与专业分组标段Map<项目ID,Map<专业Code,List<SectionProjectVo>>>
            Map<Integer,Map<String,List<SectionProjectVo>>> sectionMap_p = new HashMap<>();//标段集合
            Map<String,List<SectionProjectVo>> pMap_;
            List<SectionProjectVo> sectionList;
            Map<Integer,Integer> cpMap = new HashMap<>();//子父关系
            Map<Integer,List<Integer>> pcMap = new HashMap<>();//父子关系
            projectIds = ListUtil.distinct(projectIds);//去重
            List<SectionProjectVo> vos = this.mapper.selectSectionProjectVos(null,projectIds);
            for(SectionProjectVo vo:vos){
                if (SysEnum.SECTION.getCode().equals(vo.getOrgType())){//是标段
                    sectionMap.put(vo.getId(),vo);
                    sectionList = sectionMap_.get(vo.getProjectId());
                    if (sectionList == null){
                        sectionList = new ArrayList<>();
                        sectionMap_.put(vo.getProjectId(),sectionList);
                    }
                    sectionList.add(vo);
                    if (!ObjectUtils.isEmpty(vo.getProfessional())){
                        pMap_ = sectionMap_p.get(vo.getProjectId());
                        if (pMap_ ==  null){
                            pMap_ = new HashMap<>();
                            sectionMap_p.put(vo.getProjectId(),pMap_);
                        }
                        String[] pArry = vo.getProfessional().split(",");
                        for(String p : pArry){
                            sectionList = pMap_.get(p);
                            if (sectionList == null){
                                sectionList = new ArrayList<>();
                                pMap_.put(p,sectionList);
                            }
                            sectionList.add(vo);
                        }
                    }
                }
                cpMap.put(vo.getId(),vo.getParentId());
                ids = pcMap.get(vo.getParentId());
                if (ids == null){
                    ids = new ArrayList<>();
                    pcMap.put(vo.getParentId(),ids);
                }
                ids.add(vo.getId());
            }
            //抽取出各项目下用户有权限的标段信息
            for(Integer projectId : projectIds){
                if (!ObjectUtils.isEmpty(pMap.get(projectId))){//是业主且没有专业
                    if (!ObjectUtils.isEmpty(sectionMap_.get(projectId))) {
                        sectionProjectVos.addAll(sectionMap_.get(projectId));
                    }
                }else {
                    if (!ObjectUtils.isEmpty(ppMap.get(projectId))){//是业主但是有专业
                        professionalList = ppMap.get(projectId);
                        if (professionalList != null){
                            pMap_ = sectionMap_p.get(projectId);
                            if (pMap_ != null){
                                for(String p : professionalList){
                                    sectionList = pMap_.get(p);
                                    if (sectionList != null){
                                        sectionProjectVos.addAll(sectionList);
                                    }
                                }
                            }
                        }
                    }else{ //非业主
                        ids = projOrgMap.get(projectId);
                        if (ids != null){
                            List<SectionProjectVo> list;
                            for(Integer id : ids){
                                SectionProjectVo vo = this.getSectionProjectVo(sectionMap, cpMap ,id);
                                if (vo != null){
                                    sectionProjectVos.add(vo);
                                    if (SysEnum.SUPERVISOR.getCode().equals(vo.getTypeCode())){//监理标
                                        list = this.getChildrenSectionProjectVo(sectionMap,pcMap,vo.getId());
                                        if (list != null){
                                            sectionProjectVos.addAll(list);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //去重
            if (!ObjectUtils.isEmpty(sectionProjectVos)){
                sectionProjectVos = ListUtil.distinct(sectionProjectVos);
            }
        }
        return sectionProjectVos;
    }

    @Override
    public void initSectionOrgInfo(List<SectionVo> sectionVos, int projectId){
        if (!ObjectUtils.isEmpty(sectionVos)){
            List<com.wisdom.acm.sys.vo.ProjectTeamVo> projTeamList = this.projectTeamService.queryProjectTeam(projectId);
            if (!ObjectUtils.isEmpty(projTeamList)){
                //Map<父级ID,单位集合>
                Map<Integer,List<GeneralVo>> ptMap = new HashMap<>();
                //Map<监理标ID,施工标IDS>
                Map<Integer,List<Integer>> cpsMap = new HashMap<>();
                //Map<施工标ID,监理标IDS>
                Map<Integer,List<Integer>> jlsMap = new HashMap<>();
                List<Integer> ids;
                List<GeneralVo> cList;
                //数据分组处理（根据父子关系分组项目团队）
                for(com.wisdom.acm.sys.vo.ProjectTeamVo vo : projTeamList){
                    int parentId = vo.getParentId();
                    cList = ptMap.get(parentId);
                    if (ObjectUtils.isEmpty(cList)){
                        cList = new ArrayList<>();
                        ptMap.put(parentId, cList);
                    }
                    cList.add(new GeneralVo(vo.getId(),vo.getTeamCode(),vo.getTeamName()));
                }
                //单位类型
                //如果是施工标则记录生成 监理标对应施工标的记录
//                for(SectionVo vo : sectionVos){
//                    String typeCode = vo.getTypeCoe();
//                    if(!SysEnum.SUPERVISOR.getCode().equals(typeCode)){//除监理类标段外
//                        ids = cpsMap.get(vo.getParentId());
//                        if (ObjectUtils.isEmpty(ids)){
//                            ids = new ArrayList<>();
//                            cpsMap.put(vo.getParentId(),ids);
//                        }
//                        ids.add(vo.getId());
//                    }
//                }
                //查询监理施工对应表
                Map<String,Object> mapWhere=Maps.newHashMap();
                List<Map<String,Object>> sgJlRelats=mapper.selectSgJlRelat(mapWhere);
                for(SectionVo vo : sectionVos){
                    String typeCode = vo.getTypeCoe();
                    if(SysEnum.SUPERVISOR.getCode().equals(typeCode)){//除监理类标段外

                        List<Integer> sgdwList=Lists.newArrayList();
                        for(Map<String,Object> map:sgJlRelats)
                        {
                            if(String.valueOf(vo.getId()).equals(String.valueOf(map.get("JLB_ID"))))
                            {
                                if(!sgdwList.contains(Integer.valueOf(String.valueOf(map.get("SGB_ID")))))
                                    sgdwList.add(Integer.valueOf(String.valueOf(map.get("SGB_ID"))));
                            }

                        }
                        cpsMap.put(vo.getId(),sgdwList);
                    }
                    else
                    {
                        List<Integer> jldwList=Lists.newArrayList();
                        for(Map<String,Object> map:sgJlRelats)
                        {
                            if(String.valueOf(vo.getId()).equals(String.valueOf(map.get("SGB_ID"))))
                            {
                                if(!jldwList.contains(Integer.valueOf(String.valueOf(map.get("JLB_ID")))))
                                    jldwList.add(Integer.valueOf(String.valueOf(map.get("JLB_ID"))));
                            }

                        }
                        jlsMap.put(vo.getId(),jldwList);
                    }
                }

                //给标段增加监理单位信息和施工单位信息
                for(SectionVo vo : sectionVos){
                    String typeCode = vo.getTypeCoe();
                    if(SysEnum.SUPERVISOR.getCode().equals(typeCode)){
                        vo.setCcuList(ptMap.get(vo.getId()));
                        ids = cpsMap.get(vo.getId());
                        if (!ObjectUtils.isEmpty(ids)){
                            cList = new ArrayList<>();
                            for(Integer cid : ids){
                                if (!ObjectUtils.isEmpty(ptMap.get(cid))){
                                    cList.addAll(ptMap.get(cid));
                                }
                            }
                            vo.setCuList(cList);
                        }
                    }else {//除监理类标段外 其他标
                        vo.setCuList(ptMap.get(vo.getId()));
                        ids = jlsMap.get(vo.getId());
                        if (!ObjectUtils.isEmpty(ids)){
                            cList = new ArrayList<>();
                            for(Integer cid : ids){
                                if (!ObjectUtils.isEmpty(ptMap.get(cid))){
                                    cList.addAll(ptMap.get(cid));
                                }
                            }
                            vo.setCcuList(cList);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<SectionVo> querySectionList(List<Integer> ids) {
        return this.mapper.selectSectionByIds(ids);
    }

    @Override
    public  List<GeneralVo> querySectioinGeneralVoListByProjectId(Integer projectId, Integer pageSize, Integer currentPageNum, String key) {
        List<GeneralVo> list = null;
        List<SectionVo> sectionVos = this.mapper.selectSectionByProjectId(projectId, key);
        if (!ObjectUtils.isEmpty(sectionVos)){
            list = new ArrayList<>();
            GeneralVo gvo;
            for(SectionVo vo : sectionVos){
                gvo = new GeneralVo(vo.getId(),vo.getCode(),vo.getName());
                list.add(gvo);
            }
            PageInfo<GeneralVo> pageInfo = PageUtil.getPageInfo(list,pageSize,currentPageNum);
            list = pageInfo.getList();
        }
        return list;
    }

    @Override
    public  List<SectionVo> querySectioinListByProjectId(Integer projectId) {
        List<SectionVo> list = this.mapper.selectSectionByProjectId(projectId, null);
        //补充标段的监理单位信息和施工单位信息
        this.initSectionOrgInfo(list,projectId);
        return list;
    }

    @Override
    public Integer deleteSzxmProjectTeam(List<Integer> ids) {
        Integer result = null;
        try {
            for (Integer id:ids) {
                result = szxmCommonService.deletePorjInfoMsg(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    public Integer addSzxmProjectTeam(SysOrgPo org) {
        ProjectTeamVo vo = this.initProjectTeamVo(org);
        Integer result = null;
        try {
            if (!ObjectUtils.isEmpty(vo)){
                //只有组织且不是建设单位才需要同步
                result = szxmCommonService.addPorjInfoMsg(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    public Integer updateSzxmProjectTeam(SysOrgPo org) {
        ProjectTeamVo vo = this.initProjectTeamVo(org);
        Integer result = null;
        try {
            if (!ObjectUtils.isEmpty(vo)){
                //只有组织且不是建设单位才需要同步
                result = szxmCommonService.updatePorjInfoMsg(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    public List<ProjectTeamUserVo> queryProjectTeamUser(Integer projectId, Integer userId){
        List<ProjectTeamUserVo> projectTeamUserList = new ArrayList<>();
        List<SysOrgUserVo> oulist = mapper.selectSysOrgUserList(projectId,userId);//用户参该项目的管理团队信息
        if (!ObjectUtils.isEmpty(oulist)){
            oulist.forEach( uvo -> {
                List<ProjectTeamUserVo> volist = this.projectTeamService.queryUserListByTeamId(uvo.getOrgId());
                volist.forEach(vo ->{
                    if (userId.equals(vo.getUser().getId())){
                        projectTeamUserList.add(vo);
                    }
                });
            });
        }
        return projectTeamUserList;
    }

    @Override
    public List<ProjectTeamUserVo> queryOwnerList(Integer projectId, List<Integer> orgIds, Integer userId) {
        List<ProjectTeamUserVo> ownerList = new ArrayList<>();
        List<ProjectTeamUserVo> projectTeamUserList = this.queryOvnerUserList(projectId);//所有业主用户
        if (!ObjectUtils.isEmpty(projectTeamUserList)){
            List<SysOrgUserVo> oulist = mapper.selectSysOrgUserList(projectId,userId);//用户参该项目的管理团队信息
            if (!ObjectUtils.isEmpty(oulist)){
                List<String> sectoinProfessionals = new ArrayList<>();//所属标段的专业
                oulist.forEach( vo -> {
                    this.initUserSectionProfessionals(sectoinProfessionals, vo.getOrgId());
                });
                //根据orgIDS和用户管理的标段专业信息过滤出相关专业的业主代表
                if (!ObjectUtils.isEmpty(oulist)){
                    projectTeamUserList.forEach(vo ->{
                        if (orgIds.contains(vo.getTeamId()) && !ObjectUtils.isEmpty(vo.getProfessional())){
                            String[] professionalArr = vo.getProfessional().split(",");
                            for(int i=0;i < professionalArr.length;i++){
                                if (sectoinProfessionals.contains(professionalArr[i])){
                                    ownerList.add(vo);
                                    break;
                                }
                            }
                        }
                    });
                }
            }
        }
        return ownerList;
    }

    @Override
    public boolean checkOwner(Integer projectId, Integer userId) {
        //找到项目的业主单位
        List<ProjectTeamUserVo> projectTeamUserList = this.queryOvnerUserList(projectId);
        if (!ObjectUtils.isEmpty(projectTeamUserList)){
            for (ProjectTeamUserVo vo : projectTeamUserList){
                if (vo.getUser().getId().equals(userId))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<com.wisdom.acm.sys.vo.ProjectTeamVo> queryProjectTeamTree(Integer projectId)
    {
        List<com.wisdom.acm.sys.vo.ProjectTeamVo> list = new ArrayList<>();
        ProjectInfoVo proj = commPlanService.getProjectInfo(projectId);
        if (!ObjectUtils.isEmpty(proj)){
            com.wisdom.acm.sys.vo.ProjectTeamVo pt = new  com.wisdom.acm.sys.vo.ProjectTeamVo();
            list.add(pt);
            pt.setId(proj.getId());
            pt.setBizId(proj.getId());
            pt.setBizType("project");
            pt.setTeamCode(proj.getCode());
            pt.setTeamName(proj.getName());
            List<com.wisdom.acm.sys.vo.ProjectTeamVo> teams = this.projectTeamService.queryProjectTeamTree("project", projectId);
            pt.setChildren(teams);
        }
        return list;
    }

    @Override
    public SysOrgUserVo getSysOrgUserVo(Integer orgId, Integer userId) {
        return mapper.selectSysOrgUserVo(orgId, userId);
    }

    @Override
    public List<ProjectTeamUserVo> getOwnerBySectionId(Integer id) {
        List<ProjectTeamUserVo> ownerRList = null;
        SectionVo vo = this.mapper.selectSectionById(id);
        if (!ObjectUtils.isEmpty(vo)){
            //补充业主代表信息(首先要确保标段有专业)
            if (!ObjectUtils.isEmpty(vo.getProfessional())){
                //找到项目相关的业主
                List<ProjectTeamUserVo> projectTeamUserList = this.mapper.selectProjectTreamUserList(vo.getProjectId(),SysEnum.DEVELOPMENT_ORGANIZATION.getCode(), null);
                //根据标段主业过滤出业主代表
                if(!ObjectUtils.isEmpty(projectTeamUserList)){
                    ownerRList = new ArrayList<>();
                    String professional = vo.getProfessional();
                    String[] professionalArr = professional.split(",");
                    for (ProjectTeamUserVo userVo:projectTeamUserList) {
                        String userProfessional = ","+userVo.getProfessional()+",";
                        for(int i=0;i < professionalArr.length;i++){
                            String p = ","+professionalArr[i]+",";
                            if (userProfessional.contains(p)){
                                ownerRList.add(userVo);
                                break;
                            }
                        }
                    }
                    //补充用户角色信息
                    if (!ObjectUtils.isEmpty(ownerRList)){
                        List<Integer> teamIds = Lists.newArrayList(ownerRList.stream().map(projectTeamUserVo -> projectTeamUserVo.getTeamId()).collect(Collectors.toSet()));
                        List<SysUserOrgRolePo> userOrgRoles = Optional.ofNullable(userOrgRoleService.queryListByOrgIds(Lists.newArrayList(teamIds))).orElse(Lists.newArrayList());
                        ImmutableListMultimap<String, SysUserOrgRolePo> orgIdUserId2Po = Multimaps.index(userOrgRoles, userOrgRolePo -> userOrgRolePo.getOrgId() + "_" + userOrgRolePo.getUserId());
                        List<Integer> roleIds = Lists.newArrayList(userOrgRoles.stream().map(orgUserRole -> orgUserRole.getRoleId()).collect(Collectors.toSet()));
                        List<SysRoleVo> roles = !ObjectUtils.isEmpty(roleIds) ? roleService.queryRolesByIds(roleIds) : Lists.newArrayList();
                        ImmutableMap<Integer, SysRoleVo> roleId2Role = Maps.uniqueIndex(roles, role -> role.getId());

                        ownerRList.forEach(userVo->{
                            ImmutableList<SysUserOrgRolePo> userOrgRolePos = orgIdUserId2Po.get(userVo.getTeamId() + "_" + userVo.getUser().getId());
                            List<GeneralVo> roleVos = Optional.ofNullable(userOrgRolePos).orElse(ImmutableList.of()).stream().map(userOrgRolePo -> {
                                SysRoleVo role = Optional.ofNullable(roleId2Role.get(userOrgRolePo.getRoleId())).orElse(new SysRoleVo());
                                return new GeneralVo(role.getId(), role.getRoleCode(), role.getRoleName());
                            }).filter(role -> role.getId() != null).collect(Collectors.toList());
                            userVo.setRoles(roleVos);
                        });
                    }
                }
            }
        }
        return ownerRList;
    }

    /**
     * 获取所有有权限的标段
     * 改为：如果不是业主，则原来权限不变，施工单位能看到本标段，监理单位能看到所管辖的标段。
     * 如果是业主，首先1，根据数据字典：【项目下所有标段权限(分配角色)】 permission.allbidding  判断是否包含当前用户角色，包含就能看到所有标段
     * 其次2，否则就查看该标段的 “业主成员”(其实就是团队成员) 是否包含当前用户，如果包含，就看的到此标段。
     * @param projectId
     * @param userId
     * @return
     */
    private List<SectionTreeVo> initSectionList(Map<String, Object> mapWhere,Integer projectId, Integer userId){
        mapWhere.put("projectId",projectId);
        //查询项目团队的用户
        List<GeneralVo> roles =this.mapper.queryTeamRoles(projectId,userId);
        List<String> roleCodes=ListUtil.toValueList(roles, "code", String.class, true);
        List<SectionTreeVo> sectionTreeVos = Lists.newArrayList();
        boolean flag = true;//判断当前用户是否是业主
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
            flag=false;
        if(flag)
        {//业主
                Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode("permission.allbidding");
                for (Map.Entry<String, DictionaryVo> entry : dictMap.entrySet())
                {
                    if(ListUtil.toStr(roleCodes).indexOf(entry.getKey())>=0)
                    {//匹配到，代表能看到所有标段
                        List<SectionVo> allSectionVos = this.mapper.selectSections(mapWhere);//查询所有项目标段
                        Collections.sort(allSectionVos);
                       for(SectionVo sectionVo:allSectionVos)
                       {
                           SectionTreeVo sectionTreeVo=dozerMapper.map(sectionVo,SectionTreeVo.class);
                           sectionTreeVos.add(sectionTreeVo);
                       }
                       return sectionTreeVos;
                    }
                }
                //匹配不到的话：查看该标段的 “业主成员"(其实就是团队成员) 是否包含当前用户，如果包含，就看的到此标段。
                //查询这个用户分配的标段IDS
                 List<Integer> sectionIds=this.mapper.selectSectUserOrgIdsByUId(projectId,userId);
                if(!ObjectUtils.isEmpty(sectionIds))
                {
                    mapWhere.put("ids",sectionIds);
                    List<SectionVo> allSectionVos = this.mapper.selectSections(mapWhere);//查询所有项目标段
                    Collections.sort(allSectionVos);
                    for(SectionVo sectionVo:allSectionVos)
                    {
                        SectionTreeVo sectionTreeVo=dozerMapper.map(sectionVo,SectionTreeVo.class);
                        sectionTreeVos.add(sectionTreeVo);
                    }
                }
                return sectionTreeVos;
            }
            else { //非业主
                sectionTreeVos = this.mapper.selectSectionByUserIdAndProjectId(userId, projectId);//有权限能直接看到的标段
                List<SectionTreeVo> sectionTreeVoList=Lists.newArrayList();//监理单位SectionTreeVoList
                for(SectionTreeVo sectionTreeVo:sectionTreeVos)
                {
                        if(SysEnum.SUPERVISOR.getCode().equals(sectionTreeVo.getTypeCode()))
                            sectionTreeVoList.add(sectionTreeVo);
                }
                //查询监理单位关联的所有施工单位
                if(!ObjectUtils.isEmpty(sectionTreeVoList))
                {
                    List<String> sectionIds=ListUtil.toValueList(sectionTreeVos, "value", String.class, true);
                    List<SectionTreeVo> sgSectionTreeVoList=this.mapper.selectSgSectionByJls(sectionTreeVoList);
                    for(SectionTreeVo sectionTreeVo:sgSectionTreeVoList)
                    {
                        if(!sectionIds.contains(sectionTreeVo.getValue()))//去重
                            sectionTreeVos.add(sectionTreeVo);
                    }
                }
               //对sectionTreeVos按照标段号进行排序
                Collections.sort(sectionTreeVos, new Comparator<SectionTreeVo>() {
                    @Override
                    public int compare(SectionTreeVo o1, SectionTreeVo o2) {
                        String code1Array[]=o1.getCode().split("-");
                        String code2Array[]=o2.getCode().split("-");
                        if(Integer.valueOf(code1Array[1])>Integer.valueOf(code2Array[1]))
                           return 1;
                        else if(Integer.valueOf(code1Array[1])<Integer.valueOf(code2Array[1]))
                            return -1;
                        else if(Integer.valueOf(code1Array[2])>Integer.valueOf(code2Array[2]))
                            return 1;
                        else if(Integer.valueOf(code1Array[2])<Integer.valueOf(code2Array[2]))
                            return -1;
                        else return 0;
                    }
                });
            }
        return sectionTreeVos;
    }

    @Override
    public List<SectionTreeVo> querySectionTreeListByProjectId(Map<String, Object> mapWhere,Integer projectId, Integer userId){
        List<SectionTreeVo> sectionTreeVos = this.initSectionList(mapWhere,projectId,userId);
        for (SectionTreeVo vo:sectionTreeVos) {
            vo.setValue(vo.getId()+"");
            vo.setTitle(vo.getCode()+"-"+vo.getName());
        }
//        if (!ObjectUtils.isEmpty(sectionTreeVos)){
//            Map<Integer,Boolean> idMap = new HashMap<>();//用户判断父节点是否在结果集内 端链处理
//            sectionTreeVos.forEach(vo->{idMap.put(vo.getId(),true);});
//            for (SectionTreeVo vo:sectionTreeVos) {
//                vo.setValue(vo.getId()+"");
//                vo.setTitle(vo.getCode()+"-"+vo.getName());
//                if (idMap.get(vo.getParentId()) == null){
//                    vo.setParentId(projectId);
//                }
//            }
//            //return TreeUtil.bulid(sectionTreeVos,projectId);
//            return sectionTreeVos;
//        }
        return sectionTreeVos;
//        return Lists.newArrayList();
    }

    @Override
    public List<SectionTreeVo> queryProjectSectionTreeListByProjectId(Integer projectId) {
        UserInfo userInfo = userService.getLoginUser();
        Integer userId = userInfo.getId();
        //获取用户权限内的标段（树形）信息
        Map<String,Object> mapWhere=Maps.newHashMap();
        List<SectionTreeVo> childrenList = this.querySectionTreeListByProjectId(mapWhere,projectId, userId);
        //获取项目信息且作为树形到顶层
        PlanProjectVo projectVo = commPlanProjectService.getProject(projectId);
        SectionTreeVo vo = new SectionTreeVo();
        List<SectionTreeVo> vos = new ArrayList<>();
        vos.add(vo);
        vo.setTitle(projectVo.getName());
        vo.setValue(projectId+"");
        vo.setId(projectId);
        vo.setCode(projectVo.getCode());
        vo.setName(projectVo.getName());
        if (!ObjectUtils.isEmpty(childrenList))
            vo.setChildren(childrenList);
        return vos;
    }

    @Override
    public List<SectionTreeVo> querySectionTreeListByProjectId(Map<String, Object> mapWhere,Integer projectId) {
        UserInfo userInfo = userService.getLoginUser();
        Integer userId = userInfo.getId();
        return this.querySectionTreeListByProjectId(mapWhere,projectId, userId);
    }

    /**
     * 求当前登陆人标段list集合 ==>获得id
     * @param projectId
     * @return
     */
    @Override
    public List<Integer> querySectionVosByProjectId(Integer projectId) {
        UserInfo userInfo = userService.getLoginUser();
        Integer userId = userInfo.getId();
        Map<String,Object> mapWhere=Maps.newHashMap();
        List<SectionTreeVo> sectionTreeVos = this.initSectionList(mapWhere,projectId,userId);
        /*List<ProjectTeamUserVo> projectTeamUserList = this.mapper.selectProjectTreamUserList(projectId,SysEnum.DEVELOPMENT_ORGANIZATION.getCode(), userId);
        boolean flag = !ObjectUtils.isEmpty(projectTeamUserList);//判断当前用户是否是业主
        List<String> userProfessionalList = new ArrayList<>();//记录业主用户的专业
        List<SectionVo> sectionVos = this.mapper.selectSectionByProjectId(projectId, null);//项目标段
        if (!ObjectUtils.isEmpty(sectionVos)){
            if(flag){//业主
                for (ProjectTeamUserVo vo: projectTeamUserList) {
                    //项目内任何业主单位只要业主没有主业则就清空 记录业主用户的专业
                    if (ObjectUtils.isEmpty(vo.getProfessional())){
                        userProfessionalList.clear();
                        break;
                    }
                    String[] professionals = vo.getProfessional().split(",");
                    for(String p:professionals){
                        userProfessionalList.add(p);
                    }
                }
                userProfessionalList = ListUtil.distinct(userProfessionalList);
                //当业主有专业时则需要根据专业过滤
                if (!ObjectUtils.isEmpty(sectionVos)){
                    sectionTreeVos = new ArrayList<>();
                    if(!ObjectUtils.isEmpty(userProfessionalList)){
                        for(SectionVo vo: sectionVos){
                            String p = vo.getProfessional();
                            if (!ObjectUtils.isEmpty(p)){
                                p = ","+p+",";
                                for(String professional:userProfessionalList){
                                    professional = ","+professional+",";
                                    if (p.contains(professional)){
                                        sectionTreeVos.add(dozerMapper.map(vo,SectionTreeVo.class));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        for(SectionVo vo: sectionVos){
                            sectionTreeVos.add(dozerMapper.map(vo,SectionTreeVo.class));
                        }
                    }
                }
            }else{ //非业主
                sectionTreeVos = this.mapper.selectSectionByUserIdAndProjectId(userId, projectId);//有权限能直接看到的标段
                List<SectionTreeVo> sectionList = new ArrayList<>();//标段父子合集
                Map<Integer,Boolean> idMap = new HashMap<>();//用户去除重复数据
                this.loadSunSectionData(sectionTreeVos,sectionList,idMap);//追加子层标段
                sectionTreeVos = sectionList;
            }
        }*/
        return ListUtil.toIdList(sectionTreeVos);
//        if (!ObjectUtils.isEmpty(sectionTreeVos)){
//            Map<Integer,Boolean> idMap = new HashMap<>();//用户判断父节点是否在结果集内
//            sectionTreeVos.forEach(vo->{idMap.put(vo.getId(),true);});
//            for (SectionTreeVo vo:sectionTreeVos) {
//                vo.setValue(vo.getId()+"");
//                vo.setTitle(vo.getCode()+"-"+vo.getName());
//                if (idMap.get(vo.getParentId()) == null){
//                    vo.setParentId(projectId);
//                }
//            }
//            return ListUtil.toIdList(sectionTreeVos);
//        }
    }


    @Override
    public SectionVo getSectionById(Integer id) {
        SectionVo vo = this.mapper.selectSectionById(id);
        //补充标段的监理单位信息和施工单位信息
        if (vo != null){
            List<SectionVo> vos = this.mapper.selectSectionByProjectId(vo.getProjectId(), null);
            this.initSectionOrgInfo(vos,vo.getProjectId());
            if (!ObjectUtils.isEmpty(vos)){
                for(SectionVo v:vos){
                    if (vo.getId().equals(v.getId())){
                        vo = v;
                        break;
                    }
                }
            }
        }
        return vo;
    }

    @Override
    public List<SectionTreeVo> queryCcuListByCuId(Integer cuId) {
        List<SectionTreeVo> list = null;
        //根据施工标找到监理标,根据监理标找到监理单位
        SectionVo vo = this.mapper.selectSectionById(cuId);//施工标
        if (!ObjectUtils.isEmpty(vo) && !ObjectUtils.isEmpty(vo.getParentId())){
            List<Integer> ids = new ArrayList<>();
            ids.add(vo.getParentId());//填入监理标ID
            //根据监理标查询出监理单位
            list  = this.mapper.selectSubSectionByParentIds(ids,"org");
        }
        return list;
    }

    @Override
    public List<SectionTreeVo> queryCuListByCcuId(Integer ccuId) {
        List<SectionTreeVo> list = null;
        if (!ObjectUtils.isEmpty(ccuId)){
            //根据找到的监理标，查询出监理标下的施工标
            List<Integer> ids = new ArrayList<>();
            ids.add(ccuId);
            List<SectionTreeVo> sectionList = this.mapper.selectSubSectionByParentIds(ids,"section");
            //根据施工标找到施工标下的施工单位
            if (!ObjectUtils.isEmpty(sectionList)){
                ids.clear();
                for (SectionTreeVo svo :sectionList) {
                    ids.add(svo.getId());
                }
                list  = this.mapper.selectSubSectionByParentIds(ids,"org");
            }
        }
        return list;
    }

    @Override public List<GeneralVo> queryTeamRoles(Integer projectId, Integer id)
    {
        return mapper.queryTeamRoles(projectId,id);
    }

    /**
     * 创建项目团队VO
     * @param org
     * @return
     */
    private ProjectTeamVo initProjectTeamVo(SysOrgPo org){
        //只有是非建设单位的单位才推送
        if (SysEnum.ORG.getCode().equals(org.getExtendedColumn1()) && !SysEnum.DEVELOPMENT_ORGANIZATION.getCode().equals(org.getExtendedColumn3())){
            ProjectTeamVo vo = new ProjectTeamVo();
            vo.setProjectId(org.getBizId());
            vo.setParentId(org.getParentId());
            vo.setId(org.getId());
            vo.setCode(org.getOrgCode());
            vo.setName(org.getOrgName());
            vo.setType(org.getExtendedColumn1());
            vo.setTypeCoe(org.getExtendedColumn3());
            vo.setOrgClassification(org.getExtendedColumn4());
            return vo;
        }
        return null;
    }

    /**
     * 加载子集标段数据
     * @param list
     */
//    private void loadSunSectionData(List<SectionTreeVo> list, List<SectionTreeVo> sectionList, List<SectionVo> allSectionVos, Map<Integer,Boolean> idMap){
//        if (!ObjectUtils.isEmpty(list)){
//            List<Integer> ids = new ArrayList<>();
//            for (SectionTreeVo vo: list) {
//                if(idMap.get(vo.getId()) == null){
//                    idMap.put(vo.getId(),true);
//                    sectionList.add(vo);
//                }
//                ids.add(vo.getId());
//            }
//            if (!ObjectUtils.isEmpty(ids)){
//                List<SectionTreeVo> subList =Lists.newArrayList();
//                for(Integer id:ids)
//                {
//                    for(SectionVo sectionVo:allSectionVos)
//                    {
//                        if(sectionVo.getParentId().equals(id))
//                        {
//                            subList.add(dozerMapper.map(sectionVo,SectionTreeVo.class));
//                        }
//                    }
//                }
//                this.loadSunSectionData(subList, sectionList,allSectionVos, idMap);
//            }
//        }
//    }

    /**
     * 获取业主单位
     * @param projectId 项目ID
     * @return
     */
    private List<SectionTreeVo> queryOwnerOrgListByProjectId(Integer projectId){
        List<SectionTreeVo> list = this.mapper.selectOwnerOrgListByProjectId(projectId);
        if (!ObjectUtils.isEmpty(list)){
            List<Integer> parentIds = new ArrayList<>();
            for (SectionTreeVo vo:list) {
                parentIds.add(vo.getId());
            }
            this.loadSubOwnerOrg(list,parentIds);
        }
        return list;
    }

    /**
     * 加载补充业主单位信息
     * @param list
     * @param parentIds
     */
    private void loadSubOwnerOrg(List<SectionTreeVo> list, List<Integer> parentIds){
        List<SectionTreeVo> subList = this.mapper.selectSubSectionByParentIds(parentIds,"org");
        if (!ObjectUtils.isEmpty(subList)){
            list.addAll(subList);
            parentIds.clear();
            for (SectionTreeVo vo:subList) {
                parentIds.add(vo.getId());
            }
            this.loadSubOwnerOrg(list,parentIds);
        }
    }

    /**
     * 根据项目ID找到业主
     * @param projectId 项目ID
     * @return 业主集合
     */
    private List<ProjectTeamUserVo> queryOvnerUserList(Integer projectId){
        List<ProjectTeamUserVo> projectTeamUserList = new ArrayList<>();
        //找到业主单位
        List<SectionTreeVo> ownerOrgList = this.queryOwnerOrgListByProjectId(projectId);
        //根据业主单位找到业主
        if(!ObjectUtils.isEmpty(ownerOrgList)){
            List<ProjectTeamUserVo> userList;
            for (SectionTreeVo team:ownerOrgList) {
                userList = this.projectTeamService.queryUserListByTeamId(team.getId());
                if(!ObjectUtils.isEmpty(userList))
                    projectTeamUserList.addAll(userList);
            }
        }
        return projectTeamUserList;
    }

    /**
     * 根据用户ID和团队ID初始完善项目团队成员信息
     * @param userOrgPo
     * @return
     */
    private ProjectTeamUserVo initProjectTeamUserVo(SysUserOrgPo userOrgPo){
        SysUserVo user = this.sysUserService.queryUserById(userOrgPo.getUserId());
        SysOrgUserVo orgUserVo = this.getSysOrgUserVo(userOrgPo.getOrgId(),userOrgPo.getUserId());
        List<GeneralVo> roleVos = new ArrayList<>();
        List<SysRoleVo> roles = userOrgRoleService.queryRoleListByOrgIdAndUserId(orgUserVo.getOrgId(), orgUserVo.getUserId());
        roles.forEach(role ->{
            roleVos.add(new GeneralVo(role.getId(), role.getRoleCode(), role.getRoleName()));
        });
        ProjectTeamUserVo vo = new ProjectTeamUserVo()
                .setId(orgUserVo.getUserOrgId())
                .setPosition(orgUserVo.getPosition())
                .setProfessional(orgUserVo.getProfessional())
                .setTeamId(userOrgPo.getOrgId())
                .setUser(new UserVo(user.getId(), user.getUserName(), user.getActuName()))
                .setPhone(user.getPhone())
                .setSex(user.getSex())
                .setBirth(user.getBirth())
                .setCardNum(user.getCardNum())
                .setCardType(user.getCardType())
                .setEmail(user.getEmail())
                .setLevel(user.getLevel())
                .setStatus(user.getStatus())
                .setStaffStatus(user.getStaffStatus())
                .setRoles(roleVos);
        Date birth = vo.getBirth();
        if (!ObjectUtils.isEmpty(birth)){
            Calendar  from  =  Calendar.getInstance();
            from.setTime(birth);
            Calendar  to  =  Calendar.getInstance();
            to.setTime(new Date());
            int fromYear = from.get(Calendar.YEAR);
            int toYear = to.get(Calendar.YEAR);
            vo.setAge(toYear  -  fromYear);
        }
        return vo;
    }

    /**
     * 加载用户相关标段的专业集合
     * @param sectoinProfessionals 标段专业集合
     * @param orgId
     */
    private void initUserSectionProfessionals(List<String> sectoinProfessionals, Integer orgId){
        SectionVo svo = mapper.selectSectionById(orgId);
        if (!ObjectUtils.isEmpty(svo)){
            if ("section".equals(svo.getType())){
                if (!ObjectUtils.isEmpty(svo) && !ObjectUtils.isEmpty(svo.getProfessional())){
                    String professional = svo.getProfessional();
                    String [] parr = professional.split(",");
                    sectoinProfessionals.addAll(Arrays.asList(parr));
                }
            }
            else if (!ObjectUtils.isEmpty(svo.getParentId())){
                this.initUserSectionProfessionals(sectoinProfessionals, svo.getParentId());
            }
        }
    }

    @Override
    public List<Integer> querySectionIdsParams(int projectId, int orgId) {
        return  mapper.querySectionIdsParams(projectId, orgId);
    }
}
