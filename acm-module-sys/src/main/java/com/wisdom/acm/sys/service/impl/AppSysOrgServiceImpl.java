package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.mapper.AppSysOrgMapper;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.service.AppSysOrgService;
import com.wisdom.acm.sys.vo.app.AppSysOrgUserVo;
import com.wisdom.acm.sys.vo.app.AppSysOrgVo;
import com.wisdom.base.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppSysOrgServiceImpl extends BaseService<AppSysOrgMapper, SysUserOrgPo> implements AppSysOrgService {

    /**
     * 获取组织机构列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<AppSysOrgVo> queryAppSysOrgVoList(Integer pageSize, Integer pageNum, String key){
        PageHelper.startPage(pageNum,pageSize);
        List<AppSysOrgVo> list = mapper.selectAppSysOrgList(key);
        PageInfo<AppSysOrgVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     *获取部门联系人列表
     * @param orgId
     * @param pageSize
     * @param pageNum
     * @param key
     * @return
     */
    @Override
    public PageInfo<AppSysOrgUserVo> queryAppSysOrgUserVoList(Integer orgId, Integer pageSize, Integer pageNum,String key){
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<AppSysOrgUserVo> pageInfo = new PageInfo<>();
        if (orgId != 0) {
            List<AppSysOrgUserVo> list = mapper.selectAppUserByOrgId(orgId, key);
            pageInfo = new PageInfo<>(list);
        }else{
            List<AppSysOrgUserVo> retList = mapper.selectAppAllUser(key);
            pageInfo = new PageInfo<>(retList);
        }
        return pageInfo;
    }
}
