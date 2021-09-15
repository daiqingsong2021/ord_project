package com.wisdom.auth.server.controller;

import com.wisdom.auth.server.biz.ClientBiz;
import com.wisdom.auth.server.entity.Client;
import com.wisdom.auth.server.entity.ClientService;
import com.wisdom.base.common.msg.ObjectRestResponse;
import com.wisdom.base.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("service")
public class ServiceController {

    @Autowired
    protected ClientBiz baseService;

    @RequestMapping(value = "/{id}/client", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@PathVariable("id")int id, String clients){
        baseService.modifyClientServices(id, clients);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/client", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ClientService> getUsers(@PathVariable("id")int id){
        return new ObjectRestResponse<ClientService>().rel(true).data(baseService.getClientServices(id));
    }
}
