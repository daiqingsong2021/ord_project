package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TsPlatVo
{
    /**
     * 主键ID
     */
    private Integer id;


    private Integer projInfoId;


    private String name;

    private String address;

    private String creater;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date createTime;
}
