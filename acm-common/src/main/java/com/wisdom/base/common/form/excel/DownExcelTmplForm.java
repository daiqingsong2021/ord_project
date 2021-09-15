package com.wisdom.base.common.form.excel;

import com.wisdom.base.common.vo.excel.ExcelColumnVo;
import lombok.Data;

import java.util.List;

/**
 * 自定义下载模板表单
 *
 * @author Lcs
 * @date 2019-02-13 18:45:35
 */
@Data
public class DownExcelTmplForm {

    //代码
    private List<ExcelColumnVo> columns;

    private Integer sheet;

    private Integer row;
}
