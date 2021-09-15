package com.wisdom.base.common.vo.doc;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class DocProjectVo {

    private Integer id;

    /**
     *文件夹id
     */
    private Integer folderId;
    /**
     *文档标题
     */
    private String docTitle;

    /**
     * 标段
     */
    private ProjectTeamVo section;

    /**
     *文档编号
     */
    private String docNum;

    /**
     * 文档分类
     */
    private DictionaryVo docClassify;
    /**
     *状态
     */
    private StatusVo status;

    /**
     *版本号
     */
    private String version;

    /**
     *作者
     */
    private String author;

    /**
     * 上传人
     */
    private UserVo creator;

    /**
     * 上传时间
     */
    private Date creatTime;

    private DictionaryVo secutyLevel;

    private Integer fileId;

    /**
     * 文档是否被收藏（0/1）
     *
     */
    private Integer isFavorite;

    private Integer auth;
    //文件夹信息
    private DocProjectFolderTreeVo docProjectFolderTreeVo;
}
