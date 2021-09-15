package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.coderule.BaseCoderuleBoAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleBoUpdateForm;
import com.wisdom.acm.base.po.BaseCoderuleBoPo;
import com.wisdom.acm.base.service.BaseCoderuleBoService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleBoVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coderulebo")
public class BaseCoderuleBoController extends BaseController {

    @Autowired
    private BaseCoderuleBoService baseCoderuleboService;

    /**
     * 编码规则-业务对象列表
     * @return
     */
    @GetMapping("/list")
    public ApiResult querryCoderuleboList(){
        List<BaseCoderuleBoVo> voList = baseCoderuleboService.querryCoderuleboList();
        return ApiResult.success(voList);
    }

    /**
     * 编码规则-业务对象的基本信息
     * @param id
     * @return
     */
    @GetMapping("/{id}/info")
    public ApiResult getCoderuleboById(@PathVariable("id")Integer id){
        BaseCoderuleBoVo vo = baseCoderuleboService.getCoderuleboById(id);
        return ApiResult.success(vo);
    }

    /**
     * addCoderulebo
     * @param form
     * @return
     */
    @PostMapping("/add")
    @AddLog(title = "增加编码规则业务对象",module = LoggerModuleEnum.BM_CODERULE)
    public ApiResult addCoderulebo(@RequestBody @Valid BaseCoderuleBoAddForm form){
        //新增修改bo_code都不可重复
        this.setAcmLogger(new AcmLogger("增加编码规则业务对象，名称为："+form.getBoName()));
        BaseCoderuleBoPo po = baseCoderuleboService.addCoderulebo(form);
        BaseCoderuleBoVo vo = baseCoderuleboService.getCoderuleboById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * updateCoderulebo
     * @param form
     * @return
     */
    @PutMapping("/update")
    public ApiResult updateCoderulebo(@RequestBody @Valid BaseCoderuleBoUpdateForm form){
        BaseCoderuleBoPo po = baseCoderuleboService.updateCoderulebo(form);
        BaseCoderuleBoVo vo = baseCoderuleboService.getCoderuleboById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * deleteCoderulebo
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @AddLog(title = "删除编码规则业务对象",module = LoggerModuleEnum.BM_CODERULE)
    public ApiResult deleteCoderulebo(@PathVariable("id")Integer id){
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        String boNames = baseCoderuleboService.queryNamesByIds(ids,"boName");
        this.setAcmLogger(new AcmLogger("删除编码规则业务对象，名称为："+boNames));
        baseCoderuleboService.deleteCoderulebo(id);
        return ApiResult.success();
    }

    @GetMapping("/code/{code}")
    public ApiResult getCodeRuleBoByCode(@PathVariable("code") String code){
        BaseCoderuleBoVo bo =  baseCoderuleboService.getByBoCode(code);
        return ApiResult.success(bo);
    }
}
