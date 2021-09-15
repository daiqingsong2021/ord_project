package com.wisdom.acm.hrb.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * @author zll
 * 2020/10/20/020 13:46
 * Description:<描述>
 */
@Data
public class LineFoundationVo {
    private Integer id;
    /**
     *线路名称
     */
    private String lineName;
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
