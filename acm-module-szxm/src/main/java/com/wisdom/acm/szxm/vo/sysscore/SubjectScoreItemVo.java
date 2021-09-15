package com.wisdom.acm.szxm.vo.sysscore;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Author：wqd
 * Date：2019-12-31 13:31
 * Description：<描述>
 */

/**
 * 主观 分项得分
 */
@Data
public class SubjectScoreItemVo {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleTitle;

    /**
     * 该评分所在年份
     */
    private Integer year;

    /**
     * 评分所在月度
     */
    private Integer month;

    /**
     * 文档标题
     */
    private String fileTitle;

    /**
     * 文档id
     */
    private Integer fileId;

    /**
     * 上传时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date uploadfileTime;

    /**
     * 上传人id
     */
    private Integer uploador;

    /**
     * 上传人-名字
     */
    private String uploader;

    /**
     * 评分人id
     */
    private Integer raterId;

    /**
     * 评分人-名字
     */
    private String rater;

    /**
     * 评分
     */
    private BigDecimal score;

    /**
     * 评分时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date scoreTime;
}
