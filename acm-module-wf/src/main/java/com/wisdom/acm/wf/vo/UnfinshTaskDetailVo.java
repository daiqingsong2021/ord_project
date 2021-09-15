package com.wisdom.acm.wf.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UnfinshTaskDetailVo {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 停留时间
     */
    private String stayTime;

    /**
     * 创建时间
     */
    private Date creatTime;
}
