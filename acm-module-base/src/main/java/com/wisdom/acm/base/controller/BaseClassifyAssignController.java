package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.classify.BaseClassifyAssignForm;
import com.wisdom.acm.base.form.classify.BaseUpdateClassifyAssignForm;
import com.wisdom.acm.base.po.BaseClassifyAssignPo;
import com.wisdom.acm.base.service.BaseClassifyAssignService;
import com.wisdom.acm.base.vo.classify.BaseClassifyAssignVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/classify")
public class BaseClassifyAssignController extends BaseController {

    @Autowired
    private BaseClassifyAssignService classifyAssignService;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    /**
     * 获取分类码页签列表
     *
     * @return
     */
    @GetMapping(value = "/assign/{boCode}/{bizId}/list")
    public ApiResult queryClassifyAssignList(@PathVariable("boCode")String boCode, @PathVariable("bizId")Integer bizId) {
        List<BaseClassifyAssignVo> retList = classifyAssignService.queryClassifyAssignListByBoCodeAndBoId(boCode,bizId);
        return ApiResult.success(retList);
    }

    /**
     * 分配分类码
     * @param baseClassifyAssignForm
     * @return
     */
    @PostMapping(value = "/assign")
    public ApiResult assignClassify(@RequestBody @Valid BaseClassifyAssignForm baseClassifyAssignForm) {
        BaseClassifyAssignVo vo = classifyAssignService.assignClassify(baseClassifyAssignForm);
        return ApiResult.success(vo);
    }

    /**
     * 修改分类码的码值
     * @param baseUpdateClassifyAssignForm
     * @return
     */
    @PutMapping(value = "/assign/update")
    public ApiResult updateAssignClassify(@RequestBody @Valid BaseUpdateClassifyAssignForm baseUpdateClassifyAssignForm){
        BaseClassifyAssignVo vo = classifyAssignService.updateAssignClassify(baseUpdateClassifyAssignForm);
        return ApiResult.success(vo);
    }

    /**
     * 删除分类码分配
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/assign/delete")
    @AddLog(title = "删除分类码分配",module = LoggerModuleEnum.NONE)
    public ApiResult deleteClassifyAssign(@RequestBody List<Integer> ids) {
        String names = classifyAssignService.queryClassifyNamesByIds(ids);
        this.setAcmLogger(new AcmLogger("批量删除分类码分配，分类码码值如下："+names));
        classifyAssignService.deleteClassifyAssign(ids);
        return ApiResult.success();
    }
}
