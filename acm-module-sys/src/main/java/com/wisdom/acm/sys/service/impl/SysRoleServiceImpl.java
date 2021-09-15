package com.wisdom.acm.sys.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysSearchRoleForm;
import com.wisdom.acm.sys.po.SysUserIptRolePo;
import com.wisdom.acm.sys.po.SysUserOrgRolePo;
import com.wisdom.acm.sys.service.SysRoleAuthService;
import com.wisdom.acm.sys.service.SysRoleService;
import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.mapper.RoleMapper;
import com.wisdom.acm.sys.form.SysRoleAddForm;
import com.wisdom.acm.sys.form.SysRoleUpdateForm;

import com.wisdom.acm.sys.service.SysUserIptRoleService;
import com.wisdom.acm.sys.service.SysUserOrgRoleService;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;


import java.util.*;

@Service
public class SysRoleServiceImpl extends BaseService<RoleMapper, SysRolePo> implements SysRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    @Autowired
    private SysUserIptRoleService sysUserIptRoleService;

    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    /**
     * 获取角色列表
     *
     * @return
     */
//    @Cache(key = "acm:roles:all")
    @Override
    public List<SysRoleVo>  queryRoles() {
        PageHelper.orderBy("sort_num asc");
        List<SysRoleVo> roleList = roleMapper.selectRoleAll();
        return roleList;
    }

    /**
     * 增加角色
     *
     * @param sysRoleAddForm
     */
    @Override
    public SysRolePo addRole(SysRoleAddForm sysRoleAddForm) {
        //判断菜单代码是否重复
        List<SysRolePo> list = this.getRolePoByCode(sysRoleAddForm.getRoleCode());
        if(!ObjectUtils.isEmpty(list)){
                throw new BaseException("角色代码不能重复");
        }
        SysRolePo rolePo = dozerMapper.map(sysRoleAddForm, SysRolePo.class);
        rolePo.setSort(mapper.selectNextSort());
        super.insert(rolePo);
        return rolePo;
    }

    /**
     * 修改角色
     *
     * @param sysRoleUpdateForm
     */
//    @CacheClear(keys = {"acm:roles"})
    @Override
    @AddLog(title = "修改角色", module = LoggerModuleEnum.SM_ROLE)
    public SysRolePo updateRole(SysRoleUpdateForm sysRoleUpdateForm) {

        //判断修改的数据是否存在
        SysRolePo sysRolePo = mapper.selectByPrimaryKey(sysRoleUpdateForm.getId());
        if(ObjectUtils.isEmpty(sysRolePo)){
            throw new BaseException("该角色不存在");
        }

        // 添加修改日志
        this.addChangeLogger(sysRoleUpdateForm,sysRolePo);
        //判断菜单代码是否重复
        List<SysRolePo> list = this.getRolePoByCode(sysRoleUpdateForm.getRoleCode());
        if(!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(sysRoleUpdateForm.getId())){
                throw new BaseException("角色代码不能重复");
        }

        dozerMapper.map(sysRoleUpdateForm, sysRolePo);
        super.updateSelectiveById(sysRolePo);
        return sysRolePo;
    }

    //    @CacheClear(keys = {"acm:roles"})
    @Override
    public void deleteRole(List<Integer> ids) {
        //判断角色下是否包含用户
        List<SysUserIptRolePo> sysUserIptRolePos = sysUserIptRoleService.querySysUserIptRoleByRoleIds(ids);
        List<SysUserOrgRolePo> sysUserOrgRolePos = sysUserOrgRoleService.querySysUserOrgRoleByRoleIds(ids);
        if(!ObjectUtils.isEmpty(sysUserIptRolePos) || !ObjectUtils.isEmpty(sysUserOrgRolePos)){
            throw new BaseException("角色下包含用户成员，禁止删除!");
        }

        //删除用户角色ipt关系
        sysUserIptRoleService.deleteUserIptRoleRelationByRoleIds(ids);
        //删除用户部门角色关系
        sysUserOrgRoleService.deleteUserOrgRoleRelationByRoleIds(ids);
        //删除角色权限
        sysRoleAuthService.deleteRoleAuthByRoles(ids);
        super.deleteByIds(ids);
    }


    public List<SysRolePo> getRolePoByCode(String code){
        Example example = new Example(SysRolePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleCode",code);
        List<SysRolePo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list)? null : list;
    }

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    @Override
    public SysRoleVo getRoleInfo(Integer roleId) {
        SysRoleVo role = roleMapper.selectRoleVoById(roleId);
        return role;
    }

    /**
     * 搜索角色
     * @param searchMap
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<SysRoleVo> queryRoleBySearch(SysSearchRoleForm searchMap, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysRoleVo> list = roleMapper.selectRoleBySearch(searchMap);
        PageInfo<SysRoleVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<SysRoleVo> queryRolesByIds(List<Integer> roleIds) {
        List<SysRoleVo> list = mapper.selectRolesByIds(roleIds);
        return list;
    }

    @Override
    public List<String> queryRoleCodesByIds(List<Integer> roleIds) {
        Example example = new Example(SysRolePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",roleIds);
        List<SysRolePo> list = this.mapper.selectByExample(example);
        List<String> codes = new ArrayList<>();
        if (!ObjectUtils.isEmpty(list)){
            list.forEach(sysRolePo -> {
                codes.add(sysRolePo.getRoleCode());
            });
        }
        return codes;
    }

    @Override
    public String queryRoleNames(List<Integer> ids) {
        List<SysRolePo> list = super.selectByIds(ids);
        String retName = "";
        StringBuffer names = new StringBuffer();
        if (!ObjectUtils.isEmpty(list)){
            for (SysRolePo po : list){
                names.append(po.getRoleName() + ",");
            }
            String temp = names.toString().substring(0,names.toString().length()-1);
            retName = temp;
        }

        return retName;
    }

    @Override
    public SysRolePo queryRolePoById(Integer roleId) {
        SysRolePo sysRolePo = super.selectById(roleId);
        return sysRolePo;
    }
}
