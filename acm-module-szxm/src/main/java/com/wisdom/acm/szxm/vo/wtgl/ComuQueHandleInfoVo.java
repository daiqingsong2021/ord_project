package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.doc.DocFileInfoVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ComuQueHandleInfoVo {

    private Integer id;

    //处理结果说明
    private String handleResult;

    //处理时间
    private Date handleTime;

    //处理人
    private String handleUser;

    //附件
    private List<DocFileInfoVo> files;

    //处理记录状态
    private String handleStatus;
}
