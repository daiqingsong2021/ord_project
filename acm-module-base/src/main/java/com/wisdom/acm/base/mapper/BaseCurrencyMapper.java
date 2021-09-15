package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseCurrencyPo;
import com.wisdom.acm.base.vo.currency.BaseCurrencyVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;

public interface BaseCurrencyMapper extends CommMapper<BaseCurrencyPo> {

    /**
     * 货币设置列表
     * @return
     */
    public List<BaseCurrencyVo> selectCurrencyList();

    /**
     * 货币基本信息
     * @param currencyId
     * @return
     */
    public BaseCurrencyVo selectCurrencyById(Integer currencyId);

    /**
     * 获取默认货币
     * @return
     */
    public BaseCurrencyVo selectDefaultCurrency();

    /**
     * 通过id获取BaseCurrencyVo
     * @param id
     * @return
     */
    public BaseCurrencyVo selectOneBaseCurrencyVo(Integer id);

}