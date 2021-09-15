package com.wisdom.acm.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class SysMessageDeletedVo {

    private Integer id;

    private Integer sid;

    //标题
    private String title;

    //发送时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date sendTime;

    //发送人
    private UserVo sendUser;

    //收件人
    private UserVo recvUser;

    //消息类型
    private DictionaryVo type;

    //消息来源（0用户/1系统）
    private GeneralVo source;

    //需要回复类型
    private DictionaryVo claimDealType;

}
