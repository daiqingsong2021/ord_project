package com.wisdom.acm.sys.vo;

import com.wisdom.acm.sys.po.*;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class SysUserLoginVo {


    private Integer id;
    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户真实名称
     */
    private String actuName;

    /**
     * 用户外部用户userCode
     */
    private String userCode;
    /**
     * 邮箱
     */
    private String email;

    private List<SysRoleVo> roles;

    private Date creattime;

    /**
     * 菜单信息
     */
    private Map<String,List<SysMenuVo>> loginMenu;

    /**
     * 用户按钮权限
     */
    private List<String> funcCodes;

    private String userType;

    /**
     * 已收藏菜单编码
     */
    private List<String> favortCodes;

    /**
     * 三员管理状态（0：未开启  1：开启）
     */
    private Integer isOpen;

    /**
     * 是否修改密码（0：不修改  1： 修改）
     */
    private Integer isUpdatePwd;

    /**
     * 三级菜单权限（0：未开启  1：开启）
     */
    private Integer isThreeMenu;
    /**
     * 是否是内部人员
     */
    private boolean isInnerPeople;
}
