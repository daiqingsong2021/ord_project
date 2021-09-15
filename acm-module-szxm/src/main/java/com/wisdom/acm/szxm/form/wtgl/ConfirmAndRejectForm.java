package com.wisdom.acm.szxm.form.wtgl;

import com.wisdom.base.common.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/6/25 14:23
 * @Version 1.0
 */
@Data
public class ConfirmAndRejectForm extends BaseForm {

    //问题处理记录id
    @ApiModelProperty(value = "问题处理记录id", required = true)
    Integer handledId;

    //处理意见
    @ApiModelProperty(value = "处理意见", required = true)
    String desc;

    @ApiModelProperty(value = "下一个节点人", required = true)
    Integer nextUserId;

    //附件
    @ApiModelProperty(value = "附件", required = true)
    private List<Integer> fileIds;
}
