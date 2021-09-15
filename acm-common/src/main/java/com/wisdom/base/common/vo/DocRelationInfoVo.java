package com.wisdom.base.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DocRelationInfoVo {

    private Integer id;

    /**
     * 文档id
     */
    private Integer docId;
    /**
     * 文档标题
     */
    private String docTitle;
    /**
     * 文档编号
     */
    private String docNum;


    /**
     * 状态
     */
    private DictionaryVo status;

    /**
     * 密级
     */
    private DictionaryVo secutyLevel;

    /**
     *版本号
     */
    private String version;


    /**
     * 上传时间
     */
    private Date creatTime;

    /**
     * 上传人
     */
    private UserVo creator;

    /**
     * 文件ID
     */
    private Integer fileId;

    //========================评分需要添加字段===========
    /**
     * 模块名称
     */
    private String moduleTitle;

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
