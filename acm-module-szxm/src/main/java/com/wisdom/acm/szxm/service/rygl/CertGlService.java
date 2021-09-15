package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.CertGlAddForm;
import com.wisdom.acm.szxm.form.rygl.CertGlUpdateForm;
import com.wisdom.acm.szxm.po.rygl.CertGlPo;
import com.wisdom.acm.szxm.vo.rygl.CertGlVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface CertGlService extends CommService<CertGlPo>
{

    /**
     * 获取证书选择内容
     * @param mapWhere
     * @return
     */
    List<CertGlVo> getCertGlList(Map<String, Object> mapWhere);

    /**
     * 增加证书管理
     * @param specialWorkerTypeAddForm
     * @return
     */
    CertGlVo addCertGl(CertGlAddForm specialWorkerTypeAddForm);

    /**
     * 查询证书管理
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<CertGlVo> selectCertGlList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 删除证书管理
     * @param ids
     */
    void deleteCertGl(List<Integer> ids);

    /**
     * 更新证书管理
     * @param specialWorkerTypeUpdateForm
     * @return
     */
    CertGlVo updateCertGl(CertGlUpdateForm specialWorkerTypeUpdateForm);

    CertGlVo selectByCertGlById(Integer id);
}
