package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_message_relation")
@Data
public class SysMessageRelationPo extends BasePo {
    /**
     *  目标类型（文件）
     */
    @Column(name = "biz_type")
    private String bizType;

    /**
     * 目标id(文件id)
     */
    @Column(name = "biz_id")
    private Integer bizId;

    /**
     *  邮件/消息id
     */
    @Column(name = "message_id")
    private Integer messageId;

}
