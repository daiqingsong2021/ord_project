package com.wisdom.acm.dc2.service;

import com.wisdom.acm.dc2.form.SmsTaskAddForm;
import com.wisdom.acm.dc2.po.SmsTaskPo;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface SmsTaskService extends CommService<SmsTaskPo> {

    //新增任务
    void addSmsTask(SmsTaskAddForm form);

    //删除任务
    void delSmsTask(List<Integer> ids);

    /**
     * 查询列表
     * @param mapWhere
     * @return
     */
    List<SmsTaskVo> querySmsTaskList(Map<String, Object> mapWhere);

    //扫描定时任务
    List<SmsTaskVo> scanSmsTaskList();

    //根据任务id更新状态
    void updateSmsTaskStatus(Integer taskId, Integer status);
}
