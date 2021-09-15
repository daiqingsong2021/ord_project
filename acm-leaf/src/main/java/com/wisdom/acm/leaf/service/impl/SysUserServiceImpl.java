package com.wisdom.acm.leaf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.leaf.form.*;
import com.wisdom.acm.leaf.service.*;
import com.wisdom.acm.leaf.mapper.UserMapper;
import com.wisdom.acm.leaf.po.SysUserPo;
import com.wisdom.acm.leaf.vo.*;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.DateUtil;
import com.wisdom.base.common.enums.HMACEnum;
import com.wisdom.base.common.util.HMACUtil;
import com.wisdom.base.common.util.IPUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.*;
import com.wisdom.cache.annotation.CacheClear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends BaseService<UserMapper, SysUserPo> implements SysUserService {



    @Autowired(required = false)
    private CommDictService commDictService;


    @Override
    public PageInfo<SysUserVo> queryUserList(SysUserSearchForm searchMap, Integer pageSize, Integer currentPageNum) {
        return null;
    }
}
