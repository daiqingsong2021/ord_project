package com.wisdom.acm.sys.vo.app;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class AppSysOrgUserVo {

    private Integer id;

    private String name;

    private GeneralVo sex;

    private String phone;

    private String email;
}
