package com.wisdom.acm.hrb.sys.service;

import com.wisdom.acm.hrb.sys.form.TransportFoundationAddForm;
import com.wisdom.acm.hrb.sys.form.TransportFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.po.TransportFoundationPo;
import com.wisdom.acm.hrb.sys.vo.TransportFoundationVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;

/**
 * @author zll
 * 2020/12/1/001 13:42
 * Description:<描述>
 */
public interface TransportFoundationService extends CommService<TransportFoundationPo> {
    TransportFoundationVo addTransportFoundation(TransportFoundationAddForm transportFoundationAddForm);
    void delTransportFoundation(Integer id);
    TransportFoundationVo updateTransportFoundation(TransportFoundationUpdateForm transportFoundationUpdateForm);
    List<TransportFoundationVo> queryTransportFoundationList();
}
