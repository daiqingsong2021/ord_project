package com.wisdom.acm.sys.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.sys.form.SysSearchOrgForm;
import com.wisdom.acm.sys.mapper.SectionMapper;
import com.wisdom.acm.sys.po.SysIptPo;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.service.*;
import com.wisdom.acm.sys.mapper.OrgMapper;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.form.SysOrgAddForm;
import com.wisdom.acm.sys.form.SysOrgUpdateForm;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.acm.sys.vo.SysOrgInfoVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.util.StringHelper;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.vo.*;
import com.wisdom.cache.annotation.CacheClear;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysOrgServiceImpl extends BaseService<OrgMapper, SysOrgPo> implements SysOrgService {

    @Autowired
    private SysUserOrgService sysUserOrgService;

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    @Autowired
    private CommDictService commDictService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private SectionService sectionService;

    /**
     * 根据项目获取组织
     *
     * @param projectId
     * @return
     */
    @Override
    public List<SysOrgInfoVo> queryOrgPosByProjectId(Integer projectId) {
        boolean useTeam = this.isUseTeamByProjectId(projectId);
        Integer bizId = null;
        String bizType = null;
        if (useTeam) {
            bizId = projectId;
            bizType = "project";
        }
        List<SysOrgPo> orgPos = this.queryOrgsByBizIdAndBizType(bizId, bizType);
        List<SysOrgInfoVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(orgPos)) {
            orgPos.forEach(org -> {
                SysOrgInfoVo orgInfoVo = new SysOrgInfoVo();
                orgInfoVo.setId(org.getId());
                orgInfoVo.setOrgName(org.getOrgName());
                orgInfoVo.setOrgCode(org.getOrgCode());
                retList.add(orgInfoVo);
            });
        }
        return retList;
    }

    @Override
    public boolean isUseTeamByProjectId(Integer projectId){
        Integer id = mapper.selectProjectIsExist(projectId);
        if (id == null) {
            throw new BaseException("该项目不存在");
        }
        // 用户
        boolean useTeam = this.mapper.isUseTeamByProjectById(projectId);
        return useTeam;
    }

    /**
     * 搜索组织
     *
     * @param search
     * @return
     */
//    @Cache(key = "acm:orgs:search{1}")
    @Override
    public List<SysOrgVo> queryOrgsBySearch(SysSearchOrgForm search) {
        List<Integer> retIds = null;
        List<SysOrgVo> orgAllTree = null;
        List<SysOrgVo> retOrgTree = null;

        //判断是否存在搜索条件
        if (search.getSearcher() != null && search.getSearcher() != "") {
            //查询符合条件的组织id
            retIds = mapper.selectOrgsBySearch(search);
            //获取全部组织树
            orgAllTree = this.queryOrgTree();
            //设置标识
            boolean auth = false;
            //递归返回符合条件的树形
            retOrgTree = this.querySearchOrgTree(retIds, orgAllTree, auth);
        } else {
            retOrgTree = this.queryOrgTree();
        }

        return retOrgTree;
    }


    /**
     * 获取orgTree
     *
     * @return
     */
    @Override
    public List<SysOrgVo> queryOrgTree() {
        List<SysOrgVo> orgList = mapper.selectOrgList();
        List<SysOrgVo> orgTree = TreeUtil.bulid(orgList, 0);
        if(ObjectUtils.isEmpty(orgTree))
            return Lists.newArrayList();
        return orgTree;
    }

    /**
     * 根据组织id集合获取组织对象
     *
     * @param orgIds
     * @return
     */
    @Override
    public Map<Integer, OrgVo> queryOrgVoByOrgIds(List<Integer> orgIds) {

        List<OrgVo> retlist = new ArrayList<>();
        //根据组织id集合获取组织对象
        List<SysOrgPo> orgs = this.selectByIds(orgIds);

        if (!ObjectUtils.isEmpty(orgs)) {
            orgs.forEach(org -> {
                OrgVo o = new OrgVo(org.getId(), org.getOrgName());
                retlist.add(o);
            });
        }
        return ListUtil.listToMap(retlist, "id", Integer.class);

    }

    @Override
    public SysOrgInfoVo getUserOrgInfo(Integer userId){
        List<SysOrgInfoVo> list = this.mapper.selectOrgsByUserId(userId);
        return ObjectUtils.isEmpty(list) ? null : list.get(0);
    }


    /**
     * 获取组织信息
     *
     * @param orgId
     * @return
     */
    @Override
    public SysOrgInfoVo getOrgInfo(Integer orgId) {
        SysOrgInfoVo sysOrgInfoVo = mapper.selectOrgInfoById(orgId);
        if (ObjectUtils.isEmpty(sysOrgInfoVo.getParentName())) {
            sysOrgInfoVo.setParentName("全局机构树");
        }
        Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode("sys.org.level");
        if (!ObjectUtils.isEmpty(dictMap) && !ObjectUtils.isEmpty(sysOrgInfoVo.getOrgLevel())) {
            sysOrgInfoVo.getOrgLevel().setName(String.valueOf(dictMap.get(sysOrgInfoVo.getOrgLevel().getCode()).getName()));
        }
        return sysOrgInfoVo;
    }


    /**
     * 获取组织信息
     *
     * @param orgIds
     * @return
     */
    @Override
    public List<SysOrgInfoVo> queryOrgInfoVoByOrgIds(List<Integer> orgIds) {
        List<SysOrgInfoVo> sysOrgInfoVos  = mapper.selectOrgInfoByIds(orgIds);
        if (ObjectUtils.isEmpty(sysOrgInfoVos)) {
           for (SysOrgInfoVo sysOrgInfoVo : sysOrgInfoVos){
               Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode("sys.org.level");
               if (!ObjectUtils.isEmpty(dictMap) && !ObjectUtils.isEmpty(sysOrgInfoVo.getOrgLevel())) {
                   sysOrgInfoVo.getOrgLevel().setName(String.valueOf(dictMap.get(sysOrgInfoVo.getOrgLevel().getCode()).getName()));
               }
           }
        }
        return sysOrgInfoVos;
    }

    @Override
    public SysOrgPo addOrg(SysOrgAddForm org) {
        //判断组织代码是否重复
        List<SysOrgPo> list = this.getOrgPoByCode(org.getOrgCode());
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("组织代码不能重复");
        }
        if (ObjectUtils.isEmpty(org.getSort())) {
            Integer sort = this.getNextSortByParentId(org.getParentId());
            org.setSort(sort);
        }
        SysOrgPo sysOrgPo = dozerMapper.map(org, SysOrgPo.class);

        super.insert(sysOrgPo);
        return sysOrgPo;
    }

    public int getNextSortByParentId(Integer parentId) {
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        return this.selectCountByExample(example);
    }

    /**
     * 修改组织
     *
     * @param orgUpdate
     * @return
     */
//    @CacheClear(keys = {"acm:orgs"})
    @Override
    @AddLog(title = "修改组织" ,module = LoggerModuleEnum.SM_ORG)
    public SysOrgPo updateOrg(SysOrgUpdateForm orgUpdate) {
        //判断组织代码是否重复
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("bizType");
        criteria.andEqualTo("orgCode", orgUpdate.getOrgCode());
        criteria.andNotEqualTo("id",orgUpdate.getId());
        List<SysOrgPo> list = this.mapper.selectByExample(example);
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("组织代码不能重复");
        }

        SysOrgPo updatePo = dozerMapper.map(orgUpdate, SysOrgPo.class);
        super.updateSelectiveById(updatePo);

        SysOrgPo sysOrgPo = mapper.selectByPrimaryKey(orgUpdate.getId());
        // 添加修改日志
        this.addChangeLogger(orgUpdate,sysOrgPo);
        return sysOrgPo;
    }

    @Override
    public SysOrgPo xinZengOrg(SysOrgAddForm org) {
        //判断组织代码是否重复
        List<SysOrgPo> list = this.getOrgPoByCode(org.getOrgCode());
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("组织代码不能重复");
        }
        if (ObjectUtils.isEmpty(org.getSort())) {
            Integer sort = this.getNextSortByParentId(org.getParentId());
            org.setSort(sort);
        }
        SysOrgPo sysOrgPo = dozerMapper.map(org, SysOrgPo.class);

        super.insert(sysOrgPo);
        return sysOrgPo;
    }

    @Override
    public SysOrgPo gengXinOrg(SysOrgUpdateForm orgUpdate) {
        //判断组织代码是否重复
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("bizType");
        criteria.andEqualTo("orgCode", orgUpdate.getOrgCode());
        criteria.andNotEqualTo("id",orgUpdate.getId());
        List<SysOrgPo> list = this.mapper.selectByExample(example);
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("组织代码不能重复");
        }

        SysOrgPo updatePo = dozerMapper.map(orgUpdate, SysOrgPo.class);
        super.updateSelectiveById(updatePo);

        SysOrgPo sysOrgPo = mapper.selectByPrimaryKey(orgUpdate.getId());
        // 添加修改日志
        this.addChangeLogger(orgUpdate,sysOrgPo);
        return sysOrgPo;
    }

    private List<SysOrgPo> getOrgPoByCodeIsRepetitive(String code){
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("bizType");
        criteria.andEqualTo("orgCode", code);
        List<SysOrgPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list) ? null : list;
    }

    @Override
    public List<SysOrgPo> getOrgPoByCode(String code) {
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgCode", code);
        List<SysOrgPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list) ? null : list;
    }

    /**
     * 删除组织
     *
     * @param orgIds
     */
    @CacheClear(keys = {"acm:orgs"})
    @Override
    public void deleteOrg(List<Integer> orgIds) {
        List<Integer> orgIdss = ListUtil.toIdList(this.queryChildrenAndMePos(orgIds));

        List<SysOrgUserVo> sysUserVoList = sysUserOrgService.queryListByOrgIds(orgIdss);
        if (!ObjectUtils.isEmpty(sysUserVoList)){
            throw new BaseException("该部门下有成员，无法删除！");
        }
        //删除用户部门角色
        sysUserOrgRoleService.deleteUserOrgRoleRelationByOrgIds(orgIdss);
        //删除部门用户
        sysUserOrgService.deleteUserOrgRelationByOrgIds(orgIdss);
        //删除部门
        super.deleteByIds(orgIdss);

//        List<SysOrgVo> orgList = this.queryOrgTree();
//        for (Integer orgId : orgIds) {
//            List<SysOrgVo> orgTree = TreeUtil.bulid(orgList,orgId); //获取当前id下所有组织
//            if (!ObjectUtils.isEmpty(orgList)) {
//                this.deleteChildrenOrg(orgList);
//            }
//            super.deleteById(orgId);
//            userOrgMapper.deleteByOrgId(orgId);
//        }
    }

    /**
     * 删除子组织
     *
     * @param sysOrgVoList
     */
//    public void deleteChildrenOrg(List<SysOrgVo> sysOrgVoList) {
//        for (SysOrgVo sysOrgVo : sysOrgVoList) {
//            super.deleteById(sysOrgVo.getId());
//            userOrgMapper.deleteByOrgId(sysOrgVo.getId());
////            userOrgMapper.deleteUserOrgRoleByOrgId(sysOrgVo.getId());
//            if (!ObjectUtils.isEmpty(sysOrgVo.getChildren())) {
//                this.deleteChildrenOrg(sysOrgVo.getChildren());
//            }
//        }
//    }


    /**
     * 递归查询符合条件的org结果集
     *
     * @param retIds
     * @param orgAllTree
     * @param auth
     * @return
     */
    private List<SysOrgVo> querySearchOrgTree(List<Integer> retIds, List<SysOrgVo> orgAllTree, boolean auth) {
        List<SysOrgVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(orgAllTree)) {
            //判断当前id是否包含在查询范围内
            for (SysOrgVo sysOrgVo : orgAllTree) {
                boolean thisAuth = auth;
                if (!thisAuth && retIds.contains(sysOrgVo.getId())) {
                    thisAuth = true;
                }
                //递归查询子节点
                List<SysOrgVo> childrenList = this.querySearchOrgTree(retIds, sysOrgVo.getChildren(), thisAuth);

                if (thisAuth || !ObjectUtils.isEmpty(childrenList)) {
                    SysOrgVo retOrgVo = new SysOrgVo();
                    this.dozerMapper.map(sysOrgVo, retOrgVo);
                    if (!ObjectUtils.isEmpty(childrenList))
                        retOrgVo.setChildren(childrenList);
                    retList.add(retOrgVo);
                }
            }
        }

        return retList;
    }

    /**
     * 获取多个组织信息
     *
     * @param orgIds
     * @return
     */
    @Override
    public List<SysOrgInfoVo> queryOrgsByIds(List<Integer> orgIds) {
        List<SysOrgInfoVo> list = mapper.selectOrgInfosByIds(orgIds);
        return list;
    }

    /**
     * 根据项目获取组织
     *
     * @param projectId
     * @return
     */
    @Override
    public List<SelectVo> queryOrgSelectVosByProjectId(Integer projectId) {
        Integer id = mapper.selectProjectIsExist(projectId);
        if (id == null) {
            throw new BaseException("该项目不存在");
        }
        // 用户
        boolean useTeam = this.mapper.isUseTeamByProjectById(projectId);
        List<SysOrgVo> orgList = null;
        Integer bizId = null;
        String bizType = null;
        if (useTeam) {
            bizId = projectId;
            bizType = "project";
        }

        List<SysOrgPo> orgPos = this.queryOrgsByBizIdAndBizType(bizId, bizType);

        List<SelectVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(orgPos)) {
            orgPos.forEach(org -> {
                SelectVo selectVo = new SelectVo();
                selectVo.setTitle(org.getOrgName());
                selectVo.setType(String.valueOf(org.getOrgType()));
                selectVo.setId(org.getId());
                selectVo.setValue(org.getId());
                selectVo.setParentId(org.getParentId());
                //组织和标段区分
                selectVo.setExtendedColumn1(org.getExtendedColumn1());
                retList.add(selectVo);
            });
        }
        List<SelectVo> orgTree = TreeUtil.bulid(retList, 0);
        orgTree  = this.filterSection(orgTree);
        return orgTree;
    }

    @Override public List<SelectVo> queryQuesOrgSelectVosByProjectId(Integer projectId)
    {
        UserInfo user=commUserService.getLoginUser();
        List<GeneralVo> roles =sectionService.queryTeamRoles(projectId,user.getId());
        List<String> roleCodes= ListUtil.toValueList(roles, "code", String.class,true);
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
        {//如果是是外部单位的人，滤项目团队中的“主任室”（采用字典目录）
            List<SelectVo> selectVoList=queryOrgSelectVosByProjectId(projectId);
            Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode("comu.issue.disabledept");
            List<String> dictName=Lists.newArrayList();
            if(!ObjectUtils.isEmpty(dictMap))
            {
                for (Map.Entry<String, DictionaryVo> entry : dictMap.entrySet())
                {
                    dictName.add(entry.getValue().getName().trim());
                }
            }

            return  this.filterQuesSelectVoList(selectVoList,ListUtil.toStr(dictName));
        }
        else
        {
            return queryOrgSelectVosByProjectId(projectId);
        }
    }

    private List<SelectVo> filterQuesSelectVoList(List<SelectVo> selectVoList, String dictNames)
    {
        List<SelectVo> newSelectVoList=Lists.newArrayList();
        for (SelectVo selectVo:selectVoList)
        {
            if(dictNames.indexOf(selectVo.getTitle().trim())<=-1)
            {//说明数据字典都不包含
                List<SelectVo> children=Lists.newArrayList();
                if(!ObjectUtils.isEmpty(selectVo.getChildren()))
                    children=filterQuesSelectVoList(selectVo.getChildren(),dictNames);
                selectVo.setChildren(children);
                newSelectVoList.add(selectVo);
            }
        }
        return newSelectVoList;
    }

    /**
     * 递归过滤标段
     * @param orgTree
     * @return
     */
    private List<SelectVo> filterSection(List<SelectVo> orgTree){

        List<SelectVo> resultList = new ArrayList<>();

        if(!ObjectUtils.isEmpty(orgTree)){
            for (SelectVo selectVo : orgTree) {

                List<SelectVo> returnList = this.filterSection(selectVo.getChildren());
                 if("org".equals(selectVo.getExtendedColumn1())){
                      if(!ObjectUtils.isEmpty(returnList)){
                          selectVo.setChildren(returnList);
                      }
                      resultList.add(selectVo);
                 }else{
                      if(!ObjectUtils.isEmpty(returnList)){
                          resultList.addAll(returnList);
                      }
                 }
            }
        }
        return resultList;
    }




    /**
     * 根据项目获取组织
     *
     * @return
     */
    @Override
    public List<SelectVo> queryGlobalOrgSelectVos() {

        List<SysOrgPo> orgPos = this.queryOrgsByBizIdAndBizType(null, null);

        List<SelectVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(orgPos)) {
            orgPos.forEach(org -> {
                SelectVo selectVo = new SelectVo();
                selectVo.setTitle(org.getOrgName());
                selectVo.setType(org.getOrgType() + "");
                selectVo.setId(org.getId());
                selectVo.setValue(org.getId());
                selectVo.setParentId(org.getParentId());
                retList.add(selectVo);
            });
        }
        List<SelectVo> orgTree = TreeUtil.bulid(retList, 0);
        return orgTree;
    }

    @Override public List<SelectVo> queryQuesGlobalOrgSelectVos()
    {
        UserInfo user=commUserService.getLoginUser();
        List<Integer> userIdList=Lists.newArrayList();
        userIdList.add(user.getId());
        List<SysUserOrgRoleVo>  sysUserOrgRoleVos=sysUserOrgRoleService.queryUserRoleByUserId(userIdList);
        List<String> roleCodes= ListUtil.toValueList(sysUserOrgRoleVos, "roleCode", String.class,true);
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
        {//如果是是外部单位的人，滤项目团队中的“主任室”（采用字典目录）
            List<SelectVo> selectVoList=queryGlobalOrgSelectVos();
            Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode("comu.issue.disabledept");
            List<String> dictName=Lists.newArrayList();
            if(!ObjectUtils.isEmpty(dictMap))
            {
                for (Map.Entry<String, DictionaryVo> entry : dictMap.entrySet())
                {
                    dictName.add(entry.getValue().getName().trim());
                }
            }
            return  this.filterQuesSelectVoList(selectVoList,ListUtil.toStr(dictName));
        }
        else
        {
            return queryGlobalOrgSelectVos();
        }
    }

    @Override
    public List<SysOrgPo> queryOrgsByBizIdAndBizType(Integer bizId, String bizType) {

        Example example = new Example(SysOrgPo.class);
        example.setOrderByClause("sort_num");
        Example.Criteria criteria = example.createCriteria();
        if (bizId != null) {
            criteria.andEqualTo("bizId", bizId);
        } else {
            criteria.andIsNull("bizId");
        }

        if (bizType != null) {
            criteria.andEqualTo("bizType", bizType);
        } else {
            criteria.andIsNull("bizType");
        }
        criteria.andEqualTo("status",1);
        return this.selectByExample(example);
    }

    /**
     * 获取用户组织树形列表
     *
     * @return
     */
    @Override
    public List<SysOrgUserTreeVo> queryOrgUserTree() {
        //获取组织列表
        List<SysOrgUserTreeVo> orgList = mapper.selectOrgUserTreeVo();
        //获取用户列表
        List<SysOrgUserTreeVo> userList = sysUserOrgService.queryOrgUsers();
        if (!ObjectUtils.isEmpty(userList))
        {
            List<Integer> ids = new ArrayList<>();
            for (SysOrgUserTreeVo sysOrgUserTreeVo : userList)
            {
                ids.add(sysOrgUserTreeVo.getId());
            }
            List<SysUserOrgRoleVo> roles = sysUserOrgRoleService.queryUserRoleByUserId(ids);
            Map<Integer, List<SysUserOrgRoleVo>> roleMap = ListUtil.listToMapList(roles, "userId", Integer.class);
            for (SysOrgUserTreeVo sysOrgUserTreeVo : userList)
            {
                List<Integer> roleIds = new ArrayList<>();
                if (!ObjectUtils.isEmpty(roleMap.get(sysOrgUserTreeVo.getId())))
                {
                    for (SysUserOrgRoleVo sysUserOrgRoleVo : roleMap.get(sysOrgUserTreeVo.getId()))
                    {
                        roleIds.add(sysUserOrgRoleVo.getRoleId());
                    }
                }
                sysOrgUserTreeVo.setRoleId(roleIds);
            }
        }
        //获取组织树形结构
        List<SysOrgUserTreeVo> retTree = TreeUtil.bulid(orgList, 0);

        Map<Integer, List<SysOrgUserTreeVo>> userMap = ListUtil.bulidTreeListMap(userList, "parentId", Integer.class);
        //递归组织树形插入用户
        this.queryOrgUserTreeInsertUsers(retTree, userMap);

        return retTree;
    }

    /**
     * 递归组织树形，插入用户
     *
     * @param retTree
     * @param userMap
     * @return
     */
    private void queryOrgUserTreeInsertUsers(List<SysOrgUserTreeVo> retTree, Map<Integer, List<SysOrgUserTreeVo>> userMap) {

        if (!ObjectUtils.isEmpty(retTree)) {
            for (SysOrgUserTreeVo sysOrgUserTreeVo : retTree) {

                this.queryOrgUserTreeInsertUsers(sysOrgUserTreeVo.getChildren(), userMap);
                sysOrgUserTreeVo.addChildrens(userMap.get(sysOrgUserTreeVo.getId()));
            }
        }
    }

    @Override
    public void deleteOrgUser(List<Integer> userIds, Integer orgId) {
        if (!ObjectUtils.isEmpty(userIds)){
            sysUserOrgRoleService.deleteUserOrgRoleRelationByUserIdAndOrgId(userIds,orgId);
            sysUserOrgService.deleteUserOrgRelationByUserIdAndOrgId(userIds,orgId);
        }
    }

    /**
     * 删除组织用户关系日志
     * @param userIds
     * @param orgId
     * @return
     */
    @Override
    public String queryDeleteOrgUserLogger(List<Integer> userIds, Integer orgId) {
        String userNames = sysUserService.queryUserNamesByIds(userIds);
        SysOrgPo sysOrgPo = super.selectById(orgId);
        String logger = "删除\""+sysOrgPo.getOrgName()+"\"用户：" + userNames;
        return logger;
    }

    /**
     * 获取组织树结构
     * @return
     */
    @Override
    public List<SelectVo> queryOrgNameSelectVos(){
        List<SysOrgPo> sysOrgPos = this.queryOrgNameList("0");
        List<SelectVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(sysOrgPos)){
            for (SysOrgPo sysOrgPo : sysOrgPos) {
                SelectVo selectVo = new SelectVo();
                selectVo.setTitle(sysOrgPo.getOrgName());
                selectVo.setType(sysOrgPo.getOrgType()+"");
                selectVo.setId(sysOrgPo.getId());
                selectVo.setValue(sysOrgPo.getId());
                selectVo.setParentId(sysOrgPo.getParentId());
                retList.add(selectVo);
            }
        }
        return retList;
    }

    @Override
    public List<SysOrgSelectVo> queryOrgSelectVo(){
        List<SysOrgSelectVo> orgSelectVos = new ArrayList<SysOrgSelectVo>();
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",0);
        criteria.andEqualTo("bizType",null);
        criteria.andEqualTo("orgType",0);
        List<SysOrgPo> sysOrgPos = this.selectByExample(example);
        if(!ObjectUtils.isEmpty(sysOrgPos)){
            for (SysOrgPo sysOrgPo : sysOrgPos){
                SysOrgSelectVo sysOrgSelectVo = new SysOrgSelectVo();
                sysOrgSelectVo.setId(sysOrgPo.getId());
                sysOrgSelectVo.setName(sysOrgPo.getOrgName());
                sysOrgSelectVo.setType("公司");
                orgSelectVos.add(sysOrgSelectVo);
            }
        }
        return orgSelectVos;
    }

    @Override
    public SysOrgInfoVo querySysUserOrgPoByUserId(Integer userId){
        List<SysOrgInfoVo> list = this.mapper.selectMainOrgByUserId(userId);
        return ObjectUtils.isEmpty(list) ? null : list.get(0);
    }

    private List<SysOrgPo> queryOrgNameList(String orgType){
        Example example = new Example(SysOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgType",orgType);
        List<SysOrgPo> sysOrgPos = this.selectByExample(example);
        return sysOrgPos;
    }

    public  static void main(String args[])
    {
       System.out.println(0%1000);
        System.out.println(1000%1000);
        System.out.println(2000%1000);
    }
}
