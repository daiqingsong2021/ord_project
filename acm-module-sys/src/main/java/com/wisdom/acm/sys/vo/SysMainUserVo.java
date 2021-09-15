package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.List;

@Data
public class SysMainUserVo {

    private List<SysRoleVo> roles;

    private GeneralVo org;

    private Integer id;
}
