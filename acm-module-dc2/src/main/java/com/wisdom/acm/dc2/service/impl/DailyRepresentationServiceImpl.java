package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.common.DateUtil;
import com.wisdom.acm.dc2.common.SzxmCommonUtil;
import com.wisdom.acm.dc2.form.DailyRepresentationAddForm;
import com.wisdom.acm.dc2.form.DailyRepresentationUpdateForm;
import com.wisdom.acm.dc2.mapper.DailyRepresentationMapper;
import com.wisdom.acm.dc2.mapper.TrainMonthlyMapper;
import com.wisdom.acm.dc2.po.DailyRepresentationPo;
import com.wisdom.acm.dc2.service.DailyRepresentationService;
import com.wisdom.acm.dc2.service.TrainMonthlyService;
import com.wisdom.acm.dc2.vo.DailyRepresentationVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DailyRepresentationServiceImpl extends BaseService<DailyRepresentationMapper, DailyRepresentationPo> implements DailyRepresentationService
{




    /**
     * 新增补充情况说明
     * @param representationAddForm
     * @return
     */
    @Override
    @AddLog(title = "新增补充情况说明", module = LoggerModuleEnum.DAILY_REPRESENTATION,initContent = true)
    public DailyRepresentationVo addDailyRepresentation(DailyRepresentationAddForm representationAddForm)
    {
        DailyRepresentationPo representationPo = dozerMapper.map(representationAddForm, DailyRepresentationPo.class);
        super.insert(representationPo);
        DailyRepresentationVo representationVo = dozerMapper.map(representationPo, DailyRepresentationVo.class);//po对象转换为Vo对象
        return representationVo;
    }

    @Override
    @AddLog(title = "删除补充情况说明", module = LoggerModuleEnum.DAILY_REPRESENTATION)
    public void deleteDailyRepresentation(Integer id)
    {
        //添加日志
        DailyRepresentationPo representationPo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(representationPo))
        {
            logger.setLine(representationPo.getLine());
            logger.setRecordTime(DateUtil.getDateFormat(representationPo.getRecordTime()));
            logger.setRecordId(String.valueOf(representationPo.getId()));
            this.addDeleteLogger(logger);
        }
        super.deleteById(id);
    }


    /**
     * 更新
     * @param representationUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "更新补充情况说明", module = LoggerModuleEnum.DAILY_REPRESENTATION)
    public DailyRepresentationVo updateDailyRepresentation(DailyRepresentationUpdateForm representationUpdateForm)
    {
        DailyRepresentationPo po =this.selectById(representationUpdateForm.getId());
        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(representationUpdateForm,po);
        DailyRepresentationPo representationPo = dozerMapper.map(representationUpdateForm, DailyRepresentationPo.class);
        super.updateSelectiveById(representationPo);
        DailyRepresentationPo tempDailyRepresentationPo =super.selectById(representationPo.getId());

        DailyRepresentationVo representationVo = dozerMapper.map(tempDailyRepresentationPo, DailyRepresentationVo.class);//po对象转换为Vo对象
        return representationVo;
    }

    @Override
    public DailyRepresentationVo selectById(Integer id) {
        DailyRepresentationVo representationVo = dozerMapper.map(super.selectById(id), DailyRepresentationVo.class);//po对象转换为Vo对象
        return representationVo;
    }

    /**
     * 根据参数查询DailyRepresentationVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<DailyRepresentationVo> selectByParams(Map<String, Object> mapWhere) {

        List<DailyRepresentationVo>  representationVoList= mapper.selectByParams(mapWhere);
        return representationVoList;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<DailyRepresentationVo> selectDailyRepresentationList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<DailyRepresentationVo> representationVoList=mapper.selectByParams(mapWhere);
        PageInfo<DailyRepresentationVo> pageInfo = new PageInfo<DailyRepresentationVo>(representationVoList);
        return pageInfo;
    }



}
