package com.wisdom.acm.szxm.controller.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.rygl.PeopleChangeAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleChangeUpdateForm;
import com.wisdom.acm.szxm.service.rygl.PeopleChangeService;
import com.wisdom.acm.szxm.vo.rygl.PeopleChangeVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ResourceUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 人员变更
 */
@RestController
@RequestMapping("rygl/peopleChange")
public class PeopleChangeController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private PeopleChangeService peopleChangeService;

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @GetMapping(value = "/getPeopleChangeList/{pageSize}/{currentPageNum}")
    public ApiResult getPeopleChangeList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合

        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        PageInfo<PeopleChangeVo> queryPeopleChangeVoList = peopleChangeService.selectPeopleChangeList(mapWhere, sectionList, pageSize, currentPageNum);
        return new TableResultResponse(queryPeopleChangeVoList);
    }

    @GetMapping(value = "/getFlowPeopleChangeList")
    public ApiResult getFlowPeopleChangeList(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");//获取项目ID
        List<PeopleChangeVo> peopleChangeVoList;
        if (ObjectUtils.isEmpty(ids)) {
            String projectId = request.getParameter("projectId");//获取项目ID
            String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
            List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
            peopleChangeVoList = peopleChangeService.selectFlowPeopleChangeList(mapWhere, sectionList);
        } else {
            peopleChangeVoList = peopleChangeService.selectFlowPeopleChangeList(mapWhere, null);
        }
        return ApiResult.success(peopleChangeVoList);
    }


    @PostMapping(value = "/addPeopleChange")
    public ApiResult addPeopleChange(@RequestBody @Valid PeopleChangeAddForm peopleChangeAddForm) {
        PeopleChangeVo peopleChangeVo = peopleChangeService.addPeopleChange(peopleChangeAddForm);
        return ApiResult.success(peopleChangeVo);
    }

    @DeleteMapping(value = "/deletePeopleChange")
    public ApiResult deletePeopleChange(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        peopleChangeService.deletePeopleChange(ids);
        return ApiResult.success();
    }

    @PutMapping(value = "/updatePeopleChange")
    public ApiResult updatePeopleChange(@RequestBody @Valid PeopleChangeUpdateForm peopleChangeUpdateForm) {
        PeopleChangeVo peopleChangeVo = peopleChangeService.updatePeopleChange(peopleChangeUpdateForm);
        return ApiResult.success(peopleChangeVo);
    }

    @GetMapping(value = "/getPeopleChange/{id}")
    public ApiResult getPeopleChange(@PathVariable("id") Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(peopleChangeService.selectByPeopleChangeId(id));
    }


    @PostMapping(value = "/getPeopleChangeWord")
    public void getPeopleChangeWord(HttpServletResponse response) {
        String idStr = request.getParameter("id");//获取人员变更ID
        if (StringHelper.isNullAndEmpty(idStr)) {
            throw new BaseException("人员变更ID不能为空");
        }
        Integer id;
        try {
            id = Integer.valueOf(idStr);
        } catch (NumberFormatException e) {
            throw new BaseException("人员变更ID转换错误");
        }
        String pdfPath = peopleChangeService.ylPeopleChangeCheck(id);
        szxmCommonUtil.preViewPdf(response, pdfPath);
    }


    /**
     *获取section 人员变更信息
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/getSectionPeopleChangeList/{userId}/{pageSize}/{currentPageNum}")
    public ApiResult getSectionPeopleChangeList(@RequestParam Map<String, Object> mapWhere, @PathVariable("userId") Integer userId,@PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
        String projInfoId = request.getParameter("projInfoId");//获取基础项目ID
        //String userId = request.getParameter("userId");//获取人员id
        if (ObjectUtils.isEmpty(projectId))
        {
            return ApiResult.result(777,"projectId不能为空");
        }
        if (ObjectUtils.isEmpty(projInfoId))
        {
            return ApiResult.result(777,"id不能为空");
        }
        if (ObjectUtils.isEmpty(userId))
        {
            return ApiResult.result(777,"userId不能为空");
        }

        mapWhere.put("projectId",projectId);
        mapWhere.put("projInfoId",projInfoId);
        mapWhere.put("userId",userId);

       // List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        PageInfo<PeopleChangeVo> queryPeopleChangeVoList = peopleChangeService.selectSectionPeopleChangeList(mapWhere,  pageSize, currentPageNum);
        return new TableResultResponse(queryPeopleChangeVoList);
    }
}
