package com.wisdom.acm.szxm.po.wtgl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_comu_quehandle_confirm")
public class ConfirmPo extends BasePo {

    /**
     * 问题id
     */
    @Column(name = "question_id")
    private Integer questionId;

    /**
     * 处理记录
     */
    @Column(name = "handle_id")
    private Integer handleId;

    /**
     * 确认记录
     */
    @Column(name = "confirm_id")
    private Integer confirmId;

    @Column(name = "confirm_desc")
    private String confirmDesc;

    @Column(name = "status")
    private String status;

    @Column(name = "next_confirm_user")
    private Integer nextConfirmUser;

    @Column(name = "is_deal")
    private Integer isDeal;

}
