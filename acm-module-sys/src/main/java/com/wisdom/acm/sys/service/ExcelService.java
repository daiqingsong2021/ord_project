package com.wisdom.acm.sys.service;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


public interface ExcelService {

    /**
     * 导入Excel
     * @param request
     * @return
     * @throws IOException
     */
    Map<String,Object> importExcel(HttpServletRequest request) throws IOException;
}
