package com.wisdom.base.common.util.sql;

public class SingleBuildSqlVo extends BuildSqlVo {

    public SingleBuildSqlVo(){

    }

    public SingleBuildSqlVo(String content,String replaceContent){
        this.setContent(content);
        this.setReplaceContent(replaceContent);
        this.setContentChars(content.toCharArray());
    }

    public void build(char c,int i){
        this.buildUpdateSql(c,i);
    }

    @Override
    public String buildBracketContent() {
        return null;
    }

    @Override
    protected void beforeAddUpdateSql(int i) {

    }
}
