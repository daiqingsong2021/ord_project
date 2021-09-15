package com.wisdom.acm.dc2.vo.message;

import lombok.Data;


@Data
public class GroupManageVo
{

    /**
     *主键
     */
    private Integer id;
    
    /**
     *群组编号
     */
    private Integer groupCode;

    /**
     *群组名称
     */
    private String groupName;
    
    /**
     * 群组人数
     */
    private Integer groupNumber;

    /**
     * 群组状态
     */
    private String status;
    
    /**
     * 群组状态
     */
    private String StatusDesc;

}
