package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class ComuMyQueVo {
    //id
    private Integer id;

    private Integer projectId;
    //问题标题
    private String title;

    //问题类型
    private DictionaryVo type;

    //状态
    private String statusName;

    //优先级
    private DictionaryVo priority;

    //责任人
    private UserVo user;

    //责任主体
    private OrgVo org;

    //所属项目
    private String project;

    //创建人
    private UserVo creator;

    //创建时间
    private Date creatTime;
}
