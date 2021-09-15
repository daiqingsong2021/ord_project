package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.vo.app.AppSysOrgUserVo;
import com.wisdom.acm.sys.vo.app.AppSysOrgVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppSysOrgMapper extends CommMapper<SysUserOrgPo> {

    List<AppSysOrgVo> selectAppSysOrgList(@Param("key") String key);

    List<AppSysOrgUserVo> selectAppUserByOrgId(@Param("orgId") Integer orgId, @Param("key") String key);

    List<AppSysOrgUserVo> selectAppAllUser(@Param("key") String key);
}
