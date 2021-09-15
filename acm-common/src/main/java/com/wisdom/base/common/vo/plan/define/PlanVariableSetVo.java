package com.wisdom.base.common.vo.plan.define;


import com.wisdom.base.common.vo.DictionaryVo;
import lombok.Data;

@Data
public class PlanVariableSetVo {

    //id
    private Integer id;
    /**
     *  工期类型
     */
    private DictionaryVo taskDrtnType;

    /**
     *  任务完成%类型
     */
    private DictionaryVo taskFinpctType;

    /**
     *  关键路径
     */
    private DictionaryVo cpmType;

    /**
     *  启动内部共享
     */
    private Integer shareWbs;

    /**
     *  总浮时
     */
    private Double cpmFloat;
}
