package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class UserLevelVo {

    //id
    private Integer id;

    //姓名
    private String name;

    //用户名
    private String userName;

    //密级
    private GeneralVo level;

    //所属组织
    private GeneralVo org;

    //所属部门
    private GeneralVo department;


}
