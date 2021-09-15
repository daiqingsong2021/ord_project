package com.wisdom.acm.sys.vo;

import lombok.Data;

import java.math.BigInteger;

@Data
public class DocForMessageVo {

    private Integer id;

    private Integer messageId;

    //文件名称
    private String fileName;

    //文件类型
    private String fileType;

    //长度
    private BigInteger size;

    //拼接文件下载地址
    private String groupName;

    private String remoteFileName;

    private String fileUrl;

}
