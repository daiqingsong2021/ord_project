package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysSearchMenuForm;
import com.wisdom.acm.sys.po.SysMenuPo;
import com.wisdom.acm.sys.vo.SysAuthMenuVo;
import com.wisdom.acm.sys.vo.SysMenuLableVo;
import com.wisdom.acm.sys.vo.SysMenuVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends CommMapper<SysMenuPo> {
    /**
     * 查询menu名称  for activiti
     * @param menuCode
     */
    String queryMenuNameByCode(@Param("menuCode") String menuCode);

    public List<SysMenuPo> selectMenuByAuthorityId(@Param("authorityId") String authorityId, @Param("authorityType") String authorityType);

    /**
     * 根据用户和组的权限关系查找用户可访问菜单
     * @param userId
     * @return
     */
    public List<SysMenuPo> selectAuthorityMenuByUserId (@Param("userId") int userId);

    /**
     * 根据用户和组的权限关系查找用户可访问的系统
     * @param userId
     * @return
     */
    public List<SysMenuPo> selectAuthoritySystemByUserId (@Param("userId") int userId);

    /**
     * 获取菜单列表树形
     * @param parentId
     * @return
     */
    public List<SysMenuVo> selectMenuByPid(@Param("parentId") Integer parentId);

    /**
     * 根据菜单id获取menuVo
     * @param menuId
     * @return
     */
    SysMenuVo selectMenuVoById(@Param("menuId") Integer menuId);

    /**
     * 根据菜单id获取menuVo
     * @param menuCode
     * @return
     */
    SysMenuVo selectMenuVoByCode(@Param("menuCode") String menuCode);

    List<Integer> selectMenuBySearch(@Param("search") SysSearchMenuForm searchMap);

    /**
     * 获取菜单页签
     * @param menuCode
     * @return
     */
    List<SysMenuLableVo> selectMenuTabs(@Param("menuCode") String menuCode);

    List<SysMenuVo> selectMenuVoList();

    /**
     * 查询主观评分模板 分组
     * @return
     */
    List<SysMenuVo> selectSubjectTemplateGroup();

    /**
     * 查询主观评分模板 业务模块
     * @return
     */
    List<SysMenuVo> selectSubjectTemplateMenu(@Param("menuCode") String menuCode);

    List<SysMenuVo> selectMenuVoMenuList();

    List<SysMenuVo> selectMenuVoMenuListByMenuId(@Param("menuId") Integer menuId);

    List<SysAuthMenuVo> selectParentMenuByMenuId(@Param("menuId") Integer menuId);

    List<SysAuthMenuVo> selectAuthMenuVoList();

    List<SysMenuVo> selectMenuVoByMenuCode(@Param("codes") List<String> list);

    List<SysMenuLableVo> selectGroupMenus(@Param("codes") List<String> groupCodes);
}