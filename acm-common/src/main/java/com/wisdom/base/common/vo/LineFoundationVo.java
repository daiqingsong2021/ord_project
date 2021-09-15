package com.wisdom.base.common.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
@Data
public class LineFoundationVo {
    private Integer id;
    private String line;
    private String lineCode;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date operationTime;
}
