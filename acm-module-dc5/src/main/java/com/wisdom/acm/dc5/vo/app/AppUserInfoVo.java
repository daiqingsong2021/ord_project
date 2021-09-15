package com.wisdom.acm.dc5.vo.app;

import com.google.common.collect.Lists;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.List;

/**
 * App用户Info
 */
@Data
public class AppUserInfoVo
{
    /**
     * 主键ID
     */
    private Integer id;

    private String name;

    private String sex;

    /**
     * 头像的URL路径 ,
     */
    private String avatar;

    /**
     *  租户信息，非多住户租户系统可不返回 ,
     */
    private GeneralVo tenant=new GeneralVo();

    /**
     * 默认项目，如果不支持可不填写
     */
    private GeneralVo project=new GeneralVo();

    private List<GeneralVo> orgs= Lists.newArrayList();

    private String phone;

    private String email;

    /**
     * 鉴权ID
     */
    private String authorization ;
}
