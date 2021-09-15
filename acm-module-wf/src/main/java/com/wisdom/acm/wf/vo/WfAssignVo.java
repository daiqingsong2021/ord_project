package com.wisdom.acm.wf.vo;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class WfAssignVo extends TreeVo {

    /**
     * 流程定义显示名称
     */
    private String wfTitle;
    /**
     * 流程定义名称
     */
    private String wfDefName;

    /**
     * 计划类型
     */
    private String type;

    /**
     * 发布状态
     */
    private String status;

    /**
     * 说明
     */
    private String remark;

}
