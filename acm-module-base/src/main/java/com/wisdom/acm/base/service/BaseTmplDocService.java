package com.wisdom.acm.base.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.base.form.tmpldoc.BaseTmpldocAddForm;
import com.wisdom.acm.base.form.tmpldoc.BaseTmpldocUpdateForm;
import com.wisdom.acm.base.po.BaseTmpldocPo;
import com.wisdom.acm.base.vo.tmpldoc.BaseTmpldocVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface BaseTmplDocService extends CommService<BaseTmpldocPo> {
    /**
     * 文档模板分页列表
     * @return
     */
    public PageInfo<BaseTmpldocVo> querryTmplDocPageList(Integer pageSize, Integer currentPageNum, String key);

    /**
     * 文档模板无分页列表
     * @return
     */
    public List<BaseTmpldocVo> querryTmplDocList(String key);

    /**
     * 文档模板基本信息
     * @return
     */
    public BaseTmpldocVo getTmplDocById(Integer tmplDocId);

    /**
     * 新增文档模板
     * @param baseTmpldocAddForm
     * @return
     */
    public BaseTmpldocPo addTmplDoc(BaseTmpldocAddForm baseTmpldocAddForm);

    /**
     * 删除文档模板
     * @param baseTmpldocUpdateForm
     * @return
     */
    public BaseTmpldocPo updateTmplDoc(BaseTmpldocUpdateForm baseTmpldocUpdateForm);

    /**
     * 删除文档模板
     * @param ids
     */
    public void deleteTmplDoc(List<Integer> ids);
}
