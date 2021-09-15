package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.doc.DocFileInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AppQueLog
{
    //责任人 此时不管是创建人，责任人，在日志里面有条记录
    @ApiModelProperty(value="责任人")
    private UserVo owner;

    //责任主体
    @ApiModelProperty(value="责任部门")
    private OrgVo org;

    //处理日期 yyyy-MM-dd HH:mm:ss
    private Date processDate;

    //处理说明
    private String remark;

    //问题ID
    private Integer questionId;

    private Integer id;

    private Integer bizId;

    //文件信息
    @ApiModelProperty(value="文件信息")
    private List<DocFileInfoVo> docs;

    //0：已处理；1：已驳回；2：已确认
    @ApiModelProperty(value="状态")
    private StatusVo status;

    // 只有在状态位0 的时候才有值
    private Integer handleId;
}
