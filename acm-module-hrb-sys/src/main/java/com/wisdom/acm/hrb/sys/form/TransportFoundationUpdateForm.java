package com.wisdom.acm.hrb.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/12/1/001 13:31
 * Description:<描述>
 */
@Data
public class TransportFoundationUpdateForm extends BaseForm {
    private Integer id;
    private String line;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date initTime;
    private BigDecimal monthVolume;
    private BigDecimal monthVolumeAverage;
    private BigDecimal yearVolume;
    private BigDecimal yearVolumeAverage;
    private BigDecimal openVolume;
    private BigDecimal openVolumeAverage;
    private String remark;
}
