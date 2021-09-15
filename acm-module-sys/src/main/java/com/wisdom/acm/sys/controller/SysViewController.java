package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.form.SysViewAddForm;
import com.wisdom.acm.sys.form.SysViewUpdateForm;
import com.wisdom.acm.sys.po.SysViewPo;
import com.wisdom.acm.sys.service.SysViewService;
import com.wisdom.acm.sys.vo.SysViewTreeVo;
import com.wisdom.acm.sys.vo.SysViewVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 视图
 */
@RestController
public class SysViewController extends BaseController {

    @Autowired
    private SysViewService sysViewService;

    @Autowired
    private CommUserService commUserService;


    /**
     * 1.加载视图list
     *
     */
    @GetMapping("/view/{bizType}/list")
    public ApiResult loadView(@PathVariable("bizType") String bizType){
        UserInfo user = this.commUserService.getLoginUser();
        // 获取视图
        List<SysViewVo> sysViewVos = sysViewService.findViewByUser(user, bizType);
        return ApiResult.success(sysViewVos);
    }

    /**
     * 2.获取视图信息
     * @param viewId
     */
    @GetMapping("/view/{viewId}/info")
    public ApiResult getView(@PathVariable("viewId") Integer viewId) {
        SysViewVo sysViewVo = new SysViewVo();

        // 如果视图id为空，或者视图id等于-1，统统默认为全部视图
        if (ObjectUtils.isEmpty(viewId) || viewId.intValue() == -1) {
            sysViewVo.setId(viewId);
            sysViewVo.setViewName("全部");
            sysViewVo.setViewFields("");
            sysViewVo.setViewContent("");
        } else {
            sysViewVo = sysViewService.getSysViewVoById(viewId);
        }
        return ApiResult.success(sysViewVo);
    }


    /**
     * 3.获取视图tree
     *
     */
    @GetMapping("/view/{bizType}/tree")
    public ApiResult loadViewVoTree(@PathVariable("bizType") String bizType){
        UserInfo user = this.commUserService.getLoginUser();
        // 获取视图树形
        List<SysViewTreeVo> sysViewTreeVos = sysViewService.querySysViewTreeByUser(user, bizType);
        return ApiResult.success(sysViewTreeVos);
    }


    /**
     *  4.设为默认视图
     * @param viewId
     * @return
     */
    @PutMapping(value = "/view/{viewId}/{bizType}/default")
    public ApiResult setDefaultView(@PathVariable("viewId") Integer viewId,@PathVariable("bizType") String bizType){

        UserInfo user = this.commUserService.getLoginUser();
        sysViewService.setDefaultView(user.getId(),viewId,bizType);

        return ApiResult.success();
    }

    /**
     * 5.修改视图名称
     * @param viewId
     * @param viewName
     */
    @PutMapping("/view/{viewId}/{viewName}/update/name")
    public ApiResult updateViewName(@PathVariable("viewId") Integer viewId,@PathVariable("viewName") String viewName) {
        SysViewPo sysViewPo = sysViewService.updateViewName(viewId, viewName);
        SysViewVo sysViewVo = sysViewService.getSysViewVoById(sysViewPo.getId());
        return ApiResult.success(sysViewVo);
    }

    /**
     * 6.把个人视图转变为全局视图
     * @throws IOException
     */
    @PutMapping(value="/view/{viewId}/global")
    public ApiResult setUserView2GlobalView(@PathVariable("viewId") Integer viewId){

        sysViewService.setUserView2GlobalView(viewId);
        return ApiResult.success();
    }

    /**
     * 7.删除视图
     *
     * @param
     */
    @DeleteMapping("/view/delete")
    public ApiResult deleteGanttView(@RequestBody List<Integer> ids) {
        sysViewService.deleteViews(ids);
        return ApiResult.success();
    }


    /**
     * 8.保存视图数据
     *
     * @param sysViewUpdateForm
     */
    @PutMapping("/view/save")
    public ApiResult updateView(@RequestBody SysViewUpdateForm sysViewUpdateForm) {
        SysViewPo sysViewPo = sysViewService.updateView(sysViewUpdateForm);
        SysViewVo sysViewVo = sysViewService.getSysViewVoById(sysViewPo.getId());
        return ApiResult.success(sysViewVo);
    }

    /**
     * 9.另存为视图
     *
     * @param sysViewAddForm
     */
    @PostMapping("/view/saveas")
    public ApiResult saveView(@RequestBody SysViewAddForm sysViewAddForm) {
        UserInfo user = this.commUserService.getLoginUser();
        sysViewAddForm.setUserId(user.getId());
        SysViewPo sysViewPo = sysViewService.saveView(sysViewAddForm);
        SysViewVo sysViewVo = sysViewService.getSysViewVoById(sysViewPo.getId());
        return ApiResult.success(sysViewVo);
    }

}
