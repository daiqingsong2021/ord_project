package com.wisdom.acm.szxm.vo.rygl;

import lombok.Data;

/**
 * Author：wqd
 * Date：2019-12-10 9:58
 * Description：<描述>
 */
@Data
/**
 * 考勤记录 -- 领导视图
 */
public class SectionKqVo implements Comparable<SectionKqVo>{
    /**
     * 标段id
     */
    private Integer sectionId;

    /**
     * 标段编码
     */
    private String sectionCode;

    /**
     * 所有人员
     */
    private Integer allNums;

    /**
     * 出勤人数
     */
    private Integer attendanceQuantity;

    /**
     *  请假人数
     */
    private Integer leaveNumber;

    /**
     *  缺勤人数
     */
    private Integer absenceNumber;

    @Override
    public int compareTo(SectionKqVo o) {
        return this.allNums.compareTo(o.getAllNums());
    }
}
