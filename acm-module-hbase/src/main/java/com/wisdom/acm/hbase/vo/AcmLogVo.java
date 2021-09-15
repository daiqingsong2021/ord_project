package com.wisdom.acm.hbase.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
public class AcmLogVo {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户账号
     */
    private String userName;

}
