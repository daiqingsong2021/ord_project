package com.wisdom.acm.szxm.vo.doc;

import lombok.Data;

import java.util.Date;

@Data
public class DocumentInfo
{
    private Integer id;

    private Integer isFolder;

    private String name;

    private String code;         //文档编号

    private String category;         //文档分类

    private String title;

    private String type;             //文档类型

    private String version;         //文档版本

    private String creater;               //作者


    private Person uploadBy;                    //上传人

    private Date uploadDate;                //上传时间

    private Date lastUpdateDate;            //文档最近更新时间

    private SimpleVo folder;                //所属文件夹

    private SimpleVo org;                //所属部门

    private WbsOrTaskVo wbsOrTask;

    private String security;

    private String domain;              //文档专业

    private Integer favorite;           //是否收藏

    private Integer fileId;

    private String size;

    private Integer status;              //状态（0-编制中，1-审批中，2-已发布[1,2,3]）

    private SimpleVo section;            //标段

    /**
     * 文件后缀名
     */
    private String suffix;

}
