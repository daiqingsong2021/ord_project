package com.wisdom.acm.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysMessageDraftsVo {

    private Integer id;

    //标题
    private String title;

    //drafts_user的id
    private Integer draftsUserId;

    //创建时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;

    //收件人
    private List<UserVo> recvUser;

    //消息类型
    private DictionaryVo type;

    //需要回复类型
    private DictionaryVo claimDealType;

    //是否收藏（0/1）
    private GeneralVo collect;

}
