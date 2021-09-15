package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvAddForm;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvUpdateForm;
import com.wisdom.acm.base.po.BaseTmpldelvPo;
import com.wisdom.acm.base.po.BaseTmpldelvTypePo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvTreeVo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface BaseTmplDelvService extends CommService<BaseTmpldelvPo> {
    /**
     * 交付物列表
     * @return
     */
    public List<BaseTmpldelvTreeVo> querryTmpldelvList();

    /**
     * 通过tmpldelvId获取交付物列表
     * @param tmpldelvId
     * @return
     */
    public List<BaseTmpldelvTreeVo> querryTmpldelvListByTmplId(Integer tmpldelvId);

    /**
     * 通过tmpldelvId获取交付物列表不包括模板
     * @param tmpldelvId
     * @return
     */
    public List<BaseTmpldelvTreeVo> querryTmpldelvListSubByTmplId(Integer tmpldelvId);

    /**
     * 通过tmpldelvId获取交付物列表不包括模板
     * @param tmpldelvId
     * @return
     */
    public List<BaseTmpldelvTreeVo> querryPageBaseTmplListAssignTree(Integer tmpldelvId,Integer taskId);

    /**
     * 交付物基本信息
     * @return
     */
    public BaseTmpldelvVo getTmpldelvById(Integer tmpldelvId);

    /**
     * 新增交付物
     * @param baseTmpldelvAddForm
     * @return
     */
    public BaseTmpldelvPo addPbsTmpldelv(BaseTmpldelvAddForm baseTmpldelvAddForm);

    /**
     * 新增交付物
     * @param baseTmpldelvAddForm
     * @return
     */
    public BaseTmpldelvPo addSubTmpldelv(BaseTmpldelvAddForm baseTmpldelvAddForm);

    /**
     * 删除交付物
     * @param baseTmpldelvUpdateForm
     * @return
     */
    public BaseTmpldelvPo updateTmpldelv(BaseTmpldelvUpdateForm baseTmpldelvUpdateForm);

    /**
     * 删除交付物
     * @param ids
     */
    public void deleteTmpldelv(List<Integer> ids);

    /**
     * 根据BaseTmpldelvTypePo删除交付物
     * @param baseTmpldelvTypePo
     */
    public void deleteByDelvTypePo(BaseTmpldelvTypePo baseTmpldelvTypePo);

    /**
     * 根据交付物模板Id获取交付物列表
     * @param delvTypeId
     */
    public List<BaseTmpldelvVo> querryDelvByTypeId(Integer delvTypeId);

    /**
     * 删除交付物模板
     * @param ids
     */
    public void deleteTmpldelvType(List<Integer> ids);

    /**
     * 根据TypeId删除
     * @param typeId
     */
    public void deleteByBaseTmpldelvTypeId(Integer typeId);


    /**
     * 根据typeId查询
     * @param typeId
     * @return
     */
    public List<BaseTmpldelvVo> queryTmpldelvByTypeId(Integer typeId);

    String queryAssignTaskDelv(List<Integer> delvIds);

    List<BaseTmpldelvPo> queryTmpldelvPosByIds(List<Integer> ids);
}
