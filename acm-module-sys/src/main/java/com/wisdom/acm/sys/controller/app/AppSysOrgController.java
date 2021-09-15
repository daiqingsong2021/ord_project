package com.wisdom.acm.sys.controller.app;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.service.AppSysOrgService;
import com.wisdom.acm.sys.vo.app.AppSysOrgUserVo;
import com.wisdom.acm.sys.vo.app.AppSysOrgVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "app")
public class AppSysOrgController {

    @Autowired
    private AppSysOrgService appSysOrgService;

    /**
     * 获取组织机构列表
     * @param size
     * @param current
     * @param key
     * @return
     */
    @GetMapping("/addressbook/org/{size}/{current}/list")
    public ApiResult queryAppSysOrgVoList(@PathVariable("size") Integer size,
                                          @PathVariable("current") Integer current,
                                          String key){
        PageInfo<AppSysOrgVo> list = appSysOrgService.queryAppSysOrgVoList(size,current,key);

        return new TableResultResponse(list);
    }

    /**
     * 获取部门联系人列表
     * @param orgId
     * @param size
     * @param current
     * @param key
     * @return
     */
    @GetMapping("/addressbook/contacts/{orgId}/{size}/{current}/list")
    public ApiResult queryAppSysOrgUserList(@PathVariable("orgId") Integer orgId,
                                            @PathVariable("size") Integer size,
                                            @PathVariable("current") Integer current,
                                            String key){
        PageInfo<AppSysOrgUserVo> list = appSysOrgService.queryAppSysOrgUserVoList(orgId, size, current, key);

        return new TableResultResponse(list);
    }

}
