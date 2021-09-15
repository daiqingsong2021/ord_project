package com.wisdom.acm.szxm.controller.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.form.rygl.HolidayAddForm;
import com.wisdom.acm.szxm.form.rygl.HolidayUpdateForm;
import com.wisdom.acm.szxm.po.rygl.HolidayPo;
import com.wisdom.acm.szxm.service.rygl.HolidayService;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.vo.rygl.HolidayVo;
import com.wisdom.acm.szxm.vo.rygl.LwryHolidayVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 劳务人员请假
 */
@RestController
@RequestMapping("rygl/lwryLeave")
public class LwryLeaveController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private PeopleService peopleService;

    /**
     * 通过标段获取下面所有人员信息
     *
     * @param sectionId
     * @return
     */
    @GetMapping(value = "/getConstructionCrowdInfo/{sectionId}")
    public ApiResult getHolidayRyzw(@PathVariable("sectionId") Integer sectionId) {
        return ApiResult.success(peopleService.getConstructionCrowdInfo(sectionId));
    }
    /**
     * 添加该标段工程队的人员请假信息
     *
     * @param holidayAddForm
     * @return
     */
    @PostMapping(value = "/addLwryHoliday")
    public ApiResult addLwryHoliday(@RequestBody @Valid HolidayAddForm holidayAddForm) {
        HolidayVo holidayVo = holidayService.addHoliday(holidayAddForm);
        return ApiResult.success(holidayVo);
    }

    @PutMapping(value = "/updateLwryHoliday")
    public ApiResult updateLwryHoliday(@RequestBody @Valid HolidayUpdateForm holidayUpdateForm) {
        holidayService.updateHoliday(holidayUpdateForm);
        Map<String, Object> mapWhere = new HashMap<>();
        mapWhere.put("id",holidayUpdateForm.getId());
        mapWhere.put("projectId",holidayUpdateForm.getProjectId());//获取项目ID
        PageInfo<LwryHolidayVo> pageInfo =
                holidayService.selectSectionLwryHolidayList(mapWhere, 1, 1);
        List<LwryHolidayVo> lwryHolidayVos = pageInfo.getList();
        if(CollectionUtils.isEmpty(lwryHolidayVos)){
            return ApiResult.result(1001, "根据id:"+holidayUpdateForm.getId()+"查询为空，系统异常");
        }
        return ApiResult.success(lwryHolidayVos.get(0));
    }
    /**
     * 批量修改请假单状态：只有两个状态：新建和完成
     *
     * @param ids
     * @return
     */
    @PutMapping(value = "/updateLwryHolidayStatus")
    public ApiResult updateLwryHolidayStatus(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        HolidayPo holidayPo = new HolidayPo();
        holidayPo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVED.toString());
        holidayService.updateSelectiveByIds(holidayPo, ids);
        return ApiResult.success();
    }

    @DeleteMapping(value = "/deleteLwryHoliday")
    public ApiResult deleteHoliday(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        holidayService.deleteHoliday(ids);
        return ApiResult.success();
    }

    /**
     * 查询劳务人员的请假单，该情况只有当前人是部门的考勤管理员才可以看到，并且看到的只是他所属标段下的所有成员的信息
     * @return
     */
    @GetMapping(value = "/getLwryHolidayList/{pageSize}/{currentPageNum}")
    public ApiResult getLwryHolidayList(@RequestParam Map<String, Object> mapWhere,
                                    @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionId = request.getParameter("sectionId");//获取标段ID，劳务人员只能看到自己所在的标段
        if(StringUtils.isBlank(projectId) || StringUtils.isBlank(sectionId)){
            return ApiResult.result(1001, "project或者sectionId不能为空");
        }
        mapWhere.put("ryType","lwry");
        PageInfo<LwryHolidayVo> queryHolidayVoList =
                holidayService.selectSectionLwryHolidayList(mapWhere, pageSize, currentPageNum);
        return new TableResultResponse(queryHolidayVoList);
    }
}
