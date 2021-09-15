package com.wisdom.acm.szxm.controller.rygl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.rygl.*;
import com.wisdom.acm.szxm.service.rygl.*;
import com.wisdom.acm.szxm.vo.rygl.*;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommFileService;
import com.wisdom.base.common.feign.CommOrgService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ResourceUtil;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织信息管理控制器
 */
@Controller
@RestController
@RequestMapping("rygl/projInfo")
public class ProjInfoController
{

    /**
     * 项目组织信息Service
     */
    @Autowired
    private ProjInfoService projInfoService;

    /**
     * 人员信息Service
     */
    @Autowired
    private PeopleService peopleService;

    /**
     * 仓库信息Service
     */
    @Autowired
    private WarnHouseService warnHouseService;

    /**
     * 调试平台信息Service
     */
    @Autowired
    private TsPlatService tsPlatService;

    /**
     * 分包作业队服务
     */
    @Autowired
    private FbzydService fbzydService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    /**
     * 用户基本信息服务
     */
    @Autowired
    private CommUserService commUserService;

    @Autowired
    private CommFileService commFileService;

    @Autowired
    private ZkService zkService;

    @Autowired
    private CommOrgService commOrgService;

    /**
     * 查询项目组织信息
     *
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getProjInfoList")
    public ApiResult getProjInfoList(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合

        //UserInfo userInfo = commUserService.getLoginUser();//获取用户信息
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        return ApiResult.success(projInfoService.selectProjInfoList(mapWhere, sectionList));
    }

    @GetMapping(value = "/getOrgPeopleList")
    public ApiResult getOrgPeopleList(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合

        //UserInfo userInfo = commUserService.getLoginUser();//获取用户信息
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        return ApiResult.success(projInfoService.getOrgPeopleList(mapWhere, sectionList));
    }

    @GetMapping(value = "/getPeopleInfos")
    public ApiResult getPeopleInfos(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = String.valueOf(mapWhere.get("projectId"));//获取项目ID
        String projInfoId = String.valueOf(mapWhere.get("projInfoId"));//获取基础信息ID

        if(StringHelper.isNullAndEmpty(projectId) || StringHelper.isNullAndEmpty(projInfoId))
        {
            throw new BaseException("项目ID 或 基础信息ID不能为空");
        }
        mapWhere.put("projInfoId", StringHelper.formattString(projInfoId));
        mapWhere.put("projectId", StringHelper.formattString(projectId));
        mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
        return ApiResult.success(peopleService.selectPeople(mapWhere));
    }


    @GetMapping(value = "/getProjInfo/{id}")
    public ApiResult getProjInfo(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(projInfoService.selectByProjInfoId(id));
    }

    /**
     * 增加项目组织信息
     * @param projInfoAddForm
     * @return
     */
    @PostMapping(value = "/addPorjInfo")
    public ApiResult addPorjInfo(@RequestBody @Valid ProjInfoAddForm projInfoAddForm)
    {
        ProjInfoVo projInfoVo = projInfoService.addPorjInfo(projInfoAddForm);
        return ApiResult.success(projInfoVo);
    }

    /**
     * 更新项目组织信息
     * @param projInfoUpdateForm
     * @return
     */
    @PutMapping(value = "/updatePorjInfo")
    public ApiResult updatePorjInfo(@RequestBody @Valid ProjInfoUpdateForm projInfoUpdateForm)
    {
        ProjInfoVo projInfoVo = projInfoService.updatePorjInfo(projInfoUpdateForm);
        return ApiResult.success(projInfoVo);
    }

    /**
     * 删除项目组织信息
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deletePorjInfo")
    public ApiResult deletePorjInfo(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        projInfoService.deletePorjInfo(ids.get(0));
        return ApiResult.success();
    }

    @GetMapping(value = "/{projInfoId}/getPeopleList/{pageSize}/{currentPageNum}")
    public ApiResult getPeopleList(@RequestParam Map<String, Object> mapWhere,@PathVariable("projInfoId")Integer projInfoId,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {

        if (ObjectUtils.isEmpty(projInfoId))
        {
            return ApiResult.result(777,"id不能为空");
        }
        PageInfo<PeopleVo> queryPeopleVoList=peopleService.selectPeopleList(mapWhere,projInfoId,pageSize,currentPageNum);
        return new TableResultResponse(queryPeopleVoList);
    }

    /**
     * 增加项目组织信息
     * @return
     */
    @PostMapping(value = "/addPeople")
    public ApiResult addPeople(@RequestBody @Valid PeopleAddForm peopleAddForm)
    {
        PeopleVo peopleVo = peopleService.addPeople(peopleAddForm);
        return ApiResult.success(peopleVo);
    }

    /**
     * 更新项目组织信息
     * @return
     */
    @PutMapping(value = "/updatePeople")
    public ApiResult updatePeople(@RequestBody @Valid PeopleUpdateForm peopleUpdateForm)
    {
        PeopleVo peopleVo = peopleService.updatePeople(peopleUpdateForm);
        return ApiResult.success(peopleVo);
    }

    /**
     * 删除项目组织信息
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deletePeople")
    public ApiResult deletePeople(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        peopleService.deletePeople(ids);
        return ApiResult.success();
    }

    /**
     * 查询仓库信息
     * @param projInfoId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/{projInfoId}/getWarnHouseList/{pageSize}/{currentPageNum}")
    public ApiResult getWarnHouseList(@PathVariable("projInfoId")Integer projInfoId,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {

        if (ObjectUtils.isEmpty(projInfoId))
        {
            return ApiResult.result(777,"id不能为空");
        }
        PageInfo<WarnHouseVo> queryWarnHouseVoList=warnHouseService.selectWarnHouseList(projInfoId,pageSize,currentPageNum);
        return new TableResultResponse(queryWarnHouseVoList);
    }

    @GetMapping(value = "/getWarnHouseListByProjId")
    public ApiResult getWarnHouseListByProjId(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = String.valueOf(mapWhere.get("projectId"));//获取项目ID
        String sectionIds = String.valueOf(mapWhere.get("sectionIds"));

        if(StringHelper.isNullAndEmpty(projectId))
        {
            throw new BaseException("项目ID不能为空");
        }

        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);
        mapWhere.put("sectionList", sectionList);
        List<WarnHouseVo> queryWarnHouseVoList=warnHouseService.selectWarnHouseListByProjId(mapWhere);
        return ApiResult.success(queryWarnHouseVoList);
    }

    /**
     * 增加仓库信息
     * @return
     */
    @PostMapping(value = "/addWarnHouse")
    public ApiResult addWarnHouse(@RequestBody @Valid WarnHouseAddForm warnHouseAddForm)
    {
        WarnHouseVo warnHouseVo = warnHouseService.addWarnHouse(warnHouseAddForm);
        return ApiResult.success(warnHouseVo);
    }

    /**
     * 更新项目组织信息
     * @return
     */
    @PutMapping(value = "/updateWarnHouse")
    public ApiResult updateWarnHouse(@RequestBody @Valid WarnHouseUpdateForm warnHouseUpdateForm)
    {
        WarnHouseVo warnHouseVo = warnHouseService.updateWarnHouse(warnHouseUpdateForm);
        return ApiResult.success(warnHouseVo);
    }

    /**
     * 删除仓库信息
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteWarnHouse")
    public ApiResult deleteWarnHouse(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        warnHouseService.deleteWarnHouse(ids);
        return ApiResult.success();
    }



    /**
     * 查询调试平台信息
     * @param projInfoId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/{projInfoId}/getTsPlatList/{pageSize}/{currentPageNum}")
    public ApiResult getTsPlatList(@PathVariable("projInfoId")Integer projInfoId,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {

        if (ObjectUtils.isEmpty(projInfoId))
        {
            return ApiResult.result(777,"id不能为空");
        }
        PageInfo<TsPlatVo> queryTsPlatVoList=tsPlatService.selectTsPlatList(projInfoId,pageSize,currentPageNum);
        return new TableResultResponse(queryTsPlatVoList);
    }

    /**
     * 增加调试平台信息
     * @return
     */
    @PostMapping(value = "/addTsPlat")
    public ApiResult addTsPlat(@RequestBody @Valid TsPlatAddForm tsPlatAddForm)
    {
        TsPlatVo tsPlatVo = tsPlatService.addTsPlat(tsPlatAddForm);
        return ApiResult.success(tsPlatVo);
    }

    /**
     * 更新调试平台信息
     * @return
     */
    @PutMapping(value = "/updateTsPlat")
    public ApiResult updateTsPlat(@RequestBody @Valid TsPlatUpdateForm tsPlatUpdateForm)
    {
        TsPlatVo tsPlatVo = tsPlatService.updateTsPlat(tsPlatUpdateForm);
        return ApiResult.success(tsPlatVo);
    }

    /**
     * 删除调试平台信息
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteTsPlat")
    public ApiResult deleteTsPlat(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        tsPlatService.deleteTsPlat(ids);
        return ApiResult.success();
    }

    /**
     * 外部单位增加 项目组织信息（单位类型 非1）
     * @param sgPojectTeamVo
     * @return 返回新增的项目组织信息主键ID
     */
    @PostMapping(value = "/addPorjInfoMsg")
    public ApiResult addPorjInfoMsg(@RequestBody ProjectTeamVo sgPojectTeamVo)
    {
        Integer id= projInfoService.porjInfoMsg(sgPojectTeamVo);
        return ApiResult.success(id);
    }

    /**
     * 外部单位修改 项目组织信息（单位类型 非1）
     * @return
     */
    @PutMapping(value = "/updatePorjInfoMsg")
    public ApiResult updatePorjInfoMsg(@RequestBody ProjectTeamVo sgPojectTeamVo)
    {
        Integer id= projInfoService.updatePorjInfoMsg(sgPojectTeamVo);
        return ApiResult.success(id);
    }

    @DeleteMapping(value = "/deletePorjInfoMsg/{sysOrgid}")
    public ApiResult deletePorjInfoMsg(@PathVariable("sysOrgid")Integer sysOrgid)
    {
        Integer id= projInfoService.deletePorjInfoMsg(sysOrgid);
        return ApiResult.success(id);
    }


    @PostMapping(value = {"/dowPeopTemp"})
    public void dowPeopTemp(HttpServletResponse response)
    {
        try
        {
            InputStream inputStream= ResourceUtil.findResoureFile("template/people_Temp.xlsx");
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode("人员导入模板.xlsx","UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;Filename="+fileName);
            IOUtils.copy(inputStream,response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch (Exception e)
        {
            throw new BaseException("导出人员模板出错!");
        }
    }

    @PostMapping(value = {"/uploadPeopleFile"})
    public ApiResult uploadPeopleFile(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws
            IOException
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        if(StringHelper.isNullAndEmpty(String.valueOf(mRequest.getParameter("projInfoId"))))
            throw new BaseException("组织信息ID不能为空!");
        if (file.getSize() != 0 && !"".equals(file.getName()))
        {
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("projInfoId",mRequest.getParameter("projInfoId"));
            paramMap.put("projectId",mRequest.getParameter("projectId"));
            paramMap.put("sectionId",mRequest.getParameter("sectionId"));
            String errorId= peopleService.uploadPeopleFile(file,paramMap);
            if(StringHelper.isNotNullAndEmpty(errorId))
            {
                return ApiResult.result(1007,errorId);
            }
            else return ApiResult.success();
        }
        return ApiResult.success();
    }

    /**
     * 文件 业务绑定
     * @param bindFileForm
     * @return
     * @throws IOException
     */
    @PostMapping(value = {"/bindFile"})
    public ApiResult bindFile(@RequestBody @Valid BindFileForm bindFileForm) throws IOException
    {
        commFileService.addFileRelation(bindFileForm.getBizId(),bindFileForm.getBizType(),bindFileForm.getFileIds());
        return ApiResult.success();
    }

    /**
     * 增加分包作业队信息
     * @return
     */
    @PostMapping(value = "/addFbzyd")
    public ApiResult addFbzyd(@RequestBody @Valid FbzydAddForm fbzydAddForm)
    {
        FbzydVo fbzydVo = fbzydService.addFbzyd(fbzydAddForm);
        return ApiResult.success(fbzydVo);
    }

    /**
     * 更新分包作业队信息
     * @return
     */
    @PutMapping(value = "/updateFbzyd")
    public ApiResult updateFbzyd(@RequestBody @Valid FbzydUpdateForm fbzydUpdateForm)
    {
        FbzydVo fbzydVo = fbzydService.updateFbzyd(fbzydUpdateForm);
        return ApiResult.success(fbzydVo);
    }

    /**
     * 删除分包作业队信息
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteFbzyd")
    public ApiResult deleteFbzyd(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        fbzydService.deleteFbzyd(ids);
        return ApiResult.success();
    }

    /**
     * 查询调试平台信息
     * @param projInfoId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/{projInfoId}/getFbzydList/{pageSize}/{currentPageNum}")
    public ApiResult getFbzydList(@PathVariable("projInfoId")Integer projInfoId,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {

        if (ObjectUtils.isEmpty(projInfoId))
        {
            return ApiResult.result(777,"id不能为空");
        }
        PageInfo<FbzydVo> queryFbzydVoList=fbzydService.selectFbzydList(projInfoId,pageSize,currentPageNum);
        return new TableResultResponse(queryFbzydVoList);
    }

    @PostMapping(value = {"/dowWarnHouseTemp"})
    public void dowWarnHouseTemp(HttpServletResponse response)
    {
        try
        {
            InputStream inputStream= ResourceUtil.findResoureFile("template/warnHouse.xlsx");
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode("仓库导入模板.xlsx","UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;Filename="+fileName);
            IOUtils.copy(inputStream,response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch (Exception e)
        {
            throw new BaseException("导出仓库模板出错!");
        }
    }

    /**
     * 上传仓库信息Excel
     * @return
     */
    @PostMapping(value = {"/uploadWarnHouseFile"})
    public ApiResult uploadWarnHouseFile(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        if(StringHelper.isNullAndEmpty(String.valueOf(mRequest.getParameter("projInfoId"))))
            throw new BaseException("基础信息ID不能为空!");
        if (file.getSize() != 0 && !"".equals(file.getName()))
        {
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("projInfoId",mRequest.getParameter("projInfoId"));
            paramMap.put("projectId",mRequest.getParameter("projectId"));
            paramMap.put("sectionId",mRequest.getParameter("sectionId"));
            String errorId= warnHouseService.uploadWarnHouseFile(file,paramMap);
            if(StringHelper.isNotNullAndEmpty(errorId))
            {
                return ApiResult.result(1007,errorId);
            }
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @PostMapping(value = {"/dowTsPlatTemp"})
    public void dowTsPlatTemp(HttpServletResponse response)
    {
        try
        {
            InputStream inputStream= ResourceUtil.findResoureFile("template/tsPlat.xlsx");
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode("调试平台导入模板.xlsx","UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;Filename="+fileName);
            IOUtils.copy(inputStream,response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch (Exception e)
        {
            throw new BaseException("导出调试平台模板出错!");
        }
    }

    /**
     * 调试平台信息Excel
     * @return
     */
    @PostMapping(value = {"/uploadTsPlatFile"})
    public ApiResult uploadTsPlatFile(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        if(StringHelper.isNullAndEmpty(String.valueOf(mRequest.getParameter("projInfoId"))))
            throw new BaseException("基础信息ID不能为空!");
        if (file.getSize() != 0 && !"".equals(file.getName()))
        {
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("projInfoId",mRequest.getParameter("projInfoId"));
            paramMap.put("projectId",mRequest.getParameter("projectId"));
            paramMap.put("sectionId",mRequest.getParameter("sectionId"));
            String errorId= tsPlatService.uploadTsPlatFile(file,paramMap);
            if(StringHelper.isNotNullAndEmpty(errorId))
            {
                return ApiResult.result(1007,errorId);
            }
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @PostMapping(value = {"/dowFbzydTemp"})
    public void dowFbzydTemp(HttpServletResponse response)
    {
        try
        {
            InputStream inputStream= ResourceUtil.findResoureFile("template/Fbzyd.xlsx");
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode("分包作业队导入模板.xlsx","UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;Filename="+fileName);
            IOUtils.copy(inputStream,response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch (Exception e)
        {
            throw new BaseException("导出调试平台模板出错!");
        }
    }


    /**
     * 上传分包作业队信息Excel
     * @return
     */
    @PostMapping(value = {"/uploadFbzydFile"})
    public ApiResult uploadFbzydFile(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        if(StringHelper.isNullAndEmpty(String.valueOf(mRequest.getParameter("projInfoId"))))
            throw new BaseException("基础信息ID不能为空!");
        if (file.getSize() != 0 && !"".equals(file.getName()))
        {
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("projInfoId",mRequest.getParameter("projInfoId"));
            paramMap.put("projectId",mRequest.getParameter("projectId"));
            paramMap.put("sectionId",mRequest.getParameter("sectionId"));
            String errorId= fbzydService.uploadFbzydFile(file,paramMap);
            if(StringHelper.isNotNullAndEmpty(errorId))
            {
                return ApiResult.result(1007,errorId);
            }
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @PostMapping(value = "/addZkPorjInfo")
    public ApiResult addPorjInfo()
    {
        Map<String,Object> mapWhere=Maps.newHashMap();
        mapWhere.put("projectId",1081418);
        List<ProjInfoVo> projInfoVos=projInfoService.selectProjInfoList(mapWhere, Lists.newArrayList());
        zkService.addOrUpdateZkOrg(projInfoVos);
        return ApiResult.success();
    }

    /**
     * 根据项目查询组织信息
     *
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getOrgInfoByProjectId")
    public ApiResult getOrgInfoByProjectId(@RequestParam Map<String, Object> mapWhere) {
        String projectId = request.getParameter("projectId");//获取项目ID
        return ApiResult.success(projInfoService.getOrgInfoByProjectId(Integer.valueOf(projectId)));
    }



    /**
     * 查询项目参建单位信息
     *
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getParticipateUnitList")
    public ApiResult getParticipateUnitList(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        String departmentId = request.getParameter("departmentId");//部门ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
        String speciality = request.getParameter("speciality");//专业

        if (ObjectUtils.isEmpty(projectId))
        {
            return ApiResult.result(1001, "projectId不能为空");
        }

        List<DepartmentSectionVo> departmentSectionVoList =new ArrayList<DepartmentSectionVo>();
        //只有项目查看所的部门 标段为空
        if(StringHelper.isNotNullAndEmpty(projectId.toString()) && StringHelper.isNullAndEmpty(departmentId))
        {

            Map <String, Object> mapParams =new HashMap<String, Object>();
            mapParams.put("projectId",projectId);
            //查找部门
            List<SysOrgTreeVo> orglists= projInfoService.getOrgInfoByProjectId(Integer.valueOf(projectId));
            //判断末级部门是否为空
            if(!ObjectUtils.isEmpty(orglists) &&!ObjectUtils.isEmpty(orglists.get(0).getChildren()) && !ObjectUtils.isEmpty(orglists.get(0).getChildren().get(0).getChildren()))
            {
                List<SysOrgTreeVo> orgLastlists=orglists.get(0).getChildren().get(0).getChildren();
                // List<String> orgCodes=orglists.stream().map(SysOrgTreeVo::getOrgCode).collect(Collectors.toList());
                for (int i = 0; i < orgLastlists.size(); i++)
                {
                    //拼装部门下的section对象信息
                    DepartmentSectionVo departmentSectionVo=new DepartmentSectionVo();
                    //Integer orgCode=Integer.valueOf(orglists.get(i).getOrgCode());
                    mapParams.put("departmentId",orgLastlists.get(i).getId());
                    mapParams.put("sectionIds","");
                    List<ParticipateUnitVo>  participateUnitInfoList= projInfoService.getParticipateUnitInfo(mapParams);
                    departmentSectionVo.setProjectId(Integer.valueOf(projectId));
                    departmentSectionVo.setOrgId(orgLastlists.get(i).getId().toString());
                    departmentSectionVo.setOrgCode(orgLastlists.get(i).getOrgCode());
                    departmentSectionVo.setOrgName(orgLastlists.get(i).getOrgName());
                    //section 基本信息
                    departmentSectionVo.setParticipateUnitVo(participateUnitInfoList);
                    departmentSectionVoList.add(departmentSectionVo);
                }
            }

        }

        //部门不为空  标段为空的
        else if(StringHelper.isNotNullAndEmpty(departmentId) && StringHelper.isNullAndEmpty(sectionIds))
        {
            Map <String, Object> mapParams =new HashMap<String, Object>();
            mapParams.put("projectId",projectId);
            //拼装部门下的section对象信息
            DepartmentSectionVo departmentSectionVo=new DepartmentSectionVo();
            mapParams.put("departmentId",departmentId);
            mapParams.put("sectionIds","");
            List<ParticipateUnitVo>  participateUnitInfoList= projInfoService.getParticipateUnitInfo(mapParams);
            OrgVo orgInfo= commOrgService.getOrgVoByOrgId(Integer.valueOf(departmentId));
            departmentSectionVo.setProjectId(Integer.valueOf(projectId));
            departmentSectionVo.setOrgId(departmentId);
           // departmentSectionVo.setOrgCode(orgInfo.getOrgCode());
            departmentSectionVo.setOrgName(orgInfo.getName());
            //section 基本信息
            departmentSectionVo.setParticipateUnitVo(participateUnitInfoList);
            departmentSectionVoList.add(departmentSectionVo);
        }
        //标段不为空的
        else
        {
            Map <String, Object> mapParams =new HashMap<String, Object>();
            mapParams.put("projectId",projectId);
            //拼装部门下的section对象信息
            DepartmentSectionVo departmentSectionVo=new DepartmentSectionVo();
            mapParams.put("departmentId",departmentId);
            mapParams.put("sectionIds",sectionIds);
            List<ParticipateUnitVo>  participateUnitInfoList= projInfoService.getParticipateUnitInfo(mapParams);
            OrgVo orgInfo= commOrgService.getOrgVoByOrgId(Integer.valueOf(departmentId));
            departmentSectionVo.setProjectId(Integer.valueOf(projectId));
            departmentSectionVo.setOrgId(departmentId);
            //departmentSectionVo.setOrgCode(orgInfo.getOrgCode());
            departmentSectionVo.setOrgName(orgInfo.getName());

            departmentSectionVo.setParticipateUnitVo(participateUnitInfoList);
            departmentSectionVoList.add(departmentSectionVo);
        }

        return ApiResult.success(departmentSectionVoList);
    }

    /**
     *查询参建单位标段的具体信息
     * @param id
     * @return
     */
    @GetMapping(value = "/getSectionProjInfo/{id}")
    public ApiResult getSectionProjInfo(@RequestParam Map<String, Object> mapWhere,@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(projInfoService.selectByProjInfoId(id));
    }


    /**
     * 参建单位人员信息
     * @param mapWhere
     * @param projInfoId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/{projInfoId}/getSectionPeopleList/{pageSize}/{currentPageNum}")
    public ApiResult getSectionPeopleList(@RequestParam Map<String, Object> mapWhere,@PathVariable("projInfoId")Integer projInfoId,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {

        if (ObjectUtils.isEmpty(projInfoId))
        {
            return ApiResult.result(777,"id不能为空");
        }
        PageInfo<PeopleVo> queryPeopleVoList=peopleService.selectSectionPeopleList(mapWhere,projInfoId,pageSize,currentPageNum);
        return new TableResultResponse(queryPeopleVoList);
    }


    /**
     * 查询施工作业队信息
     * @param projInfoId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/{projInfoId}/getsectionWorkteamList/{pageSize}/{currentPageNum}")
    public ApiResult getsectionWorkteamList(@RequestParam Map<String, Object> mapWhere,@PathVariable("projInfoId")Integer projInfoId,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {

        if (ObjectUtils.isEmpty(projInfoId))
        {
            return ApiResult.result(777,"id不能为空");
        }
        String dictCode = request.getParameter("dictCode");//作业队队长
        if(StringHelper.isNullAndEmpty(dictCode))
        {
            dictCode="3";
        }
        mapWhere.put("projInfoId",projInfoId);
        mapWhere.put("dictCode",dictCode);
        PageInfo<FbzydVo> queryFbzydVoList=fbzydService.selectSectionWorkteamList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryFbzydVoList);
    }



    /**
     * 查询项目组织 单位为 施工作业队 的项目信息
     *
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/{sectionId}/getsectionWorkteamInfo")
    public ApiResult getsectionWorkteamInfo(@RequestParam Map<String, Object> mapWhere,@PathVariable("sectionId")Integer sectionId)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
        String orgCategory = "5";//单位分类 字典项 施工作业队 id为5
       // String sectionIds = request.getParameter("sectionId");//获取标段ID

        Map <String, Object> mapParams =new HashMap<String, Object>();
        List<DepartmentSectionVo> departmentSectionVoList =new ArrayList<DepartmentSectionVo>();
        mapParams.put("projectId",projectId);
        //拼装部门下的section对象信息
        DepartmentSectionVo departmentSectionVo=new DepartmentSectionVo();
        mapParams.put("sectionIds",sectionId);
        //单位分类
        mapParams.put("orgCategory",orgCategory);

        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
       // PageInfo<SubcontrApprovalVo> querySubcontrApprovalVoList=subcontrApprovalService.querySubcontrApprovalList(mapWhere,projectId,sectionList,pageSize,currentPageNum);

        List<ParticipateUnitVo>  participateUnitInfoList= projInfoService.getProjectPeopleInfo(mapParams);
        departmentSectionVo.setProjectId(Integer.valueOf(projectId));
        departmentSectionVo.setParticipateUnitVo(participateUnitInfoList);
        departmentSectionVoList.add(departmentSectionVo);
        return ApiResult.success(departmentSectionVoList);
    }

}
