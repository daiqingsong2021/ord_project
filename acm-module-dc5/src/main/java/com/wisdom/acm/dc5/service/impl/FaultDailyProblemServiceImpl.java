package com.wisdom.acm.dc5.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.common.SzxmCommonUtil;
import com.wisdom.acm.dc5.common.SzxmEnumsUtil;
import com.wisdom.acm.dc5.form.FaultDailyProblemAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemUpdateForm;
import com.wisdom.acm.dc5.mapper.FaultDailyProblemMapper;
import com.wisdom.acm.dc5.po.FaultDailyProblemPo;
import com.wisdom.acm.dc5.service.FaultDailyProblemService;
import com.wisdom.acm.dc5.vo.FaultDailyProblemVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FaultDailyProblemServiceImpl extends BaseService<FaultDailyProblemMapper, FaultDailyProblemPo> implements FaultDailyProblemService {


    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    /**
     * 新增
     * @param
     * @return
     */
    @Override
    public FaultDailyProblemVo addFaultDailyProblem(FaultDailyProblemAddForm faultDailyProblemAddForm) {

        FaultDailyProblemPo faultDailyProblemPo = dozerMapper.map(faultDailyProblemAddForm, FaultDailyProblemPo.class);
        faultDailyProblemPo.setRecordTime(new Date());
        super.insert(faultDailyProblemPo);
        FaultDailyProblemVo map = dozerMapper.map(faultDailyProblemPo, FaultDailyProblemVo.class);
        map.setProblemStatusDesc(SzxmEnumsUtil.ProblemStatusEnum.getNameByCode(map.getProblemStatus()));
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteFaultDailyProblem(List<Integer> idList) {
        //删除自身的表
        super.deleteByIds(idList);
        //删除odr_fault_daily_problem_deal数据
        if(CollectionUtils.isNotEmpty(idList)){
            Map<String,Object> deleteMap = new HashMap<>();
            deleteMap.put("problemIdList",idList);
            mapper.deleteFaultDailyProblemDealByProblemIds(deleteMap);
        }
    }

    /**
     * 更新
     * @param faultDailyProblemUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "更新故障日况问题", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT )
    public FaultDailyProblemVo updateFaultDailyProblem(FaultDailyProblemUpdateForm faultDailyProblemUpdateForm) {
        FaultDailyProblemPo po = mapper.selectByPrimaryKey(faultDailyProblemUpdateForm.getId());
        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(faultDailyProblemUpdateForm,po,"");
        FaultDailyProblemPo trainSchedulePo = dozerMapper.map(faultDailyProblemUpdateForm, FaultDailyProblemPo.class);
        super.updateSelectiveById(trainSchedulePo);
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",faultDailyProblemUpdateForm.getId());
        FaultDailyProblemVo faultDailyProblemVo = mapper.selectFaultDailyProblemByParams(queryMap).get(0);
        faultDailyProblemVo.setProblemStatusDesc(SzxmEnumsUtil.ProblemStatusEnum.getNameByCode(faultDailyProblemVo.getProblemStatus()));
        return faultDailyProblemVo;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<FaultDailyProblemVo> selectFaultDailyProblemPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<FaultDailyProblemVo> faultDailyVoList=mapper.selectFaultDailyProblemByParams(mapWhere);
        PageInfo<FaultDailyProblemVo> pageInfo = new PageInfo<>(faultDailyVoList);
        if(!ObjectUtils.isEmpty(pageInfo.getList())){
            //Map<String, String> lineMap = szxmCommonUtil.getDictionaryMap("line");
            //匹配线路的名称和状态
            for (FaultDailyProblemVo faultDailyVo : pageInfo.getList()){
                //faultDailyVo.setLineName(lineMap.get(faultDailyVo.getLine()));
                faultDailyVo.setProblemStatusDesc(SzxmEnumsUtil.ProblemStatusEnum.getNameByCode(faultDailyVo.getProblemStatus()));
            }
        }
        return pageInfo;
    }
    /**
     * 分页查询所有列表
     * @param mapWhere
     * @return
     */
    @Override
    public List<FaultDailyProblemVo> selectFaultDailyProblemList(Map<String, Object> mapWhere) {
        List<FaultDailyProblemVo> faultDailyVoList = mapper.selectFaultDailyProblemByParams(mapWhere);
        //Map<String, String> lineMap = szxmCommonUtil.getDictionaryMap("line");
        //匹配线路的名称和状态
        for (FaultDailyProblemVo faultDailyVo : faultDailyVoList){
           // faultDailyVo.setLineName(lineMap.get(faultDailyVo.getLine()));
            faultDailyVo.setProblemStatusDesc(SzxmEnumsUtil.ProblemStatusEnum.getNameByCode(faultDailyVo.getProblemStatus()));
        }
        return faultDailyVoList;
    }

}
