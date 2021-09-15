package com.wisdom.acm.szxm.form.wtgl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 问题发布Form
 */
@Data
public class QuestionPublishForm
{
    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 标段ID
     */
    private Integer sectionId;

    @NotNull(message = "问题ID不能为空")
    private Integer questionId;

    //组织机构
    @NotNull(message = "处理机构ID不能为空")
    private Integer orgId;

    //审核人
    @NotNull(message = "处理人Id不能为空")
    private Integer userId;

    //处理说明
    @NotBlank(message = "处理说明不能为空")
    private String remark;
    
}
