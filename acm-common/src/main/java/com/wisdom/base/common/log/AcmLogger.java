package com.wisdom.base.common.log;

import com.wisdom.base.common.util.DateUtil;
import com.wisdom.base.common.vo.log.LogContentsVo;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * 日志
 */
@Data
public class AcmLogger {
    // id
    private String id;
    // 模块
    private String module;
    // 标题
    private String title;
    // 日志内容
    private String content="";
    // 创建时间
    private String creatTime;
    // 创建人
    private String creator;
    // IP地址
    private String ipaddress;
    // 用户类型：（1：普通用户，其它为三员用户）
    private String userType;
    // 错误（成功/失败）
    private String status;
    // 异常信息
    private String error;
    // 操作类型
    private String optType;
    //
    private Boolean loaded;
    // 日志开始内容
    private String startContent;
    // 记录 时间
    private String recordTime= DateUtil.formatDate(new Date());
    // 线路
    private String line="";
    // 记录 id
    private String recordId;
    // 记录状态  0:新建  1：已完成
    private String recordStatus;




    private List<LogContentsVo> logContentsVos;

    public AcmLogger(){

    }

    public String getContent(){

        if(!ObjectUtils.isEmpty(this.startContent)){
            return this.startContent+","+this.content;
        }
        return this.content;
    }


    public AcmLogger(String content){
        this.content = content;
    }

    public AcmLogger(String module,String title,String content){
        this.module = module;
        this.title = title;
        this.content = content;
    }
    public AcmLogger(String recordTime,String line,String recordId,String recordStatus){
        this.recordTime = recordTime;
        this.line = line;
        this.recordId = recordId;
        this.recordStatus = recordStatus;
    }

    public AcmLogger(String content,String recordTime,String line,String recordId,String recordStatus){
        this.recordTime = recordTime;
        this.line = line;
        this.recordId = recordId;
        this.recordStatus = recordStatus;
        this.content=content;//添加日志内容
    }
}
