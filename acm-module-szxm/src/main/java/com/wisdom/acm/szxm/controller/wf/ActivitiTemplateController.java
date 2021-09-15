// package com.wisdom.acm.szxm.controller.wf;
//
// import com.wisdom.acm.szxm.form.wf.ActivitiTemplateAddForm;
// import com.wisdom.acm.szxm.service.wf.ActivitiTemplateService;
// import com.wisdom.base.common.msg.ApiResult;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import javax.validation.Valid;
//
// /**
//  * Author：wqd
//  * Date：2020-03-30 9:18
//  * Description：<描述>
//  */
//
// /**
//  * 流程定义中 增加工作流流程处理人模板
//  */
// @RestController
// @RequestMapping("activitiTemplate")
// public class ActivitiTemplateController {
//     private static final Logger logger = LoggerFactory.getLogger(ActivitiTemplateController.class);
//
//     @Autowired
//     ActivitiTemplateService activitiTemplateService;
//
//     @PostMapping("/addActivitiTemplate")
//     public ApiResult addActivitiTemplate(@RequestBody @Valid ActivitiTemplateAddForm activitiTemplateAddForm){
//         activitiTemplateService.addActivitiTemplate(activitiTemplateAddForm);
//         return ApiResult.success();
//     }
// }
