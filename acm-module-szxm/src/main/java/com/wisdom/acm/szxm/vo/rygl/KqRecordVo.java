package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 考勤记录对象
 */
@Data
public class KqRecordVo
{
    /**
     * 流水号
     */
    private String id;

    /**
     * 用户Code
     */
    private String userCode;
    /**
     * 验证方式
     */
    private String verify;

    /**
     * 打卡时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private Date checktime;

    /**
     * 设备序列号
     */
    private String sn;

    /**
     * 设备名称
     */
    private String alias;

    /**
     * 人员编号
     */
    private String pin;

    /**
     * 人员姓名
     */
    private String ename;

    /**
     *
     * 部门编号
     */
    private Integer deptnumber;

    /**
     * 部门名称.
     */
    private String deptname;

    /**
     * 考勤状态
     */
    private String stateno;

    /**
     * 考勤状态说明.
     */
    private String  state;

    /**
     *打卡方式 0 中控考勤，1 微信考勤
     */
    private String  checkType;

    /**
     * 打卡地址
     */
    private String locationAddress;
}
