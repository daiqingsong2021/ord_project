package com.wisdom.acm.szxm.controller.app;

import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.service.app.EwxService;
import com.wisdom.acm.szxm.vo.app.AppUserInfoVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 移动app鉴权控制器
 */
@RestController
@RequestMapping("app/auth")
public class AuthController
{

    @Autowired
    private EwxService ewxService;

    @GetMapping(value = "/user/info/{platform}/{code}")
    public ApiResult getUserInfo(@PathVariable("platform")String platform,@PathVariable("code")String code)
    {

        if (StringHelper.isNullAndEmpty(platform) || StringHelper.isNullAndEmpty(code))
        {
            throw new BaseException("platForm 或 code不能为空");
        }

        AppUserInfoVo appUserInfoVo=null;
        if("ewx".equals(platform))
        {//企业微信
            appUserInfoVo=ewxService.getEwxAppUserInfo(code);
        }
        else if("neusoft".equals(platform))
        {//东软
            appUserInfoVo=ewxService.getNeusoftAppUserInfo(code);
        }
        else
        {
            throw new BaseException("platform参数传递错误");
        }
        return ApiResult.success(appUserInfoVo);
    }

    /**
     * 获取企业的jsapi_ticket
     * @param url
     * @return
     */
    @GetMapping(value = "/getJsApiTicket")
    public ApiResult getJsApiTicket(@RequestParam(value="url",required=true) String url)
    {
        Map<String,Object> returnMap=ewxService.getJsApiTicket(url);
        return ApiResult.success(returnMap);
    }
}
