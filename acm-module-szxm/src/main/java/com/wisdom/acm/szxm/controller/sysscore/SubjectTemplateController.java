package com.wisdom.acm.szxm.controller.sysscore;

/**
 * Author：wqd
 * Date：2020-01-02 16:15
 * Description：<描述>
 */

import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.form.sysscore.SubjectTemplateAddForm;
import com.wisdom.acm.szxm.form.sysscore.SubjectTemplateUpdForm;
import com.wisdom.acm.szxm.service.sysscore.SubjectTemplateService;
import com.wisdom.acm.szxm.vo.sysscore.SubjectTemplateVo;
import com.wisdom.base.common.exception.BaseException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 主观考核模板
 * @author Administrator
 */
@Controller
@RestController
@RequestMapping("system/subjectTemplate")
public class SubjectTemplateController {
    @Autowired
    private SubjectTemplateService subjectTemplateService;

    @PostMapping(value = "/addSubjectTemplate")
    public ApiResult addSubjectTemplate(@RequestBody @Valid SubjectTemplateAddForm subjectTemplateAddForm) {
        SubjectTemplateVo subjectTemplateVo = subjectTemplateService.addSubjectTemplate(subjectTemplateAddForm);
        return ApiResult.success(subjectTemplateVo);
    }

    @DeleteMapping(value = "/deleteSubjectTemplate")
    public ApiResult deleteSubjectTemplate(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        subjectTemplateService.deleteSubjectTemplate(ids);
        return ApiResult.success();
    }

    @PutMapping(value = "/updSubjectTemplate")
    public ApiResult updSubjectTemplate(@RequestBody @Valid SubjectTemplateUpdForm subjectTemplateUpdForm) {
        SubjectTemplateVo subjectTemplateVo = subjectTemplateService.updSubjectTemplate(subjectTemplateUpdForm);
        return ApiResult.success(subjectTemplateVo);
    }

    @GetMapping(value = "/selectSubjectTemplate")
    public ApiResult selectSubjectTemplate(@RequestParam Map<String, Object> mapWhere) {
        return ApiResult.success(subjectTemplateService.selectSubjectTemplate(mapWhere));
    }

    @GetMapping(value = "/isInSubjectTemplate/{moduleCode}")
    public ApiResult isInSubjectTemplate(@PathVariable("moduleCode")String moduleCode) {
        if(StringHelper.isNullAndEmpty(moduleCode)){
            throw new BaseException("moduleCode不能为空");
        }
        return ApiResult.success(subjectTemplateService.isInSubjectTemplate(moduleCode));
    }
}
