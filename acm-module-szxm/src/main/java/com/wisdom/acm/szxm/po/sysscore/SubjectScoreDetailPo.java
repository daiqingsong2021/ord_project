package com.wisdom.acm.szxm.po.sysscore;

/**
 * Author：wqd
 * Date：2019-12-30 16:19
 * Description：<描述>
 */

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统评分____客观评分明细表
 */
@Table(name = "szxm_sys_subjective_detail")
@Data
public class SubjectScoreDetailPo extends BasePo {
    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 评分所属年份
     */
    @Column(name = "year")
    private Integer year;

    /**
     * 评分所属月份
     */
    @Column(name = "month")
    private Integer month;

    /**
     * 文件标题
     */
    @Column(name = "file_title")
    private String fileTitle;

    /**
     * 文件id
     */
    @Column(name = "file_id")
    private Integer fileId;

    /**
     * 上传日期
     */
    @Column(name = "uploadfile_time")
    protected Date uploadFileTime;

    /**
     * 上传人
     */
    @Column(name = "uploador")
    protected Integer uploador;

    /**
     * 评分日期
     */
    @Column(name = "score_time")
    protected Date scoreTime;

    /**
     * 评分人
     */
    @Column(name = "rater_id")
    protected Integer raterId;

    /**
     * 业务来源（主观模板表业务编码）
     */
    @Column(name = "source_type")
    private String sourceType;

    /**
     * 分值
     */
    @Column(name = "score")
    private BigDecimal score;
}
