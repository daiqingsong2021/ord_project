package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class MyMessageVo {

    private Integer id;

    private Integer messageId;

    private String title;

    //类型
    private DictionaryVo type;

    private UserVo recvUser;

    private UserVo sendUser;

    private Date sendTime;

    private DictionaryVo claimDealType;

    private GeneralVo realStatus;

}
