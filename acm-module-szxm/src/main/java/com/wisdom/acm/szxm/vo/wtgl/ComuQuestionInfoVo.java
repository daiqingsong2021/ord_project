package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;

@Data
public class ComuQuestionInfoVo {

    //id
    private Integer id;

    //问题标题
    private String title;

    //问题类型
    private DictionaryVo type;

    //责任主体
    private OrgVo org;

    //责任人
    private UserVo user;

    //优先级
    private DictionaryVo priority;

    //要求处理日期
    private Date handleTime;

    //所属项目
    private GeneralVo project;

    //状态
    private StatusVo status;

    //提出人
    private UserVo creator;

    //提出时间
    private Date creatTime;

    //问题说明
    private String remark;

    //处理要求
    private String handle;

    //来源
    private String source;
}
