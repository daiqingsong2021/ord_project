package com.wisdom.acm.szxm.common.officeUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.UUIDHexGenerator;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************
 * 通过word模板生成新的word工具类
 *
 * @Package com.cccuu.project.myUtils
 * @Author duan
 * @Date 2018/3/29 14:24
 * @Version V1.0
 *******************************************/
public class WordUtil
{

    /**
     * 根据模板生成word
     *
     * @param path     模板的路径
     * @param params   需要替换的参数
     * @param fileName 生成word文件的文件名
     * @param response
     */
    public void getWord(String path, Map<String, Object> params, String fileName, HttpServletResponse response) throws
            Exception
    {
        File file = new File(path);
        InputStream is = new FileInputStream(file);
        MyXWPFDocument doc = new MyXWPFDocument(is);
        this.replaceInPara(doc, params); // 替换文本里面的变量
        this.replaceInTable(doc, params); // 替换表格里面的变量

        String fileName_ = fileName + ".docx";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName_, "UTF-8"));
        //response.setHeader("Content-Length", String.valueOf(file.length()));
        OutputStream os = response.getOutputStream();
        doc.write(os);
        this.close(os);
        this.close(is);
    }

    /**
     * generateWord:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param sourceFile 源文件
     * @param params     要替换参数
     * @param destFile   目标文件
     * @throws Exception
     * @author wyf
     * @since JDK 1.6
     */
    public void generateWord(String sourceFile, Map<String, Object> params, String destFile) throws Exception
    {
        File file = new File(sourceFile);
        InputStream is = new FileInputStream(file);
        MyXWPFDocument doc = new MyXWPFDocument(is);
        this.replaceInPara(doc, params); // 替换文本里面的变量
        this.replaceInTable(doc, params); // 替换表格里面的变量
        FileOutputStream os = new FileOutputStream(destFile);
        doc.write(os);
        this.close(os);
        this.close(is);

    }

    public void generateWord(InputStream sourceFileInputStream, Map<String, Object> params, String destFile) throws Exception
    {
        MyXWPFDocument doc = new MyXWPFDocument(sourceFileInputStream);
        this.replaceInPara(doc, params); // 替换文本里面的变量
        this.replaceInTable(doc, params); // 替换表格里面的变量
        FileOutputStream os = new FileOutputStream(destFile);
        doc.write(os);
        this.close(os);
        this.close(sourceFileInputStream);

    }

    /**
     * 替换段落里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    private void replaceInPara(MyXWPFDocument doc, Map<String, Object> params)
    {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext())
        {
            para = iterator.next();
            this.replaceInPara(para, params, doc);
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     */
    private void replaceInPara(XWPFParagraph para, Map<String, Object> params, MyXWPFDocument doc)
    {
        List<XWPFRun> runs;
        Matcher matcher;
        if (this.matcher(para.getParagraphText()).find())
        {
            runs = para.getRuns();
            int start = -1;
            int end = -1;
            List<Map<String, Object>> startEndIndex = Lists.newArrayList();
            for (int i = 0; i < runs.size(); i++)
            {
                XWPFRun run = runs.get(i);
                String runText = run.toString();
                if ('$' == runText.charAt(0) && '{' == runText.charAt(1))
                {
                    start = i;
                    end = -1;//重置
                }
                if ('}' == runText.charAt(runText.length() - 1))
                {
                    if (start != -1)
                    {
                        end = i;
                        //记录start end
                        Map<String, Object> seMap = Maps.newHashMap();
                        seMap.put("start", start);
                        seMap.put("end", end);
                        String str = "";
                        for (int j = start + 1; j < end; j++)
                        {
                            str += runs.get(j).toString();
                        }
                        seMap.put("str", str);
                        startEndIndex.add(seMap);
                        start = -1;//重置
                    }
                }
            }
            //遍历index Map
            for (Map<String, Object> seMap : startEndIndex)
            {
                Object value = params.get(String.valueOf(seMap.get("str")));
                if (!ObjectUtils.isEmpty(value))
                {
                    int startIndex = Integer.valueOf(String.valueOf(seMap.get("start"))).intValue();
                    int endIndex = Integer.valueOf(String.valueOf(seMap.get("end"))).intValue();
                    para.removeRun(startIndex);
                    para.insertNewRun(startIndex).setText("");
                    para.removeRun(endIndex);
                    para.insertNewRun(endIndex).setText("");

                    for (int i = startIndex + 1; i < endIndex; i++)
                    {
                        //删除一个追加一个
                        para.removeRun(i);
                        if (value instanceof String)
                        {
                            XWPFRun insertRun = para.insertNewRun(i);
                            insertRun.setText(String.valueOf(value));
                            Map<String, Object> styleMap =
                                    (Map<String, Object>) params.get(String.valueOf(seMap.get("str")) + "_style");
                            if (!ObjectUtils.isEmpty(styleMap) && !ObjectUtils.isEmpty(styleMap.get("underline")))
                                insertRun.setUnderline((UnderlinePatterns) styleMap.get("underline"));
                        }
                        else if (value instanceof Map)
                        {// 图片
                            Map pic = (Map) value;
                            int width = Integer.parseInt(pic.get("width").toString());
                            int height = Integer.parseInt(pic.get("height").toString());
                            int picType = getPictureType(pic.get("type").toString());
                            byte[] byteArray = (byte[]) pic.get("content");
                            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                            try
                            {
                                doc.addPictureData(byteInputStream, picType);
                                doc.createPicture(doc.getAllPictures().size() - 1, width, height, para);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else if (value instanceof List)
                        {//
                            List valList = (List) value;
                            for (int m = 0; m < valList.size(); m++)
                            {
                                Map map = (Map) valList.get(m);
                                if (map.get("isImage") != null && "1".equals(map.get("isImage").toString()))
                                {// 如果是图片
                                    //图片描述读出
                                    String imgDesc = String.valueOf(map.get("imgDesc"));
                                    if (StringHelper.isNotNullAndEmpty(imgDesc))
                                    {
                                        XWPFRun insertRun = para.insertNewRun(i);
                                        insertRun.setText(imgDesc, 0);
                                        insertRun.addBreak();// 换行
                                    }
                                    int width = Integer.parseInt(map.get("width").toString());
                                    int height = Integer.parseInt(map.get("height").toString());
                                    int picType = getPictureType(map.get("type").toString());
                                    byte[] byteArray = (byte[]) map.get("content");
                                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                                    try
                                    {
                                        doc.addPictureData(byteInputStream, picType);
                                        doc.createPicture(doc.getAllPictures().size() - 1, width, height, para);
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {// 不是图
                                    Object contentV = map.get("content");
                                    if (contentV.getClass().isArray())
                                    {
                                        String[] contentArray = (String[]) contentV;
                                        for (String v : contentArray)
                                        {
                                            XWPFRun insertRun = para.insertNewRun(i);
                                            insertRun.setText(v);
                                            insertRun.addBreak();// 换行
                                        }
                                    }
                                    else
                                    {
                                        XWPFRun insertRun = para.insertNewRun(i);
                                        insertRun.setText(map.get("content").toString());
                                        insertRun.addBreak();// 换行
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 为表格插入数据，行数不够添加新行
     *
     * @param table     需要插入数据的表格
     * @param tableList 插入数据集合
     */
    private static void insertTable(XWPFTable table, List<String[]> tableList)
    {
        // 创建行,根据需要插入的数据添加新行，不处理表头
        for (int i = 0; i < tableList.size(); i++)
        {
            XWPFTableRow row = table.createRow();
        }
        // 遍历表格插入数据
        List<XWPFTableRow> rows = table.getRows();
        int length = rows.size();
        for (int i = 1; i < length - 1; i++)
        {
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            for (int j = 0; j < cells.size(); j++)
            {
                XWPFTableCell cell = cells.get(j);
                String s = tableList.get(i - 1)[j];
                cell.setText(s);
            }
        }
    }

    /**
     * 替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    private void replaceInTable(MyXWPFDocument doc, Map<String, Object> params)
    {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext())
        {
            table = iterator.next();
            if (table.getRows().size() > 1)
            {
                // 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (this.matcher(table.getText()).find())
                {
                    rows = table.getRows();
                    for (XWPFTableRow row : rows)
                    {
                        cells = row.getTableCells();
                        for (XWPFTableCell cell : cells)
                        {
                            paras = cell.getParagraphs();
                            for (XWPFParagraph para : paras)
                            {
                                this.replaceInPara(para, params, doc);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private Matcher matcher(String str)
    {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType)
    {
        int res = MyXWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null)
        {
            if (picType.equalsIgnoreCase("png"))
            {
                res = MyXWPFDocument.PICTURE_TYPE_PNG;
            }
            else if (picType.equalsIgnoreCase("dib"))
            {
                res = MyXWPFDocument.PICTURE_TYPE_DIB;
            }
            else if (picType.equalsIgnoreCase("emf"))
            {
                res = MyXWPFDocument.PICTURE_TYPE_EMF;
            }
            else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg"))
            {
                res = MyXWPFDocument.PICTURE_TYPE_JPEG;
            }
            else if (picType.equalsIgnoreCase("wmf"))
            {
                res = MyXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 将输入流中的数据写入字节数组
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] inputStream2ByteArray(InputStream in, boolean isClose) throws Exception
    {
        byte[] byteArray = null;
        try
        {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (isClose)
            {
                try
                {
                    in.close();
                }
                catch (Exception e2)
                {
                    e2.getStackTrace();
                    throw e2;
                }
            }
        }
        return byteArray;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private void close(InputStream is)
    {
        if (is != null)
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private void close(OutputStream os)
    {
        if (os != null)
        {
            try
            {
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void getWordToPdf(InputStream is, Map<String, Object> params, String fileName, HttpServletResponse response)
            throws Exception
    {

        MyXWPFDocument doc = new MyXWPFDocument(is);
        this.replaceInPara(doc, params); // 替换文本里面的变量
        this.replaceInTable(doc, params); // 替换表格里面的变量
        // 输出到临时目录生成一个文件
        String tempId = UUIDHexGenerator.generator();
        String tempWordUrl =
                this.getClass().getClassLoader().getResource("").getPath() + "templates/temporary/" + tempId + ".docx";
        FileOutputStream destFileOut = new FileOutputStream(tempWordUrl);
        doc.write(destFileOut);
        // 临时文件转为PDF
        String tempPdfUrl =
                this.getClass().getClassLoader().getResource("").getPath() + "templates/temporary/" + tempId + ".pdf";
       // WordToPdfUtil.office2PDF(tempWordUrl, tempPdfUrl);

        // response输出PDF
        BufferedInputStream bis = null;
        // 获取输入流
        bis = new BufferedInputStream(new FileInputStream(tempPdfUrl));
        response.setContentType("application/pdf");
        int len = 0;
        byte[] b = new byte[1024];

        while ((len = bis.read(b, 0, 1024)) != -1)
        {
            response.getOutputStream().write(b, 0, len);
        }

        this.close(bis);
        this.close(destFileOut);
        this.close(is);
        response.getOutputStream().flush();

        // 删除两份临时文件
        new File(tempWordUrl).delete();
        new File(tempPdfUrl).delete();
    }

    public static void main(String args[])
    {
        WordUtil wordUtil = new WordUtil();
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> styleMap;
        params.put("gcmc", "苏州轨道交通三号线系统工程");
        styleMap = new HashMap<String, Object>();
        styleMap.put("underline", UnderlinePatterns.SINGLE);
        params.put("gcmc_style", styleMap);
        params.put("jcsj", "2019-09-10");
        params.put("jcsj_style", styleMap);
        params.put("jcjl", "1.新区站：站台门安装缺口位置临边防护设置不严密有缺口，\n" +
                "  缺口处防护栏杆固定不牢固。\n" +
                "2.新区站：站台门安装后玻璃门保护膜破裂。\n" +
                "3.马运路站：供电三标接触用拉拔试验时移动式样车平台高宽\n" +
                "  比大于2，未双拼。\n" +
                "4.马运路站：供电三标梯车平台换道时，平台上方的拉拔设备\n" +
                "  未及时拿下车台，存在重物掉落伤人的风险。\n" +
                "5.发展路站：电扶梯施工站厅层施工人员作业时脚踏板使用木\n" +
                "  板，作业人员安全带低挂高用。");
        params.put("jcdd", "新区站 - 狮山路站");
        params.put("jcdd_style", styleMap);
        params.put("jcnr", "季度考评检查");
        params.put("jcnr_style", styleMap);
        params.put("ts", "3");
        params.put("ts_style", styleMap);
        params.put("zxjcry", "赵连连");
        params.put("zxjcry_style", styleMap);
        params.put("xmgcs", "李洋");
        params.put("xmgcs_style", styleMap);
        params.put("jldwdb", "吴云峰");
        params.put("jldwdb_style", styleMap);
        params.put("sgdwdb", "牛娟娟");
        params.put("sgdwdb_style", styleMap);
        params.put("bh", "SZXM-AQGL-ZLL-001");
        params.put("bh_style", styleMap);
        try
        {
            String sourceFile = "D:/fff/gczlaqjdjcjl.docx"; // 模板文件位置
            // String fileName = new String("测试文档.docx".getBytes("UTF-8"),
            // "iso-8859-1"); // 生成word文件的文件名
            wordUtil.generateWord(sourceFile, params, "D:/fff/安全检查测试文档.docx");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
