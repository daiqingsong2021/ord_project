package com.wisdom.acm.dc2.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

@Data
public class DailyChangeVersionAddForm extends BaseForm
{

    /**
     * 模块记录的id
     */
    private String moudleRecordId;
    /**
     *模块名称
     */
    private String moudleName;

    /**
     *模块修改备注
     */
    private String modifyRemark;
    /**
     *修改内容
     */
    private String modifyContent;

}
