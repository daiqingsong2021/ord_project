package com.wisdom.base.common.util.sql;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConcatBuildSqlVo extends ExtBuildSqlVo {

    public ConcatBuildSqlVo(){
        super("concat(","");
    }

    @Override
    public String buildBracketContent() {

        List<StringBuffer> paramsList = this.getParamsList();
        StringBuffer str = new StringBuffer();
        for(StringBuffer s : paramsList){
            if(str.length() > 0){
                str.append(" || ");
            }
            str.append(s);
        }
        return str.toString();
    }
}
