package com.wisdom.acm.szxm.po.wf;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Author：wqd
 * Date：2020-03-30 9:32
 * Description：<描述>
 */
@Table(name = "szxm_activiti_screen")
@Data
public class ActivitiTemplatePo  extends BasePo {

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
