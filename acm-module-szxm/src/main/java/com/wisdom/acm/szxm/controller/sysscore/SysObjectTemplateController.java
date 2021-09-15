package com.wisdom.acm.szxm.controller.sysscore;

/**
 * Author：wqd
 * Date：2020-01-02 16:15
 * Description：<描述>
 */

import com.wisdom.acm.szxm.form.sysscore.ObjectTemplateAddForm;
import com.wisdom.acm.szxm.form.sysscore.ObjectTemplateUpdForm;
import com.wisdom.acm.szxm.service.sysscore.ObjectTemplateService;
import com.wisdom.acm.szxm.vo.sysscore.ObjectScoreItemVo;
import com.wisdom.base.common.msg.ApiResult;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 客观考核模板
 */
@Controller
@RestController
@RequestMapping("system/objectTemplate")
public class SysObjectTemplateController {
    @Autowired
    private ObjectTemplateService objectTemplateService;

    @PostMapping(value = "/addObjectTemplate")
    public ApiResult addObjectTemplate(@RequestBody @Valid ObjectTemplateAddForm objectTemplateAddForm) {
        ObjectScoreItemVo objectScoreItemVo = objectTemplateService.addObjectTemplate(objectTemplateAddForm);
        return ApiResult.success(objectScoreItemVo);
    }

    @DeleteMapping(value = "/deleteObjectTemplate")
    public ApiResult deleteObjectTemplate(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        objectTemplateService.deleteObjectTemplate(ids);
        return ApiResult.success();
    }

    @PutMapping(value = "/updObjectTemplate")
    public ApiResult updObjectTemplate(@RequestBody @Valid ObjectTemplateUpdForm objectTemplateUpdForm) {
        ObjectScoreItemVo objectScoreItemVo = objectTemplateService.updObjectTemplate(objectTemplateUpdForm);
        return ApiResult.success(objectScoreItemVo);
    }

    @GetMapping(value = "/selectMainItemObjectTemplates")
    public ApiResult selectMainItemObjectTemplates() {
        return ApiResult.success(objectTemplateService.selectMainItemObjectTemplates());
    }

    @GetMapping(value = "/selectDetailItemObjectTemplates/{checkItemId}")
    public ApiResult selectDetailItemObjectTemplates(@PathVariable("checkItemId") Integer checkItemId) {
        if (ObjectUtils.isEmpty(checkItemId)) {
            return ApiResult.result(1001, "checkItemId不能为空");
        }
        return ApiResult.success(objectTemplateService.selectDetailItemObjectTemplates(checkItemId));
    }

}
