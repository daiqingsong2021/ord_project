package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

@Data
public class SysMessageRecvUserVo {

    private Integer id;

    private Integer messageId;

    //收件人
    private UserVo recvUser;

}
