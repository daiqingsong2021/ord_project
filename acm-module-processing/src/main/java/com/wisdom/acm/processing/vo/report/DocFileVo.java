package com.wisdom.acm.processing.vo.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * @author zll
 * 2020/9/24/024 16:57
 * Description:<描述>
 */
@Data
public class DocFileVo {
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;
    private String fileName;
}
