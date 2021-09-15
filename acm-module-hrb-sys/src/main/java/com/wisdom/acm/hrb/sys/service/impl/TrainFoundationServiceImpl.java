package com.wisdom.acm.hrb.sys.service.impl;
import com.alibaba.fescar.common.util.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.hrb.sys.common.DcCommonUtil;
import com.wisdom.acm.hrb.sys.form.TrainFoundationAddForm;
import com.wisdom.acm.hrb.sys.form.TrainFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.mapper.TrainFoundationMapper;
import com.wisdom.acm.hrb.sys.po.TrainFoundationPo;
import com.wisdom.acm.hrb.sys.service.TrainFoundationService;
import com.wisdom.acm.hrb.sys.vo.TrainFoundationVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Slf4j
public class TrainFoundationServiceImpl extends BaseService<TrainFoundationMapper, TrainFoundationPo> implements TrainFoundationService{
    @Autowired private CommDictService commDictService;@Autowired private DcCommonUtil dcCommonUtil;
    @Override  public TrainFoundationVo addTrainFoundation(TrainFoundationAddForm trainFoundationAddForm){this.insert(dozerMapper.map(trainFoundationAddForm, TrainFoundationPo.class));return null;}
    @Override  public void delTrainFoundationList(List<Integer> ids){this.deleteByIds(ids); }
    @Override  public TrainFoundationVo updateTrainFoundation(TrainFoundationUpdateForm trainFoundationUpdateForm){this.updateSelectiveById(dozerMapper.map(trainFoundationUpdateForm, TrainFoundationPo.class));return null;}
    @Override  public String checkTrainFoundationIsHave(String line,String trainCode){if(CollectionUtils.isNotEmpty(getTrainFoundationVos(line,trainCode))) return "0";else return "1";}
    @Override  public PageInfo<TrainFoundationVo> queryTrainFoundationList(String line,String trainCode,Integer pageSize, Integer currentPageNum){
        List<TrainFoundationVo>trainFoundationVos=getTrainFoundationVos(line,trainCode);PageHelper.startPage(currentPageNum, pageSize);PageInfo<TrainFoundationVo> pageInfo = new PageInfo<TrainFoundationVo>(trainFoundationVos);
        if (!ObjectUtils.isEmpty(pageInfo.getList()))pageInfo.getList().forEach(e->{e.setLineName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("line"), e.getLine()));});return pageInfo;}
    @Override  public List<TrainFoundationVo>getTrainFoundationVos(String line,String trainCode){
        Example example=new Example(TrainFoundationPo.class);Example.Criteria criteria = example.createCriteria();example.orderBy("line").asc().orderBy("trainCode").asc();
        if(!ObjectUtils.isEmpty(line)) criteria.andEqualTo("line", line);if(!ObjectUtils.isEmpty(trainCode)) criteria.andEqualTo("trainCode", trainCode);
        return this.selectByExample(example).stream().map(e->dozerMapper.map(e,TrainFoundationVo.class)).collect(Collectors.toList());}}



