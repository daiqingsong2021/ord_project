package com.wisdom.base.common.util;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板导出：简化导出代码
 * create-by:chengdongsheng
 */
public class ExportTemplateUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExportTemplateUtil.class);

    /**
     * 导出
     *
     * @param request
     * @param response
     * @param templateFileName 模板名称
     * @param destFileName     导出文件名
     * @param data             导出数据
     */
    public static void export(HttpServletRequest request,
                              HttpServletResponse response, String templateFileName,
                              String destFileName, Object data) {
        Map<String, Object> beans = new HashMap<String, Object>();
        if (null != data) {
            beans.put("data", data);
        }
        XLSTransformer transformer = new XLSTransformer();
        InputStream in = null;
        OutputStream out = null;
        // 设置响应
        try {
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            //由于火狐和其他得不一样
            if (agent.contains("firefox")) {
                response.reset();
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(destFileName.getBytes(), "ISO8859-1"));
            }else{
                destFileName = URLEncoder.encode(destFileName, "UTF-8");
                response.reset();
                // ContentType 可以不设置
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + destFileName);
            }
            in = ExportTemplateUtil.class.getClassLoader().getResourceAsStream("template/" + templateFileName);
            Workbook workbook = transformer.transformXLS(in, beans);
            out = response.getOutputStream();
            // 将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
        } catch (InvalidFormatException e) {
            logger.error("export error,", e);
        } catch (IOException e) {
            logger.error("export error,", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("export error,", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("export error,", e);
                }
            }
        }
    }

    /**
     * @param request
     * @param response
     * @param templateFileName 模板名称
     * @param destFileName     导出文件名
     * @param resultList       导出数据
     * @param newSheetNames    导出的sheet名称
     * @param key              导出数据里面的key
     */
    public static void exportMultipleSheets(HttpServletRequest request,
                                            HttpServletResponse response, String templateFileName,
                                            String destFileName, List resultList, List<String> newSheetNames, String key) {
        XLSTransformer transformer = new XLSTransformer();
        InputStream in = null;
        OutputStream out = null;
        // 设置响应
        try {
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            //由于火狐和其他得不一样
            if (agent.contains("firefox")) {
                response.reset();
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(destFileName.getBytes(), "ISO8859-1") );
            }else{
                destFileName = URLEncoder.encode(destFileName, "UTF-8");
                response.reset();
                // ContentType 可以不设置
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + destFileName);
            }
            in = ExportTemplateUtil.class.getClassLoader().getResourceAsStream("template/" + templateFileName);
            Workbook workbook = transformer.transformMultipleSheetsList(in, resultList, newSheetNames, key, new HashMap(), 0);
            out = response.getOutputStream();
            // 将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
        } catch (InvalidFormatException e) {
            logger.error("export error,", e);
        } catch (IOException e) {
            logger.error("export error,", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("export error,", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("export error,", e);
                }
            }
        }
    }

}
