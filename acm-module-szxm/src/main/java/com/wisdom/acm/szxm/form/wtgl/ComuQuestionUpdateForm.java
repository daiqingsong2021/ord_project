package com.wisdom.acm.szxm.form.wtgl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class ComuQuestionUpdateForm extends BaseForm {

    private Integer id;

    //问题标题
    @ApiModelProperty(value = "问题标题", required = true)
    @NotBlank(message = "问题标题不能为空")
    @LogParam(title = "问题标题")
    private String title;

    //问题类型
    @ApiModelProperty(value = "问题类型", required = true)
    @LogParam(title = "问题类型")
    private String type;

    //责任主体
    @ApiModelProperty(value = "责任主体", required = true)
    @LogParam(title = "责任主体")
    private Integer orgId;

    //责任人
    @LogParam(title = "责任人")
    @ApiModelProperty(value = "责任人", required = true)
    @NotNull(message = "责任人不能为空")
    private Integer userId;

    //优先级
    @ApiModelProperty(value = "优先级", required = true)
    @NotNull(message = "优先级不能为空")
    @LogParam(title = "优先级")
    private String priority;

    //要求处理日期
    @ApiModelProperty(value = "要求处理日期", required = true)
    @NotNull(message = "处理要求不能为空")
    @LogParam(title = "要求处理日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date handleTime;

    //所属关联对象id
    @ApiModelProperty(value = "所属关联对象id", required = true)
    @LogParam(title = "所属关联对象id")
    private Integer bizId;

    //所属关联对象类型
    @ApiModelProperty(value = "所属关联对象类型", required = true)
    @LogParam(title = "所属关联对象类型")
    private String bizType;

    //问题说明
    @ApiModelProperty(value = "问题说明", required = true)
    @NotBlank(message = "问题说明不能为空")
    @LogParam(title = "问题说明")
    private String remark;

    //处理要求
    @ApiModelProperty(value = "处理要求", required = true)
    @NotBlank(message = "处理要求不能为空")
    @LogParam(title = "处理要求")
    private String handle;

    /**
     * 文件Ids
     */
    private List<Integer> fileIds;


}
