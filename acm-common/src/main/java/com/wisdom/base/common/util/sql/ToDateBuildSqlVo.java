package com.wisdom.base.common.util.sql;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
public class ToDateBuildSqlVo extends ExtBuildSqlVo {

    public ToDateBuildSqlVo(){
        super("str_to_date(","to_date( ");
    }

    @Override
    public String buildBracketContent() {

        List<StringBuffer> paramsList = this.getParamsList();
        StringBuffer str = new StringBuffer();

        if(!ObjectUtils.isEmpty(paramsList) && paramsList.size() > 1){

            str.append(paramsList.get(0));
            str.append(" , ");
            String format = paramsList.get(1).toString();
            if("'%Y-%m-%d'".equals(format)){
                str.append("'yyyy-MM-dd'");
            }else if("'%Y-%m-%d %H:%i:%s'".equals(format)){
                str.append("'yyyy-MM-dd HH24:mi:ss'");
            }
            str.append(") ");
        }

        return str.toString();
    }
}
