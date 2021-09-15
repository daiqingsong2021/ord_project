package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.classify.BaseClassifyAddForm;
import com.wisdom.acm.base.form.classify.BaseClassifyUpdateForm;
import com.wisdom.acm.base.po.BaseClassifyPo;
import com.wisdom.acm.base.vo.classify.BaseClassifyTreeVo;
import com.wisdom.acm.base.vo.classify.BaseClassifyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface BaseClassifyService extends CommService<BaseClassifyPo> {

    /**
     * 根据业务对象查找分类码
     * @param boCode
     * @return
     */
    public List<BaseClassifyTreeVo> queryClassifyListByBoCode(String boCode);

    /**
     * 根据业务对象查找分类码
     * @param boCode
     * @return
     */
    public List<BaseClassifyTreeVo> queryClassifyListByBoCodeId(String boCode,Integer bizId);

    /**
     * 根据业务对象查询分类码集合
     *
     * @param boCode
     * @return
     */
    List<BaseClassifyPo> queryClassifyPosByBoCode(String boCode);

    /**
     * 根据业务对象查询分类码集合
     *
     * @param boCode
     * @return
     */
    List<BaseClassifyPo> queryClassifyPosByBoCodeId(String boCode,Integer bizId);

    /**
     * 增加分类码
     * @param classifyAddForm
     */
    public BaseClassifyPo addClassify(BaseClassifyAddForm classifyAddForm);

    /**
     * 修改分类码
     * @param classifyUpdateForm
     */
    public BaseClassifyPo updateClassify(BaseClassifyUpdateForm classifyUpdateForm);

    /**
     * 删除分类码
     * @param classifyIds
     */
    void deleteClassify(List<Integer> classifyIds);

    /**
     * 获取分类码
     * @param id
     * @return
     */
    BaseClassifyVo getClassifyInfo(int id);

    List<BaseClassifyTreeVo> queryClassifyValueList(Integer classifyId);
}
