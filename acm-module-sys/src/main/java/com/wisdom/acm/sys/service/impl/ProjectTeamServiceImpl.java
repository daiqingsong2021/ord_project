package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.*;
import com.wisdom.acm.sys.enums.SysEnum;
import com.wisdom.acm.sys.form.JlSectionAddForm;
import com.wisdom.acm.sys.form.ProjectTeamAddForm;
import com.wisdom.acm.sys.form.ProjectTeamUpdateForm;
import com.wisdom.acm.sys.mapper.OrgMapper;
import com.wisdom.acm.sys.po.*;
import com.wisdom.acm.sys.service.*;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.util.SortUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectTeamServiceImpl extends BaseService<OrgMapper, SysOrgPo> implements ProjectTeamService {

    @Autowired
    private SysUserOrgService userOrgService;

    @Autowired
    private SysUserOrgRoleService userOrgRoleService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysOrgService orgService;

    @Autowired
    private SysIptService iptService;

    @Autowired
    private SysUserIptService userIptService;

    @Autowired
    private SysUserIptRoleService userIptRoleService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private LeafService leafService;
    @Autowired
    private CommUserService commUserService;

    @Override
    public List<ProjectTeamVo> queryProjectTeam(int projectId){
        return this.mapper.selectProjectTeam(projectId, SysEnum.ORG.getCode());
    }

    @Override
    public List<Integer> deleteByBiz(String bizType, Integer bizId) {
        List<Integer> orgIds = null;

        List<ProjectTeamVo> list = this.mapper.selectProjectTeamList(bizType, bizId);

        if (!ObjectUtils.isEmpty(list)){
            //递归获取项目团队以及子项目团队的id
            List<Integer> idList = this.resolveProjectTeamId(list);

            orgIds = ListUtil.toIdList(list);
            //删除项目团队
            this.delete(idList);

        }
        return orgIds;
    }

    /**
     * 递归获取项目团队以及子项目团队的id
     * @param list
     * @return
     */
    private List<Integer> resolveProjectTeamId(List<ProjectTeamVo> list){
        List<Integer> idList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(list)){
            for (ProjectTeamVo projectTeamVo : list) {
                Integer id  = projectTeamVo.getId();
                idList.add(id);
                List<ProjectTeamVo> childList = projectTeamVo.getChildren();
                if(!ObjectUtils.isEmpty(childList)){
                   List<Integer> childIdList =  this.resolveProjectTeamId(childList);
                    idList.addAll(childIdList);
                }
            }
        }
        return idList;
    }


    @Override
    public int add(ProjectTeamAddForm form) {
        //验证代码是否重复
        Map<String, Object> btMap = new HashMap<>();
        btMap.put("bizId", form.getBizId());
        btMap.put("bizType", form.getBizType());
        List<SysOrgPo> list = this.getTeamPoByCode(form.getTeamCode(), btMap);
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("代码不能重复!");
        }
        SysOrgPo org = new SysOrgPo();
        org.setOrgCode(form.getTeamCode());
        org.setOrgName(form.getTeamName());
        org.setParentId(form.getParentId());
        org.setBizType(form.getBizType());
        org.setBizId(form.getBizId());
        org.setExtendedColumn1(form.getExtendedColumn1());
        org.setExtendedColumn2(form.getExtendedColumn2());
        org.setExtendedColumn3(form.getExtendedColumn3());
        org.setExtendedColumn4(form.getExtendedColumn4());
        org.setExtendedColumn5(form.getExtendedColumn5());
        org.setSectionStatus(form.getSectionStatus());
        org.setStartDate(form.getStartDate());
        org.setEndDate(form.getEndDate());
        org.setOpenPgd(form.getOpenPgd());
        org.setOpenExam(form.getOpenExam());
        org.setPgdStartDate(form.getPgdStartDate());
        org.setPgdEndDate(form.getPgdEndDate());
        org.setExamStartDate(form.getExamStartDate());
        org.setExamEndDate(form.getExamEndDate());
        //默认有效
        org.setStatus(1);
        org.setSort(mapper.selectProjectTeamNextSort(form.getBizType(), form.getBizId(), form.getParentId()));

        super.insert(org);

        this.sectionService.addSzxmProjectTeam(org);

        return org.getId();
    }

    @Override
    @AddLog(title = "修改项目团队", module = LoggerModuleEnum.IM_PROJTEAM)
    public int update(ProjectTeamUpdateForm form) {
        SysOrgPo org = this.mapper.selectByPrimaryKey(form.getId());
        if (ObjectUtils.isEmpty(org)) {
            throw new BaseException("修改的项目团队不存在!");
        }

        // 添加修改日志
        String logger = this.queryUpdateLogger(form,org);
        this.setAcmLogger(new AcmLogger("修改项目团队," + logger));
        //验证代码是否重复
        Map<String, Object> btMap = new HashMap<>();
        btMap.put("bizId", org.getBizId());
        btMap.put("bizType", org.getBizType());
        List<SysOrgPo> list = this.getTeamPoByCode(org.getOrgCode(), btMap);
        if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(org.getId())) {
            throw new BaseException("代码不能重复!");
        }
        org.setOrgCode(form.getTeamCode());
        org.setOrgName(form.getTeamName());
        if (SysEnum.ORG.getCode().equals(form.getExtendedColumn1())){
            form.setExtendedColumn2(null);
            form.setExtendedColumn5(null);
        }
        else{
            form.setExtendedColumn3(null);
            form.setExtendedColumn4(null);
        }
        boolean flag = false;
        if ("section".equals(form.getExtendedColumn1()) && !(org.getExtendedColumn1()+"").equals(form.getExtendedColumn1())){
            flag = true;
        }
        org.setExtendedColumn1(form.getExtendedColumn1());
        org.setExtendedColumn2(form.getExtendedColumn2());
        org.setExtendedColumn3(form.getExtendedColumn3());
        org.setExtendedColumn4(form.getExtendedColumn4());
        org.setExtendedColumn5(form.getExtendedColumn5());
        org.setSectionStatus(form.getSectionStatus());
        org.setStartDate(form.getStartDate());
        org.setEndDate(form.getEndDate());
        org.setOpenPgd(form.getOpenPgd());
        org.setOpenExam(form.getOpenExam());
        org.setPgdStartDate(form.getPgdStartDate());
        org.setPgdEndDate(form.getPgdEndDate());
        org.setExamStartDate(form.getExamStartDate());
        org.setExamEndDate(form.getExamEndDate());
        super.updateById(org);

        if (flag){
            List<Integer> ids = new ArrayList<>();
            ids.add(org.getId());
            this.sectionService.deleteSzxmProjectTeam(ids);
        }
        else{
            this.sectionService.updateSzxmProjectTeam(org);
        }
        //如果是标段的话需要往关联关系表中塞数据
        if(StringUtils.equals(form.getExtendedColumn1(),"section") && StringUtils.isNotBlank(form.getOrgCode())){
            OrgSectionRelation relation = new OrgSectionRelation();
            relation.setProjectId(org.getBizId());
            relation.setOrgId(form.getOrgId());
            relation.setOrgCode(form.getOrgCode());
            relation.setOrgName(form.getOrgName());
            relation.setSectionId(form.getId());
            relation.setSectionCode(form.getTeamCode());
            relation.setSectionName(form.getTeamName());
            relation.setUpdateTime(new Date());
            relation.setUpdateUser(String.valueOf(commUserService.getLoginUser().getId()));
            this.updateOrgSectionRelation(form.getId(),relation);
        }
        return org.getId();
    }

    private String queryUpdateLogger(ProjectTeamUpdateForm form, SysOrgPo org) {
        StringBuffer ret = new StringBuffer();
        if (!form.getTeamCode().equals(org.getOrgCode())){
            ret.append("代码").append("由【").append(org.getOrgCode()).append("】修改为【").append(form.getTeamCode()).append("】");
        }
        if (!form.getTeamName().equals(org.getOrgName())){
            ret.append("代码").append("由【").append(org.getOrgName()).append("】修改为【").append(form.getTeamName()).append("】");
        }
        return ret.toString();
    }

    public List<SysOrgPo> getTeamPoByCode(String code, Map<String, Object> btMap) {
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgCode", code);
        criteria.andEqualTo("bizId", btMap.get("bizId"));
        criteria.andEqualTo("bizType", btMap.get("bizType"));
        List<SysOrgPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list) ? null : list;
    }

    /**
     * 删除项目团队
     * @param ids
     */
    @Override
    public void delete(List<Integer> ids) {
        List<SysOrgPo> list = this.selectListAll();
        List<SysOrgPo> allList = this.queryChildrenPos(list,ids);
        ids = ListUtil.toIdList(allList);
        this.sectionService.deleteSzxmProjectTeam(ids);//删除苏轨院项目数据
        ids.forEach(id -> {
            // ProjectTeamVo team = this.mapper.selectProjectTeamById(id);
            List<SysOrgUserVo> userOrgPos = this.userOrgService.queryListByOrgId(id);
            List<Integer> userOrgIds = !ObjectUtils.isEmpty(userOrgPos) ? userOrgPos.stream().map(userOrgPo -> userOrgPo.getUserOrgId()).collect(Collectors.toList()) : Lists.newArrayList();
            this.deleteUsers(userOrgIds);
        });
        this.deleteByIds(ids);
        //this.deleteChildrenAndMe(ids);
        //如果是标段数据，删除的是关系表数据
        mapper.deleteOrgSectionRelationBySectionId(ids.get(0));
    }

    @Override
    public ProjectTeamVo get(Integer id) {
        return this.mapper.selectProjectTeamById(id);
    }

    @Override
    public List<ProjectTeamVo> queryProjectTeamList(String bizType, Integer bizId){
        List<ProjectTeamVo> list = this.mapper.selectProjectTeamList(bizType, bizId);
        return list;
    }


    @Override
    public List<ProjectTeamVo> queryProjectTeamTree(String bizType, Integer bizId) {
        List<ProjectTeamVo> list = this.mapper.selectProjectTeamList(bizType, bizId);
        Collections.sort(list, new Comparator<ProjectTeamVo>() {
            @Override
            public int compare(ProjectTeamVo o1, ProjectTeamVo o2) {
                if("section".equals(o1.getExtendedColumn1()) &&"section".equals(o2.getExtendedColumn1()))
                {
                    String code1Array[]=o1.getTeamCode().split("-");
                    String code2Array[]=o2.getTeamCode().split("-");
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
                else return 0;//不比较
            }
        });
        List<ProjectTeamVo> tree = TreeUtil.bulid(list, 0);
        //tree = this.resloveProjectTeamTreeToSort(tree);
        return tree;
    }

    /**
     * 递归排序
     * @param tree
     * @return
     */
    private List<ProjectTeamVo> resloveProjectTeamTreeToSort(List<ProjectTeamVo> tree){
          if(!ObjectUtils.isEmpty(tree)){
              for (ProjectTeamVo projectTeamVo : tree) {
                  List<ProjectTeamVo> projectTeamVos = projectTeamVo.getChildren();
                  if(!ObjectUtils.isEmpty(projectTeamVos)){
                     List<ProjectTeamVo> childList = this.resloveProjectTeamTreeToSort(projectTeamVos);
                      projectTeamVo.setChildren(childList);
                  }
              }
              tree = SortUtil.sortObj(tree,"sortNum","asc");
          }
          return tree;
    }



    @Override
    public List<ProjectTeamUserVo> queryUserListByTeamId(Integer teamId) {
        List<ProjectTeamUserVo> list = new ArrayList<>();
        SysOrgInfoVo orgVo = this.orgService.getOrgInfo(teamId);
        List<SysOrgUserVo> orgUsers = Optional.ofNullable(userOrgService.queryListByOrgId(teamId)).orElse(Lists.newArrayList());
        List<SysUserOrgRolePo> userOrgRoles = Optional.ofNullable(userOrgRoleService.queryListByOrgIds(Lists.newArrayList(teamId))).orElse(Lists.newArrayList());
        ImmutableListMultimap<String, SysUserOrgRolePo> orgIdUserId2Po = Multimaps.index(userOrgRoles, userOrgRolePo -> userOrgRolePo.getOrgId() + "_" + userOrgRolePo.getUserId());

        List<Integer> userIds = Lists.newArrayList(orgUsers.stream().map(orgUser -> orgUser.getUserId()).collect(Collectors.toSet()));
        List<Integer> roleIds = Lists.newArrayList(userOrgRoles.stream().map(orgUserRole -> orgUserRole.getRoleId()).collect(Collectors.toSet()));
        List<SysUserVo> users = !ObjectUtils.isEmpty(userIds) ? userService.queryUsersByIds(userIds) : Lists.newArrayList();
        List<SysRoleVo> roles = !ObjectUtils.isEmpty(roleIds) ? roleService.queryRolesByIds(roleIds) : Lists.newArrayList();
        ImmutableMap<Integer, SysUserVo> userId2User = Maps.uniqueIndex(users, sysUser -> sysUser.getId());
        ImmutableMap<Integer, SysRoleVo> roleId2Role = Maps.uniqueIndex(roles, role -> role.getId());
        //
        orgUsers.forEach(orgUser -> {
            ImmutableList<SysUserOrgRolePo> userOrgRolePos = orgIdUserId2Po.get(orgUser.getOrgId() + "_" + orgUser.getUserId());
            SysUserVo user = userId2User.get(orgUser.getUserId());
            if (!ObjectUtils.isEmpty(user)) {
                List<GeneralVo> roleVos = Optional.ofNullable(userOrgRolePos).orElse(ImmutableList.of()).stream().map(userOrgRolePo -> {
                    SysRoleVo role = Optional.ofNullable(roleId2Role.get(userOrgRolePo.getRoleId())).orElse(new SysRoleVo());
                    return new GeneralVo(role.getId(), role.getRoleCode(), role.getRoleName());
                }).filter(role -> role.getId() != null).collect(Collectors.toList());
                ProjectTeamUserVo vo = new ProjectTeamUserVo()
                        .setId(orgUser.getUserOrgId())
                        .setPosition(orgUser.getPosition())
                        .setProfessional(orgUser.getProfessional())
                        .setTeamId(teamId)
                        .setTeamName(orgVo.getOrgName())
                        .setTeamCode(orgVo.getOrgCode())
                        .setUser(new UserVo(user.getId(), user.getUserName(), user.getActuName(),user.getUserCode()))
                        .setPhone(user.getPhone())
                        .setSex(user.getSex())
                        .setBirth(user.getBirth())
                        .setCardNum(user.getCardNum())
                        .setCardType(user.getCardType())
                        .setEmail(user.getEmail())
                        .setLevel(user.getLevel())
                        .setStatus(user.getStatus())
                        .setStaffStatus(user.getStaffStatus())
                        .setRoles(roleVos)
                        .setSortNum(orgUser.getSort());
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
                list.add(vo);
            }
        });
        List<ProjectTeamUserVo> resultList = new ArrayList<>();
        resultList.addAll(list);
        resultList = SortUtil.sortObj(resultList,"sortNum","asc");
        return resultList;
    }

    @Override
    public void assignUser(Integer teamId, List<Map<String, Object>> users) {
        List<Integer> userIds = users.stream().map(user -> Integer.valueOf(String.valueOf(user.get("userId")))).collect(Collectors.toList());

        List<SysOrgUserVo> orgUsers = Optional.ofNullable(userOrgService.queryListByOrgId(teamId)).orElse(Lists.newArrayList());
        List<Integer> existedUserIds = orgUsers.stream().map(userOrg -> userOrg.getUserId()).collect(Collectors.toList());

        Optional.ofNullable(users).orElse(Lists.newArrayList()).forEach(user -> {
            int userId = Integer.valueOf(String.valueOf(user.get("userId")));
            if (!existedUserIds.contains(Integer.valueOf(String.valueOf(user.get("userId"))))) {
                List<Integer> roleIds = (List<Integer>) user.get("roleIds");
                if (ObjectUtils.isEmpty(roleIds)) {
                    throw new BaseException("角色ID不能为空!");
                }
                //
                userOrgService.insert(teamId, userId);
                //
                roleIds.forEach(roleId -> userOrgRoleService.insert(teamId, userId, roleId));
            }
        });
        List<Integer> diffUserIdList = ListUtil.difference(userIds, existedUserIds);
        if (ObjectUtils.isEmpty(diffUserIdList)) {
            throw new BaseException("分配用户中存在已分配的用户，请重新选择用户分配！");
        }
    }


    @Override
    public void updateUserRole(Integer teamId, Integer userId, List<Integer> roleIds) {
        if (ObjectUtils.isEmpty(teamId)) {
            throw new BaseException("修改对象不存在!");
        }
        if (ObjectUtils.isEmpty(userId)) {
            throw new BaseException("用户ID不能为空!");
        }
        List<SysUserOrgRolePo> list = Optional.ofNullable(userOrgRoleService.queryListByOrgIdAndUserId(teamId, userId)).orElse(Lists.newArrayList());
        ImmutableMap<Integer, SysUserOrgRolePo> roleId_Po = Maps.uniqueIndex(list, userOrgRolePo -> userOrgRolePo.getRoleId());
        //
        roleIds.stream().filter(roleId -> !roleId_Po.keySet().contains(roleId)).forEach(roleId -> userOrgRoleService.insert(teamId, userId, roleId));
        //
        roleId_Po.keySet().stream().filter(roleId -> !roleIds.contains(roleId)).forEach(roleId -> userOrgRoleService.delete(roleId_Po.get(roleId)));
    }

    /**
     * 根据项目团队类型初始化设置扩展列值
     * @param id
     * @return
     */
    private ProjectTeamVo initProjectTramVoExtendedColumn(int id){
        ProjectTeamVo vo = null;
        if (!ObjectUtils.isEmpty(id)){
            vo = this.mapper.selectProjectTeamById(id);
            if (vo != null){
                //2，如果选择父节点的类别为组织，则导入的数据，类别和单位都继承父节点。
                //3，如果选择父节点的类别为标段，标段类型为监理标的，则导入的数据，类别为组织，单位类型为监理单位。
                if (SysEnum.SECTION.getCode().equals(vo.getExtendedColumn1())){
                    if (SysEnum.SUPERVISOR.getCode().equals(vo.getExtendedColumn2())){
                        vo.setExtendedColumn3(SysEnum.CONSTRUCTION_CONTROL_UNIT.getCode());
                        vo.setExtendedColumn4(SysEnum.CONSTRUCTION_CONTROL_UNIT_TYPE.getCode());
                    }else{//4，如果选择父节点的类别为标段，标段类型为其他标的，则导入的数据，类别为组织，单位类型为施工单位。
                        vo.setExtendedColumn3(SysEnum.CONSTRUCTION_UNIT.getCode());
                        vo.setExtendedColumn4(SysEnum.CONSTRUCTION_UNIT_TYPE.getCode());
                    }
                }
                //清空标段相关信息
                vo.setExtendedColumn2(null);
                vo.setExtendedColumn5(null);
            }
        }
        return vo;
    }

    @Override
    @AddLog(title = "导入项目团队", module = LoggerModuleEnum.IM_PROJTEAM)
    public void importProjectTeam(String dataSource, String bizType, Integer bizId, Integer parentId, Map<String, Object> data) {
        List<Integer> ids = (List<Integer>) data.get("ids");
        boolean userFlag = (boolean) data.get("userFlag");
        ProjectTeamVo pvo = this.initProjectTramVoExtendedColumn(parentId);
        if ("org".equalsIgnoreCase(dataSource) || "projectTeam".equalsIgnoreCase(dataSource)) {
            List<SysOrgInfoVo> orgs = new ArrayList<>();
            if (!ObjectUtils.isEmpty(ids)) {
                orgs = orgService.queryOrgsByIds(ids);
            }
            if (ObjectUtils.isEmpty(orgs)) {
                throw new BaseException("未找到相关组织机构");
            }
            //处理日志信息
            this.queryLoggerImportOrgInfo(dataSource, orgs);

            ImmutableListMultimap<Integer, SysOrgInfoVo> parentIdMap = Multimaps.index(orgs, orgPo -> ids.contains(orgPo.getParentOrg().getId()) ? orgPo.getParentOrg().getId() : 0);
            //Map<Integer, Integer> orgId_teamIdMap = this.importProjectTeamFromOrg(bizType, bizId, 0, parentId, parentIdMap);
            Map<Integer, Integer> orgId_teamIdMap = this.importProjectTeamFromOrg(pvo, bizType, bizId, 0, parentId, parentIdMap);
            if (userFlag) {
                List<SysOrgUserVo> orgUsers = Optional.ofNullable(userOrgService.queryListByOrgIds(ids)).orElse(Lists.newArrayList());
                List<SysUserOrgRolePo> userOrgRoles = Optional.ofNullable(userOrgRoleService.queryListByOrgIds(ids)).orElse(Lists.newArrayList());
                ImmutableListMultimap<Integer, SysOrgUserVo> orgId2OrgUser = Multimaps.index(orgUsers, orgUser -> orgUser.getOrgId());
                ImmutableListMultimap<String, SysUserOrgRolePo> orgIdUser2OrgUserRole = Multimaps.index(userOrgRoles, orgUserRole -> orgUserRole.getOrgId() + "_" + orgUserRole.getUserId());

                orgId_teamIdMap.forEach((orgId, teamId) -> {
                    ImmutableList<SysOrgUserVo> userOrgs = orgId2OrgUser.get(orgId);
                    if (!ObjectUtils.isEmpty(userOrgs)) {
                        userOrgs.stream().forEach(userOrg -> {
                            userOrgService.insert(teamId, userOrg.getUserId());
                            ImmutableList<SysUserOrgRolePo> userOrgRoleList = orgIdUser2OrgUserRole.get(orgId + "_" + userOrg.getUserId());
                            if (!ObjectUtils.isEmpty(userOrgRoleList)) {
                                userOrgRoleList.stream().forEach(userOrgRole -> userOrgRoleService.insert(teamId, userOrg.getUserId(), userOrgRole.getRoleId()));
                            }
                        });
                    }
                });
            }
        } else {
            List<SysIptInfoVo> ipts = iptService.queryIptByIds(ids);
            if (ObjectUtils.isEmpty(ipts)) {
                throw new BaseException("未找到相关IPT");
            }
            //处理日志信息
            this.queryLoggerImportIptInfo(ipts);
            ImmutableListMultimap<Integer, SysIptInfoVo> parentIdMap = Multimaps.index(ipts, ipt -> !ObjectUtils.isEmpty(ipt.getParent()) && ids.contains(ipt.getParent().getId()) ? ipt.getParent().getId() : 0);
            Map<Integer, Integer> orgId_teamIdMap = this.importProjectTeamFromIpt(pvo,bizType, bizId, 0, parentId, parentIdMap);
            if (userFlag) {
                List<SysUserIptPo> userIpts = Optional.ofNullable(userIptService.queryUserIptRelation(ids)).orElse(Lists.newArrayList());
                List<SysUserIptRolePo> userIptRoles = Optional.ofNullable(userIptRoleService.queryUserIptRoleRelation(ids)).orElse(Lists.newArrayList());
                ImmutableListMultimap<Integer, SysUserIptPo> iptId2IptUser = Multimaps.index(userIpts, userIpt -> userIpt.getIptId());
                ImmutableListMultimap<String, SysUserIptRolePo> iptIdUser2UserIptRole = Multimaps.index(userIptRoles, userIptRole -> userIptRole.getIptId() + "_" + userIptRole.getUserId());

                orgId_teamIdMap.forEach((iptId, teamId) -> {
                    ImmutableList<SysUserIptPo> userIptList = iptId2IptUser.get(iptId);
                    if (!ObjectUtils.isEmpty(userIptList)) {
                        userIptList.stream().forEach(userIpt -> {
                            userOrgService.insert(teamId, userIpt.getUserId());

                            ImmutableList<SysUserIptRolePo> userIptRoleList = iptIdUser2UserIptRole.get(iptId + "_" + userIpt.getUserId());
                            if (!ObjectUtils.isEmpty(userIptRoleList)) {
                                userIptRoleList.stream().forEach(userIptRole -> userOrgRoleService.insert(teamId, userIptRole.getUserId(), userIptRole.getRoleId()));
                            }
                        });
                    }
                });
            }
        }
    }

    private void queryLoggerImportIptInfo(List<SysIptInfoVo> ipts) {
        if (!ObjectUtils.isEmpty(ipts)) {
            StringBuffer names = new StringBuffer();
            for (SysIptInfoVo sysIptInfoVo : ipts) {
                names.append(sysIptInfoVo.getIptName() + "，");
            }
            String retName = names.toString().substring(0, names.toString().length() - 1);
            this.setAcmLogger(new AcmLogger("从IPT导入团队，团队名称为：" + retName));
        }
    }

    private void queryLoggerImportOrgInfo(String dataSource, List<SysOrgInfoVo> orgs) {
        if ("org".equalsIgnoreCase(dataSource)) {
            if (!ObjectUtils.isEmpty(orgs)) {
                StringBuffer names = new StringBuffer();
                for (SysOrgInfoVo sysOrgInfoVo : orgs) {
                    names.append(sysOrgInfoVo.getOrgName() + "，");
                }
                String retName = names.toString().substring(0, names.toString().length() - 1);
                this.setAcmLogger(new AcmLogger("从组织机构导入团队，团队名称为：" + retName));
            }
        }
        if ("projectTeam".equalsIgnoreCase(dataSource)) {
            if (!ObjectUtils.isEmpty(orgs)) {
                StringBuffer names = new StringBuffer();
                for (SysOrgInfoVo sysOrgInfoVo : orgs) {
                    names.append(sysOrgInfoVo.getOrgName() + "，");
                }
                String retName = names.toString().substring(0, names.toString().length() - 1);
                this.setAcmLogger(new AcmLogger("从其他组织项目导入团队，团队名称为：" + retName));
            }
        }
    }

    private Map<Integer, Integer> importProjectTeamFromIpt(ProjectTeamVo pvo, String bizType, Integer bizId, int parentIptId, Integer newParentIptId, ImmutableListMultimap<Integer, SysIptInfoVo> parentIdMap) {
        Map<Integer, Integer> result = new HashMap<>();
        ImmutableList<SysIptInfoVo> ipts = parentIdMap.get(parentIptId);
        if (!ObjectUtils.isEmpty(ipts)) {
            for (SysIptInfoVo ipt : ipts) {
                ProjectTeamAddForm form = new ProjectTeamAddForm();
                form.setBizType(bizType);
                form.setBizId(bizId);
                form.setTeamCode(ipt.getIptCode());
                form.setTeamName(ipt.getIptName());
                form.setParentId(newParentIptId);
                form.setExtendedColumn1(SysEnum.ORG.getCode());//导入的团队类型只能是组织
                if (pvo != null){
                    form.setExtendedColumn2(pvo.getExtendedColumn2());
                    form.setExtendedColumn3(pvo.getExtendedColumn3());
                    form.setExtendedColumn4(pvo.getExtendedColumn4());
                    form.setExtendedColumn5(pvo.getExtendedColumn5());
                }
                Integer newId = this.add(form);

                result.put(ipt.getId(), newId);
                result.putAll(this.importProjectTeamFromIpt(pvo, bizType, bizId, ipt.getId(), newId, parentIdMap));
            }
        }
        return result;
    }

    private Map<Integer, Integer> importProjectTeamFromOrg(ProjectTeamVo pvo , String bizType, Integer bizId, int parentOrgId, Integer newParentIptId, ImmutableListMultimap<Integer, SysOrgInfoVo> parentIdMap) {
        Map<Integer, Integer> result = new HashMap<>();
        ImmutableList<SysOrgInfoVo> orgs = parentIdMap.get(parentOrgId);
        if (!ObjectUtils.isEmpty(orgs)) {
            for (SysOrgInfoVo org : orgs) {
                ProjectTeamAddForm form = new ProjectTeamAddForm();
                form.setBizType(bizType);
                form.setBizId(bizId);
                form.setTeamCode(org.getOrgCode());
                form.setTeamName(org.getOrgName());
                form.setParentId(newParentIptId);
                form.setExtendedColumn1(SysEnum.ORG.getCode());//导入的团队类型只能是组织
                if (pvo != null){
                    //2，如果选择父节点的类别为组织，则导入的数据，类别和单位都继承父节点。
                    //3，如果选择父节点的类别为标段，标段类型为监理标的，则导入的数据，类别为组织，单位类型为监理单位。
                    //4，如果选择父节点的类别为标段，标段类型为其他标的，则导入的数据，类别为组织，单位类型为施工单位。
                    form.setExtendedColumn3(pvo.getExtendedColumn3());
                    form.setExtendedColumn4(pvo.getExtendedColumn4());
                }
                else{ //1，如果没选父节点，则导入的数据类别为组织，单位类型为建设单位
                    form.setExtendedColumn3(SysEnum.DEVELOPMENT_ORGANIZATION.getCode());
                    form.setExtendedColumn4(org.getExtendedColumn4());
                }
                Integer newId = this.add(form);

                result.put(org.getId(), newId);
                result.putAll(this.importProjectTeamFromOrg(pvo, bizType, bizId, org.getId(), newId, parentIdMap));
            }
        }
        return result;
    }

    @Override
    public void copyProjectTeam(String sourceBizType, Integer sourceBizId, String targetBizType, Integer targetBizId, boolean teamCanEmpty) {
        List<ProjectTeamVo> orgs = mapper.selectProjectTeamList(sourceBizType, sourceBizId);
        if (!ObjectUtils.isEmpty(orgs)) {
            List<Integer> ids = orgs.stream().map(org -> org.getId()).collect(Collectors.toList());
            ImmutableListMultimap<Integer, ProjectTeamVo> parentIdMap = Multimaps.index(orgs, org -> org.getParentId());

            Map<Integer, Integer> orgId_teamIdMap = this.copyProjectTeam(targetBizType, targetBizId, 0, 0, parentIdMap);
            List<SysOrgUserVo> orgUsers = Optional.ofNullable(userOrgService.queryListByOrgIds(ids)).orElse(Lists.newArrayList());
            List<SysUserOrgRolePo> userOrgRoles = Optional.ofNullable(userOrgRoleService.queryListByOrgIds(ids)).orElse(Lists.newArrayList());
            ImmutableListMultimap<Integer, SysOrgUserVo> orgId2OrgUser = Multimaps.index(orgUsers, orgUser -> orgUser.getOrgId());
            ImmutableListMultimap<String, SysUserOrgRolePo> orgIdUser2OrgUserRole = Multimaps.index(userOrgRoles, orgUserRole -> orgUserRole.getOrgId() + "_" + orgUserRole.getUserId());
            orgId_teamIdMap.forEach((orgId, teamId) -> {
                ImmutableList<SysOrgUserVo> userOrgs = orgId2OrgUser.get(orgId);
                if (!ObjectUtils.isEmpty(userOrgs)) {
                    userOrgs.stream().forEach(userOrg -> {
                        userOrgService.insert(teamId, userOrg.getUserId());

                        ImmutableList<SysUserOrgRolePo> userOrgRoleList = orgIdUser2OrgUserRole.get(orgId + "_" + userOrg.getUserId());
                        if (!ObjectUtils.isEmpty(userOrgRoleList)) {
                            userOrgRoleList.stream().forEach(userOrgRole -> userOrgRoleService.insert(teamId, userOrg.getUserId(), userOrgRole.getRoleId()));
                        }
                    });
                }
            });
        } else {
            if (!teamCanEmpty) {
                throw new BaseException("没有项目团队");
            }
        }
    }

    private Map<Integer, Integer> copyProjectTeam(String bizType, Integer bizId, int parentOrgId, Integer newParentIptId, ImmutableListMultimap<Integer, ProjectTeamVo> parentIdMap) {
        Map<Integer, Integer> result = new HashMap<>();
        ImmutableList<ProjectTeamVo> orgs = parentIdMap.get(parentOrgId);
        if (!ObjectUtils.isEmpty(orgs)) {
            for (ProjectTeamVo org : orgs) {
                ProjectTeamAddForm form = new ProjectTeamAddForm();
                form.setBizType(bizType);
                form.setBizId(bizId);
                form.setTeamCode(org.getTeamCode());
                form.setTeamName(org.getTeamName());
                form.setParentId(newParentIptId);
                form.setExtendedColumn1(org.getExtendedColumn1());
                form.setExtendedColumn2(org.getExtendedColumn2());
                form.setExtendedColumn3(org.getExtendedColumn3());
                form.setExtendedColumn4(org.getExtendedColumn4());
                form.setExtendedColumn5(org.getExtendedColumn5());

                Integer newId = this.add(form);

                result.put(org.getId(), newId);
                result.putAll(this.copyProjectTeam(bizType, bizId, org.getId(), newId, parentIdMap));
            }
        }
        return result;
    }

    @Override
    public void deleteUsers(List<Integer> ids) {
        List<SysUserOrgPo> userOrgPos = Optional.ofNullable(this.userOrgService.selectByIds(ids)).orElse(Lists.newArrayList());
        userOrgPos.forEach(userOrg -> {
            this.userOrgService.delete(userOrg);
            List<SysUserOrgRolePo> userOrgRoles = userOrgRoleService.queryListByOrgIdAndUserId(userOrg.getOrgId(), userOrg.getUserId());
            List<Integer> userOrgRoleIds = userOrgRoles.stream().map(userOrgRolePo -> userOrgRolePo.getId()).collect(Collectors.toList());
            userOrgRoleService.deleteByIds(userOrgRoleIds);
        });
    }

    @Override
    public String queryLoggers(Integer teamId, List<Map<String, Object>> data) {
        SysOrgPo sysOrgPo = orgService.selectById(teamId);
        List<Integer> userIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(data)) {
            for (Map<String, Object> map : data) {
                userIds.add((Integer) map.get("userId"));
            }
            String userNames = userService.queryUserNamesByIds(userIds);
            String logger = "分配\"" + sysOrgPo.getOrgName() + "\"用户：" + userNames;
            return logger;
        }
        return "";
    }

    @Override
    public String queryTeamUserlogger(List<Integer> ids) {
        List<SysUserOrgPo> userOrgPos = this.userOrgService.selectByIds(ids);
        if (!ObjectUtils.isEmpty(userOrgPos)) {
            SysOrgPo sysOrgPo = orgService.selectById(userOrgPos.get(0).getOrgId());
            List<Integer> userIds = new ArrayList<>();
            for (SysUserOrgPo sysUserOrgPo : userOrgPos) {
                userIds.add(sysUserOrgPo.getUserId());
            }
            String userNames = userService.queryUserNamesByIds(userIds);
            String logger = "删除\"" + sysOrgPo.getOrgName() + "\"下用户：" + userNames;
            return logger;
        }
        return null;
    }

    @Override
    public List<ProjectTeamVo> updateProjectTeamSortByIdAndUpOrDown(Integer id,String upOrDown) {
        if(!ObjectUtils.isEmpty(id)){
           SysOrgPo sysOrgPo = this.selectById(id);

          Integer parentId = sysOrgPo.getParentId();
          Integer sortNum = sysOrgPo.getSort();
          String bizType = sysOrgPo.getBizType();
          Integer bizId = sysOrgPo.getBizId();

          if(!ObjectUtils.isEmpty(sortNum)){
              if("up".equals(upOrDown)){
                  SysOrgPo upSysOrgPo =this.selectSysOrgPoByParentIdAndSort(parentId,sortNum-1,bizType);
                  if(upSysOrgPo != null){
                      upSysOrgPo.setSort(sortNum);
                      this.updateById(upSysOrgPo);

                      sysOrgPo.setSort(sortNum-1);
                      this.updateById(sysOrgPo);
                  }
              }else if("down".equals(upOrDown)){
                  SysOrgPo downSysOrgPo =this.selectSysOrgPoByParentIdAndSort(parentId,sortNum+1,bizType);
                  if(downSysOrgPo != null){
                      downSysOrgPo.setSort(sortNum);
                      this.updateById(downSysOrgPo);

                      sysOrgPo.setSort(sortNum+1);
                      this.updateById(sysOrgPo);
                  }
              }
          }
               List<ProjectTeamVo> projectTeamVoList =  this.queryProjectTeamTree(bizType, bizId);
          return projectTeamVoList;
        }
        return null;
    }

    @Override
    public List<ProjectTeamUserVo> updateProjectTeamUserSortByIdAndUpdateOrDown(Integer teamId, Integer id, String upOrDown) {
        userOrgService.updateSysUserOrgPoByIdAndUpOrDown(id,teamId,upOrDown);
        return this.queryUserListByTeamId(teamId);
    }

    @Override public List<SysRoleVo> queryRoleListByProjectIdAndUserId(Integer projectId, Integer userId)
    {
        return mapper.queryTeamRoles(projectId,userId);
    }

    @Override
    public PageInfo<ProjectTeamVo> selectJlSectionList(Integer sectionId, Integer pageSize, Integer currentPageNum)
    {
        PageHelper.startPage(currentPageNum, pageSize);
        List<ProjectTeamVo> projectTeamVoList = mapper.selectJlSectionList(sectionId);
        PageInfo<ProjectTeamVo> pageInfo = new PageInfo<ProjectTeamVo>(projectTeamVoList);
        return pageInfo;
    }

    @Override public List<ProjectTeamVo> selectXyJlSectionList(Integer sectionId)
    {
        List<ProjectTeamVo> projectTeamVoList = mapper.selectXyJlSectionList(sectionId);
        return projectTeamVoList;
    }

    @Override public void addJlSection(List<JlSectionAddForm> jlSectionAddForms)
    {
        List<SectionRelationPo> insertPos = Lists.newArrayList();
        for (JlSectionAddForm jlSectionAddForm : jlSectionAddForms) {
            SectionRelationPo sectionRelationPo = dozerMapper.map(jlSectionAddForm, SectionRelationPo.class);
            sectionRelationPo.setId(leafService.getId());
            insertPos.add(sectionRelationPo);
        }
        mapper.insertJlSection(insertPos);
    }

    @Override public void deleteJlSection(List<JlSectionAddForm> jlSectionAddForms)
    {
        mapper.deleteJlSection(jlSectionAddForms);
    }

    /**
     * 根据父节点id，排序号，类型获取组织对象
     * @param parentId
     * @param sortNum
     * @param bizType
     * @return
     */
    private SysOrgPo selectSysOrgPoByParentIdAndSort(Integer parentId,Integer sortNum,String bizType){
        if(!ObjectUtils.isEmpty(parentId) && !ObjectUtils.isEmpty(sortNum) && !ObjectUtils.isEmpty(bizType)){
            Example example = new Example(SysOrgPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",parentId);
            criteria.andEqualTo("sort",sortNum);
            criteria.andEqualTo("bizType",bizType);
            return this.selectOneByExample(example);
        }
        return null;
    }

    @Override
    public void updateOrgSectionRelation(int sectionId, OrgSectionRelation relation) {
        //先根据标段id的删除记录，再新增
        if(sectionId !=0){
            mapper.deleteOrgSectionRelationBySectionId(sectionId);
        }
        mapper.insertOrgSectionRelation(relation);
    }

    @Override
    public OrgSectionRelation getOrgSectionRelation(int sectionId) {
        if(sectionId == 0){
            return null;
        }
        return mapper.getOrgSectionRelation(sectionId);
    }
}
