package com.wisdom.base.common.vo.sys;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserOrgVo {

    /**
     * ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户登陆名
     */
    private String userName;

    /**
     * 用户真实姓名
     */
    private String actuName;

    /**
     * 所属部门/单位ID
     */
    private Integer orgId;

    /**
     * 所属部门/单位代码
     */
    private String orgCode;

    /**
     * 所属部门/单位名称
     */
    private String orgName;

    /**
     * 职务
     */
    private String position;

    /**
     * 专业
     */
    private  String professional;

    /**
     * 业务对象类型（ipt/project）
     */
    private String bizType;

    /**
     * 业务对象ID
     */
    private Integer bizId;

    /**
     * 单位类型（施工/建设/监理...）
     */
    private String orgType;

}
