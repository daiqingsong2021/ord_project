package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.coderule.BaseCoderuleTypeAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleTypeUpdateForm;
import com.wisdom.acm.base.po.BaseCoderuleTypePo;
import com.wisdom.acm.base.service.BaseCoderuleTypeService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleTypeVo;
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
@RequestMapping("/coderuletype")
public class BaseCoderuleTypeController extends BaseController {

    @Autowired
    private BaseCoderuleTypeService baseCoderuleTypeService;

    /**
     * 规则数据列表
     * @param boId
     * @return
     */
    @GetMapping("/{boId}/list")
    public ApiResult querryCoderuleTypeListByboId(@PathVariable("boId")Integer boId){
        List<BaseCoderuleTypeVo> voList = baseCoderuleTypeService.querryCoderuleTypeListByboId(boId);
        return ApiResult.success(voList);
    }

    /**
     * byId
     * @param id
     * @return
     */
    @GetMapping("/{id}/info")
    public ApiResult getCoderuleTypeById(@PathVariable("id")Integer id){
        BaseCoderuleTypeVo vo = baseCoderuleTypeService.getCoderuleTypeById(id);
        return ApiResult.success(vo);
    }

    /**
     * addCoderuleType
     * @param form
     * @return
     */
    @PostMapping("/add")
    @AddLog(title = "增加编码规则类型",module = LoggerModuleEnum.BM_CODERULE)
    public ApiResult addCoderuleType(@RequestBody @Valid BaseCoderuleTypeAddForm form){
        this.setAcmLogger(new AcmLogger("增加编码规则类型，名称为："+form.getRuleTypeName()));
        BaseCoderuleTypePo po = baseCoderuleTypeService.addCoderuleType(form);
        BaseCoderuleTypeVo vo = baseCoderuleTypeService.getCoderuleTypeById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * updateCoderuleType
     * @param form
     * @return
     */
    @PutMapping("/update")
    public ApiResult updateCoderuleType(@RequestBody @Valid BaseCoderuleTypeUpdateForm form){
        BaseCoderuleTypePo po = baseCoderuleTypeService.updateCoderuleType(form);
        BaseCoderuleTypeVo vo = baseCoderuleTypeService.getCoderuleTypeById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * deleteCoderuleType
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    @AddLog(title = "删除编码规则类型",module = LoggerModuleEnum.BM_CODERULE)
    public ApiResult deleteCoderuleTypeByIds(@RequestBody List<Integer> ids){
        String ruleTypeNames = baseCoderuleTypeService.queryNamesByIds(ids,"ruleTypeName");
        this.setAcmLogger(new AcmLogger("批量删除编码规则类型，名称如下："+ruleTypeNames));
        baseCoderuleTypeService.deleteCoderuleTypeByIds(ids);
        return ApiResult.success();
    }

}
