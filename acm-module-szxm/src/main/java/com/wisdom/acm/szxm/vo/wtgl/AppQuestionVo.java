package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.doc.DocFileInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 移动端问题VO 误删
 */
@Data
public class AppQuestionVo
{

    //id
    @ApiModelProperty(value="问题id")
    private Integer id;

    //问题标题
    @ApiModelProperty(value="问题标题")
    private String title;

    //问题类型
    @ApiModelProperty(value="问题类型")
    private DictionaryVo type;

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
    private OrgVo fqOrg;

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

    //状态 0：新建；1：待处理 2待提交；3：待检查；4：已解决
    @ApiModelProperty(value="状态")
    private StatusVo status;

    //问题原始状态
    private String orgStatus;

    //解决日期
    @ApiModelProperty(value="解决日期")
    private Date endTime;

    //所属项目
    @ApiModelProperty(value="所属项目")
    private String project;

    //项目ID
    private Integer projectId;

    //文件信息
    @ApiModelProperty(value="文件信息")
    private List<DocFileInfoVo> docs;

    //标段信息
    private GeneralVo section;

    //业务信息 ID业务ID code:SECURITY-SECURITYCHECK  name:内部安全检查
    private GeneralVo bizInfo;

    //确认人 确认或驳回时用，只有status为2 待检查时才会有值
    private Integer handleId;

    private List<AppQueLog> logs;

}
