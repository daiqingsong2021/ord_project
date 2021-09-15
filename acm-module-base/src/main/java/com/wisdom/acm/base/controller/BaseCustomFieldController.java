package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.custom.BaseCustomFieldSaveForm;
import com.wisdom.acm.base.form.custom.BaseCustomFieldUpdateForm;
import com.wisdom.acm.base.service.BaseCustomFieldService;
import com.wisdom.acm.base.vo.custom.BaseCustomFieldVo;
import com.wisdom.acm.base.vo.custom.BaseCustomValueVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 自定义管理
 */
@Controller
@RequestMapping("custom")
@RestController
public class BaseCustomFieldController extends BaseController {

    @Autowired
    private BaseCustomFieldService baseCustomFieldService;

    /**
     * 获取自定义列表
     *
     * @return
     */
    @GetMapping(value = "/{tableName}/list")
    public ApiResult<List<BaseCustomFieldVo>> queryListByTableName(@PathVariable("tableName") String tableName) {
        List<BaseCustomFieldVo> voList = this.baseCustomFieldService.queryListByTableName(tableName);
        return ApiResult.success(voList);
    }

    /**
     * 获取自定义列表
     *
     * @return
     */
    @GetMapping(value = "/{tableName}/{fieldName}/info")
    public ApiResult<BaseCustomFieldVo> queryByTableNameAndFieldName(@PathVariable("tableName") String tableName, @PathVariable("fieldName") String fieldName) {
        BaseCustomFieldVo vo = this.baseCustomFieldService.queryByTableNameAndFieldName(tableName, fieldName);
        return ApiResult.success(vo);
    }

    /**
     * 保存自定义
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/save")
    public ApiResult<BaseCustomFieldVo> save(@RequestBody @Valid BaseCustomFieldSaveForm form) {
        BaseCustomFieldVo vo = this.baseCustomFieldService.save(form);
        return ApiResult.success(vo);
    }

    /**
     * 获取自定义值列表
     *
     * @return
     */
    @GetMapping(value = "/value/{tableName}/{id}/info")
    public ApiResult<List<BaseCustomValueVo>> queryValueListByTableNameAndId(@PathVariable("tableName") String tableName, @PathVariable("id") Integer id) {
        List<BaseCustomValueVo> voList = this.baseCustomFieldService.queryValueListByTableNameAndId(tableName, id);
        return ApiResult.success(voList);
    }

    /**
     * 保存自定义字段值
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/value/save")
    public ApiResult<List<BaseCustomValueVo>> saveCustomValue(@RequestBody @Valid BaseCustomFieldUpdateForm form) {
        this.baseCustomFieldService.saveCustomValue(form);
        List<BaseCustomValueVo> voList = this.baseCustomFieldService.queryValueListByTableNameAndId(form.getTableName(), form.getId());
        return ApiResult.success(voList);
    }

}
