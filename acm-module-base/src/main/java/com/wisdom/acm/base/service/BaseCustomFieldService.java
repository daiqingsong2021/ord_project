package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.custom.BaseCustomFieldSaveForm;
import com.wisdom.acm.base.form.custom.BaseCustomFieldUpdateForm;
import com.wisdom.acm.base.po.BaseCustomFieldPo;
import com.wisdom.acm.base.vo.custom.BaseCustomFieldVo;
import com.wisdom.acm.base.vo.custom.BaseCustomValueVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;

public interface BaseCustomFieldService extends CommService<BaseCustomFieldPo> {

    /**
     * 获取自定义配置信息
     * @param tableName 表名
     * @return SysCustomFieldVo
     */
    List<BaseCustomFieldVo> queryListByTableName(String tableName);

    /**
     * 获取自定义配置信息
     * @param tableName 表名
     * @return SysCustomFieldVo
     */
    BaseCustomFieldVo queryByTableNameAndFieldName(String tableName, String fieldName);

    /**
     * 保存自定义配置
     * @param form 表单
     * @return SysCustomFieldVo
     */
    public BaseCustomFieldVo save(BaseCustomFieldSaveForm form);

    /**
     * 获取自定义配置信息
     * @param tableName 表名
     * @param id 主键
     * @return SysCustomFieldVo
     */
    List<BaseCustomValueVo> queryValueListByTableNameAndId(String tableName, Integer id);

    /**
     * 保存自定义值
     * @param form 表单
     */
    public void saveCustomValue(BaseCustomFieldUpdateForm form);

}
