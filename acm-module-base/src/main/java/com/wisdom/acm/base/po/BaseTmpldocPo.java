package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_tmpldoc")
@Data
public class BaseTmpldocPo extends BasePo {

    @Column(name = "DOC_TITLE")
    private String docTitle;

    @Column(name = "DOC_NUM")
    private String docNum;

    @Column(name = "DOC_VERSION")
    private String docVersion;

    @Column(name = "DOC_OBJECT")
    private String docObject;

    @Column(name = "IS_USE")
    private String isUse;

    @Column(name = "FILE_ID")
    private Integer fileId;

    //文档专业
    @Column(name = "profession")
    private String profession;

    //密级
    @Column(name = "secuty_level")
    private String secutyLevel;

    //作者
    @Column(name = "author")
    private String author;

    //文档类别
    @Column(name = "doc_classify")
    private String docClassify;

}