package com.wisdom.acm.dc3.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc3.common.DateUtil;
import com.wisdom.acm.dc3.form.EnergyDetailUpdateForm;
import com.wisdom.acm.dc3.mapper.EnergyDetailMapper;
import com.wisdom.acm.dc3.po.EnergyDailyPo;
import com.wisdom.acm.dc3.po.EnergyDetailPo;
import com.wisdom.acm.dc3.service.EnergyDailyService;
import com.wisdom.acm.dc3.service.EnergyDetailService;
import com.wisdom.acm.dc3.vo.EnergyDetailVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@Slf4j
public class EnergyDetailServiceImpl extends BaseService<EnergyDetailMapper, EnergyDetailPo> implements EnergyDetailService
{

    @Autowired
    private EnergyDailyService EnergyDailyService;

    @Override
    public EnergyDetailVo selectById(Integer id) {
        EnergyDetailVo EnergyDetailVo = dozerMapper.map(super.selectById(id), EnergyDetailVo.class);//po对象转换为Vo对象
        return EnergyDetailVo;
    }

    /**
     * 根据参数查询EnergyDetailVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<EnergyDetailVo> selectByParams(Map<String, Object> mapWhere) {

        List<EnergyDetailVo>  EnergyDetailVoList= mapper.selectByParams(mapWhere);
        return EnergyDetailVoList;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<EnergyDetailVo> selectEnergyDatailPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<EnergyDetailVo> EnergyDetailVoList=mapper.selectByParams(mapWhere);
        PageInfo<EnergyDetailVo> pageInfo = new PageInfo<EnergyDetailVo>(EnergyDetailVoList);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @AddLog(title = "更新能耗日况详情数据", module = LoggerModuleEnum.ENERGY_DETAILEDSEARCH)
    public List<EnergyDetailVo> updateEnergyDetail(List<EnergyDetailUpdateForm> energyDailyUpdateForm) {
        List<EnergyDetailPo> energyDetailPoList= new ArrayList<>();
        List<String> ids= new ArrayList<>();
        String dailyId="";
        if(ObjectUtils.isEmpty(energyDailyUpdateForm))
        {
            return null;
        }
        for (EnergyDetailUpdateForm energyDetailUpdateForm : energyDailyUpdateForm)
        {
            EnergyDetailPo energyDetailPo = dozerMapper.map(energyDetailUpdateForm, EnergyDetailPo.class);//po对象转换为Vo对象
            energyDetailPoList.add(energyDetailPo);
            super.updateSelectiveById(energyDetailPo);
            ids.add(energyDetailPo.getId().toString());
            dailyId=energyDetailUpdateForm.getDailyId();
        }

        //更新天  月度数据
        EnergyDailyPo energyDailyPo= EnergyDailyService.selectById(dailyId);
        this.addLogger(DateUtil.getDateFormat(energyDailyPo.getRecordTime()),energyDailyPo.getLine(),energyDailyPo.getId().toString(),energyDailyPo.getReviewStatus());
        //只有审核通过的daily 数据才能更新到月度数据
        if("APPROVED".equals(energyDailyPo.getReviewStatus()))
        {
            //更新天数据
            EnergyDailyService.updateEnergyDaily(energyDailyPo.getId());
            EnergyDailyService.updateEnergyMonthly(energyDailyPo.getRecordTime(),energyDailyPo.getLine());
        }

        Map<String, Object> mapWhere =new HashMap<>();
        if(!ObjectUtils.isEmpty(ids))
        {
            mapWhere.put("ids",ids);
            return mapper.selectByParams(mapWhere)  ;
        }
        return null;

    }





}
