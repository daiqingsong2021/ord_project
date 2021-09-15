package com.wisdom.acm.szxm.vo.rygl;

import lombok.Data;

/**
 * Author：wqd
 * Date：2019-12-04 14:18
 * Description：<描述>
 * @author Administrator
 */
@Data
/**
 * 人员进退场 -- 月份对象
 */
public class WorkListMonthVo implements Comparable<WorkListMonthVo>{
    /**
     *月份
     */
    private String month;

    /**
     * 进场人数
     */
    private Integer enteringNumber;

    /**
     * 退场人数
     */
    private Integer exitNumber;


    @Override
    public int compareTo(WorkListMonthVo o) {
        return this.month.compareTo(o.getMonth());
    }
}
