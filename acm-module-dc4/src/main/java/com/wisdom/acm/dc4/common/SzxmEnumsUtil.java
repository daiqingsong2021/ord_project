package com.wisdom.acm.dc4.common;

public class SzxmEnumsUtil
{
    public enum StatusEnum
    {
        INIT("INIT", "新建"), APPROVAL("APPROVAL", "审批中"),  REJECT("REJECT", "被驳回"),APPROVED("APPROVED", "已完成");
        // 成员变量
        private String code;
        private String name;
        // 构造方法
        private StatusEnum(String code, String name)
        {
            this.code = code;
            this.name = name;
        }
        @Override
        public String toString()
        {
            return this.code;
        }
        public String getCode()
        {
            return this.code;
        }
        public String getName()
        {
            return  this.name;
        }
    }

    public enum QuestionStatusEnum {

        NEW("0", "新建"),
        DCL("1", "待处理"),
        DSH("2","待审核"),
        CLOSED("3","已关闭"),
        HANDUP("4","已挂起");


        private String code;
        private String name;


        QuestionStatusEnum(String code, String name){
            this.code=code;
            this.name=name;
        }

        public static QuestionStatusEnum getEnumByCode(String code) {
            switch (code) {
                case "0":
                    return QuestionStatusEnum.NEW;
                case "1":
                    return QuestionStatusEnum.DCL;
                case "2":
                    return QuestionStatusEnum.DSH;
                case "3":
                    return QuestionStatusEnum.CLOSED;
                case "4":
                    return QuestionStatusEnum.HANDUP;
                default:
                    return QuestionStatusEnum.NEW;
            }
        }

        @Override
        public String toString()
        {
            return this.code;
        }
        public String getCode()
        {
            return this.code;
        }
        public String getName()
        {
            return  this.name;
        }
    }

    public enum QuestionRecordActionEnum {

        NEW("0", "新建"),
        RELEASE("1", "发布"),
        HANDLE("2","处理"),
        FORWARD("3","转发"),
        REJECT("4","驳回"),
        CONFIRM("5","确认"),
        HANDUP("6","挂起"),
        CANCELHANDUP("7","取消挂起"),
        CLOSED("8","关闭");


        private String code;
        private String name;


        QuestionRecordActionEnum(String code, String name){
            this.code=code;
            this.name=name;
        }

        @Override
        public String toString()
        {
            return this.code;
        }
        public String getCode()
        {
            return this.code;
        }
        public String getName()
        {
            return  this.name;
        }
    }
}
