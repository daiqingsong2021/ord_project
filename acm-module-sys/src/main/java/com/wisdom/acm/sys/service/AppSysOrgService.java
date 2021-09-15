package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.vo.app.AppSysOrgUserVo;
import com.wisdom.acm.sys.vo.app.AppSysOrgVo;
import com.wisdom.base.common.service.CommService;

public interface AppSysOrgService extends CommService<SysUserOrgPo> {

    PageInfo<AppSysOrgVo> queryAppSysOrgVoList(Integer pageSize, Integer pageNum, String key);

    PageInfo<AppSysOrgUserVo> queryAppSysOrgUserVoList(Integer orgId, Integer pageSize, Integer pageNum, String key);
}
