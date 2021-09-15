package com.wisdom.base.common.form;

import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysMessageAddForm {

    /**
     *  标题
     */
    private String title;

    /**
     *  内容
     */
    private String content;

    /**
     *  pc端内容
     */
    private String pcContent;

    /**
     *  需要回复类型
     */
    private String claimDealType;

    /**
     *  回复时间
     */
    private Date claimDealTime;

    /**
     * 转发消息的id/回复消息的id
     */
    private Integer parentId;

    /**
     *  操作类型（0普通，1回复，2转发）
     */
    private Integer optType;

    /**
     *  类型（普通/分发）
     */
    private String type;

    /**
     *  收件人(普通)
     */
    private List<UserVo> recvUser;

    /**
     *  抄送人
     */
    private List<UserVo> copyUser;

    /**
     *  文件ids
     */
    private List<Integer> fileIds;

    /**
     *  消息来源（0用户，1系统）
     */
    private Integer source;

    /**
     * 发送类型(0 PC包括移动端  1移动端)
     */
    private Integer sendType;
    /**
     * 发送人
     */
    private UserVo senderUser;
}
