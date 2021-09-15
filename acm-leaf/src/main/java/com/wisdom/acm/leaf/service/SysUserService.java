package com.wisdom.acm.leaf.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.leaf.form.*;
import com.wisdom.acm.leaf.po.SysUserPo;
import com.wisdom.acm.leaf.vo.SysUserVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.UserVo;

import java.util.List;
import java.util.Map;

public interface SysUserService extends CommService<SysUserPo> {

    /**
     * 查询用户
     *
     * @param searchMap
     * @return
     */
    PageInfo<SysUserVo> queryUserList(SysUserSearchForm searchMap, Integer pageSize, Integer currentPageNum);

}
