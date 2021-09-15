package com.wisdom.acm.wf.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class WfDelegateUpdateForm {

    private Integer id;

    //委托人
    @NotNull(message = "委托人不能为空")
    private Integer assignee;

    //代理人
    @NotNull(message = "代理人不能为空")
    private Integer attorney;

    //开始时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startTime;

    //结束时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endTime;

    //是否全局
    private Integer global;

    //是否激活
    private Integer enable;

    //备注
    private String remarks;
}
