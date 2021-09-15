package com.wisdom.acm.activiti.controller;

import com.wisdom.acm.activiti.service.TestLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * demo  测试流程使用
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    TestLeaveService testLeaveService;

    /**
     * 查询当前用户的任务列表
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping("/getTaskByUserId")
    @ResponseBody
    public Object getTaskByUserId(String userId, HttpServletRequest request) {
        //System.out.println();
        return testLeaveService.findTaskByUserId(userId);
    }


    /**
     * 发起人查询自己的任务列表
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping("/getBusinessKey")
    @ResponseBody
    public Object getBusinessKey(String userId, HttpServletRequest request) {
        //System.out.println();
        return testLeaveService.findBusinessKey(userId);
    }

    /**
     * 处理完成任务
     * @param taskId
     * @param userId
     * @param audit
     * @param request
     * @return
     */
    @RequestMapping("/completeTask")
    @ResponseBody
    public String completeTask(String taskId, String userId, String audit, HttpServletRequest request) {
        testLeaveService.completeTaskByUser(taskId, userId, audit);
        return "审批完成...";
    }

    /**
     * 获取
     * @param request
     * @return
     */
    @RequestMapping("/getProcessEnd")
    @ResponseBody
    public Object getProcessDefinition(String processInstanceId, HttpServletRequest request) {
        return testLeaveService.queryProcessIsEnd(processInstanceId);
    }

    /**
     * 显示流程定义图
     * @param procDefId
     * @param request
     * @param response
     */
    @RequestMapping("/showProcDefImg")
    public void showProcDefImg(String procDefId, HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = testLeaveService.findProcessPic(procDefId);
            byte[] b = new byte[1024];
            int len = -1;
            while((len = inputStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示流程实例图
     * @param procInstId
     * @param request
     * @param response
     */
    @RequestMapping("/showProcInstImg")
    public void showProcInstImg(String procInstId, HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = testLeaveService.readProcessImg(procInstId);
            byte[] b = new byte[1024];
            int len = -1;
            while((len = inputStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取
     * @param request
     * @return
     */
    @RequestMapping("/findGroupTask")
    @ResponseBody
    public Object findGroupTask(String groupId, HttpServletRequest request) {
        return testLeaveService.findGroupTask(groupId);
    }

}
