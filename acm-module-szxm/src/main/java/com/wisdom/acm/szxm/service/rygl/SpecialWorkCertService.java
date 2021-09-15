package com.wisdom.acm.szxm.service.rygl;

import com.wisdom.acm.szxm.form.rygl.SpecialWorkCertAddForm;
import com.wisdom.acm.szxm.form.rygl.SpecialWorkCertUpdateForm;
import com.wisdom.acm.szxm.po.rygl.SpecialWorkCertPo;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkCertVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface SpecialWorkCertService extends CommService<SpecialWorkCertPo>
{

    /**
     * 获取特殊工种证书
     * @param mapWhere
     * @return
     */
    List<SpecialWorkCertVo> selectSpecialWorkCert(Map<String, Object> mapWhere);

    /**
     * 增加特殊工种证书
     * @param specialWorkTypeAddForm
     * @return
     */
    SpecialWorkCertVo addSpecialWorkCert(SpecialWorkCertAddForm specialWorkTypeAddForm);

    /**
     * 删除特殊工种证书
     * @param ids
     */
    void deleteSpecialWorkCert(List<Integer> ids);

    /**
     * 更新特殊工种证书
     * @param specialWorkTypeUpdateForm
     * @return
     */
    SpecialWorkCertVo updateSpecialWorkCert(SpecialWorkCertUpdateForm specialWorkTypeUpdateForm);

    /**
     * 特殊特殊工种证书，附带将特殊工种查出来
     * @return
     */
    List<SpecialWorkCertVo> selectSwWorkWithWorker();

    /**
     * 根据userid 查出证书
     * @param mapWhere
     * @return
     */
    List<SpecialWorkCertVo> getCertByUserIdList(Map<String, Object> mapWhere);
}
