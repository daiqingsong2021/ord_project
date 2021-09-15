package com.wisdom.acm.sys.rpc;


import com.wisdom.base.common.log.LogInfo;
import org.springframework.web.bind.annotation.*;


@RequestMapping("api")
@RestController
public class LogRest {

    @RequestMapping(value="/log/save",method = RequestMethod.POST)
    public @ResponseBody void saveLog(@RequestBody LogInfo info) {

    }
}
