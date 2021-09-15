package com.wisdom.acm.activiti.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author：wqd
 * Date：2020-03-30 9:32
 * Description：<描述>
 */
@Table(name = "szxm_activiti_screen")
@Data
public class ActivitiTemplatePo {

    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 流程节点id
     */
    @Column(name = "activiti_id")
    private String activitiId;

    /**
     * 筛选编码
     */
    @Column(name = "screen_code")
    private String screenCode;
}
