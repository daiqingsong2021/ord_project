package com.wisdom.base.common.vo.plan.change;

import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/3/29 10:02
 * @Version 1.0
 */
@Data
public class PlanTaskChangeRecordVo {

    private int id;

    private StatusVo changeType;

    private String oldData;

    private String newData;

    private StatusVo status;

    private String changeWay;

    //创建日期
    private Date creatTime;
    //创建人
    private UserVo creator;
    //变更申请原因
    private String changeReason;

    //变更影响分析
    private String changeEffect;

    //采取措施说明
    private String changeWayDesc;

}
