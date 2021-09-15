package com.wisdom.acm.szxm.controller.rygl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.officeUtils.ExcelError;
import com.wisdom.acm.szxm.common.redisUtils.RedisUtil;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryDetailAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryDetailUpdateForm;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryUpdateForm;
import com.wisdom.acm.szxm.service.rygl.PeopleEntryDetailService;
import com.wisdom.acm.szxm.service.rygl.PeopleEntryService;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryDetailVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ResourceUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 人员进退场
 */
@Controller
@RestController
@RequestMapping("rygl/peopleEntry")
public class PeopleEntryController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private PeopleEntryService peopleEntryService;

    @Autowired
    private PeopleEntryDetailService peopleEntryDetailService;

    @Autowired
    private RedisUtil redisUtil;


    @GetMapping(value = "/getPeopleEntryList/{pageSize}/{currentPageNum}")
    public ApiResult getPeopleEntryList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合

        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        PageInfo<PeopleEntryVo> queryPeopleEntryVoList = peopleEntryService.selectPeopleEntryList(mapWhere, sectionList, pageSize, currentPageNum);
        return new TableResultResponse(queryPeopleEntryVoList);
    }

    @GetMapping(value = "/getWorkersList")
    public ApiResult getWorkersList(@RequestParam Map<String, Object> mapWhere) {
        String projectId = String.valueOf(mapWhere.get("projectId"));
        if (StringHelper.isNullAndEmpty(projectId)) {
            //项目ID 不能为空
            throw new BaseException("项目ID不能为空!");
        }
        return ApiResult.success(peopleEntryService.getWorkersList(mapWhere));
    }


    @GetMapping(value = "/getFlowPeopleEntryList")
    public ApiResult getFlowPeopleEntryList(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");//获取项目ID
        List<PeopleEntryVo> peopleEntryVoList;
        if (ObjectUtils.isEmpty(ids)) {
            String projectId = request.getParameter("projectId");//获取项目ID
            String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
            List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
            peopleEntryVoList = peopleEntryService.selectFlowPeopleEntryList(mapWhere, sectionList);
        } else {
            peopleEntryVoList = peopleEntryService.selectFlowPeopleEntryList(mapWhere, null);
        }
        return ApiResult.success(peopleEntryVoList);
    }

    @GetMapping(value = "/getPeopleEntry/{id}")
    public ApiResult getPeopleEntry(@PathVariable("id") Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(peopleEntryService.selectByPeopleEntryId(id));
    }


    @PostMapping(value = "/addPeopleEntry")
    public ApiResult addPeopleEntry(@RequestBody @Valid PeopleEntryAddForm peopleEntryAddForm) {
        PeopleEntryVo peopleEntryVo = peopleEntryService.addPeopleEntry(peopleEntryAddForm);
        return ApiResult.success(peopleEntryVo);
    }


    @PutMapping(value = "/updatePeopleEntry")
    public ApiResult updatePorjInfo(@RequestBody @Valid PeopleEntryUpdateForm projEntryUpdateForm) {
        PeopleEntryVo peopleEntryVo = peopleEntryService.updatePeopleEntry(projEntryUpdateForm);
        return ApiResult.success(peopleEntryVo);
    }


    @DeleteMapping(value = "/deletePeopleEntry")
    public ApiResult deletePorjInfo(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        peopleEntryService.deletePeopleEntry(ids);
        return ApiResult.success();
    }

    @GetMapping(value = "/{entryId}/getPeopleEntryDetailList/{pageSize}/{currentPageNum}")
    public ApiResult getPeopleList(@RequestParam Map<String, Object> mapWhere, @PathVariable("entryId") Integer entryId, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {

        if (ObjectUtils.isEmpty(entryId)) {
            return ApiResult.result(777, "id不能为空");
        }
        PageInfo<PeopleEntryDetailVo> queryPeopleEntryDetailVoList = peopleEntryDetailService.selectPeopleEntryDetailList(mapWhere, entryId, pageSize, currentPageNum);
        return new TableResultResponse(queryPeopleEntryDetailVoList);
    }


    @PostMapping(value = "/addPeopleEntryDetail")
    public ApiResult addPeople(@RequestBody @Valid PeopleEntryDetailAddForm peopleEntryDetailAddForm) {
        PeopleEntryDetailVo peopleEntryDetailVo = peopleEntryDetailService.addPeopleEntryDetail(peopleEntryDetailAddForm);
        return ApiResult.success(peopleEntryDetailVo);
    }


    @PutMapping(value = "/updatePeopleEntryDetail")
    public ApiResult updatePeople(@RequestBody @Valid PeopleEntryDetailUpdateForm peopleEntryDetailUpdateForm) {
        PeopleEntryDetailVo peopleEntryDetailVo = peopleEntryDetailService.updatePeopleEntryDetail(peopleEntryDetailUpdateForm);
        return ApiResult.success(peopleEntryDetailVo);
    }


    @DeleteMapping(value = "/deletePeopleEntryDetail")
    public ApiResult deletePeople(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        peopleEntryDetailService.deletePeopleEntryDetail(ids);
        return ApiResult.success();
    }

    /**
     * 上传人员进退场人员信息Excel
     *
     * @return
     */
    @PostMapping(value = {"/uploadPeoEntryDetailFile"})
    public ApiResult uploadPeoEntryDetailFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        if (StringHelper.isNullAndEmpty(String.valueOf(mRequest.getParameter("enTryId"))))
            throw new BaseException("进退场主键ID不能为空!");
        if (file.getSize() != 0 && !"".equals(file.getName())) {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("enTryId", mRequest.getParameter("enTryId"));
            paramMap.put("projectId", mRequest.getParameter("projectId"));
            paramMap.put("sectionId", mRequest.getParameter("sectionId"));
            String errorId = peopleEntryDetailService.uploadPeoEntryDetailFile(file, paramMap);
            if (StringHelper.isNotNullAndEmpty(errorId)) {
                return ApiResult.result(1007, errorId);
            }
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @PostMapping(value = {"/dowPeopTemp"})
    public void dowPeopTemp(HttpServletResponse response) {
        try {
            InputStream inputStream = ResourceUtil.findResoureFile("template/peoEntryDetail_Temp.xlsx");
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode("人员进场导入模板.xlsx", "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new BaseException("导出人员模板出错!");
        }
    }

    @PostMapping(value = {"/dowErrorWb"})
    public void dowErrorWb(HttpServletResponse response) {
        try {
            String errorId = request.getParameter("errorId");
            if (StringHelper.isNullAndEmpty(errorId))
                throw new BaseException("errorId不能为空!");
            ExcelError excelError = redisUtil.getObjectValue(errorId, ExcelError.class);
            if (ObjectUtils.isEmpty(excelError))
                throw new BaseException("导出错误日志出错!");
            InputStream inputStream = ResourceUtil.findResoureFile("template/errorTemp.xlsx");
            Workbook workbook = szxmCommonUtil.exportExcelError(excelError, inputStream);
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode("错误日志.xlsx", "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
            // 获取导出数据的集合
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new BaseException("导出错误日志出错!");
        }
    }

    @PostMapping(value = "/addOutPeoEntryDe")
    public ApiResult addOutPeoEntryDe(@RequestBody @Valid List<PeopleEntryDetailAddForm> peopleEntryDetailAddForms) {
        List<PeopleEntryDetailVo> peopleEntryDetailVos = peopleEntryDetailService.addPeopleEntryDetail(peopleEntryDetailAddForms);
        return ApiResult.success(peopleEntryDetailVos);
    }


    /**
     *查询人员进退场信息
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/getSectionPeopleEntryList/{userId}/{pageSize}/{currentPageNum}")
    public ApiResult getSectionPeopleEntryList(@RequestParam Map<String, Object> mapWhere,@PathVariable("userId") Integer userId, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        String projInfoId = request.getParameter("projInfoId");//项目基础信息id
        String projectId = request.getParameter("projectId");//获取项目ID
       // String userId = request.getParameter("userId");//userId

        if (ObjectUtils.isEmpty(projectId))
        {
            return ApiResult.result(777,"projectId不能为空");
        }
        if (ObjectUtils.isEmpty(projInfoId)) {
            return ApiResult.result(1001, "projInfoId不能为空");
        }
        if (ObjectUtils.isEmpty(userId)) {
            return ApiResult.result(1001, "userId不能为空");
        }
        mapWhere.put("userId",userId);
        mapWhere.put("projInfoId",projInfoId);
        mapWhere.put("projectId",projectId);
        //List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        PageInfo<PeopleEntryVo> queryPeopleEntryVoList = peopleEntryService.selectSectionPeopleEntryList(mapWhere, pageSize, currentPageNum);
        return new TableResultResponse(queryPeopleEntryVoList);
    }
}
