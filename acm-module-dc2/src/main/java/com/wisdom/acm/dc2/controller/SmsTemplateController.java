package com.wisdom.acm.dc2.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.SmsTemplateAddForm;
import com.wisdom.acm.dc2.service.SmsTemplateService;
import com.wisdom.acm.dc2.vo.SmsTemplateVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

//短信模板
@RestController
@RequestMapping(value = "sms/template")
public class SmsTemplateController {

    @Autowired
    private SmsTemplateService smsTemplateService;

    /**
     * 列表查询
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult list(@RequestParam Map<String, Object> mapWhere,
                          @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {

        PageInfo<SmsTemplateVo> pageInfo =
                smsTemplateService.querySmsTemplatePageList(mapWhere, pageSize, currentPageNum);
        return new TableResultResponse(pageInfo);
    }

    /**
     * 新增
     * @param form 基本信息表单
     * @return
     */
    @PostMapping(value = "/insert")
    public ApiResult insert(@RequestBody @Valid SmsTemplateAddForm form) {
        smsTemplateService.insertSmsTemplate(form);
        return ApiResult.success();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/del")
    public ApiResult del(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "ids不能为空");
        }
        smsTemplateService.delSmsTemplate(ids);
        return ApiResult.success();
    }

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    public ApiResult detail(@PathVariable("id") Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(smsTemplateService.getSmsTemplateDetail(id));
    }
}
