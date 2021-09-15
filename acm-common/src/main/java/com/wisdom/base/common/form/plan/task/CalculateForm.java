package com.wisdom.base.common.form.plan.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CalculateForm {

    /**
     * 计划定义id集合
     */
    List<Integer> defineIds;

    //当前时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date nowTime;

    /**
     * 计算方式
     */
    private String calculMethod;

    /**
     * 关键路径
     */
    private String keyPath;

}
