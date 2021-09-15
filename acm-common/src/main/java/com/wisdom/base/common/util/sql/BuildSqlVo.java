package com.wisdom.base.common.util.sql;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BuildSqlVo {

    // 替换内容
    private String content;
    // 替换内容
    private String replaceContent;
    // 被替换的char数组
    private char[] contentChars;
    // 坐标
    private int index = 0;
    // 替换方法长度
    private int len = 0;

    // 存放SQL需要修改的集合
    private List<UpdateSqlVo> updateSqlVoList = new ArrayList<>();
    // 单引号是否需要配对
    private boolean apostrophe = false;

    /**
     * 构建
     * @param c
     * @param i
     */
    public abstract void build(char c,int i);

    public abstract String buildBracketContent();

    protected abstract void beforeAddUpdateSql(int i);

    public void buildUpdateSql(char c,int i){

        if( this.getIndex() < this.getContentChars().length){

            // 记录单引号是否配对
            if(c == '\''){
                if(!this.isApostrophe()){
                    // 标识存在单引号未配对
                    this.setApostrophe(true);
                }else{
                    this.setApostrophe(false);
                }
            }
            else if(!this.isApostrophe()){

                if (c >= 'A' && c <= 'Z') {
                    c += 32;
                }
                char b = this.getContentChars()[this.getIndex()];
                if(c == b){
                    if(this.getIndex() == this.getContentChars().length-1){
                        // 开始执行括号内内容
                        this.beforeAddUpdateSql(i);
                        // 满足条件
                        UpdateSqlVo updateSql = new UpdateSqlVo(i - this.getLen(), i, this.getReplaceContent());
                        this.getUpdateSqlVoList().add(updateSql);
                        this.setIndex(0);
                        this.setLen(0);
                        return ;
                    }else{
                        this.setIndex( this.getIndex() + 1);
                        // this.setLen(this.getLen() + 1);
                    }
                }else if(((int)c) != 32){
                    this.setIndex(0);
                    this.setLen(0);
                    return;
                }
            }

            if(this.getIndex() > 0){
                // 去除空格的同时，但是需要记录长度。以便计算初始位置
                this.setLen(this.getLen() + 1);
            }
        }else {
            this.setIndex(0);
            this.setLen(0);
        }
    }


}
