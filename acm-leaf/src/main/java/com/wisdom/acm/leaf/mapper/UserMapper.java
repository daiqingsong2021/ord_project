package com.wisdom.acm.leaf.mapper;

import com.wisdom.acm.leaf.form.SysUserSearchForm;
import com.wisdom.acm.leaf.po.SysUserPo;
import com.wisdom.acm.leaf.vo.*;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserMapper extends CommMapper<SysUserPo> {

    /**
     * 查询用户列表
     * @param searchMap
     * @return
     */
    List<SysUserVo> selectUsers(@Param("search") SysUserSearchForm searchMap);

}

