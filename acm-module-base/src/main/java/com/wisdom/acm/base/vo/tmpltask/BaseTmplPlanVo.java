package com.wisdom.acm.base.vo.tmpltask;

import com.wisdom.base.common.vo.UserVo;
import lombok.Data;
import java.util.Date;

@Data
public class BaseTmplPlanVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 模板名称
     */
    private String tmplName;

    /**
     * 类型tmpl、wsd、task
     */
    private String type;

    /**
     * 模板代码
     */
    //private String tmplCode;

    /**
     * 计划状态
     */
    //private String status;

    /**
     * 是否全局
     */
    private Integer isGlobal;

    /**
     * 创建人
     */
    private UserVo creator;

    /**
     * 创建日期
     */
    private Date creatTime;



}