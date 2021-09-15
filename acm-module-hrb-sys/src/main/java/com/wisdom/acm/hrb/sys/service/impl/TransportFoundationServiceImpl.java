package com.wisdom.acm.hrb.sys.service.impl;

import com.wisdom.acm.hrb.sys.common.DcCommonUtil;
import com.wisdom.acm.hrb.sys.form.TransportFoundationAddForm;
import com.wisdom.acm.hrb.sys.form.TransportFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.mapper.TransportFoundationMapper;
import com.wisdom.acm.hrb.sys.po.TransportFoundationPo;
import com.wisdom.acm.hrb.sys.service.TransportFoundationService;
import com.wisdom.acm.hrb.sys.vo.TransportFoundationVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zll
 * 2020/12/1/001 13:43
 * Description:<描述>
 */
@Service
@Slf4j
public class TransportFoundationServiceImpl extends BaseService<TransportFoundationMapper, TransportFoundationPo> implements TransportFoundationService {
    @Autowired private CommDictService commDictService;@Autowired private DcCommonUtil dcCommonUtil;
    @Override public TransportFoundationVo addTransportFoundation(TransportFoundationAddForm transportFoundationAddForm) { this.insert(dozerMapper.map(transportFoundationAddForm, TransportFoundationPo.class));return null; }
    @Override public void delTransportFoundation(Integer id) { this.deleteById(id);}
    @Override public TransportFoundationVo updateTransportFoundation(TransportFoundationUpdateForm transportFoundationUpdateForm) { this.updateSelectiveById(dozerMapper.map(transportFoundationUpdateForm, TransportFoundationPo.class));return null; }
    @Override public List<TransportFoundationVo> queryTransportFoundationList(){
        List<TransportFoundationVo> transportFoundationVos = this.selectByExample(new Example(TransportFoundationPo.class)).stream().map(e->dozerMapper.map(e,TransportFoundationVo.class)).collect(Collectors.toList());
        transportFoundationVos.forEach(e-> {if(commDictService.getDictMapByTypeCode("line").keySet().contains(e.getLine())){e.setLineName(dcCommonUtil.getDictionaryName
                (commDictService.getDictMapByTypeCode("line"), e.getLine()));}});
        return transportFoundationVos;
    }
}
