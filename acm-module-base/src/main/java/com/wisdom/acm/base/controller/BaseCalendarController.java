package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.calendar.BaseCalendarExceptionsUpdateForm;
import com.wisdom.acm.base.form.calendar.BaseCalendarUpdateForm;
import com.wisdom.acm.base.form.calendar.BaseCalendarWeekDaysUpdateForm;
import com.wisdom.acm.base.po.BaseCalendarPo;
import com.wisdom.acm.base.service.BaseCalendarService;
import com.wisdom.acm.base.vo.calendar.BaseCalendarInfoVo;
import com.wisdom.acm.base.vo.calendar.BaseCalendarVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.CalendarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calendar")
public class BaseCalendarController extends BaseController {

    @Autowired
    private BaseCalendarService baseCalendarService;

    /**
     * 日历设置列表
     *
     * @return
     */
    @GetMapping("/list")
    public ApiResult queryCalendarList() {
        List<BaseCalendarVo> allCalendarList = baseCalendarService.queryCalendarList();
        return ApiResult.success(allCalendarList);
    }

    /**
     * 获取默认日历
     *
     * @return
     */
    @GetMapping("/default/info")
    public ApiResult getDefaultCalendar() {
        BaseCalendarInfoVo defaultCalendar = baseCalendarService.getDefaultCalendarInfo();
        return ApiResult.success(defaultCalendar);
    }

    /**
     * 获取下拉显示默认日历
     *
     * @return
     */
    @GetMapping("/default/calendar/info")
    public ApiResult getDefaultCalendarInfo() {
        BaseCalendarVo calendarVo = baseCalendarService.getDefaultCalendar();
        return ApiResult.success(calendarVo);
    }

    /**
     * 设置默认日历
     *
     * @return
     */
    @PutMapping("/{id}/default")
    @AddLog(title = "设置默认日历", module = LoggerModuleEnum.BM_CALENDAR)
    public ApiResult updateDefaultCalendar(@PathVariable("id") Integer id) {
        BaseCalendarPo baseCalendarPo = baseCalendarService.getBaseClndrData(id);
        this.setAcmLogger(new AcmLogger("设置\"" + baseCalendarPo.getCalName() + "\"为默认日历"));

        baseCalendarService.updateDefaultCalendar(id);
        return ApiResult.success();
    }

    /**
     * 删除日历设置信息
     *
     * @param
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除日历设置信息", module = LoggerModuleEnum.BM_CALENDAR)
    public ApiResult deleteBaseCurrency(@RequestBody List<Integer> ids) {
        //添加日志
        String names = baseCalendarService.queryNamesByIds(ids, "calName");
        this.setAcmLogger(new AcmLogger("批量删除日历设置，名称如下：" + names));

        baseCalendarService.deleteCalendarByIds(ids);
        return ApiResult.success();
    }

    /**
     * 新增日历设置
     *
     * @param
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "新增日历设置", module = LoggerModuleEnum.BM_CALENDAR)
    public ApiResult addCalendar() {
        this.setAcmLogger(new AcmLogger("新增日历设置,日历名称：新增日历"));
        BaseCalendarPo baseCalendarPo = baseCalendarService.addCalendar();
        BaseCalendarVo vaseCalendarVo = baseCalendarService.getBaseCalendarVo(baseCalendarPo.getId());
        return ApiResult.success(vaseCalendarVo);
    }

    /**
     * 修改日历设置
     *
     * @param
     * @return
     */
    @PutMapping(value = "/weekdays/update")
    public ApiResult updateWeekDays(@RequestBody @Valid BaseCalendarWeekDaysUpdateForm updateForm) {
        baseCalendarService.updateWeekDays(updateForm);
        return ApiResult.success();
    }

    /**
     * 修改日历设置
     *
     * @param
     * @return
     */
    @PutMapping(value = "/exceptions/update")
    public ApiResult updateExceptions(@RequestBody @Valid BaseCalendarExceptionsUpdateForm updateForm) {
        baseCalendarService.updateExceptions(updateForm);
        return ApiResult.success();
    }


    /**
     * 取消日历设置
     *
     * @return
     */
    @PutMapping("/exceptions/{id}/{time}/cancle")
    public ApiResult cancleExceptions(@PathVariable("id") Integer id, @PathVariable("time") String time) {
        baseCalendarService.cancleExceptions(id, time);
        return ApiResult.success();
    }

    /**
     * 日历基本信息
     *
     * @param calendarId
     * @return
     */
    @GetMapping("/{id}/info")
    public ApiResult getCalendarById(@PathVariable("id") Integer calendarId) {
        BaseCalendarInfoVo calendarInfoVo = baseCalendarService.getCalendarInfoVo(calendarId);
        return ApiResult.success(calendarInfoVo);
    }

    /**
     * 更新日历基本信息
     *
     * @param baseCalendarUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateCalendar(@RequestBody @Valid BaseCalendarUpdateForm baseCalendarUpdateForm) {
        BaseCalendarPo baseCalendarPo = baseCalendarService.updateCalendar(baseCalendarUpdateForm);
        BaseCalendarVo vaseCalendarVo = baseCalendarService.getBaseCalendarVo(baseCalendarPo.getId());
        return ApiResult.success(vaseCalendarVo);
    }

    /**
     * 复制日历
     *
     * @param calendarId
     * @return
     */
    @PostMapping(value = "/{id}/copy")
    @AddLog(title = "复制日历", module = LoggerModuleEnum.BM_CALENDAR)
    public ApiResult copyCalendar(@PathVariable("id") Integer calendarId) {
        BaseCalendarPo baseCalendarPo = baseCalendarService.copyCalendar(calendarId);
        this.setAcmLogger(new AcmLogger("复制日历，日历名称：" + baseCalendarPo.getCalName()));
        BaseCalendarVo vaseCalendarVo = baseCalendarService.getBaseCalendarVo(baseCalendarPo.getId());
        return ApiResult.success(vaseCalendarVo);
    }

    /**
     * 根据id获取日历集合
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/calc/pos")
    public ApiResult<List<BaseCalendarPo>> getCalendarPos(@RequestBody List<Integer> ids) {
        List<BaseCalendarPo> baseCalendarPos = baseCalendarService.getBaseCalendarPos(ids);
        return ApiResult.success(baseCalendarPos);
    }

    /**
     * 根据id获取日历集合
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/calc/pos/info")
    public ApiResult<List<CalendarVo>> getCalendarPoInfos(@RequestBody List<Integer> ids) {
        List<BaseCalendarPo> baseCalendarPos = baseCalendarService.selectByIds(ids);
        List<CalendarVo> CalendarVos = baseCalendarService.getCalendarVoInfos(baseCalendarPos);
        return ApiResult.success(CalendarVos);
    }

    /**
     * 根据id获取日历
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/calc/pos/{id}/info")
    public ApiResult<CalendarVo> getCalendarPoInfo(@PathVariable("id") Integer id) {
        BaseCalendarPo baseCalendarPo = baseCalendarService.selectById(id);
        return ApiResult.success(getCalendarVo(baseCalendarPo));
    }

    /**
     * 获取日历默认集合
     *
     * @return
     */
    @GetMapping(value = "/calc/pos/default/info")
    public ApiResult<CalendarVo> getCalendarDefaultInfo() {
        BaseCalendarPo baseCalendarPo = baseCalendarService.getDefaultCalendarPo();
        return ApiResult.success(getCalendarVo(baseCalendarPo));
    }

    private CalendarVo getCalendarVo(BaseCalendarPo baseCalendarPo) {
        if (baseCalendarPo != null) {
            List<BaseCalendarPo> baseCalendarPos = new ArrayList<>();
            baseCalendarPos.add(baseCalendarPo);
            List<CalendarVo> CalendarVos = baseCalendarService.getCalendarVoInfos(baseCalendarPos);
            return CalendarVos.get(0);
        }
        return null;
    }
}
