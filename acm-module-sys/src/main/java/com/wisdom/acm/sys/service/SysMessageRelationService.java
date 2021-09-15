package com.wisdom.acm.sys.service;


import com.wisdom.acm.sys.po.SysMessageRelationPo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysMessageRelationService extends CommService<SysMessageRelationPo> {
    /**
     * 增加消息关联
     *
     * @param messageId
     * @param bizType
     * @param bizIds
     */
    void addMessageRelation(Integer messageId, String bizType, List<Integer> bizIds);

    /**
     *  删除消息关系
     * @param messageId
     */
    void deleteMessageRelation(Integer messageId);

    /**
     * 查询所有文件关联表信息
     * @return
     */
    List<SysMessageRelationPo> querySysMessageRelationPos(Integer messageId);
}
