package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class QuestionRelVo {

    //id
    private Integer id;

    //问题标题
    private String title;

    //问题类型
    private DictionaryVo type;

    //优先级
    private DictionaryVo priority;

    //责任人
    private UserVo user;

    //责任主体
    private OrgVo org;


    //所属项目
    private String project;



    private String remark;

    /**
     * 处理要求
     */
    private String handle;

    /**
     * 要求处理日期
     */
    private Date handleTime;

    private Date creatTime;

    private UserVo creator;

    //来源
    private String source;
}
