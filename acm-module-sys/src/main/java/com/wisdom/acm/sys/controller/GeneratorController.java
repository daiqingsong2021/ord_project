package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ace on 2017/8/25.
 */
@RestController
@RequestMapping("generator")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

//    /**
//     * 列表
//     */
//    @ResponseBody
//    @RequestMapping("/page")
//    public TableResultResponse<Map<String, Object>> list(@RequestParam Map<String, Object> params) {
//        List<Map<String, Object>> result = generatorService.queryList(params);
//        int total = generatorService.queryTotal(params);
//        return new TableResultResponse<Map<String, Object>>(total, result);
//    }

    /**
     * 生成代码 D:\sssss\pppppp
     */
    @RequestMapping(value = "/code" , method = RequestMethod.POST)
    public void code(@RequestBody Map<String,List<String>> tableMap) {
//        tableNames = JSON.parseArray().toArray(tableNames);
        List<String> tableNames = tableMap.get("tableList");
        byte[] data = generatorService.generatorCode(tableNames);

    }

    @RequestMapping(value = "/tablecode", method = RequestMethod.POST)
    @ResponseBody
    public void tableCode(@RequestBody Map<String,String> sqlMap)  {
//        tableNames = JSON.parseArray().toArray(tableNames);
        generatorService.reactCode(sqlMap);

    }

    @RequestMapping(value = "/formCode", method = RequestMethod.POST)
    @ResponseBody
    public void formCode(@RequestBody Map<String,String> sqlMap) throws IOException {
//        tableNames = JSON.parseArray().toArray(tableNames);
       generatorService.formCode(sqlMap);

    }
}
