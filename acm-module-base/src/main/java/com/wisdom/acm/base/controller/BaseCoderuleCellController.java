package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.coderule.BaseCoderuleCellAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleCellUpdateForm;
import com.wisdom.acm.base.po.BaseCoderuleCellPo;
import com.wisdom.acm.base.service.BaseCoderuleCellService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleCellVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/coderulecell")
public class BaseCoderuleCellController {

    @Autowired
    private BaseCoderuleCellService baseCoderuleCellService;

    /**
     * getCoderuleCellById
     * @param position
     * @return
     */
    @GetMapping("/{ruleId}/{position}/info")
    public ApiResult getCoderuleCellById(@PathVariable("ruleId")Integer ruleId,@PathVariable("position")Integer position){
        BaseCoderuleCellVo vo = baseCoderuleCellService.getCoderuleCellByRuleIdAndPosition(ruleId,position);
        return ApiResult.success(vo);
    }

    /**
     * byId
     * @param id
     * @return
     */
    @GetMapping("/{id}/info")
    public ApiResult getCoderuleCellById(@PathVariable("id")Integer id){
        BaseCoderuleCellVo vo = baseCoderuleCellService.getCoderuleCellById(id);
        return ApiResult.success(vo);
    }

    /**
     * add
     * @param form
     * @return
     */
    @PostMapping("/add")
    public ApiResult addCoderuleCell(@RequestBody @Valid BaseCoderuleCellAddForm form){
        BaseCoderuleCellPo po = baseCoderuleCellService.addCoderuleCell(form);
        BaseCoderuleCellVo vo = baseCoderuleCellService.getCoderuleCellById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * update
     * @param form
     * @return
     */
    @PutMapping("/update")
    public ApiResult updateCoderuleCell(@RequestBody @Valid BaseCoderuleCellUpdateForm form){
        BaseCoderuleCellPo po = baseCoderuleCellService.updateCoderuleCell(form);
        BaseCoderuleCellVo vo = baseCoderuleCellService.getCoderuleCellById(po.getId());
        return ApiResult.success(vo);
    }

    /**
     * delete
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public ApiResult deleteCoderuleCellByIds(@RequestBody List<Integer> ids){
        baseCoderuleCellService.deleteCoderuleCellByIds(ids);
        return ApiResult.success();
    }

    @GetMapping("/{ruleId}/list")
    public ApiResult queryByRuleId(@PathVariable("ruleId") Integer ruleId){
        List<BaseCoderuleCellPo> cells = baseCoderuleCellService.queryByRuleId(ruleId);
        return ApiResult.success(cells);
    }
}
