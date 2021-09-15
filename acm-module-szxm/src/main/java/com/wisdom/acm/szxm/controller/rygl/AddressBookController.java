package com.wisdom.acm.szxm.controller.rygl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.PageInfoUtiil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.rygl.SearchAdboForm;
import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.ProjInfoService;
import com.wisdom.acm.szxm.vo.rygl.AddressBookOrgVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.acm.szxm.vo.rygl.ProjInfoVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("rygl/addressBook")
public class AddressBookController
{

    @Autowired private SzxmCommonUtil szxmCommonUtil;

    @Autowired private ProjInfoService projInfoService;

    @Autowired private PeopleService peopleService;

    /**
     * 用户基本信息服务
     */
    @Autowired private CommUserService commUserService;


    @Autowired
    private CommPlanProjectService commPlanProjectService;


    @GetMapping(value = "/getOrgTree/{projectId}")
    public ApiResult getOrgTree(@RequestParam Map<String, Object> mapWhere,@PathVariable("projectId") String projectId)
    {
        List<AddressBookOrgVo> retList=Lists.newArrayList();
        if (StringHelper.isNotNullAndEmpty(projectId))
        {
            mapWhere.put("projectId",projectId);
            List<AddressBookOrgVo> addressBookOrgVoist = szxmCommonUtil.generateTree(projInfoService.selectAddressBookOrg(mapWhere));
            ProjInfoVo projInfo=new ProjInfoVo();
            projInfo.setId(Integer.valueOf(projectId));
            projInfo.setProjectId(Integer.valueOf(projectId));
            PlanProjectVo projectVo=commPlanProjectService.getProject(Integer.valueOf(projectId));
            AddressBookOrgVo addressBookOrgVo=new AddressBookOrgVo();
            addressBookOrgVo.setId(projectVo.getId());
            addressBookOrgVo.setOrgId(projectVo.getId());
            addressBookOrgVo.setOrgName(projectVo.getName());
            addressBookOrgVo.setType("project");
            addressBookOrgVo.setSource("0");
            addressBookOrgVo.setParentId(0);
            if(!ObjectUtils.isEmpty(addressBookOrgVoist))
                addressBookOrgVo.setChildren(addressBookOrgVoist);
            retList.add(addressBookOrgVo);
        }
        return ApiResult.success(retList);
    }

    @PostMapping(value = "/getPeople/{pageSize}/{currentPageNum}")
    public ApiResult getSpecialWorkerList(@RequestParam Map<String, Object> mapWhere,
            @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum)
    {
        List<PeopleVo> peopleVoList = Lists.newArrayList();//定义返回数据
        Integer projectId=Integer.valueOf(String.valueOf(mapWhere.get("projectId")));
        String source=String.valueOf(mapWhere.get("source"));//来源 0 项目团队 1 系统基础信息
        String type=String.valueOf(mapWhere.get("type"));//project 项目 section 标段 //org 组织
        Integer orgId=Integer.valueOf(String.valueOf(mapWhere.get("orgId")));//
        String searcher=String.valueOf(mapWhere.get("searcher"));

        UserInfo user=commUserService.getLoginUser();
        //查询项目团队的用户
        List<GeneralVo> roles =szxmCommonUtil.queryTeamRoles(projectId,user.getId());
        List<String> roleCodes=ListUtil.toValueList(roles, "code", String.class, true);
        PageHelper.startPage(currentPageNum, pageSize);
        if (ListUtil.toStr(roleCodes).indexOf("YZ_") <= -1)
        {//非业主
            //如果选择项目，查询项目团队下与自己有关的业主代表 和 所有该项目下的外部单位用户
            if("project".equals(type))
               peopleVoList=projInfoService.selectAddressBookPeopleFyz(projectId, user.getId(),StringHelper.formattString(searcher));

            //如果选择标段，则查询改标段下的组织机构用户（数据来源projInfo）
            else if("section".equals(type))
            {
                Map<String,Object> mapWhere1=Maps.newHashMap();
                mapWhere1.put("projectId",projectId);
                mapWhere1.put("sectionId",orgId);
                mapWhere1.put("searcher", StringHelper.formattString(searcher));
                peopleVoList=peopleService.selectAddressBookPeople(mapWhere1);
            }
            //如果选择组织机构，看来源，如果来源是项目团队，查orgId相关业主代表，否则projInfo下
            else if("org".equals(type))
            {
                if("0".equals(source))
                {//来源项目团队，查询改组织下的 是当前用户所处标段的业主代表
                    peopleVoList=projInfoService.selectAddressBookPeople2(projectId,orgId,user.getId(),StringHelper.formattString(searcher));
                }
                else if("1".equals(source))
                {//来源于 projInfo
                    Map<String,Object> mapWhere1=Maps.newHashMap();
                    mapWhere1.put("projectId",projectId);
                    mapWhere1.put("projInfoId",orgId);
                    mapWhere1.put("searcher", StringHelper.formattString(searcher));
                    peopleVoList=peopleService.selectAddressBookPeople(mapWhere1);
                }
            }
        }
        else
        {//为业主方的

            //如果选择项目，查询项目团队下业主的数据 和 所有该项目下的外部单位用户
            if("project".equals(type))
                peopleVoList=projInfoService.selectAddressBookPeopleYz(projectId,StringHelper.formattString(searcher));
            //如果选择标段，则查询改标段下的组织机构用户（数据来源projInfo）
            else if("section".equals(type))
            {
                Map<String,Object> mapWhere1=Maps.newHashMap();
                mapWhere1.put("projectId",projectId);
                mapWhere1.put("sectionId",orgId);
                mapWhere1.put("searcher", StringHelper.formattString(searcher));
                peopleVoList=peopleService.selectAddressBookPeople(mapWhere1);
            }
            //如果选择组织机构，看来源，如果来源是项目团队，查orgId下的项目团队用户，否则projInfo下
            else if("org".equals(type))
            {
                if("0".equals(source))
                {
                    peopleVoList=projInfoService.queryUserListByProjectTeamId(orgId,StringHelper.formattString(searcher));
                }
                else if("1".equals(source))
                {//来源于 projInfo
                    Map<String,Object> mapWhere1=Maps.newHashMap();
                    mapWhere1.put("projectId",projectId);
                    mapWhere1.put("projInfoId",orgId);
                    mapWhere1.put("searcher", StringHelper.formattString(searcher));
                    peopleVoList=peopleService.selectAddressBookPeople(mapWhere1);
                }
            }

        }
        PageInfo<PeopleVo> pageInfo = new PageInfo<PeopleVo>(peopleVoList);
        return new TableResultResponse(pageInfo);

    }

    private List<ProjectTeamUserVo> queryOwnerList(List<ProjectTeamUserVo>  allTeamUsers)
    {
        List<ProjectTeamUserVo> returnList=Lists.newArrayList();
        if (!ObjectUtils.isEmpty(allTeamUsers))
        {
            for(ProjectTeamUserVo projectTeamUserVo:allTeamUsers)
            {
                List<GeneralVo> roles = projectTeamUserVo.getRoles();
                if (!ObjectUtils.isEmpty(roles))
                {
                    for (int i = 0; i < roles.size(); i++)
                    {
                        GeneralVo generalVo = roles.get(i);
                        if ("YZ_YZDB".equals(generalVo.getCode()))
                        {
                            returnList.add(projectTeamUserVo);
                        }
                    }
                }
            }

        }
        return returnList;
    }

    public static void main(String args[])
    {
        List<String> roleNames=Lists.newArrayList();
        roleNames.add("哈哈");
        roleNames.add("你好");
        roleNames.add("小王");
        System.out.println(roleNames.indexOf("哈哈"));
    }
}
