package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.service.ExcelService;
import com.wisdom.acm.sys.vo.excel.ExcelErrorListVo;
import com.wisdom.acm.sys.vo.excel.ExcelErrorVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ExcelCell;
import com.wisdom.base.common.util.ExcelUtil;
import com.wisdom.base.common.util.ResourceUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@ResponseBody
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    /**
     * 获取用户收藏夹
     * @return
     */
    @PostMapping(value = "/excel/import")
    public ApiResult importExcel(HttpServletRequest request){
        Map<String,Object> excelError = null;
        List<Map<String,Object>> errorList = null;
        try {
            excelError = excelService.importExcel(request);
            if(excelError != null){
                errorList = (List<Map<String, Object>>) excelError.get("errorList");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.success(errorList);
    }



    /**
     * 导出计划反馈excel
     * @param response
     * @return
     */
    @PostMapping("/excel/import/error/down")
    public ApiResult exportPlan(@RequestBody ExcelErrorVo excelErrorVo, HttpServletResponse response) throws Exception {

        InputStream inputStream =  ResourceUtil.findResoureFile("files/错误日志.xls");

        ExcelCell[] cells = new ExcelCell[]{
                new ExcelCell("arr"),
                new ExcelCell("error")
        };
        List<ExcelErrorListVo> excelErrorVos = excelErrorVo.getErrors();
        List<Map<String,Object>> dataList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(excelErrorVos)){

            for(ExcelErrorListVo errorVo : excelErrorVos){
                Map<String,Object> map = new HashMap<>();
                map.put("arr",errorVo.getArr());
                map.put("error",errorVo.getError());
                dataList.add(map);
            }

        }

        Workbook workbook = ExcelUtil.getWorkbook(inputStream);
        Sheet sheet0 = workbook.getSheetAt(0);
        ExcelUtil.writeSheet(sheet0, cells, dataList, false, 1, null);

        String fileName = URLEncoder.encode("错误日志.xls","UTF-8");
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");

        // 获取导出数据的集合
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        return ApiResult.success();
    }

}
