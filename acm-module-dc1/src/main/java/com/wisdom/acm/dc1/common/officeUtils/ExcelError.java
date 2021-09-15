package com.wisdom.acm.dc1.common.officeUtils;

import com.wisdom.base.common.dc.util.StringHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.ObjectUtils;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ExcelError implements Serializable
{

    private static final long serialVersionUID = 4523695532444443789L;

    private Map<String, Map<String, List<Map<String, String>>>> errorInfo =
            new HashMap<String, Map<String, List<Map<String, String>>>>();

    private static final Map<String,ExcelError> errorMap=new ConcurrentHashMap<>();
    // 行下表
    private int rowIndex = 0;
    // 是否有错误
    private boolean hasError = false;
    // sheetbook 下标
    private int sheetIndex = 0;
    // 错误展示方式
    private String errorType = "";

    // 工作薄的配置
    private List<Map<String, Object>> sheetConfig = new ArrayList<Map<String, Object>>();
    // excel workbook
    private Workbook workBook = null;

    public void addRow(int rowIndex)
    {
        this.rowIndex = rowIndex;
    }

    public void addSheet(int sheet)
    {
        this.sheetIndex = sheet;
    }

    public void addError(Integer cellIndex, String cellName, String error)
    {
        addError(String.valueOf(cellIndex), cellName, error);
    }

    public void addError(String cellIndex, String cellName, String error)
    {
        this.setHasError(true);

        Map<String, List<Map<String, String>>> sheetErrorInfoMap = errorInfo.get(this.sheetIndex + "");

        if (ObjectUtils.isEmpty(sheetErrorInfoMap))
        {
            sheetErrorInfoMap = new HashMap<String, List<Map<String, String>>>();
            errorInfo.put(this.sheetIndex + "", sheetErrorInfoMap);
        }

        List<Map<String, String>> errorList = sheetErrorInfoMap.get(this.rowIndex + "");

        if (ObjectUtils.isEmpty(errorList))
        {
            errorList = new ArrayList<Map<String, String>>();
            sheetErrorInfoMap.put(this.rowIndex + "", errorList);
        }

        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("cellIndex", String.valueOf(cellIndex));
        errorMap.put("cellName", cellName);
        errorMap.put("error", error);

        errorInfo.get(this.sheetIndex + "").get(this.rowIndex + "").add(errorMap);
    }

    public void addError(String cellName, String error)
    {
        this.addError("", cellName, error);
    }

    public List<Map<String, Object>> getErrorList()
    {
        List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
        Iterator<Entry<String, Map<String, List<Map<String, String>>>>> sheetIter =
                this.errorInfo.entrySet().iterator();

        while (sheetIter.hasNext())
        {
            Entry<String, Map<String, List<Map<String, String>>>> sheetmap = sheetIter.next();
            String sheetnum = sheetmap.getKey();
            Map<String, List<Map<String, String>>> rowMap = sheetmap.getValue();
            Iterator<Entry<String, List<Map<String, String>>>> rowIter = rowMap.entrySet().iterator();

            while (rowIter.hasNext())
            {
                Entry<String, List<Map<String, String>>> cellEntry = rowIter.next();

                String row = cellEntry.getKey();
                List<Map<String, String>> cellErrorList = cellEntry.getValue();

                String error = "";

                for (Map<String, String> map : cellErrorList)
                {
                    error += StringHelper.isNullAndEmpty(error)? map.get("cellName") + "列" + map.get("error") :
                            "," + map.get("cellName") + "列" + map.get("error");
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sheetnum", sheetnum);
                map.put("row", row);
                map.put("error", error);

                //error="第"+sheetnum+"工作簿第"+row+"行错误："+error;
                errorList.add(map);
            }
        }
        // 排序
        this.sort(errorList);

        return errorList;
    }

    /**
     * 排序
     *
     * @param list
     * @return
     */
    private List<Map<String, Object>> sort(List<Map<String, Object>> list)
    {
        for (int i = 0; i < list.size() - 1; i++)
        {
            for (int j = 0; j < list.size() - i - 1; j++)
            {
                int sheetnum1 = Integer.valueOf(String.valueOf(list.get(j).get("sheetnum")));
                int row1 = Integer.valueOf(String.valueOf(list.get(j).get("row")));
                int sheetnum2 = Integer.valueOf(String.valueOf(list.get(j + 1).get("sheetnum")));
                int row2 = Integer.valueOf(String.valueOf(list.get(j + 1).get("row")));

                Map<String, Object> map;
                if (sheetnum1 > sheetnum2 || (sheetnum1 == sheetnum2 && row1 > row2))
                {
                    map = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, map);
                }
            }
        }

        return list;
    }

    public boolean isHasError()
    {
        return hasError;
    }

    public void setHasError(boolean hasError)
    {
        this.hasError = hasError;
    }

    public String getErrorType()
    {
        return errorType;
    }

    public void setErrorType(String errorType)
    {
        this.errorType = errorType;
    }

    public Workbook getWorkBook()
    {
        return workBook;
    }

    public void setWorkBook(Workbook workBook)
    {
        this.workBook = workBook;
    }

    public List<Map<String, Object>> getSheetConfig()
    {
        return sheetConfig;
    }

    public void setSheetConfig(List<Map<String, Object>> sheetConfig)
    {
        this.sheetConfig = sheetConfig;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public static ExcelError getExcelError(String keyUid)
    {
        ExcelError excelError=errorMap.get(keyUid);
        return excelError;
    }

    public static void addExcelError(String keyUid,ExcelError excelError)
    {
       errorMap.put(keyUid,excelError);
    }


    public static void delExcelError(String errorId)
    {
        errorMap.remove(errorId);
    }

}
