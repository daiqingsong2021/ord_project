package com.wisdom.acm.szxm.vo.rygl;

import lombok.Data;

import java.util.List;

/**
 * Author：wqd
 * Date：2019-12-04 14:09
 * Description：<描述>
 */
@Data
/**
 * 人员进退场列表 -- 领导视图
 */
public class WorkListsVo {
    /**
     * 在场人数
     */
    private Integer presenceNumber;

    /**
     * 进场人数
     */
    private Integer enteringNumber;

    /**
     *  退场人数
     */
    private Integer exitNumber;

    /***
     * 人员进退场 -- 月份
     */
    private List<WorkListMonthVo> workListMonthVos;

}
