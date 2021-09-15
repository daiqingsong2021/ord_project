package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.currency.BaseCurrencyAddForm;
import com.wisdom.acm.base.form.currency.BaseCurrencyUpdateForm;
import com.wisdom.acm.base.po.BaseCurrencyPo;
import com.wisdom.acm.base.vo.currency.BaseCurrencyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface BaseCurrencyService extends CommService<BaseCurrencyPo> {

    /**
     * 货币设置列表
     * @return
     */
    public List<BaseCurrencyVo> queryCurrencyList();

    /**
     * 获取默认货币
     * @return
     */
    public BaseCurrencyVo getDefaultCurrency();

    /**
     * 删除货币设置信息
     * @param
     * @return
     */
    public void deleteCurrencyByIds(List<Integer> ids);

    /**
     * 新增货币设置
     * @param baseCurrencyAddForm
     * @return
     */
    public BaseCurrencyPo addCurrency(BaseCurrencyAddForm baseCurrencyAddForm);

    /**
     * 货币基本信息
     * @param currencyId
     * @return
     */
    public BaseCurrencyVo getCurrencyById(Integer currencyId);

    /**
     * 更新货币基本信息
     * @param baseCurrencyUpdateForm
     * @return
     */
    public BaseCurrencyPo updateCurrency(BaseCurrencyUpdateForm baseCurrencyUpdateForm);

    /**
     * 通过id获取BaseCurrencyVo
     * @param id
     * @return
     */
    public BaseCurrencyVo getBaseCurrencyVo(Integer id);

    /**
     * 设为基准货币
     * @param id
     */
    public void updateCurrencyBase(Integer id);
}
