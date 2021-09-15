package com.wisdom.acm.szxm.service.app.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.common.PageInfoUtiil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.app.CommonlyUsedUserAddForm;
import com.wisdom.acm.szxm.mapper.app.AppAddressBookMapper;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.service.app.AppAddressBookService;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.ProjInfoService;
import com.wisdom.acm.szxm.vo.app.ContactsUserVo;
import com.wisdom.acm.szxm.vo.app.OrgInfoVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AppAdressBookServiceImpl  implements AppAddressBookService
{
    @Autowired
    private AppAddressBookMapper appAddressBookMapper;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private ProjInfoService projInfoService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private CommDictService commDictService;

    @Autowired
    private org.dozer.Mapper dozerMapper;

    @Override public void mergeIntoCommonUserTable(CommonlyUsedUserAddForm commonlyUsedUserAddForm)
    {
        appAddressBookMapper.mergeIntoCommonUserTable(commonlyUsedUserAddForm);
    }

    @Override public List<ContactsUserVo> queryCommonlyUsedUser(Integer userId)
    {
        return appAddressBookMapper.queryCommonlyUsedUser(userId);
    }

    @Override
    public List<ContactsUserVo> queryInnerPeople(Map<String, Object> mapWhere)
    {
        UserInfo user=commUserService.getLoginUser();
        //查询项目团队的用户
        List<GeneralVo> roles =szxmCommonUtil.queryTeamRoles(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))),user.getId());
        List<String> roleCodes= ListUtil.toValueList(roles, "code", String.class, true);
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
        {//非业主，非业主只能看到本单位的人员
            //查询该用户所对应的的 人员信息内的数据，取其projInfoId ,根据projInfoID 查询所有人员
            mapWhere.put("telPhone",user.getPhone());
            mapWhere.put("contactsName", StringHelper.formattString(String.valueOf(mapWhere.get("contactsName"))));
            List<ContactsUserVo> contactsUserVoList=appAddressBookMapper.selectWbInnerPeople(mapWhere);
            return contactsUserVoList;
        }
        else
        {//业主,查看项目ID下所有的内部部门用户
            mapWhere.put("contactsName", StringHelper.formattString(String.valueOf(mapWhere.get("contactsName"))));
            List<ContactsUserVo> contactsUserVoList=appAddressBookMapper.selectYzInnerPeople(mapWhere);
            return contactsUserVoList;
        }
    }

    @Override public List<OrgInfoVo> queryOuterOrg(Map<String, Object> mapWhere)
    {
        UserInfo user=commUserService.getLoginUser();
        //查询项目团队的用户
        List<GeneralVo> roles =szxmCommonUtil.queryTeamRoles(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))),user.getId());
        List<String> roleCodes= ListUtil.toValueList(roles, "code", String.class, true);
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
        {//非业主，能看到项目ID下所有的 单位，排除本身，只查顶层节点
            mapWhere.put("projectId", StringHelper.formattString(String.valueOf(mapWhere.get("projectId"))));
            List<OrgInfoVo> orgInfoVoList=appAddressBookMapper.selectWbOuterOrg(mapWhere);
            PlanProjectVo projectVo=commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for(OrgInfoVo orgInfoVo:orgInfoVoList)
            {
                if("1".equals(orgInfoVo.getSource()))
                {//如果是来源于项目团队的
                    orgInfoVo.setSectionId(null);
                    orgInfoVo.setSectionCode("");
                    orgInfoVo.setSectionName("");
                }
                else
                {
                    ProjectTeamVo sectionVo = sectionMap.get(orgInfoVo.getSectionId());
                    orgInfoVo.setProjectName(projectVo.getName());
                    orgInfoVo.setSectionCode(sectionVo.getCode());
                    orgInfoVo.setSectionName(sectionVo.getName());
                }

            }
            return orgInfoVoList;

        }
        else
        {//业主的。查询所有项目ID下 projInfo信息
            Example example = new Example(ProjInfoPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId","0");//只查顶层节点
            criteria.andEqualTo("projectId",StringHelper.formattString(String.valueOf(mapWhere.get("projectId"))));//项目ID
            List<ProjInfoPo> projInfoPos = projInfoService.selectByExample(example);
            List<OrgInfoVo> orgInfoVoList= Lists.newArrayList();
            PlanProjectVo projectVo=commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for(ProjInfoPo projInfoPo:projInfoPos)
            {
                ProjectTeamVo sectionVo = sectionMap.get(projInfoPo.getSectionId());
                OrgInfoVo orgInfoVo=dozerMapper.map(projInfoPo,OrgInfoVo.class);
                orgInfoVo.setOrgId(projInfoPo.getId());
                orgInfoVo.setProjectName(projectVo.getName());
                orgInfoVo.setSectionCode(sectionVo.getCode());
                orgInfoVo.setSectionName(sectionVo.getName());
                orgInfoVo.setSource("0");//来源于人员管理
                orgInfoVoList.add(orgInfoVo);
            }
            return orgInfoVoList;
        }
    }

    @Override
    public List<ContactsUserVo> queryOuterUserByOrgId(Map<String, Object> mapWhere)
    {
        UserInfo user=commUserService.getLoginUser();
        //查询项目团队的用户
        List<GeneralVo> roles =szxmCommonUtil.queryTeamRoles(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))),user.getId());
        List<String> roleCodes= ListUtil.toValueList(roles, "code", String.class, true);
        Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
        String source=StringHelper.formattString(String.valueOf(mapWhere.get("source")));//0 人员管理，1 项目团队
        String orgId=StringHelper.formattString(String.valueOf(mapWhere.get("orgId")));
        String orgName=StringHelper.formattString(String.valueOf(mapWhere.get("orgName")));
        String projectId=StringHelper.formattString(String.valueOf(mapWhere.get("projectId")));
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
        {//非业主的
            if("0".equals(source))
            {//来源于 人员管理
                Example example = new Example(PeoplePo.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("projInfoId",orgId);//组织ID
                if(StringHelper.isNotNullAndEmpty(StringHelper.formattString(String.valueOf(mapWhere.get("contactsName")))))
                {
                    criteria.andEqualTo("contactsName",StringHelper.formattString(String.valueOf(mapWhere.get("contactsName"))));//联系人姓名
                }
                List<PeoplePo> peoplePoList = peopleService.selectByExample(example);
                List<ContactsUserVo> contactsUserVoList=Lists.newArrayList();
                for(PeoplePo peoplePo:peoplePoList)
                {
                    ContactsUserVo contactsUserVo=new ContactsUserVo();
                    contactsUserVo.setSource(source);
                    contactsUserVo.setSex(peoplePo.getSex());
                    contactsUserVo.setContactsId(peoplePo.getId());
                    contactsUserVo.setContactsName(peoplePo.getName());
                    contactsUserVo.setContactsDept(orgName);
                    contactsUserVo.setTelPhone(peoplePo.getTelPhone());
                    contactsUserVo.setJob(szxmCommonUtil.getDictionaryName(psotionDictMap, peoplePo.getJob()));
                    contactsUserVoList.add(contactsUserVo);
                }
                return contactsUserVoList;
            }
            else if("1".equals(source))
            {//来源于 项目团队,只查到orgId 下与其有关的业主代表数据
                List<ContactsUserVo> contactsUserVoList=Lists.newArrayList();
                List<PeopleVo> peopleVoList=projInfoService.selectAddressBookPeople2(Integer.valueOf(projectId),Integer.valueOf(orgId),user.getId(),StringHelper.formattString(String.valueOf(mapWhere.get("contactsName"))));
                for(PeopleVo peopleVo:peopleVoList)
                {
                    ContactsUserVo contactsUserVo=new ContactsUserVo();
                    contactsUserVo.setSource(source);
                    contactsUserVo.setSex(peopleVo.getSexVo().getCode());
                    contactsUserVo.setContactsId(peopleVo.getId());
                    contactsUserVo.setContactsName(peopleVo.getName());
                    contactsUserVo.setContactsDept(orgName);
                    contactsUserVo.setTelPhone(peopleVo.getTelPhone());
                    contactsUserVo.setJob(peopleVo.getJobVo().getName());
                    contactsUserVoList.add(contactsUserVo);
                }
                return contactsUserVoList;
            }

        }
        else
        {//业主的

            Example example = new Example(PeoplePo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("projInfoId",orgId);//组织ID
            if(StringHelper.isNotNullAndEmpty(StringHelper.formattString(String.valueOf(mapWhere.get("contactsName")))))
            {
                criteria.andEqualTo("contactsName",StringHelper.formattString(String.valueOf(mapWhere.get("contactsName"))));//联系人姓名
            }
            List<PeoplePo> peoplePoList = peopleService.selectByExample(example);
            List<ContactsUserVo> contactsUserVoList=Lists.newArrayList();
            for(PeoplePo peoplePo:peoplePoList)
            {
                ContactsUserVo contactsUserVo=new ContactsUserVo();
                contactsUserVo.setSource(source);
                contactsUserVo.setSex(peoplePo.getSex());
                contactsUserVo.setContactsId(peoplePo.getId());
                contactsUserVo.setContactsName(peoplePo.getName());
                contactsUserVo.setContactsDept(orgName);
                contactsUserVo.setTelPhone(peoplePo.getTelPhone());
                contactsUserVo.setJob(szxmCommonUtil.getDictionaryName(psotionDictMap, peoplePo.getJob()));
                contactsUserVoList.add(contactsUserVo);
            }
            return contactsUserVoList;
        }
        return null;
    }

    @Override public List<ContactsUserVo> queryOnePeople(String searcher,Integer projectId)
    {
        List<ContactsUserVo> contactsUserVoList=Lists.newArrayList();
        //查询项目团队下 和 人员表中匹配 该人员的数据，并根据当前登录用户身份，是否有权限看到该用户。
        UserInfo user=commUserService.getLoginUser();
        //查询项目团队的用户
        List<GeneralVo> roles =szxmCommonUtil.queryTeamRoles(projectId,user.getId());
        List<String> roleCodes= ListUtil.toValueList(roles, "code", String.class, true);
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
        {//非业主的，只查所属标段 专业 相同的业主代表人 和 人员表里的数据
            contactsUserVoList=appAddressBookMapper.selectFyzUnionAll(projectId,user.getId(),searcher);
        }
        else
        {//业主 查询 项目团队中 业主的人  和 人员表中的数据
            contactsUserVoList=appAddressBookMapper.selectYzUnionAll(projectId,searcher);
        }
        return contactsUserVoList;
    }
}
