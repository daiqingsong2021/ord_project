package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 文件
 */
@Data
@Table(name = "wsd_doc_file")
public class DocFilePo extends BasePo {

    /**
     * 文档id
     */
    @Column(name = "doc_id")
    private Integer docId;

    /**
     * 文件名称
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 文件类型（企业、项目、交付物、临时）
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 文档位置
     */
    @Column(name = "file_loctn")
    private String fileLoctn;

    /**
     * 密级
     */
    @Column(name = "secuty_level")
    private Double secutyLevel;


    /**
     * 版本
     */
    @Column(name = "version")
    private String version;

    /**
     * 是否被覆盖：1：被覆盖，空没有被覆盖的文件
     */
    @Column(name = "is_cover")
    private String isCover;

    /**
     * 文档服务器组名
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 文档服务器文件名
     */
    @Column(name = "remote_file_name")
    private String remoteFileName;

    /**
     * 文档服务器预览文件名字
     */
    @Column(name = "view_remote_name")
    private String viewRemoteName;

    /**
     * 文件大小
     */
    @Column(name = "size_")
    private Long size;

    // 标识是否删除
    @Column(name = "del")
    private Integer del;

    // 删除时间
    @Column(name = "del_time")
    private Date delTime;

}
