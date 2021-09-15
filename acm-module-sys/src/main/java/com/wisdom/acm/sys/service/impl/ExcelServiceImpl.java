package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.service.ExcelService;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.excel.ExcelDataForm;
import com.wisdom.base.common.form.excel.ExcelSheetDataForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ExcelUtil;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.JsonUtil;
import com.wisdom.base.common.util.WebUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Map<String,Object> importExcel(HttpServletRequest request) throws IOException {
        Workbook wb = null;
        List<Map<String,Object>> sheets = null;
        Map<String,Object> params = null;
        String action = null;
        try {
            MultipartHttpServletRequest mul = (MultipartHttpServletRequest) request;
            Map<String, String[]> map =  mul.getParameterMap();
            String sheetsString = FormatUtil.toString(map.get("sheets"));
            String paramsString = FormatUtil.toString(map.get("params"));
            action = FormatUtil.toString(map.get("action"));
            sheets = JsonUtil.readValue(sheetsString,List.class);
            params = (Map<String,Object>) JsonUtil.readValue(paramsString,Map.class);

            Map<String, MultipartFile> fileMap = mul.getFileMap();
            out:
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                for (MultipartFile file : mul.getFiles(entity.getKey())) {
                    wb = ExcelUtil.getWorkbook(file.getInputStream());
                    break out;
                }
            }
        } catch (Exception e) {
        }
        Sheet sheet;
        List<ExcelSheetDataForm> sheetDataForms = new ArrayList<>();
        List<Map<String, Object>> valueList, columnList;
        for (Map<String,Object> sheetForm : sheets) {

            ExcelSheetDataForm sheetDataForm = new ExcelSheetDataForm();
            Integer index = FormatUtil.parseInt(sheetForm.get("index"));
            Integer colEnd = FormatUtil.parseInt(sheetForm.get("colEnd"));
            Integer rowStart = FormatUtil.parseInt(sheetForm.get("rowStart"));
            // 打开指定工作簿
            sheet = ExcelUtil.getSheet(wb, index);
            // 取出指定工作簿的内容
            valueList = ExcelUtil.getSheetValueMap(sheet,colEnd,rowStart,-1);
            // 列集合
            columnList = ExcelUtil.getSheetValueMap(sheet,colEnd,rowStart-1,rowStart);
            List<Map<String,Object>> columns = new ArrayList<>();
            if( !ObjectUtils.isEmpty(columnList)){
                Map<String,Object> column = columnList.get(0);
                column.forEach((k,v) -> {
                    if(!"rowIndex".equals(FormatUtil.toString(k))){
                        Map<String,Object> m = new HashMap<>();
                        m.put("field", FormatUtil.toString(v).replace("*","").trim());
                        m.put("title", FormatUtil.toString(v).replace("*","").trim());
                        m.put("col",k);
                        columns.add(m);
                    }
                });
            }

            sheetDataForm.setIndex(index);
            sheetDataForm.setColumns(columns);
            sheetDataForm.setDatas(valueList);
            sheetDataForms.add(sheetDataForm);
        }
        ExcelDataForm dataForm = new ExcelDataForm();
        dataForm.setSheets(sheetDataForms);
        dataForm.setParams(params);
        // 调用后台数据
        Map<String,Object> error = this.callImportAction(dataForm,action);
        return error;
    }
    /**
     * 调用发起工作流后事件
     *
     * @param from      表单
     * @param url       url
     * @return 工作流运行VO
     * HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
     * String ip = ClientUtil.getClientIp(request);
     * int port = request.getServerPort();
     * ApiResult apiResult = restTemplate.postForObject(url, entity, ApiResult.class);
     * WfRuningProcessVo procVo = this.dozerMapper.map(apiResult.getData(), WfRuningProcessVo.class);
     */
    private Map<String,Object> callImportAction(ExcelDataForm from, String url) {

        // ExcelError excelError = WebUtil.post(url,from,ExcelError.class);
        ApiResult apiResult = WebUtil.post(url,from);
        if (!apiResult.isSuccess()) {
            throw new BaseException(apiResult.getMessage());
        }
        if(!Tools.isEmpty(apiResult.getData())){
            return (Map<String,Object>)apiResult.getData();
        }
        return null;
        /*
        HttpHeaders headers = this.getHttpHeaders();
        // HttpHeaders headers = WebUtil.getHttpHeaders();
        HttpEntity<ExcelDataForm> entity = new HttpEntity<>(from, headers);
        RestTemplate restTemplate = new RestTemplate();
        ApiResult<ExcelError> apiResult = restTemplate.exchange(url, HttpMethod.POST, entity,
                new ParameterizedTypeReference<ApiResult<ExcelError>>() {
                }).getBody();
        if (!apiResult.isSuccess()) {
            throw new BaseException(apiResult.getMessage());
        } else {
            return apiResult.getData();
        }
        */
    }


    /**
     * 得到HttpHeaders
     *
     * @return HttpHeaders
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        if (!ObjectUtils.isEmpty(headerNames)) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = servletRequest.getHeader(name);
                headers.add(name, value);
            }
        }
        return headers;
    }
}
