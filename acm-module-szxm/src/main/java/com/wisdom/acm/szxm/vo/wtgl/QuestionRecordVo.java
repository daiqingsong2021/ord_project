package com.wisdom.acm.szxm.vo.wtgl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.doc.DocFileInfoVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionRecordVo
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
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 处理记录
     */
    private String remark;


    /**
     * 下一节点处理人组织信息
     */
    private GeneralVo orgVo;

    /**
     * 下一节点处理人
     */
    private UserVo userVo;

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
     * 问题状态 (0新建、1待处理、2待审核、3已关闭、4已挂起)（数据字典：comu.question.status）
     */
    private GeneralVo statusVo;


    /**
     * 动作(0新建、1发布、2处理、3转发、4驳回、5确认、6挂起、7关闭)
     */
    private GeneralVo actionVo;

    /**
     * 文件数量
     */
    private Integer fileCount;

    /**
     * 文件信息
     */
    private List<DocFileInfoVo> docs;
}
