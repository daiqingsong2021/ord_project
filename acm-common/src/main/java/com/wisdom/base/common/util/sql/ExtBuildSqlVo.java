package com.wisdom.base.common.util.sql;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExtBuildSqlVo extends BuildSqlVo {
    // 是否左括号已经开始
    private boolean bracketStart = false;
    //
    private boolean bracket = false;
    private int bracketStartIndex = 0;
    private int bracketEndIndex = 0;
    // 参数集合
    private List<StringBuffer> paramsList = new ArrayList<>();
    // 当前执行的参数坐标
    private int paramIndex = 0;
    // 内部括号，未配对括号个数
    private int internalBracketNum = 0;

    public ExtBuildSqlVo(String content, String replaceContent){
        this.setContent(content);
        this.setReplaceContent(replaceContent);
        this.setContentChars(content.toCharArray());
    }

    /**
     * 计算内部括号是否对等，为空代表已经对等，否者没有对等
     *
     * @param c
     */
    public void initInternalBracketNum(char c){

        if(c == '('){
            this.setInternalBracketNum(this.getInternalBracketNum()+1);
        }else if( this.getInternalBracketNum() > 0 && c == ')'){
            this.setInternalBracketNum(this.getInternalBracketNum() - 1);
        }
    }

    public void buildBracket(char c,int i){

        if( this.getIndex() < this.getContentChars().length){

            if(!this.isBracketStart()){// 括号以内的内容开始
                // 标识括号以内的内容开始
                this.setBracketStart(true);
                // 括号内部开始坐标
                this.setBracketStartIndex(i);
                // 参数为第一个
                this.setParamIndex(0);
                // 内部括号，未配对括号个数为0
                this.setInternalBracketNum(0);
                // 第一个参数内容拼接
                paramsList.add(new StringBuffer().append(c));

                this.initInternalBracketNum(c);
            }else if(this.isBracketStart()){
                // 如果未配对的括号大于0，都作为参数继续拼接字符串
                if(this.getInternalBracketNum() > 0){
                    this.paramsList.get(this.getParamIndex()).append(c);
                }else{
                    // 没有内部未匹配括号
                    if(c == ','){
                        // 标识下一个参数
                        this.setParamIndex(this.getParamIndex()+1);
                        paramsList.add(new StringBuffer());
                    }else if(c == ')'){ // 主体方法结束

                        // 设置括号的结束位置
                        this.setBracketEndIndex(i);
                        // 获取括号内的所有内容【根据实现】
                        String content = this.buildBracketContent();
                        // 组合updateSql
                        UpdateSqlVo updateSql = new UpdateSqlVo(this.getBracketStartIndex(), this.getBracketEndIndex(), content);
                        this.getUpdateSqlVoList().add(updateSql);
                        // 重置计算括号的属性
                        this.setBracket(false);
                        this.paramsList.clear();
                        this.setBracketStart(false);
                        this.setParamIndex(0);
                        this.setInternalBracketNum(0);
                    }else{
                        this.paramsList.get(this.getParamIndex()).append(c);
                    }
                }

                this.initInternalBracketNum(c);
            }
        }
    }

    public void build(char c,int i){

        if(this.isBracket()){
            // 括号里面的内容，
            this.buildBracket(c,i);
        }else{
            // 括号以外的内容
            this.buildUpdateSql(c,i);
        }
    }

    @Override
    public String buildBracketContent() {
        return null;
    }

    /**
     * 方法验证完成后，自动将内部作为括号开始。
     *
     * @param i
     */
    @Override
    protected void beforeAddUpdateSql(int i) {
        this.setBracket(true);
        this.setBracketStartIndex(0);
    }
}
