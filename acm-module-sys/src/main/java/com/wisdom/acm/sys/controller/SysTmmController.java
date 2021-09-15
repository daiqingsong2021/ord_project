package com.wisdom.acm.sys.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.*;
import com.wisdom.acm.sys.po.SysIPAccessPo;
import com.wisdom.acm.sys.po.SysOperationAuditPo;
import com.wisdom.acm.sys.po.SysPwdRulePo;
import com.wisdom.acm.sys.service.SysIPAccRuleService;
import com.wisdom.acm.sys.service.SysLoginService;
import com.wisdom.acm.sys.service.SysOperationAuditService;
import com.wisdom.acm.sys.service.SysPwdRuleService;
import com.wisdom.acm.sys.vo.SysIPAccessVo;
import com.wisdom.acm.sys.vo.SysOperationAuditVo;
import com.wisdom.acm.sys.vo.SysPwdRuleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.LoggerService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "tmm")
public class SysTmmController extends BaseController {
    @Autowired
    private SysIPAccRuleService sysIPAccRuleService;

    @Autowired
    private SysPwdRuleService sysPwdRuleService;

    /**
     * 三员管理状态开启/关闭
     *
     * @param isOpen
     * @return
     */
    @GetMapping(value = "/{isopen}")
    public ApiResult openTmmAuth(@PathVariable("isopen") Integer isOpen) {
//        sysLoginService.updateIsOpenTmm(isOpen);
//        if (isOpen == 1) {
//            loggerService.addAcmLoggerInfo("SM-TMM", "三员管理开启","开启三员管理");
//        } else if (isOpen == 0) {
//            loggerService.addAcmLoggerInfo("SM-TMM", "三员管理关闭","关闭三员管理");
//        }
        return ApiResult.success();
    }

    /**
     * 查询所有访问设置
     *
     * @return
     */
    @GetMapping(value = "/ipaccess/list/{pageSize}/{currentPageNum}")
    public ApiResult queryIPAccessList(@PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        PageInfo<SysIPAccessVo> list = sysIPAccRuleService.queryIPAccAll(pageSize, currentPageNum);
        return new TableResultResponse(list);
    }

    /**
     * 增加设置访问
     *
     * @param sysIPAccRuleAddForm
     * @return
     */
    @PostMapping(value = "/ipaccess/add")
    @AddLog(title = "增加访问设置", module = LoggerModuleEnum.SM_TMM)
    public ApiResult addIPAcess(@RequestBody SysIPAccRuleAddForm sysIPAccRuleAddForm) {
        this.setAcmLogger(new AcmLogger("增加访问设置，起始IP:" + sysIPAccRuleAddForm.getStartIP()+",结束IP:" + sysIPAccRuleAddForm.getEndIP()));
        SysIPAccessPo sysIPAccessPo = sysIPAccRuleService.addIPAccessRule(sysIPAccRuleAddForm);
        SysIPAccessVo sysIPAccessVo = sysIPAccRuleService.getIPAcc(sysIPAccessPo.getId());
        return ApiResult.success(sysIPAccessVo);
    }

    /**
     * 更新设置访问
     *
     * @param sysIPAccRuleUpdateForm
     * @return
     */
    @PutMapping(value = "/ipaccess/update")
    public ApiResult updateIPAcess(@RequestBody SysIPAccRuleUpdateForm sysIPAccRuleUpdateForm) {
        SysIPAccessPo bean = sysIPAccRuleService.updateIPAccessRule(sysIPAccRuleUpdateForm);
        SysIPAccessVo IPAccessVo = sysIPAccRuleService.getIPAcc(bean.getId());
        return ApiResult.success(IPAccessVo);
    }

    /**
     * 删除设置访问
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/ipaccess/delete")
    @AddLog(title = "删除访问设置", module = LoggerModuleEnum.SM_TMM)
    public ApiResult deleteIPAcess(@RequestBody List<Integer> ids) {
        String logger = sysIPAccRuleService.queryIpLoggerByIds(ids);
        this.setAcmLogger(new AcmLogger("删除设置访问：" + logger));
        sysIPAccRuleService.deleteIPAcc(ids);
        return ApiResult.success();
    }

    /**
     * 查询密码设置
     *
     * @return
     */
    @GetMapping(value = "/pwdrule/info")
    public ApiResult queryPwdRuleAll() {
        SysPwdRuleVo sysPwdRuleVo = sysPwdRuleService.queryPwdRuleAll();
        return ApiResult.success(sysPwdRuleVo);
    }

    /**
     * 更新密码设置
     *
     * @param sysPwdRuleUpdateForm
     * @return
     */
    @PutMapping(value = "/pwdrule/update")
    public ApiResult updatePwdRule(@RequestBody SysPwdRuleUpdateForm sysPwdRuleUpdateForm) {
        SysPwdRulePo bean = sysPwdRuleService.updatePwdRule(sysPwdRuleUpdateForm);
        SysPwdRuleVo pwdRuleVo = sysPwdRuleService.getPwdRule(bean.getId());
        return ApiResult.success(pwdRuleVo);
    }

//    /**-
//     * 操作审计列表
//     *
//     * @param searchMap
//     * @param pageSize
//     * @param currentPageNum
//     * @return
//     */
//    @GetMapping(value = "/audit/list/{pageSize}/{currentPageNum}")
//    public ApiResult queryOperationAudit(SysSearchAuditForm searchMap, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
//        PageInfo<SysOperationAuditVo> retList = sysOperationAuditService.queryOperationAudit(searchMap, pageSize, currentPageNum);
//        return new TableResultResponse(retList);
//    }

}
