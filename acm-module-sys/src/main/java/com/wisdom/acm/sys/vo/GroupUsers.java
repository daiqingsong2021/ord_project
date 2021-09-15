package com.wisdom.acm.sys.vo;

import com.wisdom.acm.sys.po.SysUserPo;

import java.util.List;

/**
 * Created by Ace on 2017/6/18.
 */
public class GroupUsers {
    List<SysUserPo> members ;
    List<SysUserPo> leaders;

    public GroupUsers() {
    }

    public GroupUsers(List<SysUserPo> members, List<SysUserPo> leaders) {
        this.members = members;
        this.leaders = leaders;
    }

    public List<SysUserPo> getMembers() {
        return members;
    }

    public void setMembers(List<SysUserPo> members) {
        this.members = members;
    }

    public List<SysUserPo> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<SysUserPo> leaders) {
        this.leaders = leaders;
    }
}
