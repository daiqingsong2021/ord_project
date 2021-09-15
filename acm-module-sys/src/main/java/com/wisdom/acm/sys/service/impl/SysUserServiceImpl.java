package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysUpdatePassWordForm;
import com.wisdom.acm.sys.form.SysUserAddBatchFrom;
import com.wisdom.acm.sys.form.SysUserAddFrom;
import com.wisdom.acm.sys.form.SysUserSearchForm;
import com.wisdom.acm.sys.form.SysUserUpdateFrom;
import com.wisdom.acm.sys.form.UserLevelSearchForm;
import com.wisdom.acm.sys.form.UserLevelUpdateForm;
import com.wisdom.acm.sys.mapper.UserMapper;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.po.SysUserOrgRolePo;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.service.SysIPAccRuleService;
import com.wisdom.acm.sys.service.SysLoginService;
import com.wisdom.acm.sys.service.SysOrgService;
import com.wisdom.acm.sys.service.SysPwdRuleService;
import com.wisdom.acm.sys.service.SysUserIptRoleService;
import com.wisdom.acm.sys.service.SysUserIptService;
import com.wisdom.acm.sys.service.SysUserOrgRoleService;
import com.wisdom.acm.sys.service.SysUserOrgService;
import com.wisdom.acm.sys.service.SysUserService;
import com.wisdom.acm.sys.vo.SysAllUserVo;
import com.wisdom.acm.sys.vo.SysIPAccessVo;
import com.wisdom.acm.sys.vo.SysIptUserVo;
import com.wisdom.acm.sys.vo.SysPwdRuleVo;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.acm.sys.vo.SysUserInfoVo;
import com.wisdom.acm.sys.vo.SysUserIptRoleVo;
import com.wisdom.acm.sys.vo.SysUserOrgRoleVo;
import com.wisdom.acm.sys.vo.SysUserProjectInfoVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.acm.sys.vo.SystemVo;
import com.wisdom.acm.sys.vo.UserLevelVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.HMACEnum;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.DateUtil;
import com.wisdom.base.common.util.HMACUtil;
import com.wisdom.base.common.util.IPUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.PageUtil;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
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

    @Autowired
    private SysUserOrgService sysOrgUserService;

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;


    @Autowired
    private SysUserIptService sysUserIptService;

    @Autowired
    private SysUserIptRoleService sysUserIptRoleService;

    @Autowired
    private SysPwdRuleService sysPwdRuleService;

    @Autowired
    private SysIPAccRuleService sysIPAccRuleService;

    @Autowired(required = false)
    private CommDictService commDictService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private SystemVo systemVo;

    @Autowired
    private SysOrgService orgService;

    /**
     * 给activiti使用
     * @param userId
     * @return
     */
    @Override
    public GeneralVo selectUserInfoForAct(String userId) {
        return mapper.selectUserInfoForAct(userId);
    }

    //    @Cache(key = "acm:users:search{1}")
    @Override
    public PageInfo<SysUserVo> queryUserList(SysUserSearchForm searchMap, Integer pageSize, Integer currentPageNum) {
        //用户分页
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysUserVo> sysUserVoList = mapper.selectUsers(searchMap);
        PageInfo<SysUserVo> pageInfo = new PageInfo<>(sysUserVoList);

        //获取分页后用户集合
        List<SysUserVo> userList = pageInfo.getList();

        if (!ObjectUtils.isEmpty(userList)) {
            //获取用户id集合
            List<Integer> ids = new ArrayList<>();
            for (SysUserVo sysUserVo : userList) {
                ids.add(sysUserVo.getId());
            }
            //获取用户角色
            List<SysUserOrgRoleVo> roles = sysUserOrgRoleService.queryUserRoleByUserId(ids);
            Map<Integer, List<SysUserOrgRoleVo>> roleMap = ListUtil.bulidTreeListMap(roles, "userId", Integer.class);
            SysRoleVo sysRoleVo1 = null;
            List<SysRoleVo> roleVos1 = null;
            //添加用户角色信息
            for (SysUserVo sysUserVo : userList) {
                if (!ObjectUtils.isEmpty(roleMap.get(sysUserVo.getId()))) {
                    roleVos1 = new ArrayList<>();
                    for (SysUserOrgRoleVo sysUserOrgRoleVo : roleMap.get(sysUserVo.getId())) {
                        sysRoleVo1 = new SysRoleVo();
                        sysRoleVo1.setId(sysUserOrgRoleVo.getRoleId());
                        sysRoleVo1.setRoleName(sysUserOrgRoleVo.getRoleName());
                        roleVos1.add(sysRoleVo1);
                    }
                    sysUserVo.setRoles(roleVos1);
                }
            }
            //角色拼接成字符串
            List<SysRoleVo> roleVos = null;
            if (!ObjectUtils.isEmpty(userList)) {
                for (SysUserVo sysUserVo : userList) {
                    roleVos = sysUserVo.getRoles();
                    StringBuffer str = new StringBuffer();
                    if (!ObjectUtils.isEmpty(roleVos)) {
                        for (SysRoleVo sysRoleVo : roleVos) {
                            str.append("," + sysRoleVo.getRoleName());
                        }
                        String retRole = str.substring(1);
                        sysUserVo.setRetRole(retRole);
                        sysUserVo.setRoles(null);
                    }
                }
            }
        }
        return pageInfo;
    }

    @Override
    public SysUserVo queryUserById(Integer id) {
        SysUserVo sysUserVo = mapper.selectUserByUserId(id);
        if (!ObjectUtils.isEmpty(sysUserVo)) {
            //获取用户id集合
            List<Integer> ids = new ArrayList<>();
            ids.add(sysUserVo.getId());
            //获取用户角色
            List<SysUserOrgRoleVo> roles = sysUserOrgRoleService.queryUserRoleByUserId(ids);
            SysRoleVo sysRoleVo1 = null;
            List<SysRoleVo> roleVos1 = null;
            //添加用户角色信息
            if (!ObjectUtils.isEmpty(roles)) {
                roleVos1 = new ArrayList<>();
                for (SysUserOrgRoleVo sysUserOrgRoleVo : roles) {
                    sysRoleVo1 = new SysRoleVo();
                    sysRoleVo1.setId(sysUserOrgRoleVo.getRoleId());
                    sysRoleVo1.setRoleName(sysUserOrgRoleVo.getRoleName());
                    roleVos1.add(sysRoleVo1);
                }
            }
            sysUserVo.setRoles(roleVos1);

            //角色拼接成字符串
            List<SysRoleVo> roleVos = null;
            roleVos = sysUserVo.getRoles();
            StringBuffer str = new StringBuffer();
            if (!ObjectUtils.isEmpty(roleVos)) {
                for (SysRoleVo sysRoleVo : roleVos) {
                    str.append("," + sysRoleVo.getRoleName());
                }
                String retRole = str.substring(1);
                sysUserVo.setRetRole(retRole);
                sysUserVo.setRoles(null);
            }


        }
        return sysUserVo;
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public SysUserInfoVo getUserInfo(Integer userId) {
        SysUserInfoVo sysUserPoVo = mapper.selectUserInfo(userId);
        GeneralVo sysMainUserVo = mapper.selectUserMainOrg(userId);
//        if (ObjectUtils.isEmpty(sysMainUserVo)) {
//            throw new BaseException("该数据异常，没有主部门！");
//        }
        List<SysRoleVo> roles = null;
        if (!ObjectUtils.isEmpty(sysMainUserVo)) {
            roles = sysUserOrgRoleService.queryRoleListByOrgIdAndUserId(sysMainUserVo.getId(), userId);
            sysUserPoVo.setOrg(sysMainUserVo);
            sysUserPoVo.setRoles(roles);
        }
        List<String> dictList = new ArrayList<>();
        dictList.add("comm.secutylevel");
        dictList.add("sys.user.cardtype");
        DictionarysMap dictionarysMap = commDictService.getDictMapByTypeCodes(dictList);
        if (!ObjectUtils.isEmpty(dictionarysMap) && !ObjectUtils.isEmpty(sysUserPoVo)) {
            if (!ObjectUtils.isEmpty(sysUserPoVo.getLevel())) {
                sysUserPoVo.getLevel().setName(dictionarysMap.getDictionaryName("comm.secutylevel", sysUserPoVo.getLevel().getCode()));
            }
            if (!ObjectUtils.isEmpty(sysUserPoVo.getCardType())) {
                sysUserPoVo.getCardType().setName(dictionarysMap.getDictionaryName("sys.user.cardtype", sysUserPoVo.getCardType().getCode()));
            }
        }
        return sysUserPoVo;
    }

    @Override
    public SysUserInfoVo getUserDept(Integer userId) {
        SysUserInfoVo sysUserPoVo = mapper.selectUserInfo(userId);
        GeneralVo sysMainUserVo = mapper.selectUserMainOrg(userId);
        return null;
    }

    /**
     * 增加用户
     *
     * @param sysUserAddFrom
     * @return
     */
    @Override
    public SysUserPo addUser(SysUserAddFrom sysUserAddFrom) {
        if (!ObjectUtils.isEmpty(sysUserAddFrom.getSort())) {
            sysUserAddFrom.setSort(mapper.selectNextSort());
        }
        SysUserPo sysUserPo = dozerMapper.map(sysUserAddFrom, SysUserPo.class);
        SysUserPo sysUserPoIs = mapper.selectUserByUserName(sysUserPo.getUserName());
        if (!ObjectUtils.isEmpty(sysUserPoIs)) {
            throw new BaseException("用户名已存在");
        }
        //导入uuv 内部人员数据  status 根据StaffStatus 设置，然后将StaffStatus设为在职
        if (!ObjectUtils.isEmpty(sysUserAddFrom.getStaffStatus())) {
            if (1 == sysUserAddFrom.getStaffStatus()) {
                sysUserPo.setStatus(1);
            } else {
                sysUserPo.setStatus(0);
            }
            sysUserPo.setStaffStatus("1");
            sysUserAddFrom.setStaffStatus(1);
        }
        if (ObjectUtils.isEmpty(sysUserPo.getStatus())) {
            sysUserPo.setStatus(1);
        }

        if (ObjectUtils.isEmpty(sysUserAddFrom.getSort())) {
            sysUserPo.setSort(mapper.selectNextSort());
        }
        //密码加密
        String pwd = null;
        if (ObjectUtils.isEmpty(sysUserPo.getPassword())) {
            pwd = HMACUtil.getHMAC("123456", HMACEnum.HMAC_KEY.getType());
            sysUserPo.setPassword(pwd);
        }else{
            pwd = HMACUtil.getHMAC(sysUserPo.getPassword(), HMACEnum.HMAC_KEY.getType());
            sysUserPo.setPassword(pwd);
        }
        sysUserPo.setUserType("1");
        sysUserPo.setUpdatePwdTime(new Date());
        //保存用户基本信息
        super.insert(sysUserPo);

        Integer userId = sysUserPo.getId();
        SysUserOrgPo sysOrgUserPo = new SysUserOrgPo();
        SysUserOrgRolePo sysRoleUserPo = null;

        sysOrgUserPo.setOrgId(sysUserAddFrom.getOrgId());
        sysOrgUserPo.setUserId(sysUserPo.getId());
        sysOrgUserPo.setMainOrg(1);

        //保存用户部门关系
        sysOrgUserService.addOrgUserRelation(sysOrgUserPo);

        //保存用户角色关系
        if (!ObjectUtils.isEmpty(sysUserAddFrom.getRoles())) {
            for (Integer roleId : sysUserAddFrom.getRoles()) {
                sysRoleUserPo = new SysUserOrgRolePo();
                sysRoleUserPo.setRoleId(roleId);
                sysRoleUserPo.setUserId(sysUserPo.getId());
                sysRoleUserPo.setOrgId(sysUserAddFrom.getOrgId());
                sysUserOrgRoleService.addUserOrgRoleRelation(sysRoleUserPo);
            }
        }
        return sysUserPo;
    }

    /**
     * 修改用户
     *
     * @param sysUserUpdateVo
     * @return
     */
//    @CacheClear(keys = {"acm:users"})
    @Override
    @AddLog(title = "修改用户" , module = LoggerModuleEnum.SM_USER)
    public SysUserPo updateUser(SysUserUpdateFrom sysUserUpdateVo) {
        SysUserPo sysUserPo = mapper.selectByPrimaryKey(sysUserUpdateVo.getId());
        if (ObjectUtils.isEmpty(sysUserPo)) {
            throw new BaseException("该用户不存在");
        }

        // 添加修改日志
        this.addChangeLogger(sysUserUpdateVo,sysUserPo);
        String userName = sysUserUpdateVo.getUserName();
        SysUserPo sysUserPoIs = mapper.selectUserByUserName(userName);
        if (!ObjectUtils.isEmpty(sysUserPoIs) && !sysUserPoIs.getId().equals(sysUserUpdateVo.getId())) {
            throw new BaseException("用户名已存在");
        }

        //导入uuv 内部人员数据  status 根据StaffStatus 设置，然后将StaffStatus设为在职
        if (!ObjectUtils.isEmpty(sysUserUpdateVo.getStaffStatus())) {
            if (1 == sysUserUpdateVo.getStaffStatus()) {
                sysUserPo.setStatus(1);
            } else {
                sysUserPo.setStatus(0);
            }
            sysUserPo.setStaffStatus("1");
            sysUserUpdateVo.setStaffStatus(1);
        }

        //获取用户主组织
        GeneralVo sysMainUserVo = mapper.selectUserMainOrg(sysUserUpdateVo.getId());
        Integer oldOrgId = null;
        if (!ObjectUtils.isEmpty(sysMainUserVo))
            oldOrgId = sysMainUserVo.getId();
        //修改用户基本信息
        dozerMapper.map(sysUserUpdateVo, sysUserPo);
        super.updateSelectiveById(sysUserPo);

        //修改用户部门
        if (!ObjectUtils.isEmpty(sysUserUpdateVo.getOrgId()) && !sysUserUpdateVo.getOrgId().equals(oldOrgId)) {
            List<Integer> userIds = new ArrayList<>();
            userIds.add(sysUserUpdateVo.getId());
            SysUserOrgPo sysUserOrgPo = new SysUserOrgPo();
            sysUserOrgPo.setUserId(sysUserUpdateVo.getId());
            sysUserOrgPo.setOrgId(sysUserUpdateVo.getOrgId());
            sysUserOrgPo.setMainOrg(1);
            sysOrgUserService.deleteUserOrgRelationByUserIdAndOrgId(userIds, oldOrgId);
            sysOrgUserService.addOrgUserRelation(sysUserOrgPo);
        }

        //修改用户角色
        SysUserOrgRolePo sysRoleUserPo = null;
        if (!ObjectUtils.isEmpty(sysUserUpdateVo.getRoles())) {
            mapper.deleRoleUserByOrgId(sysUserUpdateVo.getId(), oldOrgId);
            for (Integer roleId : sysUserUpdateVo.getRoles()) {
                sysRoleUserPo = new SysUserOrgRolePo();
                sysRoleUserPo.setUserId(sysUserUpdateVo.getId());
                sysRoleUserPo.setOrgId(sysUserUpdateVo.getOrgId());
                sysRoleUserPo.setRoleId(roleId);
                sysUserOrgRoleService.addUserOrgRoleRelation(sysRoleUserPo);
            }
        }

        return sysUserPo;
    }

    @Override
    public SysUserPo updateUserLastLoginTime(SysUserPo sysUserPo) {
        sysUserPo.setLastloginTime(new Date());
        mapper.updateUserLastLoginTime(sysUserPo);
        return null;
    }

    /**
     * 从uuv增加用户
     *
     * @param sysUserAddFrom
     * @return
     */
    @Override
    public SysUserPo xinZengUser(SysUserAddFrom sysUserAddFrom) {
        if (!ObjectUtils.isEmpty(sysUserAddFrom.getSort())) {
            sysUserAddFrom.setSort(mapper.selectNextSort());
        }
        SysUserPo sysUserPo = dozerMapper.map(sysUserAddFrom, SysUserPo.class);
        SysUserPo sysUserPoIs = mapper.selectUserByUserName(sysUserPo.getUserName());
        if (!ObjectUtils.isEmpty(sysUserPoIs)) {
            throw new BaseException("用户名已存在");
        }
        //导入uuv 内部人员数据  status 根据StaffStatus 设置，然后将StaffStatus设为在职
        if (!ObjectUtils.isEmpty(sysUserAddFrom.getStaffStatus())) {
            if (1 == sysUserAddFrom.getStaffStatus()) {
                sysUserPo.setStatus(1);
            } else {
                sysUserPo.setStatus(0);
            }
            sysUserPo.setStaffStatus("1");
            sysUserAddFrom.setStaffStatus(1);
        }
        if (ObjectUtils.isEmpty(sysUserPo.getStatus())) {
            sysUserPo.setStatus(1);
        }

        if (ObjectUtils.isEmpty(sysUserAddFrom.getSort())) {
            sysUserPo.setSort(mapper.selectNextSort());
        }
        //密码加密
        String pwd = null;
        if (ObjectUtils.isEmpty(sysUserPo.getPassword())) {
            pwd = HMACUtil.getHMAC("123456", HMACEnum.HMAC_KEY.getType());
            sysUserPo.setPassword(pwd);
        }else{
            pwd = HMACUtil.getHMAC(sysUserPo.getPassword(), HMACEnum.HMAC_KEY.getType());
            sysUserPo.setPassword(pwd);
        }
        sysUserPo.setUserType("1");
        sysUserPo.setUpdatePwdTime(new Date());
        //保存用户基本信息
        super.insert(sysUserPo);

        Integer userId = sysUserPo.getId();
        SysUserOrgPo sysOrgUserPo = new SysUserOrgPo();
        SysUserOrgRolePo sysRoleUserPo = null;

        sysOrgUserPo.setOrgId(sysUserAddFrom.getOrgId());
        sysOrgUserPo.setUserId(sysUserPo.getId());
        sysOrgUserPo.setMainOrg(1);

        //保存用户部门关系
        sysOrgUserService.addOrgUserRelation(sysOrgUserPo);

        //保存用户角色关系
        if (!ObjectUtils.isEmpty(sysUserAddFrom.getRoles())) {
            for (Integer roleId : sysUserAddFrom.getRoles()) {
                sysRoleUserPo = new SysUserOrgRolePo();
                sysRoleUserPo.setRoleId(roleId);
                sysRoleUserPo.setUserId(sysUserPo.getId());
                sysRoleUserPo.setOrgId(sysUserAddFrom.getOrgId());
                sysUserOrgRoleService.addUserOrgRoleRelation(sysRoleUserPo);
            }
        }
        return sysUserPo;
    }

    /**
     * 从uuv修改用户
     *
     * @param sysUserUpdateVo
     * @return
     */
//    @CacheClear(keys = {"acm:users"})
    @Override
    public SysUserPo xiuGaiUser(SysUserUpdateFrom sysUserUpdateVo) {
        SysUserPo sysUserPo = mapper.selectByPrimaryKey(sysUserUpdateVo.getId());
        if (ObjectUtils.isEmpty(sysUserPo)) {
            throw new BaseException("该用户不存在");
        }

        // 添加修改日志
        this.addChangeLogger(sysUserUpdateVo,sysUserPo);
        String userName = sysUserUpdateVo.getUserName();
        SysUserPo sysUserPoIs = mapper.selectUserByUserName(userName);
        if (!ObjectUtils.isEmpty(sysUserPoIs) && !sysUserPoIs.getId().equals(sysUserUpdateVo.getId())) {
            throw new BaseException("用户名已存在");
        }

        //获取用户主组织
        GeneralVo sysMainUserVo = mapper.selectUserMainOrg(sysUserUpdateVo.getId());
        Integer oldOrgId = null;
        if (!ObjectUtils.isEmpty(sysMainUserVo))
            oldOrgId = sysMainUserVo.getId();
        //修改用户基本信息
        dozerMapper.map(sysUserUpdateVo, sysUserPo);

        //导入uuv 内部人员数据  status 根据StaffStatus 设置，然后将StaffStatus设为在职
        if (!ObjectUtils.isEmpty(sysUserUpdateVo.getStaffStatus())) {
            if (1 == sysUserUpdateVo.getStaffStatus()) {
                sysUserPo.setStatus(1);
            } else {
                sysUserPo.setStatus(0);
            }
            sysUserPo.setStaffStatus("1");
            sysUserUpdateVo.setStaffStatus(1);
        }
        super.updateSelectiveById(sysUserPo);

        //修改用户部门
        if (!ObjectUtils.isEmpty(sysUserUpdateVo.getOrgId()) && !sysUserUpdateVo.getOrgId().equals(oldOrgId)) {
            List<Integer> userIds = new ArrayList<>();
            userIds.add(sysUserUpdateVo.getId());
            SysUserOrgPo sysUserOrgPo = new SysUserOrgPo();
            sysUserOrgPo.setUserId(sysUserUpdateVo.getId());
            sysUserOrgPo.setOrgId(sysUserUpdateVo.getOrgId());
            sysUserOrgPo.setMainOrg(1);
            sysOrgUserService.deleteUserOrgRelationByUserIdAndOrgId(userIds, oldOrgId);
            sysOrgUserService.addOrgUserRelation(sysUserOrgPo);
        }

        //修改用户角色
        SysUserOrgRolePo sysRoleUserPo = null;
        if (!ObjectUtils.isEmpty(sysUserUpdateVo.getRoles())) {
            mapper.deleRoleUserByOrgId(sysUserUpdateVo.getId(), oldOrgId);
            for (Integer roleId : sysUserUpdateVo.getRoles()) {
                sysRoleUserPo = new SysUserOrgRolePo();
                sysRoleUserPo.setUserId(sysUserUpdateVo.getId());
                sysRoleUserPo.setOrgId(sysUserUpdateVo.getOrgId());
                sysRoleUserPo.setRoleId(roleId);
                sysUserOrgRoleService.addUserOrgRoleRelation(sysRoleUserPo);
            }
        }

        return sysUserPo;
    }

    /**
     * 删除用户
     *
     * @param userIds
     * @return
     */
    @CacheClear(keys = {"acm:users"})
    @Override
    public void deleteUser(List<Integer> userIds) {
        sysOrgUserService.deleteUserOrgRelationByUserIds(userIds);
        sysUserOrgRoleService.deleteUserOrgRoleRelationByUserIds(userIds);
        sysUserIptService.deleteUserIptRelationByUserIds(userIds);
        sysUserIptRoleService.deleteUserIptRelationByUserIds(userIds);
        super.deleteByIds(userIds);
    }

    /**
     * 批量增加用户
     *
     * @param userList
     * @return
     */
    @Override
    //@Transactional
    public List<Integer> addUserBatch(List<SysUserAddBatchFrom> userList) {
        List<Integer> userIds = new ArrayList<>();
        for (SysUserAddBatchFrom sysUserAddFrom : userList) {
            SysUserPo sysUserPo = dozerMapper.map(sysUserAddFrom, SysUserPo.class);
            SysUserPo sysUserPoIs = mapper.selectUserByUserName(sysUserPo.getUserName());
            if (!ObjectUtils.isEmpty(sysUserPoIs)) {
                throw new BaseException("用户名已存在");
            }
            if (ObjectUtils.isEmpty(sysUserPo.getStatus())) {
                sysUserPo.setStatus(1);
            }
            //密码加密
            String pwd = null;

            pwd = HMACUtil.getHMAC(sysUserAddFrom.getPassword(), HMACEnum.HMAC_KEY.getType());
            sysUserPo.setPassword(pwd);

            sysUserPo.setStaffStatus("1");
            sysUserPo.setSort(mapper.selectNextSort());
            sysUserPo.setUserType("1");
            sysUserPo.setUpdatePwdTime(new Date());
            //保存用户基本信息
            super.insert(sysUserPo);
            userIds.add(sysUserPo.getId());
            Integer userId = sysUserPo.getId();
            SysUserOrgPo sysOrgUserPo = new SysUserOrgPo();
            SysUserOrgRolePo sysRoleUserPo = null;

            sysOrgUserPo.setOrgId(sysUserAddFrom.getOrgId());
            sysOrgUserPo.setUserId(sysUserPo.getId());
            sysOrgUserPo.setMainOrg(1);

            //保存用户部门信息
            sysOrgUserService.addOrgUserRelation(sysOrgUserPo);

            //保存用户角色信息
            if (!ObjectUtils.isEmpty(sysUserAddFrom.getRoles())) {
                for (Integer roleId : sysUserAddFrom.getRoles()) {
                    sysRoleUserPo = new SysUserOrgRolePo();
                    sysRoleUserPo.setRoleId(roleId);
                    sysRoleUserPo.setUserId(sysUserPo.getId());
                    sysRoleUserPo.setOrgId(sysUserAddFrom.getOrgId());
                    sysUserOrgRoleService.addUserOrgRoleRelation(sysRoleUserPo);
                }
            }
        }
        return userIds;
    }


    /**
     * 获取组织用户
     *
     * @param orgId
     * @return
     */
    @Override
    public List<SysUserVo> selectUsersByOrgId(Integer orgId) {
        List<SysUserVo> list = mapper.selectUsersByOrgId(orgId);
        return list;
    }

    /**
     * 获取组织用户分页
     *
     * @param id
     * @param pageSize
     * @param curragenum
     * @return
     */
    @Override
    public PageInfo<SysUserVo> queryUsersByOrgIdPage(Integer id, Integer pageSize, Integer curragenum) {
        PageHelper.startPage(curragenum, pageSize);
        List<SysUserVo> list = mapper.selectUsersByOrgId(id);
        PageInfo<SysUserVo> pageInfo = new PageInfo<>(list);
        List<SysRoleVo> roleVos = null;
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            for (SysUserVo sysUserVo : pageInfo.getList()) {
                roleVos = sysUserVo.getRoles();
                StringBuffer str = new StringBuffer();
                if (!ObjectUtils.isEmpty(roleVos)) {
                    for (SysRoleVo sysRoleVo : roleVos) {
                        str.append("," + sysRoleVo.getRoleName());
                    }
                    String retRole = str.substring(1);
                    sysUserVo.setRetRole(retRole);
                    sysUserVo.setRoles(null);
                }
            }
        }
        return pageInfo;
    }

    /**
     * 获取ipt用户分页
     *
     * @param iptId
     * @return
     */
    @Override
    public PageInfo<SysIptUserVo> queryUsersByIptIdPage(Integer iptId, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysIptUserVo> list = mapper.selectUsersByIptId(iptId);
        PageInfo<SysIptUserVo> pageInfo = new PageInfo<>(list);
        List<SysIptUserVo> userIptPos = pageInfo.getList();
        if (!ObjectUtils.isEmpty(userIptPos)) {
            List<Integer> ids = new ArrayList<>();
            for (SysIptUserVo sysIptUserVo : userIptPos) {
                ids.add(sysIptUserVo.getId());
            }
            List<SysUserIptRoleVo> roles = sysUserIptRoleService.queryUserIptRoleRelationByUserId(ids, iptId);
            Map<Integer, List<SysUserIptRoleVo>> roleMap = ListUtil.bulidTreeListMap(roles, "userId", Integer.class);
            GeneralVo generalVo = null;
            List<GeneralVo> generalVos = null;
            for (SysIptUserVo sysIptUserVo : userIptPos) {
                generalVos = new ArrayList<>();
                if (!ObjectUtils.isEmpty(roleMap.get(sysIptUserVo.getId()))) {
                    for (SysUserIptRoleVo sysUserIptRoleVo : roleMap.get(sysIptUserVo.getId())) {
                        generalVo = new GeneralVo();
                        generalVo.setId(sysUserIptRoleVo.getRoleId());
                        generalVo.setName(sysUserIptRoleVo.getRoleName());
                        generalVos.add(generalVo);
                    }
                    sysIptUserVo.setRole(generalVos);
                }
            }
        }
        return pageInfo;
    }


    @Override
    public UserInfo getUserInfoByName(String userName) {

        // 根据用户名称获取用户信息
        SysUserPo userPo = this.getUserPoByName(userName);
        if (userPo != null) {
            // PO对象装配到userInfo中
            UserInfo userInfo = this.dozerMapper.map(userPo, UserInfo.class);
            // 根据用户ID查询所有的orgIds集合
            List<Integer> orgIds = sysOrgUserService.queryOrgIdsByUserId(userInfo.getId());
            userInfo.setOrgIds(orgIds);

            return userInfo;
        }
        return null;
    }


    @Override
    public UserInfo getUserInfoByCode(String userCode) {
        // 根据用户名称获取用户信息
        Example example = new Example(SysUserPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userCode", userCode);
        SysUserPo userPo = this.selectOneByExample(example);
        if (userPo != null) {
            // PO对象装配到userInfo中
            UserInfo userInfo = this.dozerMapper.map(userPo, UserInfo.class);
            // 根据用户ID查询所有的orgIds集合
            List<Integer> orgIds = sysOrgUserService.queryOrgIdsByUserId(userInfo.getId());
            userInfo.setOrgIds(orgIds);

            return userInfo;
        }
        return null;
    }

    @Override
    public SysUserPo selectUserByUserName(String userName) {
        return mapper.selectUserByUserName(userName);
    }

    @Override
    public SysUserPo getUserPoByName(String userName) {

        Example example = new Example(SysUserPo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userName", userName);

        List<SysUserPo> list = this.mapper.selectByExample(example);
        return !ObjectUtils.isEmpty(list) ? list.get(0) : null;

    }

    /**
     * 根据多个用户id获取用户信息
     *
     * @param userIds
     * @return
     */
    @Override
    public List<SysUserVo> queryUsersByIds(List<Integer> userIds) {
        List<SysUserVo> list = mapper.selectUsersByIds(userIds);
        return list;
    }

    /**
     * 根据多个用户id获取用户对象
     *
     * @param userIds
     * @return
     */
    @Override
    public Map<Integer, UserVo> queryUserVoByUserIds(List<Integer> userIds) {

        List<UserVo> userVos = new ArrayList<>();
        List<SysUserPo> userVoList = this.selectByIds(userIds);
        if (!ObjectUtils.isEmpty(userVoList)) {
            userVoList.forEach(user -> {
                UserVo u = new UserVo(user.getId(), user.getUserName(), user.getActuName(),user.getUserCode());
                userVos.add(u);
            });
        }
        return ListUtil.listToMap(userVos, "id", Integer.class);
    }

    /**
     * 用户登录验证
     *
     * @param userName
     * @param password
     * @param hostIp
     * @return
     */
    @Override
    public UserInfo validate(String userName, String password, String hostIp) {
        String pwd = HMACUtil.getHMAC(password, HMACEnum.HMAC_KEY.getType());
        // 根据用户名读取用户，如果查询为空，提示用户不存在
        SysUserPo userPo = this.getUserPoByName(userName);
        if (ObjectUtils.isEmpty(userPo)){
            throw new BaseException("该用户不存在!");
        }
        //判断是否为内部用户 需提示经单点登录进入
        if (!ObjectUtils.isEmpty(userPo.getUserCode())) {
            if (!userPo.getUserCode().contains("UVU")) {
                throw new BaseException("内部用户需要经单点登录进入系统!!");
            }
        }
        // 判断用户是否被锁定
        if (userPo.getStatus().equals(0) && ObjectUtils.isEmpty(userPo.getErrotTime())){
            throw new BaseException("该用户已被锁定，禁止登录!");
        }
        // 获取密码规则
        SysPwdRuleVo sysPwdRuleVo = sysPwdRuleService.queryPwdRuleAll();
        // 如果没有密码规则，默认输错五次后，锁定用户，半小时后解锁
        int errorNum = 5;
        int time = 30;
        if(sysPwdRuleVo != null && sysPwdRuleVo.getErrorNumber() != null){
            errorNum = sysPwdRuleVo.getErrorNumber();
            time = sysPwdRuleVo.getLockTime();
        }

        if(!ObjectUtils.isEmpty(userPo.getErrotTime())){
            int timeDiff = DateUtil.getTimeChaByTwoTime(userPo.getErrotTime(), new Date());
            // 根据密码规则的时间，判断当前锁定是否在时间范围内。
            if(time <= timeDiff){
                userPo.setStatus(1);
                userPo.setErrorNumber(0);
                userPo.setErrotTime(null);
                super.updateById(userPo);
            }
            // 当前用户输错次数达到密码规则规定的次数，禁止登陆。
            if(userPo.getErrorNumber() >= errorNum ){
                userPo.setStatus(0);
                super.updateById(userPo);
                throw new BaseException("该用户已被锁定,请在" + (time - timeDiff) + "分钟后重新登录");
            }
        }
        // 根据用户名和密码读取用户
        if(!pwd.equals(userPo.getPassword())){
            int num = this.validatePwdUpdateCount(userPo, errorNum , time);
            
            if(num == errorNum){
                throw new BaseException("用户密码输入错误已达"+errorNum+"次，该用户已被锁定，请在" + time + "分钟后重新登录");
            }

            throw new BaseException("用户密码错误!错误次数" + num + "，剩余次数" + (sysPwdRuleVo.getErrorNumber() - num) + "");
        }

        UserInfo userInfo = mapper.getUserInfoByUsername(userName,pwd);
        this._validate(userInfo, hostIp);
        return userInfo;
    }

    /**
     * 用户单点登录验证
     *
     * @param userName
     * @param password
     * @param hostIp
     * @return
     */
    @Override
    public UserInfo snValidate(String userName, String password, String hostIp) {
        UserInfo user = mapper.getUserInfoByUsername(userName, password);
        this._validate(user, hostIp);
        return user;
    }

    /**
     * 用户登录验证
     *
     * @param user
     * @param hostIp
     * @return
     */
    private void _validate(UserInfo user, String hostIp) {
        SysUserPo userPo = this.getUserPoByName(user.getUserName());

        //过滤超级管理员账号
        if (ObjectUtils.isEmpty(userPo)) {
            throw new BaseException("用户账号名或密码不正确！");
        }
        //获取三员管理状态
        Integer tmm = systemVo.getEnable();
        if((tmm == 1 && userPo.getUserName().equals("admin"))){
            throw new BaseException("用户账号存在！");
        }
        if (tmm == 1) {
            //获取密码规则设置
            SysPwdRuleVo sysPwdRuleVo = sysPwdRuleService.queryPwdRuleAll();
            //判断ip是否允许访问
            boolean flag = this.validateIpAccess(hostIp);
            if (flag) {
                throw new BaseException("禁止该IP访问！@@");
            }
            //判断密码锁定时长
            //this.validatePwdLockTime(userPo, sysPwdRuleVo);
            if (ObjectUtils.isEmpty(userPo)){
                throw new BaseException("用户不存在或账户密码错误!O(∩_∩)O~∑");
            }
            //判断密码修改周期
//            this.validateUpdatePwdCycly(userPo, sysPwdRuleVo);
//            if (ObjectUtils.isEmpty(user) && !ObjectUtils.isEmpty(userPo)) {
//                //判断验证密码次数
//                int num = this.validatePwdUpdateCount(userPo, sysPwdRuleVo.getErrorNumber(),sysPwdRuleVo.getLockTime());
//                throw new BaseException("用户密码错误!错误次数" + num + "，剩余次数" + (sysPwdRuleVo.getErrorNumber() - num) + "");
//            }

        } else {
            if (ObjectUtils.isEmpty(user)) {
                throw new BaseException("用户不存在或账户密码错误!");
            }
        }
        if (tmm == 0 && (userPo.getUserType().equals("3") || userPo.getUserType().equals("4"))) {
            throw new BaseException("三员管理未开启，该账户不允许登陆！");
        }
        userPo.setErrorNumber(0);
        // userPo.setLastloginTime(new Date()); 在后续登录时候修改登录时间
        Integer visitNum = userPo.getVisits() == null ? 0 : userPo.getVisits();
        userPo.setVisits(visitNum+1);
        super.updateById(userPo);
    }

    private boolean validateIpAccess(String hostIp) {
        List<SysIPAccessVo> rules = sysIPAccRuleService.queryIPAccByIsEffect();
        boolean flag = false;
        if (!ObjectUtils.isEmpty(rules)) {
            for (SysIPAccessVo sysIPAccessVo : rules) {
                Long userIp = IPUtil.getIpNum(hostIp);
                Long startIp = IPUtil.getIpNum(sysIPAccessVo.getStartIP());
                Long endIp = IPUtil.getIpNum(sysIPAccessVo.getEndIP());
                flag = IPUtil.isInner(userIp, startIp, endIp);
                if (flag) {
                    return flag;
                }
            }
        }
        return flag;
    }

    /**
     * 判断密码修改周期
     *
     * @param userPo
     * @param sysPwdRuleVo
     */
    private void validateUpdatePwdCycly(SysUserPo userPo, SysPwdRuleVo sysPwdRuleVo) {
        int time = DateUtil.getDaysChaByTwoTime(userPo.getUpdatePwdTime(), new Date());
        if (time >= sysPwdRuleVo.getCycle()) {
            userPo.setStatus(0);
            super.updateById(userPo);
            throw new BaseException("用户密码修改周期超时，请修改密码！(=￣ω￣=)");
        }
    }

    /**
     * 判断密码锁定时长
     *
     * @param userPo
     * @param sysPwdRuleVo
     */
    private void validatePwdLockTime(SysUserPo userPo, SysPwdRuleVo sysPwdRuleVo) {
        if (!ObjectUtils.isEmpty(userPo.getStatus())) {
            if (userPo.getStatus() == 0) {
                int time = DateUtil.getTimeChaByTwoTime(userPo.getErrotTime(), new Date());
                if (time >= sysPwdRuleVo.getLockTime()) {
                    userPo.setStatus(1);
                } else {
                    throw new BaseException("该用户已被锁定！(=￣ω￣=)");
                }
            }
        }
    }

    /**
     * 修改密码
     *
     * @param sysUpdatePassWordForm
     * @param userName
     */
    @Override
    public void updatePassWord(SysUpdatePassWordForm sysUpdatePassWordForm, String userName) {
        if (!ObjectUtils.isEmpty(sysUpdatePassWordForm)) {
            SysUserPo userPo = this.getUserPoByName(userName);

            //获取密码规则设置
            SysPwdRuleVo sysPwdRuleVo = sysPwdRuleService.queryPwdRuleAll();

            String newPwd = sysUpdatePassWordForm.getNewPassWord();

            //判断密码长度
            this.validatePwdLength(newPwd, sysPwdRuleVo.getLength());

            //新密码加密
            String newPassword = HMACUtil.getHMAC(newPwd, HMACEnum.HMAC_KEY.getType());

            String oldPwd = HMACUtil.getHMAC(sysUpdatePassWordForm.getOldPassWord(), HMACEnum.HMAC_KEY.getType());
            if (newPassword.equals(oldPwd)) {
                throw new BaseException("新增密码与原密码相同");
            }
            //判断原密码是否正确
            if (!userPo.getPassword().equals(oldPwd)) {
                throw new BaseException("原密码不正确");
            }

            //修改密码
            SysUserPo sysUserpo = mapper.selectByPrimaryKey(userPo.getId());

            int time = DateUtil.getDaysChaByTwoTime(userPo.getUpdatePwdTime(), new Date());
            if (time >= sysPwdRuleVo.getCycle()) {
                sysUserpo.setStatus(1);
            }
            sysUserpo.setPassword(newPassword);
            sysUserpo.setUpdatePwdTime(new Date());
            sysUserpo.setLastloginTime(new Date());
            super.updateById(sysUserpo);
        }
    }

    @Override
    public void updUserPo(SysUserPo userPo) {
        super.updateById(userPo);
    }

    /**
     * 判断密码验证次数
     *  @param userPo
     * @param errorNumber
     * @param lockTime
     */
    private int validatePwdUpdateCount(SysUserPo userPo, Integer errorNumber, Integer lockTime) {
        //密码验证超过规定次数
//        if (userPo.getErrorNumber() != null) {
//            if (userPo.getErrorNumber() + 1 >= errorNumber) {
//                //验证次数超过，锁定账号
//                userPo.setStatus(0);
//                //验证次数重置
//                userPo.setErrorNumber(0);
//                //密码验证错误时间
//                userPo.setErrotTime(new Date());
//                super.updateById(userPo);
//               // throw new BaseException("密码输入次数已达到" + errorNumber + "次,该账号已锁定!Σ( ° △ °|||)︴，锁定时长："+lockTime+"分钟");
//            }
//        }
        userPo.setErrotTime(new Date());
        Integer number = userPo.getErrorNumber() == null ? 0 : userPo.getErrorNumber();
        userPo.setErrorNumber(number + 1);
        super.updateById(userPo);
        return number + 1;
    }

    /**
     * 判断密码长度
     *
     * @param newPassWord
     * @param length
     */
    private void validatePwdLength(String newPassWord, Integer length) {
        if (newPassWord.length() < length) {
            throw new BaseException("密码长度不符合规则! 必须不小于"+length+"位！ (￣▽￣)~*");
        }
    }

    /**
     * 重置密码
     *
     * @param userIds
     */
    @Override
    public void resetPassword(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds)) {
            String pwd = HMACUtil.getHMAC("123456", HMACEnum.HMAC_KEY.getType());
            mapper.updatePasswordByUserIds(userIds, new Date(), pwd);
        }
    }

    /**
     * 锁定用户
     *
     * @param userIds
     */
    @Override
    public void lockUser(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds)) {
            List<Integer> ids = new ArrayList<>();
            for (Integer userId : userIds) {
                SysUserPo sysUserPo = mapper.selectByPrimaryKey(userId);
                if (sysUserPo.getStatus().equals(1)) {
                    ids.add(sysUserPo.getId());
                }
            }
            mapper.updateLockUser(ids, new Date());
        }
    }

    /**
     * 解锁用户
     *
     * @param userIds
     */
    @Override
    public void unlockUser(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds)) {
            mapper.updateUnLockUser(userIds, new Date());
        }
    }

    /**
     * 获取组织用户（全局）
     *
     * @param orgId
     * @return
     */
    @Override
    public List<SelectVo> queryUserSelectVosByOrgId(Integer orgId) {
        List<SelectVo> list = mapper.selectUserSelectByOrgId(orgId);
        return list;
    }

    /**
     * 根据用户id获取个人信息以及项目信息
     *
     * @param userId
     * @return
     */
    @Override
    public SysUserInfoVo getUserProjectInfo(Integer userId) {
        SysUserInfoVo sysUserInfoVo = this.getUserInfo(userId);
        if (!ObjectUtils.isEmpty(sysUserInfoVo)) {
            List<SysUserProjectInfoVo> getUserProjectInfoVos = mapper.getUserProjectInfoVos(userId);
            sysUserInfoVo.setProjectInfoVos(getUserProjectInfoVos);
        }
        return sysUserInfoVo;
    }

    /**
     * 获取所有正常用户
     *
     * @return
     */
    @Override
    public List<SysAllUserVo> queryAllUserList() {
        List<SysAllUserVo> sysUserVoList = mapper.selectAllUsers();
        return sysUserVoList;
    }

    @Override
    public List<SysUserVo> queryAllUser() {
        return mapper.queryAllUser();
    }


    /**
     * 拼接用户名
     *
     * @param userIds
     * @return
     */
    @Override
    public String queryUserNamesByIds(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds)) {
            List<SysUserVo> list = this.queryUsersByIds(userIds);
            StringBuffer names = new StringBuffer();
            for (SysUserVo sysUserVo : list) {
                names.append(sysUserVo.getActuName() + "，");
            }
            String retName = names.toString().substring(0, names.toString().length() - 1);
            return retName;
        }
        return "";
    }

    @Override
    public PageInfo<UserLevelVo> queryUserLevelList(UserLevelSearchForm searchForm, Integer pageSize, Integer
            currentPageNum) {
        //1、查询所有的用户UserLevelVo 根据级别过滤
        List<UserLevelVo> list = mapper.selectUserLevelList(searchForm);
        //2、所有部门集合
        List<SysOrgPo> orgPos = orgService.queryOrgsByBizIdAndBizType(null, null);
        // 部门集合转换为父子关系集合
        Map<Integer, SysOrgPo> sysPoMap = ListUtil.listToMap(orgPos);
        List<UserLevelVo> reList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(list)) {
            for (UserLevelVo userLevelVo : list) {
                // 计算部门的组织对象
                SysOrgPo org = this.getOrgName(sysPoMap, userLevelVo.getDepartment().getId());
                if (org != null) {
                    userLevelVo.setOrg(new GeneralVo(org.getId(), org.getOrgName()));
                }
                // 如果有组织查询条件，按照组织过滤
                if (!ObjectUtils.isEmpty(searchForm.getOrgId())) {
                    //　组织不为空，并且组织的ID与查询条件答案组织ID一致
                    if (org != null && org.getId().equals(searchForm.getOrgId())) {
                        reList.add(userLevelVo);
                    }
                } else {
                    // 没有查询条件，直接添加到集合中
                    reList.add(userLevelVo);
                }
            }
        }

        PageInfo<UserLevelVo> pageInfo = PageUtil.getPageInfo(reList,pageSize,currentPageNum);

        return pageInfo;
    }

    private SysOrgPo getOrgName(Map<Integer, SysOrgPo> sysPoMap, Integer id) {
        SysOrgPo orgPo = sysPoMap.get(id);
        if (orgPo != null && orgPo.getOrgType() != null && orgPo.getOrgType().equals(0)) {
            return orgPo;
        } else if (orgPo != null) {
            return this.getOrgName(sysPoMap, orgPo.getParentId());
        }
        return orgPo;
    }

    /**
     * 用户密级修改
     *
     * @param updateForm
     * @return
     */
    @Override
    public SysUserPo updateUserLevel(UserLevelUpdateForm updateForm) {
        SysUserPo sysUserPo = this.selectById(updateForm.getId());
        if (!ObjectUtils.isEmpty(sysUserPo)) {
            this.dozerMapper.map(updateForm, sysUserPo);
            this.updateById(sysUserPo);
        }
        return sysUserPo;
    }

    @Override
    public List<UserVo> querySysUserPoByIds(List<Integer> ids){
        return this.mapper.selectUserVoByUserIds(ids);
    }

    @Override
    public void updateUserByUserIdAndUpOrDown(Integer id, String upOrDown) {
        if(!ObjectUtils.isEmpty(id)){
            SysUserPo userPo = this.selectById(id);

            Integer sortNum = userPo.getSort();

            if(!ObjectUtils.isEmpty(sortNum)){
                if("up".equals(upOrDown)){
                    SysUserPo upUserPo = this.selectUserPoBySortNum(sortNum-1);

                    upUserPo.setSort(sortNum);
                    this.updateById(upUserPo);

                    userPo.setSort(sortNum-1);
                    this.updateById(userPo);
                }else if("down".equals(upOrDown)){
                    SysUserPo downUserPo = this.selectUserPoBySortNum(sortNum+1);

                    downUserPo.setSort(sortNum);
                    this.updateById(downUserPo);

                    userPo.setSort(sortNum+1);
                    this.updateById(userPo);
                }
            }
        }
    }

    /**
     * 根据sort获取用户对象
     * @param sortNum
     * @return
     */
    private SysUserPo selectUserPoBySortNum(Integer sortNum){
        if(!ObjectUtils.isEmpty(sortNum)){
            Example example = new Example(SysUserPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sort",sortNum);
            return this.selectOneByExample(example);
        }
        return null;
    }

    @Override
    public List<UserOrgVo> queryUserOrgByProjectId(Integer projectId) {
        // 是否开启项目团队
        boolean useTeam = orgService.isUseTeamByProjectId(projectId);
        String bizType = null;
        Integer bizId = null;
        if(useTeam){
            bizId = projectId;
            bizType = "project";
        }
        return this.mapper.selectUserOrgsByBiz(bizType,bizId);

    }
}
