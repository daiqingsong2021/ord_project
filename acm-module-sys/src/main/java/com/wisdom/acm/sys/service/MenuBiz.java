package com.wisdom.acm.sys.service;

import com.wisdom.base.common.service.BaseService;
import com.wisdom.cache.annotation.Cache;
import com.wisdom.cache.annotation.CacheClear;
import com.wisdom.acm.sys.constant.AdminCommonConstant;
import com.wisdom.acm.sys.po.SysMenuPo;
import com.wisdom.acm.sys.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
//@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseService<MenuMapper, SysMenuPo> {
    @Override
    @Cache(key = "permission:menu")
    public List<SysMenuPo> selectListAll() {
        return super.selectListAll();
    }

    @Override
    @CacheClear(keys = {"permission:menu", "permission"})
    public int insertSelective(SysMenuPo entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setUrl("/" + entity.getMenuCode());
        } else {
            SysMenuPo parent = this.selectById(entity.getParentId());
            entity.setUrl(parent.getUrl() + "/" + entity.getMenuCode());
        }
       int count = super.insertSelective(entity);
        return count;
    }

    @Override
    @CacheClear(keys = {"permission:menu", "permission"})
    public boolean updateById(SysMenuPo entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setUrl("/" + entity.getMenuCode());
        } else {
            SysMenuPo parent = this.selectById(entity.getParentId());
            entity.setUrl(parent.getUrl() + "/" + entity.getMenuCode());
        }
        super.updateById(entity);
        return super.updateById(entity);
    }

    @Override
    @CacheClear(keys = {"permission:menu", "permission"})
    public int updateSelectiveById(SysMenuPo entity) {
        super.updateSelectiveById(entity);
        return super.updateSelectiveById(entity);
    }

    /**
     * 获取用户可以访问的菜单
     *
     * @param id
     * @return
     */
    @Cache(key = "permission:menu:u{1}")
    public List<SysMenuPo> getUserAuthorityMenuByUserId(int id) {
        return mapper.selectAuthorityMenuByUserId(id);
    }

    /**
     * 根据用户获取可以访问的系统
     *
     * @param id
     * @return
     */
    public List<SysMenuPo> getUserAuthoritySystemByUserId(int id) {
        return mapper.selectAuthoritySystemByUserId(id);
    }

    @Cache(key = "test:cache1{1}{2}")
    public String addTestCache(String name, String name2) {
        System.out.println("addTestCache==> " + name + " " + name2);
        return name + " " + name2;
    }

    @Cache(key = "test:cache2")
    public String addTestCache2(String name) {
        System.out.println("addTestCache2==> " + name);
        return name;
    }

    @CacheClear(keys = {"test:cache1", "test:cache2"})
    public void deleteTestCache() {
        System.out.println("deleteTestCache==> ");
    }


}
