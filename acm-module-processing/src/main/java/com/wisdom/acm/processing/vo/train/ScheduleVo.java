package com.wisdom.acm.processing.vo.train;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * @author zll
 * 2020/8/25/025 12:37
 * Description:<描述>
 */
@Data
public class ScheduleVo {
    private String drskbbh;
    private String sfz;
    private String zdz;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private Date sfzsj;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private Date zdzsj;

    private String drskbsxlcs;
}
