package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseTmpldelvPo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;

public interface BaseTmpldelvMapper extends CommMapper<BaseTmpldelvPo> {

    /**
     * 交付物列表
     * @return
     */
    public List<BaseTmpldelvVo> selectTmpldelvList();

    /**
     * 交付物列表
     * @return
     */
    public List<BaseTmpldelvVo> selectTmpldelvAssignList(Integer taskId);

    /**
     * 交付物基本信息
     * @return
     */
    public BaseTmpldelvVo selectTmpldelvById(Integer tmpldelvId);

    /**
     * 根据交付物模板Id获取交付物列表
     * @param delvTypeId
     * @return
     */
    public List<BaseTmpldelvVo> selectDelvByTypeId(Integer delvTypeId);

}