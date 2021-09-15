package com.wisdom.acm.dc5.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.common.SzxmCommonUtil;
import com.wisdom.acm.dc5.form.FaultDailyProblemDealAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemDealUpdateForm;
import com.wisdom.acm.dc5.mapper.FaultDailyProblemDealMapper;
import com.wisdom.acm.dc5.po.FaultDailyProblemDealPo;
import com.wisdom.acm.dc5.service.FaultDailyProblemDealService;
import com.wisdom.acm.dc5.vo.FaultDailyProblemDealVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FaultDailyProblemDealServiceImpl extends BaseService<FaultDailyProblemDealMapper, FaultDailyProblemDealPo> implements FaultDailyProblemDealService {


    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    /**
     * 新增
     * @param
     * @return
     */
    @Override
    public FaultDailyProblemDealVo addFaultDailyProblemDeal(FaultDailyProblemDealAddForm faultDailyProblemDealAddForm) {

        FaultDailyProblemDealPo faultDailyProblemDealPo = dozerMapper.map(faultDailyProblemDealAddForm, FaultDailyProblemDealPo.class);
        faultDailyProblemDealPo.setRecordTime(new Date());
        super.insert(faultDailyProblemDealPo);
        return dozerMapper.map(faultDailyProblemDealPo, FaultDailyProblemDealVo.class);
    }

    @Override
    public void deleteFaultDailyProblemDeal(List<Integer> idList) {
        super.deleteByIds(idList);
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<FaultDailyProblemDealVo> selectFaultDailyProblemDealPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<FaultDailyProblemDealVo> faultDailyVoList=mapper.selectFaultDailyProblemDealByParams(mapWhere);
        PageInfo<FaultDailyProblemDealVo> pageInfo = new PageInfo<>(faultDailyVoList);
        return pageInfo;
    }
    /**
     * 分页查询所有列表
     * @param mapWhere
     * @return
     */
    @Override
    public List<FaultDailyProblemDealVo> selectFaultDailyProblemDealList(Map<String, Object> mapWhere) {
        List<FaultDailyProblemDealVo> faultDailyVoList = mapper.selectFaultDailyProblemDealByParams(mapWhere);
        return faultDailyVoList;
    }

    /**
     * 更新
     * @param faultDailyProblemDealUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "更新故障日况处理详情", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT)
    public FaultDailyProblemDealVo updateFaultDailyProblemDeal(FaultDailyProblemDealUpdateForm faultDailyProblemDealUpdateForm)
    {
        FaultDailyProblemDealPo po = mapper.selectByPrimaryKey(faultDailyProblemDealUpdateForm.getId());
        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(faultDailyProblemDealUpdateForm,po,"");
        FaultDailyProblemDealPo trainSchedulePo = dozerMapper.map(faultDailyProblemDealUpdateForm, FaultDailyProblemDealPo.class);
        super.updateSelectiveById(trainSchedulePo);
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",faultDailyProblemDealUpdateForm.getId());
        return mapper.selectFaultDailyProblemDealByParams(queryMap).get(0);
}

}
