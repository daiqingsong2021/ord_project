package com.wisdom.acm.szxm.vo.sysscore;

/**
 * Author：wqd
 * Date：2019-12-31 13:39
 * Description：<描述>
 */

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 主观得分
 */
@Data
public class SubjectScoreVo {
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
     * 考核分项
     */
    private PageInfo<SubjectScoreItemVo> subjectScoreItemVos;
}
