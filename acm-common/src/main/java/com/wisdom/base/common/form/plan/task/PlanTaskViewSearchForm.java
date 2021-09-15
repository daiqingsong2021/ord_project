package com.wisdom.base.common.form.plan.task;

import com.wisdom.base.common.util.DateUtil;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Data
public class PlanTaskViewSearchForm {


    //名称/代码
    private String nameOrCode;

    private Integer orgId;

    private Integer userId;

    private String startDateLimitBefore;

    private String startDateLimitAfter;

    private String endDateLimitBefore;

    private String endDateLimitAfter;

    //计划完成类型
    private String planEndType;

    private Boolean nullDelv;

    private Boolean nullWbs;

    private Boolean nullUser;

    private Boolean nullRsrc;

    //反馈状态
    private List<Integer> feedbackStatus;

    //计划状态
    private List<String> status;

    //子节点
    private Boolean children;

    //进查询任务列表
    private Boolean onlyTask;

    //模糊查询
    private Boolean fuzzySearch;

    //计划完成类型获得的开始时间
    private String endDateLimitBefore2;
    //计划完成类型获得的完成时间
    private String endDateLimitAfter2;

    /**
     * 是否需要反馈1是需要，0或空是不需要
     */
    private Integer isFeedback;

    /**
     * 计划类型
     */
    private String planType;

    /**
     * 扩展字段
     */
    private String custom01;

    /**
     * 扩展字段
     */
    private String custom02;

    /**
     * 扩展字段
     */
    private String custom03;

    /**
     * 扩展字段
     */
    private String custom04;

    /**
     * 扩展字段
     */
    private String custom05;

    /**
     * 扩展字段
     */
    private String custom06;

    /**
     * 扩展字段
     */
    private String custom07;

    /**
     * 扩展字段
     */
    private String custom08;

    /**
     * 扩展字段
     */
    private String custom09;

    /**
     * 扩展字段
     */
    private String custom10;

    public String getStartDateLimitBefore(){
        if (!ObjectUtils.isEmpty(this.startDateLimitBefore)){
            return this.startDateLimitBefore +" 00:00:00";
        }
        return null;
    }

    public String getStartDateLimitAfter(){
        if (!ObjectUtils.isEmpty(this.startDateLimitAfter)){
            return this.startDateLimitAfter +" 23:59:59";
        }
        return null;
    }

    public String getEndDateLimitBefore(){
        if (!ObjectUtils.isEmpty(this.endDateLimitBefore)){
            return this.endDateLimitBefore +" 00:00:00";
        }
        return null;
    }

    public String getEndDateLimitAfter(){
        if (!ObjectUtils.isEmpty(this.endDateLimitAfter)){
            return this.endDateLimitAfter +" 23:59:59";
        }
        return null;
    }
    public String getEndDateLimitBefore2(){
        if("year".equals(this.planEndType)){
            return DateUtil.getYearStart();
        }else if("quarter".equals(this.planEndType)){
            return DateUtil.getQuarterStart();
        }else if("month".equals(this.planEndType)){
            return DateUtil.getMonthStart();
        }else if("week".equals(this.planEndType)){
            return DateUtil.getWeekStart();
        }
        return null;
    }

    public String getEndDateLimitAfter2(){
        if("year".equals(this.planEndType)){
            return DateUtil.getYearEnd();
        }else if("quarter".equals(this.planEndType)){
            return DateUtil.getQuarterEnd();
        }else if("month".equals(this.planEndType)){
            return DateUtil.getMonthEnd();
        }else if("week".equals(this.planEndType)){
            return DateUtil.getWeekEnd();
        }
        return null;
    }


    public  boolean getNullDelv(){
        if(this.nullDelv == null || !this.nullDelv){
            return false;
        }
        return true;
    }


    public  boolean getNullWbs(){
        if(this.nullWbs == null || !this.nullWbs){
            return false;
        }
        return true;
    }



    public  boolean getNullUser(){
        if(this.nullUser == null || !this.nullUser){
            return false;
        }
        return true;
    }

    public  boolean getNullRsrc(){
        if(this.nullRsrc == null || !this.nullRsrc){
            return false;
        }
        return true;
    }

    public  boolean getChildren(){
        if(this.children == null || this.children){
            return true;
        }
        return false;
    }

    public  boolean getFuzzySearch(){
        if(this.fuzzySearch == null || this.fuzzySearch){
            return true;
        }
        return false;
    }

    public boolean getOnlyTask(){
        if(this.onlyTask == null || !this.onlyTask){
            return false;
        }
        return true;
    }
}
