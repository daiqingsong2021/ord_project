package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseCustomFieldPo;
import com.wisdom.acm.base.vo.custom.BaseCustomFieldVo;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.po.BaseCustomPo;
import com.wisdom.base.common.vo.BaseCustomVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseCustomFieldMapper extends CommMapper<BaseCustomFieldPo> {

    /**
     *根据表名查询自定义字段信息
     * @param id 主键
     * @return SysCustomFieldVo
     */
    BaseCustomFieldVo queryById(@Param("id") Integer id);

    /**
     * 根据表名和自定义名称查询自定义字段信息
     * @param tableName
     * @param fieldName
     * @return
     */
    BaseCustomFieldVo queryByTableNameAndFieldName(@Param("tableName") String tableName, @Param("fieldName") String fieldName);

    /**
     *根据表名查询自定义字段信息
     * @param tableName 表名
     * @return SysCustomFieldVo
     */
    List<BaseCustomFieldVo> queryListByTableName(@Param("tableName") String tableName);

    /**
     *根据表名查询已启用的自定义字段信息
     * @param tableName 表名
     * @return SysCustomFieldVo
     */
    List<BaseCustomFieldVo> queryEnableListByTableName(@Param("tableName") String tableName);

    /**
     * 根据表名和自定义名称查询自定义字段信息
     * @param tableName 表名
     * @param id 业务主键
     * @return
     */
    BaseCustomVo queryValueListByTableNameAndId(@Param("tableName") String tableName, @Param("id") Integer id);

    /**
     * 保存自定义值
     * @param tableName 表名
     * @param customPo 自定义值
     */
    void saveCustomValueById(@Param("tableName") String tableName, @Param("po") BaseCustomPo customPo);

}
