package com.wisdom.base.common.vo.doc;


import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class DocProjectReleaseVo {
    /**
     * id
     */
    private Integer id;
    /**
     * 文档标题
     */
    private String docTitle;
    /**
     * 文档编号
     */
    private String docNum;

    private String type;

    /**
     * 文档分类
     */
    private DictionaryVo docClassify;

    /**
     *作者
     */
    private String author;

    /**
     * 上传人
     */
    private UserVo creator;

    private String version;

    /**
     * 上传时间
     */
    private Date creatTime;

    /**
     * 标段
     */
    private ProjectTeamVo section;
}
