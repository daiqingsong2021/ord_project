package com.wisdom.acm.szxm.vo.wtgl;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WtZzVo
{
    /**
     * 日期
     */
    @ApiModelProperty(value="日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    /**
     * 类型 0 提出 1处理 2确认
     */
    @ApiModelProperty(value="类型")
    private String type;

    /**
     * 问题标题，只有第一次提出的时候才会有值
     */
    @ApiModelProperty(value="问题标题")
    private String title;

    /**
     * 问题描述
     */
    @ApiModelProperty(value="问题描述")
    private String desc;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private String creater;

    /**
     * 创建部门
     */
    @ApiModelProperty(value="创建部门")
    private String createrDept;

    //附件
    @ApiModelProperty(value="附件")
    private Integer fileCount;

    // 问题处理handle 问题确认 confirm  问题ques
    @ApiModelProperty(value="问题处理handle 问题确认 confirm  问题ques")
    private String bizType;

    @ApiModelProperty(value="bizId")
    private Integer bizId;
    /**
     * 处理次数，只有type为1的时候才有值
     */
    @ApiModelProperty(value="处理次数")
    private Integer clcs;

    /**
     * 确认结果 0 不通过 1通过
     */
    @ApiModelProperty(value="确认结果 0 不通过 1通过")
    private String qrjg;
}
