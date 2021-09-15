package com.wisdom.acm.szxm.po.doc;

import com.google.common.collect.ImmutableMap;
import com.wisdom.base.common.po.BasePo;
import com.wisdom.base.common.util.FormatUtil;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_doc")
@Data
public class DocPo extends BasePo {

    /**
     *文件夹id
     */
    @Column(name = "folder_id")
    private Integer folderId;
    /**
     *文档标题
     */
    @Column(name = "doc_title")
    private String docTitle;

    /**
     *文档编号
     */
    @Column(name = "doc_num")
    private String docNum;

    /**
     *文档类型
     */
    @Column(name = "doc_type")
    private String docType;

    /**
     *描述
     */
    @Column(name = "remark")
    private String remark;

    /**
     *版本号
     */
    @Column(name = "version")
    private String version;
    /**
     *状态
     */
    @Column(name = "status")
    private String status;
    /**
     *删除标识
     */
    @Column(name = "del")
    private Integer del;

    /**
     * 删除时间
     */
    @Column(name = "del_time")
    private Date delTime;

    /**
     *项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     *作者
     */
    @Column(name = "author")
    private String author;
    /**
     *文档专业
     */
    @Column(name = "profession")
    private String profession;
    /**
     *文档分类
     */
    @Column(name = "doc_classify")
    private String docClassify;
    /**
     *组织
     */
    @Column(name = "org_id")
    private Integer orgId;

    //  密级
    @Column(name = "secuty_level")
    private String secutyLevel;

    public ImmutableMap.Builder<String, Object> toCodeCalParam(){
        return ImmutableMap.<String,Object>builder()//
                .put("_bo_code", ObjectUtils.isEmpty(this.projectId) ? "EP_DOC" : "PROJ_DOC")//
                .put("project_id", FormatUtil.parseInt(this.projectId))//
                ;
    }
}
