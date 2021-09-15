package com.wisdom.base.common.form.doc;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class DocProjectAddForm extends BaseForm {

    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 文档标题
     */
    @LogParam(title = "文档标题")
    private String docTitle;
    /**
     * 文档编号
     */
    @LogParam(title = "文档编号")
    private String docNum;
    /**
     * 文档类型
     */
    private String docClassify;
    /**
     * 作者
     */
    private String author;
    /**
     * 所属部门
     */
    private Integer orgId;
    /**
     * 目标id
     */
    private Integer bizId;

    /**
     * 目标类型（wbs/任务）
     */
    private String bizType;

    /**
     * 文件夹id
     */
    private Integer folderId;

    // 专业
    private String profession;

    /**
     * 文件id
     */
    private Integer fileId;

    /**
     * 密级
     */
    private String secutyLevel;

    /**
     * 版本
     */
    private String version;

    /**
     * 标段id
     */
    private Integer sectionId;
}
