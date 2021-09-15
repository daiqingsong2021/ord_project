package com.wisdom.acm.szxm.form.wtgl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 问题转发Form
 */
@Data
public class QuestionForwardForm
{
    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    /**
     * 标段ID
     */
    @NotNull(message = "标段ID不能为空")
    private Integer sectionId;

    @NotNull(message = "问题ID不能为空")
    private Integer questionId;

    //组织机构
    @NotNull(message = "组织机构ID不能为空")
    private Integer orgId;

    // 下一步处理人
    @NotNull(message = " 下一步处理人Id不能为空")
    private Integer userId;

    //说明
    @NotBlank(message = "说明不能为空")
    private String remark;

}
