package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.mapper.SysMessageRelationMapper;
import com.wisdom.acm.sys.po.SysMessageRelationPo;
import com.wisdom.acm.sys.service.SysMessageRelationService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMessageRelationServiceImpl extends BaseService<SysMessageRelationMapper, SysMessageRelationPo> implements SysMessageRelationService {

    /**
     *  添加到关系中间表
     * @param messageId
     * @param bizType
     * @param bizIds
     */
    @Override
    public void addMessageRelation(Integer messageId, String bizType, List<Integer> bizIds) {
        if (!ObjectUtils.isEmpty(bizIds)){
            for (Integer bizId : bizIds){
                SysMessageRelationPo sysMessageRelationPo = new SysMessageRelationPo();
                // 目标类型
                sysMessageRelationPo.setBizType(bizType);
                // 目标id
                sysMessageRelationPo.setBizId(bizId);
                // 消息id
                sysMessageRelationPo.setMessageId(messageId);
                //增加
                this.insert(sysMessageRelationPo);
            }
        }

    }

    /**
     *  删除关系中间表
     * @param messageId
     */
    @Override
    public void deleteMessageRelation(Integer messageId) {
        Example example = new Example(SysMessageRelationPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("messageId", messageId);
        //获取消息的所有的附件中间表信息
        List<SysMessageRelationPo> list = this.mapper.selectByExample(example);
        //获取中间表id集合
        List<Integer> ids = ListUtil.toIdList(list);
        this.deleteByIds(ids);
    }


    /**
     * 查询所有文件关联表信息
     * @return
     */
    @Override
    public List<SysMessageRelationPo> querySysMessageRelationPos(Integer messageId){
        Example example = new Example(SysMessageRelationPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bizType", "file");
        criteria.andEqualTo("messageId",messageId);
        List<SysMessageRelationPo> list = this.selectByExample(example);
        return ObjectUtils.isEmpty(list)? null : list;
    }

}
