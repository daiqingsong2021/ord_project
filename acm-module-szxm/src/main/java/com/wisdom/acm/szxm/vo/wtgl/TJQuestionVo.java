package com.wisdom.acm.szxm.vo.wtgl;

import lombok.Data;

@Data
public class TJQuestionVo implements Comparable<TJQuestionVo>
{
    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 标段编号code
     */
    private String sectionCode;

    /**
     * 问题总数
     */
    private Integer totalQuantity;

    /**
     * 未关闭数量
     */
    private Integer unclosedQUantity;
    /**
     * 站点ID
     */
    private Integer stationId;
    /**
     * 站点Code
     */
    private String stationCode;

    /**
     * 站点名称
     */
    private String stationName;

    @Override
    public int compareTo(TJQuestionVo o) {
        return this.totalQuantity.compareTo(o.getTotalQuantity());
    }
}
