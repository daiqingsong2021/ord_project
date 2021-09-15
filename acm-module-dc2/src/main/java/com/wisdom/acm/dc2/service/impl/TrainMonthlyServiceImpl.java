package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.common.DateUtil;
import com.wisdom.acm.dc2.common.SzxmCommonUtil;
import com.wisdom.acm.dc2.form.TrainDailyAddForm;
import com.wisdom.acm.dc2.form.TrainDailyUpdateForm;
import com.wisdom.acm.dc2.mapper.TrainDailyMapper;
import com.wisdom.acm.dc2.mapper.TrainMonthlyMapper;
import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.po.TrainMonthlyPo;
import com.wisdom.acm.dc2.service.TrainDailyService;
import com.wisdom.acm.dc2.service.TrainMonthlyService;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainMonthlyServiceImpl extends BaseService<TrainMonthlyMapper, TrainMonthlyPo> implements TrainMonthlyService
{



    /**
     * 根据参数查询TrainDailyVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<TrainMonthlyPo> selectByParams(Map<String, Object> mapWhere) {

        List<TrainMonthlyPo>  trainMonthlyPoList= mapper.selectByParams(mapWhere);
        return trainMonthlyPoList;
    }






}
