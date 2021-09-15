package com.wisdom.acm.sys.form;

import lombok.Data;

import java.util.Date;

/**
 * @author zll
 * 2020/8/11/011 22:16
 * Description:<描述>
 */
@Data
public class SysMessageUserForm {
    private Integer id;
    /**
     *  消息id
     */
    private Integer messageId;

    /**
     *  收件人
     */
    private Integer recvUser;

    /**
     * 收件时间
     */
    private Date recvTime;

    /**
     * 收件人类型（1收件人，2抄送人）
     */
    private Integer type;

    /**
     * 是否已读
     */
    private Integer realStatus;

    /**
     * 是否删除（0/1）
     */
    private Integer del;

    /**
     *  收藏（0/1）
     */
    private Integer collect;
}
