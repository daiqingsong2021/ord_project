package com.wisdom.base.common.vo.doc;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class DocFileInfoVo {
    /**
     *  id
     */
    private Integer id;

    // 文件大小
    private String size;

    /**
     *  文件名称
     */
    private String fileName;

    /**
     *  版本
     */
    private String version;

    /**
     * 地址
     */
    private String fileUrl;

    /**
     * 预览地址
     */
    private String fileViewUrl;

    /**
     * 创建时间
     */
    private Date creatTime;
    /**
     * fast组
     */
    private String groupName;
    /**
     * 远程文件名称
     */
    private String remoteFileName;

    /**
     * 文档服务器预览文件名字
     */
    private String viewRemoteName;

    /**
     * 文件后缀名
     */
    private String suffix;

    private String fileType;
}
