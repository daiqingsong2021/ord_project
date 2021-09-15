package com.wisdom.base.common.util.sql;

import lombok.Data;

@Data
public class UpdateSqlVo {

    // 开始坐标
    private int start;
    // 结束坐标
    private int end;
    // 替换内容
    private String content;

    public UpdateSqlVo(){

    }

    public UpdateSqlVo(int start,int end ,String content){
        this.start = start;
        this.end = end;
        this.content = content;
    }

}
