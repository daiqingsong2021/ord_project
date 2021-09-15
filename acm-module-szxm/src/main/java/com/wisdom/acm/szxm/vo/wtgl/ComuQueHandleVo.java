package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.StatusVo;
import lombok.Data;

import java.util.Date;

@Data
public class ComuQueHandleVo {

    private Integer id;

    //处理结果说明
    private String handleResult;

    //处理时间
    private Date handleTime;

    //处理人
    private String handleUser;

    //审批说明
    private String approveResult;

    //审批时间
    private Date approveTime;

    //审批人
    private String approveUser;

    //审批状态
    private StatusVo approveStatus;

    //跟踪记录创建者
    private Integer creator;

    //附件
    private Integer fileCount;

    //处理记录状态
    private StatusVo handleStatus;

    //是否最新
    private boolean latest;
}
