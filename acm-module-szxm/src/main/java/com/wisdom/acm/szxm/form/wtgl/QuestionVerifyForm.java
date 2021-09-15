package com.wisdom.acm.szxm.form.wtgl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 问题审核Form
 */
@Data
public class QuestionVerifyForm
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
    private Integer orgId;

    //处理人
    private Integer userId;
    /**
     * 0 否 1是
     */
    @NotBlank(message = "是否审核通过不能为空")
    private String isPass;

    //处理说明
    @NotBlank(message = "处理说明不能为空")
    private String remark;

    /**
     * 文件Ids
     */
    private List<Integer> fileIds;

}
