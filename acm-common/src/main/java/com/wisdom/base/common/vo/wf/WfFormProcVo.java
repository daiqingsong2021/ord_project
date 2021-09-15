package com.wisdom.base.common.vo.wf;

import lombok.Data;

@Data
public class WfFormProcVo {
    /**
     * 流程实例id
     */
    private String procInstId;
    /**
     * 标题
     */
    private String title;
    /**
     * 创建人
     */
    private Integer creator;
}
