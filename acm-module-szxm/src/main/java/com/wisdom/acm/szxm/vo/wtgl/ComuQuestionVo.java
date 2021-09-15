package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ComuQuestionVo {

    //id
    @ApiModelProperty(value="问题id")
    private Integer id;

    //问题标题
    @ApiModelProperty(value="问题标题")
    private String title;

    //问题类型
    @ApiModelProperty(value="问题类型")
    private DictionaryVo type;

    //问题来源
    private DictionaryVo bizType;

    //优先级
    @ApiModelProperty(value="优先级")
    private DictionaryVo priority;

    //责任人
    @ApiModelProperty(value="责任人")
    private UserVo user;

    //责任主体
    @ApiModelProperty(value="责任主体")
    private OrgVo org;

    //发起部门
    @ApiModelProperty(value="发起部门")
    private String fqOrgName;

    //问题说明
    @ApiModelProperty(value="问题说明")
    private String remark;

    //处理要求
    @ApiModelProperty(value="处理要求")
    private String handle;

    //要求处理日期
    @ApiModelProperty(value="要求处理日期")
    private Date handleTime;

    //提出人
    @ApiModelProperty(value="提出人")
    private UserVo creator;

    //提出日期
    @ApiModelProperty(value="提出日期")
    private Date creatTime;

    //状态
    @ApiModelProperty(value="状态")
    private StatusVo status;

    //解决日期
    @ApiModelProperty(value="解决日期")
    private Date endTime;

    //所属项目
    @ApiModelProperty(value="所属项目")
    private String project;

    @ApiModelProperty(value="文件数量")
    private Integer fileCount;


}
