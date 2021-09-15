package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.coderule.BaseCoderuleAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleUpdateForm;
import com.wisdom.acm.base.po.BaseCoderulePo;
import com.wisdom.acm.base.service.BaseCoderuleService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleVo;
import com.wisdom.acm.base.vo.coderule.ReturnMsgVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coderule")
public class BaseCoderuleController extends BaseController {

    @Autowired
    private BaseCoderuleService baseCoderuleService;

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    @AddLog(title = "删除编码规则",module = LoggerModuleEnum.BM_CODERULE)
    public ApiResult deleteCoderule(@RequestBody List<Integer> ids){
        String ruleNames = baseCoderuleService.queryNamesByIds(ids,"ruleName");
        this.setAcmLogger(new AcmLogger("批量删除编码规则，名称如下："+ruleNames));
        baseCoderuleService.deleteCoderuleByIds(ids);
        return ApiResult.success();
    }

    /**
     * 获取单个规则数据
     * @param id
     * @return
     */
    @GetMapping("/{id}/info")
    public ApiResult getCoderuleById(@PathVariable("id")Integer id){
        BaseCoderuleVo vo = baseCoderuleService.getCoderuleById(id);
        return ApiResult.success(vo);
    }

    /**
     * 规则数据列表
     * @param boId
     * @return
     */
    @GetMapping("/{boId}/list")
    public ApiResult querryCoderuleListByboId(@PathVariable("boId")Integer boId){
        List<BaseCoderuleVo> voList = baseCoderuleService.querryCoderuleListByboId(boId);
        return ApiResult.success(voList);
    }

    /**
     * add
     * @param form
     * @return
     */
    @AddLog(title = "增加编码规则",module = LoggerModuleEnum.BM_CODERULE)
    @PostMapping("/add")
    public ApiResult addCoderule(@RequestBody @Valid BaseCoderuleAddForm form){
        this.setAcmLogger(new AcmLogger("增加编码规则，名称为："+form.getRuleName()));
        BaseCoderulePo po = baseCoderuleService.addCoderule(form);
        BaseCoderuleVo vo = baseCoderuleService.getCoderuleById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * update
     * @param form
     * @return
     */
    @PutMapping("/update")
    public ApiResult updateCoderule(@RequestBody @Valid BaseCoderuleUpdateForm form){
        BaseCoderulePo po = baseCoderuleService.updateCoderule(form);
        BaseCoderuleVo vo = baseCoderuleService.getCoderuleById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * 校验correct/error
     * @return
     */
    @PutMapping("/{ruleId}/check")
    public ApiResult updateBaseCoderuleStatus(@PathVariable("ruleId")Integer ruleId){
        ReturnMsgVo msgVo = baseCoderuleService.updateBaseCoderuleStatus(ruleId);
        return ApiResult.success(msgVo);
    }


    //test
    @GetMapping("/tables")
    public ApiResult queryTables(){
        List<SelectVo> selectVos = this.baseCoderuleService.queryTables();

        return ApiResult.success(selectVos);
    }

    @GetMapping("/{tableName}/fields")
    public ApiResult queryFieldsByTableName(@PathVariable("tableName")String tableName){
        List<SelectVo> selectVos = this.baseCoderuleService.queryFieldsByTableName(tableName);

        return ApiResult.success(selectVos);
    }

    @GetMapping("/{boId}/default")
    public ApiResult getDefaultCodeRuleByBoId(@PathVariable("boId")Integer boId){
        BaseCoderuleVo coderule = baseCoderuleService.getDefaultByBoId(boId);
        return ApiResult.success(coderule);
    }
}
