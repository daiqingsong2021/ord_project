package com.wisdom.base.common.vo.plan.change;

import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

/**
 * @Author: szc
 * @Date: 2019/3/22 14:16
 * @Version 1.0
 */
@Data
public class PlanTaskChangeReleasaeTreeVo extends TreeVo<PlanTaskChangeReleasaeTreeVo> {


    private Integer taskId;

    private Integer changeId;
    //名称
    private String name;

    private StatusVo changeType;

    //节点类型
    private String nodeType;

    //
    private Integer taskType;

    //变更前数据
    private String oldData;

    //变更后数据
    private String newData;

    private Integer check;


























}
