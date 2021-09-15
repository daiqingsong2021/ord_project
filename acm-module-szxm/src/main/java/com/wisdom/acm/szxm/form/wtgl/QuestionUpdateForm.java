package com.wisdom.acm.szxm.form.wtgl;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class QuestionUpdateForm
{
    /**
     * 项目ID
     */
    @NotNull(message = "问题ID不能为空")
    private Integer id;

    /**
     * 问题来源
     */
    private String bizType;

    /**
     * 标段ID
     */
    private Integer sectionId;

    //问题标题
    @NotBlank(message = "问题标题不能为空")
    private String title;

    //问题类型
    @NotBlank(message = "问题类型不能为空")
    private String type;

    //责任主体
    @NotNull(message = "责任主体ID不能为空")
    private Integer orgId;

    //责任人
    @NotNull(message = "责任人ID不能为空")
    private Integer userId;

    //优先级
    private String priority;

    //要求处理日期
    @NotNull(message = "要求处理日期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date handleTime;

    //问题说明
    @NotBlank(message = "问题说明不能为空")
    private String remark;

    //处理要求
    @NotBlank(message = "处理要求不能为空")
    private String handle;

    // 站点/区间 逗号相隔 传code
    private String station;
    /**
     * 文件Ids
     */
    private List<Integer> fileIds;

}
