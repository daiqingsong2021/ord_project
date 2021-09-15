package com.wisdom.acm.hrb.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

/**
 * @author zll
 * 2020/10/20/020 11:10
 * Description:<线路管理信息>
 */
@Data
public class LineFoundationAddForm extends BaseForm {
    /**
     *线路名称
     */
    private String line;
    /**
     *线路编号
     */
    private String lineCode;
    /**
     *投入运行时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date operationTime;
    /**
     * 安全运行起始日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date saftOperationTime;
    /**
     *  运营公司
     */
    private String company;
}
