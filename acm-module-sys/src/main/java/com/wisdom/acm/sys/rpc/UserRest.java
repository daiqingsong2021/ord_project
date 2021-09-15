package com.wisdom.acm.sys.rpc;

import com.wisdom.cache.annotation.Cache;
import com.wisdom.acm.sys.rpc.service.PermissionService;
import com.wisdom.base.common.authority.PermissionInfo;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 */
@RestController
@RequestMapping("api")
public class UserRest {
    @Autowired
    private PermissionService permissionService;

    @Cache(key="permission")
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public @ResponseBody
    List<PermissionInfo> getAllPermission(){
        return permissionService.getAllPermission();
    }

    @Cache(key="permission:u{1}")
    @RequestMapping(value = "/user/un/{userName}/permissions", method = RequestMethod.GET)
    public @ResponseBody List<PermissionInfo> getPermissionByUsername(@PathVariable("userName") String userName){
        return permissionService.getPermissionByUsername(userName);
    }

    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    public @ResponseBody
    UserInfo validate(@RequestBody Map<String,String> body){
        return permissionService.validate(body.get("userName"),body.get("password"));
    }
}
