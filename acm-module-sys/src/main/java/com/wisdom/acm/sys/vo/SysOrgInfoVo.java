package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.ExtendedColumnVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.StatusVo;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class SysOrgInfoVo extends ExtendedColumnVo {

    private Integer id;
    /**
     * 名称
     */
    private String orgName;

    /**
     * 代码
     */
    private String orgCode;

    /**
     * 父ID
     */
    private GeneralVo parentOrg;

    private String parentName;

    /**
     * 组织类型
     */
    private GeneralVo orgType;

    /**
     * OBS级别
     */
    private GeneralVo orgLevel;

    /**
     * 组织状态
     */
    private GeneralVo status;

    /**
     * 所属地域
     */
    private String grogLoc;

    /**
     * 组织地址
     */
    private String orgAddress;

    /**
     * 邮编
     */
    private String zipCode;

    /**
     * 联系人
     */
    private String orgContract;

    /**
     * 联系电话
     */
    private String contractNum;

    /**
     * 电子邮箱
     */
    private String orgEmail;

    /**
     * 网站地址
     */
    private String webAddress;

    /**
     * 生效日期
     */
    private Date effectDate;

    /**
     * 失效日期
     */
    private Date invalidDate;

    /**
     * 备注
     */
    private String remark;

    private Integer sort;
}
