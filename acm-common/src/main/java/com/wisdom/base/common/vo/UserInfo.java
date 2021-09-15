package com.wisdom.base.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
@Data
public class UserInfo{
    /**
     * 序列号.
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String tenantId;

    private String lastUpdIp;
    /**
     * 用户登录账号
     */
    private String userName;

    /**
     * 用户code
     */
    private String userCode;
    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户真实名称
     */
    private String actuName;

    /**
     * 邮箱
     */
    private String email;

    private String remark;

    private String menutype;

    private String authmode;
    /**
     * 1:正常，0:锁定
     */
    private Integer status;

    private Date lastlogin;

    private String sex;

    private String phone;

    private String card;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date birth;

    private Integer errcount;

    private List<Integer> orgIds;

    private  String userType;

    public List<Integer> getOrgIds(){
        return this.orgIds == null ? new ArrayList<>() : this.orgIds;
    }

}
