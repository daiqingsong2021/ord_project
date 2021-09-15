package com.wisdom.acm.leaf.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
@Data
public class SysUserVo {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户真实名称
     */
    private String actuName;

    /**
     * 1:正常，2:锁定
     */
    private GeneralVo status;

    protected Date lastloginTime;

    /**
     * 用户性别
     */
    private GeneralVo sex;

    /**
     * 员工状态
     */
    private GeneralVo staffStatus;

    private Integer visits;

    public Integer getVisits() {
        return 0;
    }

    private String retRole;
}
