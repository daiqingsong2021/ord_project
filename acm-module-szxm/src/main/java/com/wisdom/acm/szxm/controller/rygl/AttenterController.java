package com.wisdom.acm.szxm.controller.rygl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.common.officeUtils.ExcelUtil;
import com.wisdom.acm.szxm.form.rygl.*;
import com.wisdom.acm.szxm.po.rygl.HolidayPo;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.service.rygl.*;
import com.wisdom.acm.szxm.vo.rygl.HolidayVo;
import com.wisdom.acm.szxm.vo.rygl.KqConfigVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ResourceUtil;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 考勤管理
 */
@RestController
@RequestMapping("rygl/attenter")
public class AttenterController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private KqConfigService kqConfigService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private KqReportService kqReportService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private KqRecordService kqRecordService;


    @PostMapping(value = "/addKqConfig")
    public ApiResult addKqConfig(@RequestBody @Valid KqConfigAddForm kqConfigAddForm) {
        KqConfigVo kqConfigVo = kqConfigService.addKqConfig(kqConfigAddForm);
        return ApiResult.success(kqConfigVo);
    }

    @PutMapping(value = "/updateKqConfig")
    public ApiResult updateKqConfig(@RequestBody @Valid KqConfigUpdateForm kqConfigUpdateForm) {
        KqConfigVo kqConfigVo = kqConfigService.updateKqConfig(kqConfigUpdateForm);
        return ApiResult.success(kqConfigVo);
    }

    @DeleteMapping(value = "/deleteKqConfig")
    public ApiResult deleteKqConfig(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        kqConfigService.deleteKqConfig(ids);
        return ApiResult.success();
    }

    @GetMapping(value = "/getKqConfigList/{pageSize}/{currentPageNum}")
    public ApiResult getKqConfigList(@RequestParam Map<String, Object> mapWhere,
                                     @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合

        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        PageInfo<KqConfigVo> queryKqConfigVoList =
                kqConfigService.selectKqConfigList(mapWhere, sectionList, pageSize, currentPageNum);
        return new TableResultResponse(queryKqConfigVoList);
    }

    @GetMapping(value = "/getKqConfig/{id}")
    public ApiResult getKqConfig(@PathVariable("id") Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(kqConfigService.selectByKqConfigId(id));
    }

    @PutMapping(value = "/saveAllKqConfig")
    public ApiResult saveAllKqConfig(@RequestBody @Valid AllKqConfigForm allKqConfigForm) {
        KqConfigVo kqConfigVo = kqConfigService.saveAllKqConfig(allKqConfigForm);
        return ApiResult.success(kqConfigVo);
    }

    @GetMapping(value = "/getAllKqConfig")
    public ApiResult getAllKqConfig() {
        KqConfigVo kqConfigVo = kqConfigService.getAllKqConfig();
        return ApiResult.success(kqConfigVo);
    }

    /**
     * 查询请假管理人员职务（只有非考勤专员才能查）
     *
     * @param sectionId
     * @return
     */
    @GetMapping(value = "/getHolidayRyzw/{sectionId}")
    public ApiResult getHolidayRyzw(@PathVariable("sectionId") Integer sectionId) {
        UserInfo userInfo = commUserService.getLoginUser();
        Example example = new Example(PeoplePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("telPhone", userInfo.getPhone());//手机号验证
        criteria.andEqualTo("sectionId", sectionId);//标段ID
        criteria.andEqualTo("status", "1");//进场的人员
        PeoplePo exisPeoplePo = peopleService.selectOneByExample(example);
        String jobName = "";
        if (!ObjectUtils.isEmpty(exisPeoplePo))
            jobName = szxmCommonUtil.getDictionaryName("base.position.type", exisPeoplePo.getJob());
        else
            throw new BaseException("该人员在该标段下未进场，无法获取职务！");
        return ApiResult.success(jobName);
    }

    @PostMapping(value = "/addHoliday")
    public ApiResult addHoliday(@RequestBody @Valid HolidayAddForm holidayAddForm) {
        HolidayVo holidayVo = holidayService.addHoliday(holidayAddForm);
        return ApiResult.success(holidayVo);
    }

    @PutMapping(value = "/updateHoliday")
    public ApiResult updateHoliday(@RequestBody @Valid HolidayUpdateForm holidayUpdateForm) {
        HolidayVo holidayVo = holidayService.updateHoliday(holidayUpdateForm);
        return ApiResult.success(holidayVo);
    }

    @PutMapping(value = "/updateHolidayStatus")
    public ApiResult updateHolidayStatus(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        HolidayPo holidayPo = new HolidayPo();
        holidayPo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVED.toString());
        holidayService.updateSelectiveByIds(holidayPo, ids);
        return ApiResult.success();
    }

    @DeleteMapping(value = "/deleteHoliday")
    public ApiResult deleteHoliday(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        holidayService.deleteHoliday(ids);
        return ApiResult.success();
    }

    @GetMapping(value = "/getHolidayList/{pageSize}/{currentPageNum}")
    public ApiResult getHolidayList(@RequestParam Map<String, Object> mapWhere,
                                    @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
        mapWhere.put("ryType","glry");
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        PageInfo<HolidayVo> queryHolidayVoList =
                holidayService.selectHolidayList(mapWhere, sectionList, pageSize, currentPageNum);
        return new TableResultResponse(queryHolidayVoList);
    }

    @GetMapping(value = "/getFlowHolidayList")
    public ApiResult getFlowHolidayList(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");//获取项目ID
        List<HolidayVo> holidayVoList;
        if (ObjectUtils.isEmpty(ids)) {
            String projectId = request.getParameter("projectId");//获取项目ID
            String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
            List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
            holidayVoList = holidayService.selectFlowHolidayList(mapWhere, sectionList);
        } else {
            holidayVoList = holidayService.selectFlowHolidayList(mapWhere, null);
        }
        return ApiResult.success(holidayVoList);
    }

    @GetMapping(value = "/getHoliday/{id}")
    public ApiResult getHoliday(@PathVariable("id") Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(holidayService.selectByHolidayId(id));
    }

    @GetMapping(value = "/getHolidayDay")
    public ApiResult getHolidayDay(@RequestParam Map<String, Object> mapWhere) {
        String projectId = String.valueOf(mapWhere.get("projectId"));
        String sectionId = String.valueOf(mapWhere.get("sectionId"));
        String startTime = String.valueOf(mapWhere.get("startTime"));
        String endTime = String.valueOf(mapWhere.get("endTime"));
        if (StringHelper.isNullAndEmpty(projectId) || StringHelper.isNullAndEmpty(sectionId) || StringHelper
                .isNullAndEmpty(startTime) || StringHelper.isNullAndEmpty(endTime)) {
            throw new BaseException("项目ID 或者 标段ID 或者 开始时间 或者时间 存在空值");
        }

        return ApiResult.success(holidayService.getDaysBySIdAndTime(Integer.valueOf(projectId),
                Integer.valueOf(sectionId),
                DateUtil.formatDate(startTime, DateUtil.DATE_DEFAULT_FORMAT),
                DateUtil.formatDate(endTime, DateUtil.DATE_DEFAULT_FORMAT)));
    }

    /**
     * 预览审批单
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/getHolidayWord/{id}")
    public void getHolidayWord(@PathVariable("id") String id, HttpServletResponse response) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BaseException("id不能为空");
        }
        String pdfPath = holidayService.getHolidayWord(Integer.valueOf(id));
        szxmCommonUtil.preViewPdf(response, pdfPath);
    }

    @GetMapping(value = "/getKqRecordReport/{pageSize}/{currentPageNum}")
    public ApiResult getKqRecordReport(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        //定义返回结果集
        List<Map<String, Object>> resultList = Lists.newArrayList();

        String projectId = String.valueOf(mapWhere.get("projectId"));
        String sectionIds = String.valueOf(mapWhere.get("sectionIds"));
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List

        String type = String.valueOf(mapWhere.get("type"));//人员类型，0 管理人员 1普通人员
        String startTime = StringHelper.formattString(String.valueOf(mapWhere.get("startTime")));
        String endTime = StringHelper.formattString(String.valueOf(mapWhere.get("endTime")));
        String searcher = StringHelper.formattString(String.valueOf(mapWhere.get("searcher")));//姓名或者单位

        if (StringHelper.isNullAndEmpty(type)) {
            throw new BaseException("人员类型 存在空值");
        }
        mapWhere.put("sectionList", sectionList);

        if (StringHelper.isNotNullAndEmpty(startTime)) {
            startTime = DateUtil.getDateFormat(DateUtil.formatDate(startTime, "yyyy-MM-dd"), "yyyyMMdd");
        }

        if (StringHelper.isNotNullAndEmpty(endTime)) {
            endTime = DateUtil.getDateFormat(DateUtil.formatDate(endTime, "yyyy-MM-dd"), "yyyyMMdd");
        }
        mapWhere.put("startTime", startTime);
        mapWhere.put("endTime", endTime);
        PageHelper.startPage(currentPageNum, pageSize);
        if ("0".equals(type)) {//管理人员
            mapWhere.put("peopleName", searcher);
            resultList = kqReportService.selectGlRyReport(mapWhere);

        } else {
            mapWhere.put("orgName", searcher);
            resultList = kqReportService.selectLwRyReport(mapWhere);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(resultList);
        PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(projectId)));

        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for (Map<String, Object> result : pageInfo.getList()) {
                Integer sectionId = Integer.valueOf(String.valueOf(result.get("SECTION_ID")));
                ProjectTeamVo sectionVo = sectionMap.get(sectionId);
                result.put("SECTION_CODE", sectionVo.getCode());
                result.put("SECTION_NAME", sectionVo.getName());
                result.put("PROJECT_NAME", projectVo.getName());
            }

        }

        return ApiResult.success(pageInfo);
    }

    @PostMapping(value = {"/getKqRecordReport/export"})
    public void dowPeopTemp(HttpServletResponse response, @RequestParam Map<String, Object> mapWhere) {
        List<Map<String, Object>> resultList = this.getReportList(mapWhere);
        String type = String.valueOf(mapWhere.get("type"));//人员类型，0 管理人员 1普通人员

        try {

            if ("0".equals(type)) {//管理人员
                InputStream inputStream = ResourceUtil.findResoureFile("template/KqReport.xlsx");
                Workbook workbook = ExcelUtil.getWorkbook(inputStream);

                String[][] cellArr = new String[11][11];
                //姓名
                cellArr[0][0] = "0";
                cellArr[0][1] = "PEOPLE_NAME";
                cellArr[0][2] = "string";
                cellArr[0][4] = "center";
                cellArr[0][5] = "borderThin";
                cellArr[0][6] = "0";
                //身份证号
                cellArr[1][0] = "1";
                cellArr[1][1] = "ID_CARD";
                cellArr[1][2] = "string";
                cellArr[1][4] = "center";
                cellArr[1][5] = "borderThin";
                cellArr[1][6] = "0";
                //项目名称
                cellArr[2][0] = "2";
                cellArr[2][1] = "PROJECT_NAME";
                cellArr[2][2] = "string";
                cellArr[2][4] = "center";
                cellArr[2][5] = "borderThin";
                cellArr[2][6] = "0";
                //标段名称
                cellArr[3][0] = "3";
                cellArr[3][1] = "SECTION_NAME";
                cellArr[3][2] = "string";
                cellArr[3][4] = "center";
                cellArr[3][5] = "borderThin";
                cellArr[3][6] = "0";
                //应出勤天数
                cellArr[4][0] = "4";
                cellArr[4][1] = "DAYS";
                cellArr[4][2] = "string";
                cellArr[4][4] = "center";
                cellArr[4][5] = "borderThin";
                cellArr[4][6] = "0";
                //缺勤天数
                cellArr[5][0] = "5";
                cellArr[5][1] = "QQDAYS";
                cellArr[5][2] = "string";
                cellArr[5][4] = "center";
                cellArr[5][5] = "borderThin";
                cellArr[5][6] = "0";
                //请假天数
                cellArr[6][0] = "6";
                cellArr[6][1] = "QJDAYS";
                cellArr[6][2] = "string";
                cellArr[6][4] = "center";
                cellArr[6][5] = "borderThin";
                cellArr[6][6] = "0";
                //实际出勤天数
                cellArr[7][0] = "7";
                cellArr[7][1] = "ACTDAYS";
                cellArr[7][2] = "string";
                cellArr[7][4] = "center";
                cellArr[7][5] = "borderThin";
                cellArr[7][6] = "0";
                //迟到天数
                cellArr[8][0] = "8";
                cellArr[8][1] = "CDDAYS";
                cellArr[8][2] = "string";
                cellArr[8][4] = "center";
                cellArr[8][5] = "borderThin";
                cellArr[8][6] = "0";
                //早退天数
                cellArr[9][0] = "9";
                cellArr[9][1] = "ZTDAYS";
                cellArr[9][2] = "string";
                cellArr[9][4] = "center";
                cellArr[9][5] = "borderThin";
                cellArr[9][6] = "0";
                //异常天数
                cellArr[10][0] = "10";
                cellArr[10][1] = "YCDAYS";
                cellArr[10][2] = "string";
                cellArr[10][4] = "center";
                cellArr[10][5] = "borderThin";
                cellArr[10][6] = "0";
                ExcelUtil.writeSheet(workbook, 0, 2, cellArr, resultList);

                // 设置响应头和客户端保存文件名
                String fileName = URLEncoder.encode("管理人员考勤汇总" + mapWhere.get("startTime") + "_" + mapWhere.get("endTime") + ".xlsx", "UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
                // 获取导出数据的集合
                workbook.write(response.getOutputStream());
                response.getOutputStream().flush();
                response.getOutputStream().close();

            } else {
                InputStream inputStream = ResourceUtil.findResoureFile("template/LwKqReport.xlsx");
                Workbook workbook = ExcelUtil.getWorkbook(inputStream);
                String[][] cellArr = new String[6][11];
                //单位
                cellArr[0][0] = "0";
                cellArr[0][1] = "ORG_NAME";
                cellArr[0][2] = "string";
                cellArr[0][4] = "center";
                cellArr[0][5] = "borderThin";
                cellArr[0][6] = "0";
                //项目名称
                cellArr[1][0] = "1";
                cellArr[1][1] = "PROJECT_NAME";
                cellArr[1][2] = "string";
                cellArr[1][4] = "center";
                cellArr[1][5] = "borderThin";
                cellArr[1][6] = "0";
                //标段名称
                cellArr[2][0] = "2";
                cellArr[2][1] = "SECTION_NAME";
                cellArr[2][2] = "string";
                cellArr[2][4] = "center";
                cellArr[2][5] = "borderThin";
                cellArr[2][6] = "0";
                //应出勤人数
                cellArr[3][0] = "3";
                cellArr[3][1] = "ALLRS";
                cellArr[3][2] = "string";
                cellArr[3][4] = "center";
                cellArr[3][5] = "borderThin";
                cellArr[3][6] = "0";
                //缺勤人数
                cellArr[4][0] = "4";
                cellArr[4][1] = "QQRS";
                cellArr[4][2] = "string";
                cellArr[4][4] = "center";
                cellArr[4][5] = "borderThin";
                cellArr[4][6] = "0";
                //实际出勤人数
                cellArr[5][0] = "5";
                cellArr[5][1] = "ACTRS";
                cellArr[5][2] = "string";
                cellArr[5][4] = "center";
                cellArr[5][5] = "borderThin";
                cellArr[5][6] = "0";
                ExcelUtil.writeSheet(workbook, 0, 2, cellArr, resultList);
                // 设置响应头和客户端保存文件名
                String fileName = URLEncoder.encode("劳务人员考勤汇总" + mapWhere.get("startTime") + "_" + mapWhere.get("endTime") + ".xlsx", "UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
                // 获取导出数据的集合
                workbook.write(response.getOutputStream());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

        } catch (Exception e) {
            throw new BaseException("导出失败");
        }
    }

    private List<Map<String, Object>> getReportList(Map<String, Object> mapWhere) {
        //定义返回结果集
        List<Map<String, Object>> resultList = Lists.newArrayList();

        String projectId = String.valueOf(mapWhere.get("projectId"));
        String sectionIds = String.valueOf(mapWhere.get("sectionIds"));
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List

        String type = String.valueOf(mapWhere.get("type"));//人员类型，0 管理人员 1普通人员
        String startTime = StringHelper.formattString(String.valueOf(mapWhere.get("startTime")));
        String endTime = StringHelper.formattString(String.valueOf(mapWhere.get("endTime")));
        String searcher = StringHelper.formattString(String.valueOf(mapWhere.get("searcher")));//姓名或者单位

        if (StringHelper.isNullAndEmpty(type)) {
            throw new BaseException("人员类型 存在空值");
        }
        mapWhere.put("sectionList", sectionList);


        if (StringHelper.isNotNullAndEmpty(startTime)) {
            startTime = DateUtil.getDateFormat(DateUtil.formatDate(startTime, "yyyy-MM-dd"), "yyyyMMdd");
        }
        if (StringHelper.isNotNullAndEmpty(endTime)) {
            endTime = DateUtil.getDateFormat(DateUtil.formatDate(endTime, "yyyy-MM-dd"), "yyyyMMdd");
        }
        mapWhere.put("startTime", startTime);
        mapWhere.put("endTime", endTime);
        if ("0".equals(type)) {//管理人员
            mapWhere.put("peopleName", searcher);
            resultList = kqReportService.selectGlRyReport(mapWhere);

        } else {
            mapWhere.put("orgName", searcher);
            resultList = kqReportService.selectLwRyReport(mapWhere);
        }
        PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(projectId)));

        if (!ObjectUtils.isEmpty(resultList)) {
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for (Map<String, Object> result : resultList) {
                Integer sectionId = Integer.valueOf(String.valueOf(result.get("SECTION_ID")));
                ProjectTeamVo sectionVo = sectionMap.get(sectionId);
                result.put("SECTION_CODE", sectionVo.getCode());
                result.put("SECTION_NAME", sectionVo.getName());
                result.put("PROJECT_NAME", projectVo.getName());
            }
        }
        return resultList;
    }

    /**
     * 获取标段考勤记录
     *
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/getSectionKqRecord/{pageSize}/{currentPageNum}")
    public ApiResult getSectionKqRecord(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        //定义返回结果集
        List<Map<String, Object>> resultList = Lists.newArrayList();
        String projectId = String.valueOf(mapWhere.get("projectId"));
        if (StringHelper.isNullAndEmpty(String.valueOf(mapWhere.get("sectionId")))) {
            String sectionIds = String.valueOf(mapWhere.get("sectionIds"));
            List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
            mapWhere.put("sectionList", sectionList);
        }

        Map<String, Object> returnMap = kqReportService.getSectionKqRecord(mapWhere, pageSize, currentPageNum);
        return ApiResult.success(returnMap);
    }

    /**
     * 获取当日考勤记录 -- 所有标段
     *
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getAttendanceRecords")
    public ApiResult getAttendanceRecords(@RequestParam Map<String, Object> mapWhere) {
        String projectId = String.valueOf(mapWhere.get("projectId"));
        if (StringHelper.isNullAndEmpty(projectId)) {
            //项目ID 不能为空
            throw new BaseException("项目ID不能为空!");
        }
        Map<String, Object> returnMap = kqReportService.getAttendanceRecords(mapWhere);
        return ApiResult.success(returnMap);
    }

    /**
     * 间隔时间少于一个月
     *
     * @param startDate yyyy-MM-dd 开始时间
     * @param endDate   yyyy-MM-dd 完成时间
     * @param type      0 全部(管理人员/劳务人员)
     *                  1 管理人员
     *                  2 劳务人员
     * @return
     */
    @GetMapping(value = "/hzkqRecord/{startDate}/{endDate}/{type}")
    public ApiResult hzkqRecord(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("type") String type) {
        kqRecordService.hzkqRecord(startDate, endDate, type);
        return ApiResult.success();
    }

    /**
     * 获取人员月度考勤记录
     *
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getPeopleMonthKqRecord")
    public ApiResult getPeopleMonthKqRecord(@RequestParam Map<String, Object> mapWhere) {
        Map<String, Object> returnMap = kqReportService.getPeopleMonthKqRecord(mapWhere);
        return ApiResult.success(returnMap);
    }
}
