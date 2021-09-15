package com.wisdom.acm.wf.vo;

import com.wisdom.base.common.vo.sys.UserVo;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class WfDelegateVo {

    private Integer id;

    //委托人
    private UserVo assignee;

    //代理人
    private UserVo attorney;

    //开始时间
    private Date startTime;

    //结束时间
    private Date endTime;

    //是否全局
    private Integer global;

    //是否启用
    private Integer enable;

    //备注
    private String remark;
}
