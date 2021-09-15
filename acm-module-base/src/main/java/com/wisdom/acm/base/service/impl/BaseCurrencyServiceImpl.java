package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.currency.BaseCurrencyAddForm;
import com.wisdom.acm.base.form.currency.BaseCurrencyUpdateForm;
import com.wisdom.acm.base.mapper.BaseCurrencyMapper;
import com.wisdom.acm.base.po.BaseCurrencyPo;
import com.wisdom.acm.base.service.BaseCurrencyService;
import com.wisdom.acm.base.vo.currency.BaseCurrencyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BaseCurrencyServiceImpl extends BaseService<BaseCurrencyMapper, BaseCurrencyPo> implements BaseCurrencyService {


    @Override
    public List<BaseCurrencyVo> queryCurrencyList() {
        List<BaseCurrencyVo> allCurrencyList = this.mapper.selectCurrencyList();
        return allCurrencyList;
    }

    @Override
    public BaseCurrencyVo getDefaultCurrency() {
        BaseCurrencyVo defaultCurrency = this.mapper.selectDefaultCurrency();
        return defaultCurrency;
    }

    @Override
    public void deleteCurrencyByIds(List<Integer> ids) {
        super.deleteByIds(ids);
    }

    @Override
    public BaseCurrencyPo addCurrency(BaseCurrencyAddForm baseCurrencyAddForm) {
        BaseCurrencyPo baseCurrencyPo = this.dozerMapper.map(baseCurrencyAddForm, BaseCurrencyPo.class);
        baseCurrencyPo.setCurrencyBase(0);
        List<BaseCurrencyPo> list = this.getBaseCurrencyPoByCode(baseCurrencyAddForm.getCurrencyCode());
        // 验证编码是否重复
        if(!ObjectUtils.isEmpty(list)){
            throw new BaseException("货币代码不能重复!");
        }
        super.insert(baseCurrencyPo);
        return baseCurrencyPo;
    }

    @Override
    public BaseCurrencyVo getCurrencyById(Integer currencyId) {
        BaseCurrencyVo defaultCurrency = this.mapper.selectCurrencyById(currencyId);
        return defaultCurrency;
    }

    @Override
    @AddLog(title = "修改货币设置",module = LoggerModuleEnum.BM_CURRENY)
    public BaseCurrencyPo updateCurrency(BaseCurrencyUpdateForm baseCurrencyUpdateForm) {
        BaseCurrencyPo baseCurrencyPo = this.selectById(baseCurrencyUpdateForm.getId());
        if(baseCurrencyPo == null){
            throw new BaseException("修改的货币信息不存在!");
        }

        // 添加修改日志
        this.addChangeLogger(baseCurrencyUpdateForm,baseCurrencyPo);

        List<BaseCurrencyPo> list = this.getBaseCurrencyPoByCode(baseCurrencyUpdateForm.getCurrencyCode());
        if(!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(baseCurrencyUpdateForm.getId())){
            throw new BaseException("货币代码不能重复");
        }
        this.dozerMapper.map(baseCurrencyUpdateForm, baseCurrencyPo);
        this.updateById(baseCurrencyPo);
        return baseCurrencyPo ;
    }

    @Override
    public BaseCurrencyVo getBaseCurrencyVo(Integer id) {
        BaseCurrencyVo baseCurrencyVo = this.mapper.selectOneBaseCurrencyVo(id);
        return baseCurrencyVo;
    }

    /**
     * 设为基准货币
     * @param id
     */
    @Override
    public void updateCurrencyBase(Integer id) {
        BaseCurrencyPo baseCurrencyPo = this.selectById(id);
        if(baseCurrencyPo == null){
            throw new BaseException("设置的货币信息不存在!");
        }
        List<BaseCurrencyPo> currencyList = selectListAll();
        if(!ObjectUtils.isEmpty(currencyList)){
            for (BaseCurrencyPo baseCurrencyPo1:currencyList){
                baseCurrencyPo1.setCurrencyBase(0);
                this.updateById(baseCurrencyPo1);
            }
        }
        //1代表基准货币
        baseCurrencyPo.setCurrencyBase(1);
        this.updateById(baseCurrencyPo);
    }

    /**
     * 验证编码是否重复
     *
     * @return
     */
    public List<BaseCurrencyPo> getBaseCurrencyPoByCode(String code){
        Example example = new Example(BaseCurrencyPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("currencyCode",code);
        List<BaseCurrencyPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list)? null : list;
    }
}
