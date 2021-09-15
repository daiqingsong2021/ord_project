package com.wisdom.base.common.vo.plan.task;

import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;

/**
 * 计划看板
 */
@Data
public class KanbanTaskVo {

    private Integer id;

    //代码
    private String taskCode;

    //名称
    private String taskName;

    //所属标段x
    private DictionaryVo section;

    //所属施工专业（依据绑定的计划定义）
    private DictionaryVo construction;

    //责任主体
    private DictionaryVo org;

    //责任人
    private DictionaryVo user;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    //实际开始时间
    private Date actStartTime;

    //实际完成时间
    private Date actEndTime;

    //完成百分比
    private Double completePct;

}
