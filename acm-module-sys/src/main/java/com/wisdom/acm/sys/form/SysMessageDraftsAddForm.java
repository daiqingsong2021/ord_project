package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class SysMessageDraftsAddForm {

    /**
     *  标题
     */
    @NotBlank(message = "标题不能为空!")
    private String title;

    /**
     *  内容
     */
    @NotBlank(message = "内容不能为空!")
    private String content;

    /**
     *  需要回复类型
     * （INXXBEFOREHF--在...之前回复，INXXBEFOREPZ--在...之前批准，INXXBEFORETYJ--在...之前提意见，INXXBEFOREXGTJ--在...之前修改并提交，INXXBEFORETJBJ--在...之前提交并报价，INXXBEFOREJF--于...之前根据图纸制造并交付，INXXBEFOREZBJZ--于...之前招标截至，INXXBEFOREGZSD--于...之前告知收到）
     */
    private String claimDealType;

    /**
     *  回复时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date claimDealTime;

    /**
     * 转发消息的id/回复消息的id
     */
    private Integer parentId;

    /**
     *  操作类型（0普通，1回复，2转发）
     */
    private Integer optType;


    /**
     *  收件人(普通)
     */
    private List<Integer> recvUser;

    /**
     *  抄送人
     */
    private List<Integer> copyUser;

    /**
     *  文件ids
     */
    private List<Integer> fileIds;


}
