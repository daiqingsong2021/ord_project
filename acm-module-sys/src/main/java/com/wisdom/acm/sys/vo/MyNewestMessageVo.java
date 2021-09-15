package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class MyNewestMessageVo {

    private Integer id;

    private Integer messageId;

    private String title;

    private UserVo sendUser;

    private Date sendTime;

    //接收时间
    private Date recvTime;

    //相隔时长
    private String duration;

    //消息类型
    private DictionaryVo type;

    //消息来源（0用户/1系统）
    private GeneralVo source;

    //已读未读
    private GeneralVo realStatus;

    //需要回复类型
    private DictionaryVo claimDealType;

    //是否收藏（0/1）
    private GeneralVo collect;



}
