package com.wisdom.acm.activiti.controller;

import com.wisdom.acm.activiti.po.Role;
import com.wisdom.acm.activiti.po.User;
import com.wisdom.acm.activiti.service.RoleService;
import com.wisdom.acm.activiti.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/service")
public class CandidateController {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    /**
     *  查询组
     */
    @RequestMapping("/candidate/getGroupList")
    @ResponseBody
    public Map<String, Object> getGroupList(int pageNum, int pageSize, String findContent ) {
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("rows", this.roleService.getList(pageNum, pageSize, findContent));
        info.put("total", this.roleService.getCount(findContent));
        return info;
    }

    /**
     *  查询用户
     */
    @RequestMapping("/candidate/getUserList")
    @ResponseBody
    public Map<String, Object> getUserList(int pageNum, int pageSize, String findContent ) {
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("rows", this.userService.getList(pageNum, pageSize, findContent));
        info.put("total", this.userService.getCount(findContent));
        return info;
    }

    /**
     *  查询用户
     */
    @RequestMapping("/candidate/getCandidateByIds")
    @ResponseBody
    public Map<String, Object> getUserListByIds(String groupIds, String userIds) {
        List<String> groupId = new ArrayList<String>();
        List<String> userId = new ArrayList<String>();
        if(groupIds != null && groupIds.trim().length() != 0){
            groupId = Arrays.asList(groupIds.split(","));
        }
        if(userIds != null && userIds.trim().length() != 0){
            userId = Arrays.asList(userIds.split(","));;
        }
        Map<String, Object> info = new HashMap<String, Object>();
        List<Role> roles = this.roleService.getListById(groupId);
        info.put("groups", roles);
        List<User> users = this.userService.getListById(userId);
        info.put("users", users);
        return info;
    }
}
