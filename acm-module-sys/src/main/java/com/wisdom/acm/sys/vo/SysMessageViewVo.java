package com.wisdom.acm.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysMessageViewVo {

    private Integer id;

    //标题
    private String title;

    //转发/回复的消息信息id
    private Integer parentId;

    //转发/回复
    private Integer optType;

    //发送时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date sendTime;

    //内容
    private String content;

    //发件人
    private UserVo sendUser;

    //收件人
    private List<UserVo> recvUser;

    //需要回复类型
    private DictionaryVo claimDealType;

    //需要回复时间
    private Date claimDealTime;

    //files
    private List<Integer> fileIds;

}
