package com.wisdom.acm.activiti.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Table;

@Table(name = "wsd_doc_favorite")
@Data
public class WorkflowPo extends BasePo {
    /**
     * 标题
     */
    private String title;
    /**
     * 流程实例
     */
    private String wfInstance;
    /**
     * 表单
     */
    private String wfForm;
    /**
     * 业务对象
     */
    private String biz_obj;
    /**
     * 计划id
     */
    private Integer defineId;
    /**
     * 当前节点
     */
    private String wfNode;
    /**
     * 项目id
     */
    private Integer projectId;

}
