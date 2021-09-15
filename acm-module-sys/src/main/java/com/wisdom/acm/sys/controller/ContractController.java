package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.form.SectionAddForm;
import com.wisdom.acm.sys.service.GCContractService;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Author：wqd
 * Date：2019-09-25 13:44
 * Description：<描述>
 */
@RestController
@RequestMapping(value = "org")
public class ContractController {
    @Autowired
    private GCContractService contractService;

    @GetMapping(value = "/getGcSections")
    public ApiResult getGcSections() {
        return ApiResult.success(contractService.queryGCSections());
    }

    @PostMapping(value = "/addGcSections")
    public ApiResult addGcSections(@RequestBody @Valid List<SectionAddForm> projectTeamAddForms) {
        contractService.tbSection(projectTeamAddForms);
        return ApiResult.success();
    }

}
