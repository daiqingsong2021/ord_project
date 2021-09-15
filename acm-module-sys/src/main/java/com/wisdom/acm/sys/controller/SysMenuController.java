package com.wisdom.acm.sys.controller;


import com.wisdom.acm.sys.form.SysMenuAddForm;
import com.wisdom.acm.sys.form.SysMenuUpdateForm;
import com.wisdom.acm.sys.form.SysSearchMenuForm;
import com.wisdom.acm.sys.po.SysMenuPo;
import com.wisdom.acm.sys.service.SysMenuService;
import com.wisdom.acm.sys.vo.SysMenuLableVo;
import com.wisdom.acm.sys.vo.SysMenuVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequestMapping("menu")
@RestController
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;

    @Autowired
    protected HttpServletRequest request;

    /**
     * 查询menu名称  for activiti
     * @return
     */
    @GetMapping(value = "/queryMenuNameByCode")
    public ApiResult queryMenuNameByCode(@RequestParam String menuCode) {
        return ApiResult.success(menuService.queryMenuNameByCode(menuCode));
    }

    //@Value("${myww}") // git配置文件里的key http://192.168.2.65:7010/acm-module-sys/dev
    private String myww = "";

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    @ResponseBody
    public String hi() {
        return myww;
    }

    /**
     * 获取菜单列表
     *
     * @return
     */
    @GetMapping(value = "/tree")
    public ApiResult queryMenuTree() {
        List<SysMenuVo> menuTree = menuService.queryMenuAll(); //获取菜单列表
        return ApiResult.success(menuTree);
    }

    /**
     * 查询主观评分模板 分组
     * @return
     */
    @GetMapping(value = "/subjectTemplateGroup")
    public ApiResult selectSubjectTemplateGroup() {
        return ApiResult.success(menuService.selectSubjectTemplateGroup());
    }

    /**
     * 查询主观评分模板 业务模块
     * @return
     */
    @GetMapping(value = "/subjectTemplateMenu/{menuCode}")
    public ApiResult selectSubjectTemplateMenu(@PathVariable("menuCode") String menuCode) {
        return ApiResult.success(menuService.selectSubjectTemplateMenu(menuCode));
    }

    /**
     * 获取菜单列表（不包含标签）
     * @return
     */
    @GetMapping("/menu/tree/menuId/{menuId}")
    public ApiResult queryMenuTreeMenu(@PathVariable("menuId") Integer menuId){
        List<SelectVo> menuTree = menuService.queryMenu(menuId); //获取菜单列表(不包含标签)
        return ApiResult.success(menuTree);
    }

    /**
     * 获取父级菜单
     * @param menuId
     * @return
     */
    @GetMapping("/menu/tree/parentId/{menuId}")
    public ApiResult selectParentMenuByMenuId(@PathVariable("menuId") Integer menuId){
        return ApiResult.success(menuService.selectParentMenuByMenuId(menuId));
    }

    /**
     * 根据菜单id获取菜单信息
     * @param menuId
     * @return
     */
    @GetMapping("/menuId/{menuId}/menu")
    public ApiResult queryMenuById(@PathVariable("menuId") Integer menuId){
        return ApiResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 根据菜单id集合获取菜单信息集合
     * @param menuIds
     * @return
     */
    @PostMapping("/menuIds/menu")
    public ApiResult queryMenusByIds(@RequestBody List<Integer> menuIds){
        return ApiResult.success(menuService.queryMenuByIds(menuIds));
    }

    /**
     * 搜索菜单列表
     *
     * @return
     */
    @GetMapping(value = "/search")
    public ApiResult queryMenyBySearch(SysSearchMenuForm searchMap) {
        List<SysMenuVo> menuTree = menuService.queryMenuTreeBySearch(searchMap);
        return ApiResult.success(menuTree);
    }

    /**
     * 获取菜单信息
     *
     * @param menuId
     * @return
     */
    @GetMapping(value = "/{menuId}/info")
    public ApiResult getMenuInfo(@PathVariable("menuId")Integer menuId) {
        if (ObjectUtils.isEmpty(menuId)) {
            return ApiResult.result(777,"id不能为空");
        }
        SysMenuVo sysMenuVo = menuService.getMenuVoById(menuId);
        return ApiResult.success(sysMenuVo);
    }

    /**
     * 获取菜单信息
     *
     * @param menuCode
     * @return
     */
    @GetMapping(value = "/code/{menuCode}/info")
    public ApiResult getMenuInfo(@PathVariable("menuCode") String menuCode) {
        SysMenuVo sysMenuVo = menuService.getMenuVoByCode(menuCode);
        return ApiResult.success(sysMenuVo);
    }



    /**
     * 修改菜单信息
     *
     * @param menu
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateMenu(@RequestBody @Valid SysMenuUpdateForm menu) {
        SysMenuPo sysMenuPo = menuService.updateMenu(menu);
        SysMenuVo sysMenuVo = menuService.getMenuVoById(sysMenuPo.getId());
        return ApiResult.success(sysMenuVo);
    }

    /**
     * 增加菜单
     *
     * @param menu
     * @return
     */
    @AddLog(title = "增加菜单",module = LoggerModuleEnum.SM_MENU, initContent = true)
    @PostMapping(value = "/add")
    public ApiResult addMenu(@RequestBody @Valid SysMenuAddForm menu) {
        SysMenuPo sysMenuPo = menuService.addMenu(menu);
        SysMenuVo sysMenuVo = menuService.getMenuVoById(sysMenuPo.getId());
        return ApiResult.success(sysMenuVo);
    }

    /**
     * 删除菜单
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除菜单",module = LoggerModuleEnum.SM_MENU)
    public ApiResult deleteMenu(@RequestBody List<Integer> ids) {
        // 根据菜单ID获取菜单名称字符串
        String menuNames = menuService.queryNamesByIds(ids,"menuName");
        this.setAcmLogger(new AcmLogger("批量删除菜单及子节点菜单，菜单名称如下："+menuNames));
        menuService.deleteMenu(ids);
        return ApiResult.success();
    }


    /**
     * 获取菜单页签
     *
     * @param menuCode
     * @return
     */
    @GetMapping(value = "/label/{menuCode}/infos")
    public ApiResult queryMenuTabs(@PathVariable("menuCode")String menuCode) {
        Map<String,List<SysMenuLableVo>> map = menuService.queryMenuTabs(menuCode);
        return ApiResult.success(map);
    }
}


