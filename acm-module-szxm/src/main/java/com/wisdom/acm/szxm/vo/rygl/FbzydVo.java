package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FbzydVo
{
    /**
     * 主键ID
     */
    private Integer id;


    private Integer projInfoId;


    private String name;

    private String address;

    private Integer residentNum;

    private String houseCharacter;

    private String houseArea;

    private String creater;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date createTime;

    //施工作业队队长
    private String teamLeader;
    //联系方式
    private String telPhone;


}
