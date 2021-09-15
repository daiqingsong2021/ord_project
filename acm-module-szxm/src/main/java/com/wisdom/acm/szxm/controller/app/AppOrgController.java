package com.wisdom.acm.szxm.controller.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.po.rygl.TsPlatPo;
import com.wisdom.acm.szxm.po.rygl.WarnHousePo;
import com.wisdom.acm.szxm.service.rygl.ProjInfoService;
import com.wisdom.acm.szxm.service.rygl.TsPlatService;
import com.wisdom.acm.szxm.service.rygl.WarnHouseService;
import com.wisdom.acm.szxm.vo.app.OrgInfoVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleChangeVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.feign.sys.CommSectionService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import com.wisdom.base.common.vo.sys.SectionProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 单位信息控制器
 */
@RestController
@RequestMapping("app/org")
public class AppOrgController
{

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private ProjInfoService projInfoService;
    /**
     * 仓库信息Service
     */
    @Autowired
    private WarnHouseService warnHouseService;

    @Autowired
    private TsPlatService tsPlatService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private org.dozer.Mapper dozerMapper;

    @Autowired
    private HttpServletRequest request;


    /**
     * 查询组织信息
     * @param mapWhere orgName 组织名称
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryOrgInfo/{pageSize}/{currentPageNum}")
    public ApiResult queryOrgInfo(@RequestParam Map<String, Object> mapWhere,
            @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        if(StringHelper.isNullAndEmpty(projectId))
        {
            throw new BaseException("项目ID不能为空!");
        }
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, "");//查询能看到的所有标段List
//        UserInfo userInfo=commUserService.getLoginUser();
//        List<SectionProjectVo> sectionProjectVos=commSectionService.querySectioinListByUserId(userInfo.getId());
//        if(ObjectUtils.isEmpty(sectionProjectVos))
//        {
//            PageInfo<OrgInfoVo> pageInfo = new PageInfo<OrgInfoVo>(Lists.newArrayList());
//            return new TableResultResponse(pageInfo);
//        }
//        Map<Integer,SectionProjectVo> sectionProjectVoMap= ListUtil.listToMap(sectionProjectVos,"id",Integer.class);
        Example example = new Example(ProjInfoPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId","0");//只查顶层节点
        criteria.andIn("sectionId",sectionList);
        if(StringHelper.isNotNullAndEmpty(StringHelper.formattString(String.valueOf(mapWhere.get("orgName")))))
        {
            criteria.andLike("orgName",StringHelper.formattString(String.valueOf(mapWhere.get("orgName"))));//只查顶层节点
        }
        PageHelper.startPage(currentPageNum, pageSize);
        List<ProjInfoPo> projInfoPos = projInfoService.selectByExample(example);
        PageInfo<ProjInfoPo> pageInfo_ = new PageInfo<ProjInfoPo>(projInfoPos);
        List<OrgInfoVo> orgInfoVoList=Lists.newArrayList();
        if(!ObjectUtils.isEmpty(pageInfo_.getList()))
        {
            PlanProjectVo projectVo=commPlanProjectService.getProject(Integer.valueOf(projectId));
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for(ProjInfoPo projInfoPo:pageInfo_.getList())
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

        }
        PageInfo<OrgInfoVo> pageInfo = new PageInfo<OrgInfoVo>(orgInfoVoList);
        szxmCommonUtil.cooyPageInfo(pageInfo_,pageInfo);
        return new TableResultResponse(pageInfo);
    }

    /***
     * 查询仓库和调试平台 根据projInfoId
     * @param orgId
     * @return
     */
    @GetMapping(value = "{orgId}/queryWhAndTsPlat")
    public ApiResult queryWhAndTsPlat(@PathVariable("orgId") Integer orgId)
    {
        Example example = new Example(WarnHousePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projInfoId",orgId);
        List<WarnHousePo> warnHousePoList=warnHouseService.selectByExample(example);

        example.clear();
        example = new Example(TsPlatPo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projInfoId",orgId);
        List<TsPlatPo> tsPlatPoList= tsPlatService.selectByExample(example);

        Map<String,Object> returnMap=Maps.newHashMap();
        returnMap.put("warnHouse",warnHousePoList);
        returnMap.put("tsPlat",tsPlatPoList);
        return ApiResult.success(returnMap);
    }

    /**
     * 查询项目地图
     * @param orgId 组织ID
     * @return
     */
    @GetMapping(value = "/{orgId}/queryOrgMap")
    public ApiResult queryOrgInfo(@PathVariable("orgId") Integer orgId)
    {
        if (ObjectUtils.isEmpty(orgId))
        {
            throw new BaseException("单位ID不能为空!");
        }
        //根据OrgId 查询仓库地址
        Example example = new Example(WarnHousePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projInfoId", orgId);
        List<WarnHousePo> warnHousePoList = warnHouseService.selectByExample(example);
        if (warnHousePoList == null)
            warnHousePoList = Lists.newArrayList();
        //根据OrgId 查询调试平台
        example.clear();
        example = new Example(WarnHousePo.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("projInfoId", orgId);
        List<TsPlatPo> tsPlatPoList = tsPlatService.selectByExample(example);
        if (tsPlatPoList == null)
            tsPlatPoList = Lists.newArrayList();
        //查询项目部地址
        ProjInfoPo projInfoPo=projInfoService.selectById(orgId);

        //定义返回数据格式
        Map<String, Object> returnMap = Maps.newHashMap();
        List<String> returnNameList = Lists.newArrayList();
        returnNameList.add(StringHelper.formattString(projInfoPo.getOrgName()));
        List<String> returnAddressList = Lists.newArrayList();
        returnAddressList.add(StringHelper.formattString(projInfoPo.getProjUnitAddress()));
        List<String> returnTypeList = Lists.newArrayList();
        returnTypeList.add("proj");
        returnMap.put("name", returnNameList);
        returnMap.put("address", returnAddressList);
        returnMap.put("type", returnTypeList);
        if (warnHousePoList.size() >= tsPlatPoList.size())
        {
            for (int i = 0; i < warnHousePoList.size(); i++)
            {
                WarnHousePo warnHousePo = warnHousePoList.get(i);
                returnNameList.add(StringHelper.formattString(warnHousePo.getName()));
                returnAddressList.add(StringHelper.formattString(warnHousePo.getAddress()));
                returnTypeList.add("warnHouse");//仓库
                if ((i+1)<=tsPlatPoList.size() && !ObjectUtils.isEmpty(tsPlatPoList.get(i)))
                {
                    returnNameList.add(StringHelper.formattString(tsPlatPoList.get(i).getName()));
                    returnAddressList.add(StringHelper.formattString(tsPlatPoList.get(i).getAddress()));
                    returnTypeList.add("tsPlat");//调试平台
                }
            }
        }
        else
        {
            for (int i = 0; i < tsPlatPoList.size(); i++)
            {
                TsPlatPo tsPlatPo = tsPlatPoList.get(i);
                returnNameList.add(StringHelper.formattString(tsPlatPo.getName()));
                returnAddressList.add(StringHelper.formattString(tsPlatPo.getAddress()));
                returnTypeList.add("tsPlat");//调试平台
                if ((i+1)<=warnHousePoList.size() && !ObjectUtils.isEmpty(warnHousePoList.get(i)))
                {
                    returnNameList.add(StringHelper.formattString(warnHousePoList.get(i).getName()));
                    returnAddressList.add(StringHelper.formattString(warnHousePoList.get(i).getAddress()));
                    returnTypeList.add("warnHouse");//仓库
                }
            }
        }
        return ApiResult.success(returnMap);
    }


}
