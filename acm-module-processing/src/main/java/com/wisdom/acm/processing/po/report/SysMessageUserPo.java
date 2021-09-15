package com.wisdom.acm.processing.po.report;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_sys_message_user")
@Data
public class SysMessageUserPo extends BasePo {

    /**
     *  消息id
     */
    @Column(name = "message_id")
    private Integer messageId;

    /**
     *  收件人
     */
    @Column(name = "recv_user")
    private Integer recvUser;

    /**
     * 收件时间
     */
    @Column(name = "recv_time")
    private Date recvTime;

    /**
     * 收件人类型（1收件人，2抄送人）
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 是否已读
     */
    @Column(name = "real_status")
    private Integer realStatus;

    /**
     * 是否删除（0/1）
     */
    @Column(name = "del")
    private Integer del;

    /**
     *  收藏（0/1）
     */
    @Column(name ="collect")
    private Integer collect;
}

