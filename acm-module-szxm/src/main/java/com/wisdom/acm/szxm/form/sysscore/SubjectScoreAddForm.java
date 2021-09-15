package com.wisdom.acm.szxm.form.sysscore;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Author：wqd
 * Date：2020-01-02 14:00
 * Description：<描述>
 */

/**
 * 主观评分对象
 */
@Data
public class SubjectScoreAddForm {
    /**
     * 项目ID
     */
    @NotNull(message = "主观评分所属项目id不能为空")
    private Integer projectId;

    /**
     * 标段ID
     */
    @NotNull(message = "主观评分标段id不能为空")
    private Integer sectionId;

    /**
     * 文件标题
     */
    @NotBlank(message = "文件标题不能为空")
    private String fileTitle;

    /**
     * 文件id
     */
    @NotNull(message = "文件id不能为空")
    private Integer fileId;

    /**
     * 上传日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    protected Date uploadFileTime;

    /**
     * 上传人
     */
    @NotNull(message = "上传人id不能为空")
    protected Integer uploador;

    /**
     * 评分日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    protected Date scoreTime;

    /**
     * 评分人
     */
    protected Integer raterId;

    /**
     * 业务来源（主观模板表业务编码--菜单名称）
     */
    @NotBlank(message = "业务来源不能为空")
    private String sourceType;

    /**
     * 分值
     */
    private BigDecimal score;
}
