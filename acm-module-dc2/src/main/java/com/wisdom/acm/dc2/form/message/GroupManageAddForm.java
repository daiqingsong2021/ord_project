package com.wisdom.acm.dc2.form.message;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class GroupManageAddForm
{

    /**
     *群组编号
     */
    private Integer groupCode;

    /**
     *群组名称
     */
    @NotBlank(message = "群组名称")
    private String groupName;
    
    /**
     * 群组人数
     */
    private Integer groupNumber;

    /**
     * 群组状态
     */
    private String status;
    
    
    private List<GroupManageDetailsAddForm> addForm;

}
