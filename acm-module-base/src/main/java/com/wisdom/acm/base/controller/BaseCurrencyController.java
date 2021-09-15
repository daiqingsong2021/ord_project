package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.currency.BaseCurrencyAddForm;
import com.wisdom.acm.base.form.currency.BaseCurrencyUpdateForm;
import com.wisdom.acm.base.po.BaseCurrencyPo;
import com.wisdom.acm.base.service.BaseCurrencyService;
import com.wisdom.acm.base.vo.currency.BaseCurrencyVo;
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
@RequestMapping("/currency")
public class BaseCurrencyController extends BaseController {

    @Autowired
    private BaseCurrencyService baseCurrencyService;

    /**
     * 货币设置列表
     * @return
     */
    @GetMapping("/list")
    public ApiResult queryCurrencyList() {
        List<BaseCurrencyVo> allCurrencyList = baseCurrencyService.queryCurrencyList();
        return ApiResult.success(allCurrencyList);
    }

    /**
     * 获取默认货币
     * @return
     */
    @GetMapping("/defaultCurrency")
    public ApiResult getDefaultCurrency() {
        BaseCurrencyVo defaultCurrency = baseCurrencyService.getDefaultCurrency();
        return ApiResult.success(defaultCurrency);
    }

    /**
     * 删除货币设置信息
     * @param
     * @return
     */

    //@RequestMapping(value = "/{id}", method = {RequestMethod.DELETE}, produces = "application/json;charset=UTF-8")
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除货币设置",module = LoggerModuleEnum.BM_CURRENY)
    public ApiResult deleteCurrencyById(@RequestBody List<Integer> ids) {
        String names = baseCurrencyService.queryNamesByIds(ids,"currencyName");
        this.setAcmLogger(new AcmLogger("批量删除货币设置信息，名称为：" + names));
        baseCurrencyService.deleteCurrencyByIds(ids);
        return ApiResult.success();
    }

    /**
     * 新增货币设置
     * @param baseCurrencyAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "新增货币设置",module = LoggerModuleEnum.BM_CURRENY)
    public ApiResult addCurrency(@RequestBody @Valid BaseCurrencyAddForm baseCurrencyAddForm) {
        BaseCurrencyPo baseCurrencyPo = baseCurrencyService.addCurrency(baseCurrencyAddForm);
        BaseCurrencyVo baseCurrencyVo = baseCurrencyService.getBaseCurrencyVo(baseCurrencyPo.getId());
        return ApiResult.success(baseCurrencyVo);
    }

    /**
     * 货币基本信息
     * @param currencyId
     * @return
     */
    @GetMapping("/{id}/info")
    public ApiResult getCurrencyById(@PathVariable("id") Integer currencyId) {
        BaseCurrencyVo defaultCurrency = baseCurrencyService.getCurrencyById(currencyId);
        return ApiResult.success(defaultCurrency);
    }

    /**
     * 更新货币基本信息
     * @param baseCurrencyUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateCurrency(@RequestBody @Valid BaseCurrencyUpdateForm baseCurrencyUpdateForm) {
        BaseCurrencyPo baseCurrencyPo = baseCurrencyService.updateCurrency(baseCurrencyUpdateForm);
        BaseCurrencyVo baseCurrencyVo = baseCurrencyService.getBaseCurrencyVo(baseCurrencyPo.getId());
        return ApiResult.success(baseCurrencyVo);
    }

    /**
     * 设为基准货币
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}/setCurrencyBase")
    @AddLog(title = "设为基准货币",module = LoggerModuleEnum.BM_CURRENY)
    public ApiResult updateCurrency(@PathVariable("id") Integer id) {
        BaseCurrencyVo baseCurrencyVo = baseCurrencyService.getBaseCurrencyVo(id);
        this.setAcmLogger(new AcmLogger("将\"" +baseCurrencyVo.getCurrencyName() +"\"设为基准货币" ));
        baseCurrencyService.updateCurrencyBase(id);
        return ApiResult.success();
    }

}
