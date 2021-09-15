package com.wisdom.acm.hbase.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Table(name = "wsd_sys_user")
@Data
public class AcmLogPo extends BasePo {

    /**
     * 用户账号
     */
    @Column(name = "user_name")
    private String userName;


}
