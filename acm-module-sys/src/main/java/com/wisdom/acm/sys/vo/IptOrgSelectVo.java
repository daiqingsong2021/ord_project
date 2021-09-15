package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class IptOrgSelectVo {

    private int orgId;

    private int parentId;

    private boolean leafFlag;

    private boolean topFlag;

    private String roleIds;

    public boolean getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(boolean topFlag) {
        this.topFlag = topFlag;
    }

    public boolean getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(boolean leafFlag) {
        this.leafFlag = leafFlag;
    }
}
