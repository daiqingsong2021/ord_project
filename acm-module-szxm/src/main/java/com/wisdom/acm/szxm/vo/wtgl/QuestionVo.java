package com.wisdom.acm.szxm.vo.wtgl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.doc.DocFileInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionVo
{
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 标段编号code
     */
    private String sectionCode;

    /**
     * 标段名称
     */
    private String sectionName;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 业务ID
     */
    private Integer bizId;

    /**
     * 业务类型 问题来源，code是码值，name是名称（数据字典：comu.question.biztype）
     */
    private GeneralVo bizTypeVo;

    /**
     * 问题类型 （数据字典：comu.question.type）
     */
    private GeneralVo typeVo;

    /**
     * 问题优先级（数据字典：comu.question.priority）
     */
    private GeneralVo priorityVo;

    /**
     * 责任主体
     */
    private GeneralVo orgVo;

    /**
     * 责任人
     */
    private UserVo userVo;

    /**
     * 当前处理人所属组织
     */
    private GeneralVo currentUserOrgVo;

    /**
     * 当前处理人
     */
    private UserVo currentUserVo;


    /**
     * 问题上一步处理人所属组织
     */
    private GeneralVo lastUserOrgVo;

    /**
     * 问题上一步处理人
     */
    private UserVo lastUserVo;

    /**
      * 创建人所属组织
     */
    private GeneralVo createrOrgVo;

    /**
     *创建人
     */
    private UserVo createrVo;

    /**
     * 创建时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 问题结束时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;


    /**
     * 要求处理日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /**
     * 问题状态 (0新建、1待处理、2待审核、3已关闭、4已挂起)（数据字典：comu.question.status）
     */
    private GeneralVo statusVo;


    /**
     * 问题上一步状态 (0新建、1待处理、2待审核、3已关闭、4已挂起)（数据字典：comu.question.status）
     */
    private GeneralVo lastStatusVo;


    /**
     * 问题说明
     */
    private String remark;

    /**
     * 处理要求
     */
    private String handle;

    /**
     * （多个以逗号,相连）
     */
    private String station;

    /**
     * 站点/区间
     */
    private List<GeneralVo> stationVo= Lists.newArrayList();

    /**
     * 文件信息
     */
    private List<DocFileInfoVo> docs;

    /**
     * 问题日志
     */
    private List<QuestionRecordVo> logs;
}
