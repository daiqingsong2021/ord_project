package com.wisdom.base.common.util.sql;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
public class IfBuildSqlVo extends ExtBuildSqlVo {

    public IfBuildSqlVo(){
        super("if(","case when ");
    }

    @Override
    public String buildBracketContent() {

        List<StringBuffer> paramsList = this.getParamsList();
        StringBuffer str = new StringBuffer();

        if(!ObjectUtils.isEmpty(paramsList) && paramsList.size() > 2){
            str.append(paramsList.get(0));
            str.append(" then ");
            str.append(paramsList.get(1));
            str.append(" else ");
            str.append(paramsList.get(2));
            str.append(" end ");
        }

        return str.toString();
    }
}
