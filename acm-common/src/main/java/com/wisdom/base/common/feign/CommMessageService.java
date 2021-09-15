package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.form.ActivSysMessageAddForm;
import com.wisdom.base.common.form.SysMessageAddForm;
import com.wisdom.base.common.form.SysMessageUserForm;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 */
@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface CommMessageService {

    /**
     * 转发消息/新增/回复
     * @param
     * @return
     */
    @RequestMapping(value = "/message/write", method = RequestMethod.POST)
    ApiResult sendMessageRecv(@RequestBody SysMessageAddForm sysMessageAddForm);

    @PutMapping("/message/addMesRecvForActivi")
    public ApiResult<Integer> addMessageRecvForActivi(@RequestBody SysMessageAddForm sysMessageRecvForm);
    /**
     * 发现现有的方法在commom包调用sys包的时候对象不一致，调用错误一个对象是UserVo一个是Integer
     * @param
     * @return
     */
    @PutMapping("/message/addMesRecvForActivi")
    public ApiResult<Integer> addMessageRecvForActivi(@RequestBody ActivSysMessageAddForm activSysMessageAddForm);

    @PutMapping("/message/addMesUserForActivi")
    public ApiResult<Integer> addMesUserForActivi(@RequestBody SysMessageUserForm sysMessageUserForm);
}
