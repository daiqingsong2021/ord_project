package com.wisdom.acm.szxm.common.officeUtils;

import com.aspose.cells.HtmlSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class DocTransformUtil
{

    private static final Logger logger = LoggerFactory.getLogger(DocTransformUtil.class);

    private InputStream wordLicense = null;

    private InputStream excelLicense = null;

    @Value("${doc.font.enable}")
    private String enableFont;

    /**
     * 设置字体
     *
     * @param doc
     */
    public void setFont(Document doc)
    {
        if ("1".equals(this.enableFont))
        {
            FontSettings font = FontSettings.getDefaultInstance();
            font.setFontsFolder(LoadingContext.getFontsFolder(), true);
            doc.setFontSettings(font);
        }
    }

    /**
     * word转化为PDF
     */
    public void docToPDF(InputStream inputStream, String pdfFilePath) throws Exception
    {
        wordLicense = LoadingContext.findResoureFile("aspose/license.xml");
        com.aspose.words.License license = new com.aspose.words.License();
        license.setLicense(wordLicense);

        Document doc = new Document(inputStream);
        // 设置字体
        this.setFont(doc);

        FileOutputStream fileOS = new FileOutputStream(new File(pdfFilePath));
        doc.save(fileOS, com.aspose.words.SaveFormat.PDF);
        if (fileOS != null)
            fileOS.close();
        if (inputStream != null)
            inputStream.close();
    }

    /**
     * excel转化为Html
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public void xlsToHtml(InputStream inputStream, String htmlFilePath) throws Exception
    {
        excelLicense = LoadingContext.findResoureFile("aspose/license.xml");
        com.aspose.cells.License aposeLic = new com.aspose.cells.License();
        aposeLic.setLicense(excelLicense);
        Workbook wb = new Workbook(inputStream);
        wb.save(htmlFilePath, com.aspose.words.SaveFormat.PDF);
        if (inputStream != null)
            inputStream.close();
    }

    /**
     * Excel 转PDF
     * @param inputStream
     * @param pdfFilePath
     * @throws Exception
     */
    public void xlsToPDF(InputStream inputStream, String pdfFilePath) throws Exception
    {
        excelLicense = LoadingContext.findResoureFile("aspose/license.xml");
        com.aspose.cells.License aposeLic = new com.aspose.cells.License();
        aposeLic.setLicense(excelLicense);
        Workbook wb = new Workbook(inputStream);
        HtmlSaveOptions save = new HtmlSaveOptions(SaveFormat.PDF);
        wb.save(pdfFilePath, save);
        if (inputStream != null)
            inputStream.close();
    }

    public static void main(String args[])
    {
        String sourceFile = "D:/物料管理功能说明.docx"; // 模板文件位置
        try
        {
            FileInputStream fileInputStream=new FileInputStream(sourceFile);
            DocTransformUtil docTransformUtil=new DocTransformUtil();
            docTransformUtil.docToPDF(fileInputStream,"D:/物料管理功能说明.pdf");

            FileInputStream fileInputStream2=new FileInputStream("D:/1.xlsx");
            docTransformUtil.xlsToHtml(fileInputStream2,"D:/1.html");

            FileInputStream fileInputStream3=new FileInputStream("D:/1.xlsx");
            docTransformUtil.xlsToPDF(fileInputStream3,"D:/1.pdf");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
