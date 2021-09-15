package com.wisdom.acm.sys.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysIptAddForm;
import com.wisdom.acm.sys.form.SysIptUpdateForm;
import com.wisdom.acm.sys.form.SysSearchIptForm;
import com.wisdom.acm.sys.form.SysUserIptAddForm;
import com.wisdom.acm.sys.po.SysIptPo;
import com.wisdom.acm.sys.service.SysIptService;
import com.wisdom.acm.sys.service.SysUserService;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.LoggerService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ipt")
public class SysIptController extends BaseController {

    @Autowired
    private SysIptService iptService;


    /**
     * 获取Ipt
     *
     * @return
     */
    @GetMapping(value = "/tree")
    public ApiResult queryIptTree() {
        List<SysIptVo> list = iptService.queryIptTree();
        return ApiResult.success(list);
    }

    /**
     * 搜索ipt
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public ApiResult queryIptsBySearch(SysSearchIptForm searchMap) {
        List<SysIptVo> list = iptService.queryIptsBySearch(searchMap);
        return ApiResult.success(list);
    }


    /**
     * 获取Ipt信息
     *
     * @param iptId
     * @return
     */
    @GetMapping(value = "/{iptId}/info")
    public ApiResult queryIptInfo(@PathVariable("iptId")Integer iptId) {
        SysIptInfoVo sysIptInfoVo = iptService.getIptInfo(iptId);
        return ApiResult.success(sysIptInfoVo);
    }


    /**
     * 增加Ipt
     *
     * @param sysIptAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "增加IPT" , module = LoggerModuleEnum.SM_IPT)
    public ApiResult addIpt(@RequestBody @Valid SysIptAddForm sysIptAddForm) {
        SysIptPo sysIptPo = iptService.addIpt(sysIptAddForm);
        SysIptInfoVo sysIptInfoVo = iptService.getIptInfo(sysIptPo.getId());
        SysIptVo sysIptVo = new SysIptVo();
        sysIptVo.setIptCode(sysIptInfoVo.getIptCode());
        sysIptVo.setIptName(sysIptInfoVo.getIptName());
        sysIptVo.setLevel(sysIptInfoVo.getLevel().getName());
        sysIptVo.setRemark(sysIptInfoVo.getRemark());
        sysIptVo.setId(sysIptInfoVo.getId());
        if (ObjectUtils.isEmpty(sysIptInfoVo.getParent())){
            sysIptVo.setParentId(0);
        }else{
            sysIptVo.setParentId(sysIptInfoVo.getParent().getId());
        }
        return ApiResult.success(sysIptVo);
    }

    /**
     * 修改Ipt
     *
     * @param sysIptUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateIpt(@RequestBody @Valid SysIptUpdateForm sysIptUpdateForm) {
        SysIptPo sysIptPo = iptService.updateIpt(sysIptUpdateForm);
        SysIptInfoVo sysIptInfoVo = iptService.getIptInfo(sysIptPo.getId());
        SysIptVo sysIptVo = new SysIptVo();
        sysIptVo.setIptCode(sysIptInfoVo.getIptCode());
        sysIptVo.setIptName(sysIptInfoVo.getIptName());
        sysIptVo.setLevel(sysIptInfoVo.getLevel().getName());
        sysIptVo.setRemark(sysIptInfoVo.getRemark());
        sysIptVo.setId(sysIptInfoVo.getId());
        return ApiResult.success(sysIptVo);
    }

    /**
     * 删除Ipt
     *
     * @param iptIds
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除IPT" , module = LoggerModuleEnum.SM_IPT)
    public ApiResult deleteIpt(@RequestBody List<Integer> iptIds) {
        String names = iptService.queryNamesByIds(iptIds,"iptName");
        this.setAcmLogger(new AcmLogger("批量删除IPT及子节点IPT：" + names));
        iptService.deleteIpt(iptIds);
        return ApiResult.success();
    }

    /**
     * 分配Ipt用户
     *
     * @param list
     * @return
     */
    @PostMapping(value = "/{iptId}/user/add")
    @AddLog(title = "分配IPT用户" , module = LoggerModuleEnum.SM_IPT)
    public ApiResult addIptUser(@RequestBody @Valid List<SysUserIptAddForm> list,@PathVariable("iptId")Integer iptId) {
        String logger = iptService.queryAssignIptUserLogger(list,iptId);
        this.setAcmLogger(new AcmLogger(logger));
        iptService.addIptUser(list,iptId);
        return ApiResult.success();

    }

   /**
     * Ipt用户角色
     *
     * @param sysUserIptAddForm
     * @return
     */
    @PutMapping(value = "/{iptId}/user/roles/add")
    public ApiResult addIptUserRole(@RequestBody @Valid SysUserIptAddForm sysUserIptAddForm,@PathVariable("iptId")Integer iptId) {
        iptService.addIptUserRole(sysUserIptAddForm,iptId);
        return ApiResult.success();

    }

    /**
     * 删除Ipt分配用户
     *
     * @param userIds
     * @return
     */
    @DeleteMapping(value = "/{iptId}/user/delete")
    @AddLog(title = "删除IPT分配用户" , module = LoggerModuleEnum.SM_IPT)
    public ApiResult deleteIptUser(@RequestBody List<Integer> userIds,@PathVariable("iptId")Integer iptId) {
        String logger = iptService.queryDeleteIptUserLogger(userIds,iptId);
        this.setAcmLogger(new AcmLogger(logger));
        iptService.deleteIptUser(userIds,iptId);
        return ApiResult.success();

    }
}
