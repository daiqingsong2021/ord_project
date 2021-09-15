package com.wisdom.acm.base.vo.tmpldoc;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class BaseTmpldocVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String docTitle;

    /**
     * 编号
     */
    private String docNum;

    /**
     * 版本号
     */
    private String docVersion;

    /**
     * 所属业务对象
     */
    private DictionaryVo docObject;

    /**
     * 是否启用
     */
    private String isUse;

    /**
     * 文件id
     */
    private DictionaryVo fileId;


    //文档专业
    private DictionaryVo profession;

    //密级
    private DictionaryVo secutyLevel;

    //作者
    private String author;

    //文档类别
    private DictionaryVo docClassify;

    private Date creatTime;

    private UserVo creator;



}