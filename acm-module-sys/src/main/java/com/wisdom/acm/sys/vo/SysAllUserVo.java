package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class SysAllUserVo {


    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户真实名称
     */
    private String actuName;

    /**
     * 用户名
     */
    private String userName;
}
