package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseTmplTaskMapper extends CommMapper<BaseTmplTaskPo> {

    /**
     * 获取计划模板的数据
     * @return
     */
    public List<BaseTmplTaskVo> selectTmplTaskList();

    /**
     * 根据id获取模板wbs和task数据
     */
    public BaseTmplTaskVo selectTmplTaskById(Integer TmpltaskId);

    /**
     * 根据类型ID删除计划定义
     * @param id
     */
    public void deleTmplTaskById(@Param("id") Integer id);

    /**
     * 根据tmplId查找
     * @param tmplId
     * @return
     */
    public List<BaseTmplTaskVo> selectTmplTaskByTmplId(Integer tmplId);
}
