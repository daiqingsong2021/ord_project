package com.wisdom.acm.hrb.sys.service.impl;

import com.wisdom.acm.hrb.sys.common.DcCommonUtil;
import com.wisdom.acm.hrb.sys.form.LineFoundationAddForm;
import com.wisdom.acm.hrb.sys.mapper.LineFoundationMapper;
import com.wisdom.acm.hrb.sys.po.LineFoundationPo;
import com.wisdom.acm.hrb.sys.service.LineFoundationService;
import com.wisdom.acm.hrb.sys.service.StationFoundationService;
import com.wisdom.acm.hrb.sys.service.StationStationFoundationService;
import com.wisdom.acm.hrb.sys.vo.LineFoundationVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zll
 * 2020/10/20/020 10:59
 * Description:<线路管理信息>
 */
@Service
@Slf4j
public class LineFoundationServiceImpl extends BaseService<LineFoundationMapper, LineFoundationPo> implements LineFoundationService {
    @Autowired
    private CommDictService commDictService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)@Override
    public List<LineFoundationVo> addLineFoundationList(List<LineFoundationAddForm> lineFoundationAddForms) {
        this.insert(lineFoundationAddForms.stream().map(e->dozerMapper.map(e,LineFoundationPo.class)).collect(Collectors.toList()));return queryLineFoundationList();}
    @Override
    public List<LineFoundationVo> queryLineFoundationList() {
        List<LineFoundationVo> lineFoundationVos = this.selectByExample(new Example(LineFoundationPo.class)).stream().map(e->dozerMapper.map(e,LineFoundationVo.class)).collect
                (Collectors.toList()).stream().sorted(Comparator.comparing(LineFoundationVo::getLine)).collect(Collectors.toList());
        lineFoundationVos.forEach(e-> {if(commDictService.getDictMapByTypeCode("line").keySet().contains(e.getLine())){e.setLineName(dcCommonUtil.getDictionaryName
                (commDictService.getDictMapByTypeCode("line"), e.getLine()));}});
        return lineFoundationVos;
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED) @Override
    public void delLineFoundationList(List<LineFoundationVo>lineFoundationVos) {this.deleteByIds(lineFoundationVos.stream().map(LineFoundationVo::getId).distinct().collect(Collectors.toList()));}
    @Override
    public String checkLineFoundationIsHave(String line){if(queryLineFoundationList().stream().map(LineFoundationVo::getLine).distinct().collect(Collectors.toList()).contains(line))return "0";else return "1";}
}
