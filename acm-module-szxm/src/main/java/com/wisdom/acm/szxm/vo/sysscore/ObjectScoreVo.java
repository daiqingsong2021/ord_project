package com.wisdom.acm.szxm.vo.sysscore;

/**
 * Author：wqd
 * Date：2019-12-31 13:39
 * Description：<描述>
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客观得分
 */
@Data
public class ObjectScoreVo {
    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 实际得分
     */
    private BigDecimal actualScore ;

    /**
     * 标段ID
     */
    private Integer sectionId;
    /**
     * 标段号
     */
    private String sectionCode;
    /**
     * 标段名称
     */
    private String sectionName;
    /**
     * 项目 ID
     */
    private Integer projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 施工单位
     */
    private String sgdw;

    /**
     * 创建时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;

    /**
     * 考核分项
     */
    private List<ObjectScoreItemVo> scoreItemVos;
}
