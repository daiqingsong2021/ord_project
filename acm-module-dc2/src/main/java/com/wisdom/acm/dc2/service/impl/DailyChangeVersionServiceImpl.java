package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.common.DateUtil;
import com.wisdom.acm.dc2.form.DailyChangeVersionAddForm;
import com.wisdom.acm.dc2.mapper.DailyChangeVersionMapper;
import com.wisdom.acm.dc2.po.DailyChangeVersionPo;
import com.wisdom.acm.dc2.service.DailyChangeVersionService;
import com.wisdom.acm.dc2.vo.DailyChangeVersionVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DailyChangeVersionServiceImpl extends BaseService<DailyChangeVersionMapper, DailyChangeVersionPo> implements DailyChangeVersionService
{




    /**
     * 新增补充情况说明
     * @param changeVersionAddForm
     * @return
     */
    @Override
    @AddLog(title = "新增补充情况说明", module = LoggerModuleEnum.DAILY_REPRESENTATION,initContent = true)
    public DailyChangeVersionVo addDailyChangeVersion(DailyChangeVersionAddForm changeVersionAddForm)
    {
        DailyChangeVersionPo changeVersionPo = dozerMapper.map(changeVersionAddForm, DailyChangeVersionPo.class);
        super.insert(changeVersionPo);
        DailyChangeVersionVo changeVersionVo = dozerMapper.map(changeVersionPo, DailyChangeVersionVo.class);//po对象转换为Vo对象
        return changeVersionVo;
    }

    @Override
    @AddLog(title = "删除补充情况说明", module = LoggerModuleEnum.DAILY_REPRESENTATION)
    public void deleteDailyChangeVersion(Integer id)
    {
        //添加日志
        DailyChangeVersionPo changeVersionPo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(changeVersionPo))
        {
            logger.setRecordId(String.valueOf(changeVersionPo.getId()));
            this.addDeleteLogger(logger);
        }
        super.deleteById(id);
    }



    @Override
    public DailyChangeVersionVo selectById(Integer id) {
        DailyChangeVersionVo changeVersionVo = dozerMapper.map(super.selectById(id), DailyChangeVersionVo.class);//po对象转换为Vo对象
        return changeVersionVo;
    }

    /**
     * 根据参数查询DailyChangeVersionVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<DailyChangeVersionVo> selectByParams(Map<String, Object> mapWhere) {

        List<DailyChangeVersionVo>  changeVersionVoList= mapper.selectByParams(mapWhere);
        return changeVersionVoList;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<DailyChangeVersionVo> selectDailyChangeVersionList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<DailyChangeVersionVo> changeVersionVoList=mapper.selectByParams(mapWhere);
        PageInfo<DailyChangeVersionVo> pageInfo = new PageInfo<DailyChangeVersionVo>(changeVersionVoList);
        return pageInfo;
    }



}
