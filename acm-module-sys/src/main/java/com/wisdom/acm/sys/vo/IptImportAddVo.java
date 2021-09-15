package com.wisdom.acm.sys.vo;

import lombok.Data;

import java.util.List;

@Data
public class IptImportAddVo {

    private boolean userFlag;

    private List<IptOrgSelectVo> orgs;
}
