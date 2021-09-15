package com.wisdom.acm.szxm.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_sys_message")
@Data
public class SysMessageRecvPo extends BasePo {
    /**
     *  标题
     */
    @Column(name = "title")
    private String title;

    /**
     *  内容
     */
    @Column(name = "content_")
    private String content;

    /**
     * 发送人
     */
    @Column(name = "send_user")
    private Integer sendUser;

    /**
     *  发送时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     *  消息分类
     */
    @Column(name = "type")
    private String type;

    /**
     *  需要回复类型
     */
    @Column(name = "claim_deal_type")
    private String claimDealType;

    /**
     *  回复时间
     */
    @Column(name = "claim_deal_time")
    private Date claimDealTime;

    /**
     * 转发消息的id/回复消息的id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     *  操作类型（0普通，1回复，2转发）
     */
    @Column(name = "opt_type")
    private Integer optType;

    /**
     *  删除（0普通，1删除，2保存到草稿箱）
     */
    @Column(name = "del")
    private Integer del;



    /**
     *  消息来源（0用户，1系统）
     */
    @Column(name = "source")
    private Integer source;

    /**
     *  收藏（0不收藏，1收藏）
     */
    @Column(name = "collect")
    private Integer collect;
}
