package com.wisdom.acm.dc2.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_modle_daily_change")
@Data
public class DailyChangeVersionPo extends BasePo
{

    /**
     * 模块记录的id
     */
    @Column(name = "moudle_record_id")
    private String moudleRecordId;
    /**
     *模块名称
     */
    @Column(name = "moudle_name")
    private String moudleName;


    /**
     *模块修改备注
     */
    @Column(name = "modify_remark")
    private String modifyRemark;
    /**
     *修改内容
     */
    @Column(name = "modify_content")
    private String modifyContent;


}
