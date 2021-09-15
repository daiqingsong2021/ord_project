package com.wisdom.acm.hrb.sys.service.impl;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fescar.common.util.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.hrb.sys.common.DcCommonUtil;
import com.wisdom.acm.hrb.sys.form.StationFoundationAddForm;
import com.wisdom.acm.hrb.sys.form.StationFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.form.StationStationFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.mapper.StationFoundationMapper;
import com.wisdom.acm.hrb.sys.po.StationFoundationPo;
import com.wisdom.acm.hrb.sys.po.StationStationFoundationPo;
import com.wisdom.acm.hrb.sys.service.StationFoundationService;
import com.wisdom.acm.hrb.sys.service.StationStationFoundationService;
import com.wisdom.acm.hrb.sys.vo.StationFoundationVo;
import com.wisdom.acm.hrb.sys.vo.StationStationFoundationVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Slf4j
public class StationFoundationServiceImpl extends BaseService<StationFoundationMapper, StationFoundationPo> implements StationFoundationService {
    @Autowired
    private CommDictService commDictService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Autowired
    private StationStationFoundationService stationStationFoundationService;
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)@Override
    public List<StationFoundationVo> addStationFoundationList(List<StationFoundationAddForm> stationFoundationAddForms) {
        int stationSum =0;
        if(StringUtils.equalsIgnoreCase("0",stationFoundationAddForms.get(0).getStationType().toString())) stationSum=this.getStationList(stationFoundationAddForms.get(0).getLine(),"0").size();
        else stationSum=this.getStationList(stationFoundationAddForms.get(0).getLine(),null).size();
        int finalStationSum = stationSum;
        stationFoundationAddForms.forEach(e->e.setStationNum(finalStationSum +1));
        this.insert(stationFoundationAddForms.stream().map(e->dozerMapper.map(e,StationFoundationPo.class)).collect(Collectors.toList()));
        stationFoundationAddForms.stream().map(e->dozerMapper.map(e,StationFoundationPo.class)).collect(Collectors.toList()).
                stream().map(e->dozerMapper.map(e,StationFoundationVo.class)).collect(Collectors.toList()).
                stream().sorted(Comparator.comparing(StationFoundationVo::getStationNum)).collect(Collectors.toList()).forEach(e->{
            if(commDictService.getDictMapByTypeCode("line").keySet().contains(e.getLine())) e.setLineName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("line"), e.getLine()));
            if(commDictService.getDictMapByTypeCode("station.type").keySet().contains(e.getStationType().toString())) e.setStationTypeName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("station.type"), String.valueOf(e.getStationType())));
            String relationStationName="";
            if(!ObjectUtils.isEmpty(e.getRelationStation())){
                List<String> stationList = new ArrayList<>(Arrays.asList(e.getRelationStation().split(",")));
                if(CollectionUtils.isNotEmpty(stationList)){
                    if(stationList.size()==1){
                        relationStationName=getStationListMap(e.getLine(),null).get(stationList.get(0));
                    }else{
                        for(int i=1;i<=stationList.size();i++){
                            if(i==stationList.size()){
                                relationStationName+=getStationListMap(e.getLine(),"0").get(stationList.get(i-1));
                            }else{
                                relationStationName+=getStationListMap(e.getLine(),"0").get(stationList.get(i-1))+",";
                            }
                        }
                    }
                }
            }
            if("0".equals(e.getStationType().toString())){
                if(StringUtils.equalsIgnoreCase("0",e.getIsChangeStation().toString())){
                    e.setIsChangeStationName("否");
                }else{
                    e.setIsChangeStationName("是");
                }
            }
            e.setRelationStationName(relationStationName);
        });
        List<StationStationFoundationPo>stationStationFoundationPos=Lists.newArrayList();
        int num=0;
        if(StringUtils.equalsIgnoreCase("0",stationFoundationAddForms.get(0).getStationType().toString())){
            num=stationStationFoundationService.queryStationDistanceFoundationList(stationFoundationAddForms.get(0).getLine()).stream().
                    filter(e-> StringUtils.equalsIgnoreCase("0",e.getDistanceType().toString())).distinct().collect(Collectors.toList()).size();
        }else{
            num=stationStationFoundationService.queryStationDistanceFoundationList(stationFoundationAddForms.get(0).getLine()).stream().
                    filter(e-> StringUtils.equalsIgnoreCase("1",e.getDistanceType().toString())).distinct().collect(Collectors.toList()).size();
        }
        if(StringUtils.equalsIgnoreCase("0",stationFoundationAddForms.get(0).getStationType().toString())){
            if(stationSum>=1){
                StationStationFoundationPo stationStationFoundationPo=new StationStationFoundationPo();
                if(stationSum==1){
                    stationStationFoundationPo.setStationUp(this.getStationList(stationFoundationAddForms.get(0).getLine(),"0").stream().
                            sorted(Comparator.comparing(StationFoundationVo::getStationNum)).collect(Collectors.toList()).get(0).getStationCode());
                    stationStationFoundationPo.setStationDown(this.getStationList(stationFoundationAddForms.get(0).getLine(),"0").stream().
                            sorted(Comparator.comparing(StationFoundationVo::getStationNum)).collect(Collectors.toList()).get(1).getStationCode());
                }else{
                    stationStationFoundationPo.setStationUp(stationStationFoundationService.queryStationDistanceFoundationList(stationFoundationAddForms.get(0).getLine()).stream().
                            filter(e-> StringUtils.equalsIgnoreCase("0",e.getDistanceType().toString())).distinct().collect(Collectors.toList()).get(num-1).getStationDown());
                    stationStationFoundationPo.setStationDown(stationFoundationAddForms.get(0).getStationCode());
                }
                stationStationFoundationPo.setSum(num+1);
                stationStationFoundationPo.setStationDownDistance(new BigDecimal("0.000"));
                stationStationFoundationPo.setStationUpDistance(new BigDecimal("0.000"));
                stationStationFoundationPo.setIsNewLine(1);//否
                stationStationFoundationPo.setDistanceType(0);
                stationStationFoundationPo.setLine(stationFoundationAddForms.get(0).getLine());
                stationStationFoundationPos.add(stationStationFoundationPo);
            }
        }else{
            for(StationFoundationAddForm form:stationFoundationAddForms){
                for(String code:new ArrayList<>(Arrays.asList(form.getRelationStation().split(",")))){
                    StationStationFoundationPo stationStationFoundationPo=new StationStationFoundationPo();
                    stationStationFoundationPo.setSum(num++);
                    stationStationFoundationPo.setStationDownDistance(new BigDecimal("0.000"));
                    stationStationFoundationPo.setStationUpDistance(new BigDecimal("0.000"));
                    stationStationFoundationPo.setIsNewLine(1);
                    stationStationFoundationPo.setDistanceType(1);
                    stationStationFoundationPo.setLine(form.getLine());
                    stationStationFoundationPo.setStationUp(form.getStationCode());
                    stationStationFoundationPo.setStationDown(code);
                    stationStationFoundationPos.add(stationStationFoundationPo);
                }
            }
        }
        stationStationFoundationService.insert(stationStationFoundationPos);
        return null;
    }
    @Override
    public List<StationFoundationVo> queryStationFoundationList(String line, String stationType) {
        Example example = new Example(StationFoundationPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("line", line);
        if("0".equals(stationType)){
            criteria.andEqualTo("stationType", stationType);
        }else{
            criteria.andNotEqualTo("stationType", "0");
        }
        List<StationFoundationVo> stationFoundationVos = this.selectByExample(example).stream().map(e->dozerMapper.map(e,StationFoundationVo.class)).
                collect(Collectors.toList()).stream().sorted(Comparator.comparing(StationFoundationVo::getStationNum)).collect(Collectors.toList());
        stationFoundationVos.stream().forEach(e->{
            if(commDictService.getDictMapByTypeCode("line").keySet().contains(e.getLine())) {
                e.setLineName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("line"), e.getLine()));
            }
            if(commDictService.getDictMapByTypeCode("station.type").keySet().contains(e.getStationType().toString())) {
                e.setStationTypeName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("station.type"), String.valueOf(e.getStationType())));
            }
            String relationStationName="";
            if(!ObjectUtils.isEmpty(e.getRelationStation())){
                List<String> stationList = new ArrayList<>(Arrays.asList(e.getRelationStation().split(",")));
                if(CollectionUtils.isNotEmpty(stationList)){
                    if(stationList.size()==1){
                        relationStationName=getStationListMap(e.getLine(),null).get(stationList.get(0));
                    }else{
                        for(int i=1;i<=stationList.size();i++){
                            if(i==stationList.size()){
                                relationStationName+=getStationListMap(e.getLine(),"0").get(stationList.get(i-1));
                            }else{
                                relationStationName+=getStationListMap(e.getLine(),"0").get(stationList.get(i-1))+",";
                            }
                        }
                    }
                }
            }
            if("0".equals(e.getStationType().toString())){
                if(StringUtils.equalsIgnoreCase("0",e.getIsChangeStation().toString())){
                    e.setIsChangeStationName("否");
                }else{
                    e.setIsChangeStationName("是");
                }
            }
            e.setRelationStationName(relationStationName);
        });
        return stationFoundationVos;
    }
    public Map<String,String> getStationListMap(String line,String status){
        Example example = new Example(StationFoundationPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("line", line);
        if("0".equals(status)){
            criteria.andEqualTo("stationType", status);
        }
        List<StationFoundationVo> stationFoundationVos = this.selectByExample(example).stream().map(e->dozerMapper.map(e,StationFoundationVo.class)).
                distinct().collect(Collectors.toList()).stream().sorted(Comparator.comparing(StationFoundationVo::getStationNum)).collect(Collectors.toList());
        Map<String,String>map= Maps.newHashMap();
        for(StationFoundationVo vo:stationFoundationVos){
            map.put(vo.getStationCode(),vo.getStationName());
        }
        return  map;
    }
    public Map<String,List<String>> getAuxiliaryStationList(String line){
        Map<String,List<String>> map=Maps.newHashMap();
        Example example = new Example(StationFoundationPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("line", line);
        criteria.andNotEqualTo("stationType", "0");
        List<StationFoundationVo>stationFoundationVos=this.selectByExample(example).stream().map(e->dozerMapper.map(e,StationFoundationVo.class)).distinct().collect(Collectors.toList());
        for(StationFoundationVo vo:stationFoundationVos){
            List<String>auxiliaryStationList=new ArrayList<>(Arrays.asList(vo.getRelationStation().split(",")));
            map.put(vo.getStationCode(),auxiliaryStationList);
        }
        return  map;
    }
    @Override
    public void delStationFoundationListByLine(List<String> lines) {
        mapper.delStationFoundationListByLine(lines);
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)@Override
    public void delStationFoundationList(List<StationFoundationVo> stationFoundationVos) {
        this.deleteByIds(stationFoundationVos.stream().map(StationFoundationVo::getId).distinct().collect(Collectors.toList()));
        for(String line:stationFoundationVos.stream().map(StationFoundationVo::getLine).distinct().collect(Collectors.toList())){
            if(StringUtils.equalsIgnoreCase("0",stationFoundationVos.get(0).getStationType().toString())){
                if(CollectionUtils.isNotEmpty(this.getStationList(line,"0"))){
                    mapper.updateStationFoundationList(afreshSortList(this.getStationList(line,"0"),null).
                            stream().map(e->dozerMapper.map(e,StationFoundationPo.class)).collect(Collectors.toList()));
                }
                List<Integer>delIds=Lists.newArrayList();
                List<StationStationFoundationVo> stationStationFoundationVos=stationStationFoundationService.queryStationDistanceFoundationList(line).stream().filter
                        (e-> StringUtils.equalsIgnoreCase("0",e.getDistanceType().toString())).distinct().collect(Collectors.toList());
                List<StationStationFoundationVo> delList=Lists.newArrayList();
                String stationDown="";
                for(StationStationFoundationVo vo:stationStationFoundationVos){
                    if(stationFoundationVos.stream().map(StationFoundationVo::getStationCode).distinct().collect(Collectors.toList()).contains(vo.getStationUp())){
                        delIds.add(vo.getId());
                        delList.add(vo);
                        stationDown=vo.getStationDown();
                    }
                    if(stationStationFoundationVos.size()==vo.getSum().intValue() && stationFoundationVos.stream().map(StationFoundationVo::getStationCode).distinct().collect(Collectors.toList()).contains(vo.getStationDown())){
                        delIds.add(vo.getId());
                        delList.add(vo);
                    }
                }
                for(StationStationFoundationVo vo:stationStationFoundationVos){
                    if(stationFoundationVos.stream().map(StationFoundationVo::getStationCode).distinct().collect(Collectors.toList()).contains(vo.getStationDown())){
                        if(stationStationFoundationVos.size()-1==vo.getSum().intValue()){
                            vo.setStationDown(stationDown);
                        }
                    }
                }
                stationStationFoundationVos.removeAll(delList);
                stationStationFoundationService.deleteByIds(delIds);
                List<StationStationFoundationVo>stationStationFoundationVoList=resertStationDownList(stationStationFoundationVos,line);
                if(this.getStationList(line,"0").size()==1){
                    if(CollectionUtils.isNotEmpty(stationStationFoundationVoList)){
                        stationStationFoundationService.deleteById(stationStationFoundationVoList.get(0).getId());
                    }
                }else{
                    if(CollectionUtils.isNotEmpty(stationStationFoundationVoList)){
                        updateStationDistanceFoundationList(stationStationFoundationVoList.stream().filter(e->!delIds.contains(e.getId())).distinct().collect(Collectors.toList()).stream().
                                sorted(Comparator.comparing(StationStationFoundationVo::getSum)).collect(Collectors.toList()));
                    }
                }
                List<Integer>ids=Lists.newArrayList();
                List<StationFoundationVo>stationFoundationVosList=Lists.newArrayList();
                for(StationFoundationVo vo:this.queryStationFoundationList(line,null)){
                    for(StationFoundationVo ss:stationFoundationVos){
                        if(new ArrayList<>(Arrays.asList(vo.getRelationStation().split(","))).contains(ss.getStationCode())){
                            ids.add(vo.getId());
                            stationFoundationVosList.add(vo);
                        }
                    }
                }
                this.deleteByIds(ids.stream().distinct().collect(Collectors.toList()));
                delStationAndDistanceToSort(line,stationFoundationVosList.stream().distinct().collect(Collectors.toList()));
            }else{
                delStationAndDistanceToSort(line,stationFoundationVos);
            }
        }
    }
    private List<StationStationFoundationVo> resertStationDownList(List<StationStationFoundationVo> stationStationFoundationVoList,String line) {
        List<String>codes=this.queryStationFoundationList(line,"0").stream().map(StationFoundationVo::getStationCode).distinct().collect(Collectors.toList());
        for(int i=0;i<stationStationFoundationVoList.size();i++){
            if(!codes.contains(stationStationFoundationVoList.get(i).getStationDown())) {
                if(codes.size() != stationStationFoundationVoList.size()){
                    stationStationFoundationVoList.get(i).setStationDown(codes.get(i+1));
                    stationStationFoundationVoList.get(i).setStationDownDistance(new BigDecimal("0.000"));
                }
            }
        }
        return stationStationFoundationVoList;
    }
    private void delStationAndDistanceToSort(String line,List<StationFoundationVo> stationFoundationVos) {
        if(CollectionUtils.isNotEmpty(this.getStationList(line,null))) mapper.updateStationFoundationList(afreshSortList(this.getStationList(line,null),null).stream().map(e->dozerMapper.map(e,StationFoundationPo.class)).collect(Collectors.toList()));
        List<Integer>delIds=Lists.newArrayList();
        for(StationStationFoundationVo vo:stationStationFoundationService.queryStationDistanceFoundationList(line).stream().filter
                (e->StringUtils.equalsIgnoreCase("1",e.getDistanceType().toString())).distinct().collect(Collectors.toList())){
            if(stationFoundationVos.stream().map(StationFoundationVo::getStationCode).collect(Collectors.toList()).contains(vo.getStationUp())) delIds.add(vo.getId());
        }
        stationStationFoundationService.deleteByIds(delIds.stream().distinct().collect(Collectors.toList()));
        if(CollectionUtils.isNotEmpty(stationStationFoundationService.queryStationDistanceFoundationList(line).stream().filter(e->StringUtils.equalsIgnoreCase("1",e.getDistanceType().toString())).
                distinct().collect(Collectors.toList()).stream().sorted(Comparator.comparing(StationStationFoundationVo::getSum)).collect(Collectors.toList()).stream().
                filter(e->!delIds.stream().distinct().collect(Collectors.toList()).contains(e.getId())).distinct().collect(Collectors.toList()))){
            updateStationDistanceFoundationList(stationStationFoundationService.queryStationDistanceFoundationList(line).stream().filter(e->StringUtils.equalsIgnoreCase("1",e.getDistanceType().toString())).
                    distinct().collect(Collectors.toList()).stream().sorted(Comparator.comparing(StationStationFoundationVo::getSum)).collect(Collectors.toList()).stream().
                    filter(e->!delIds.stream().distinct().collect(Collectors.toList()).contains(e.getId())).distinct().collect(Collectors.toList()));
        }
    }
    private void updateStationDistanceFoundationList(List<StationStationFoundationVo>stationStationFoundationVos) {
        List<StationStationFoundationUpdateForm> forms= Lists.newArrayList();
        for(int i=0;i<stationStationFoundationVos.size();i++){
            StationStationFoundationUpdateForm form=dozerMapper.map(stationStationFoundationVos.get(i),StationStationFoundationUpdateForm.class);
            form.setSum(i+1);
            forms.add(form);
        }
        stationStationFoundationService.updateStationDistanceFoundationList(forms);
    }
    private List<?> afreshSortList(List<StationFoundationVo> stationFoundationVoList,List<StationStationFoundationVo> stationStationFoundationVoList) {
        if(CollectionUtils.isNotEmpty(stationFoundationVoList)){
            for(int i=0;i<stationFoundationVoList.size();i++){
                stationFoundationVoList.get(i).setStationNum(i+1);
            }
            return stationFoundationVoList;
        }
        if(CollectionUtils.isNotEmpty(stationStationFoundationVoList)){
            for(int i=0;i<stationStationFoundationVoList.size();i++){
                stationStationFoundationVoList.get(i).setSum(i+1);
            }
            return stationStationFoundationVoList;
        }return null;
    }
    public List<StationFoundationVo> getStationList(String line,String status){
        Example example = new Example(StationFoundationPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("line", line);
        if("0".equals(status))
            criteria.andEqualTo("stationType", status);else criteria.andNotEqualTo("stationType", "0");
        return this.selectByExample(example).stream().map(e->dozerMapper.map(e,StationFoundationVo.class)).distinct().collect(Collectors.toList()).
                stream().sorted(Comparator.comparing(StationFoundationVo::getStationNum)).collect(Collectors.toList());
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)@Override
    public List<StationFoundationVo> updateStationFoundationList(List<StationFoundationUpdateForm> stationFoundationUpdateForms) {
        mapper.updateStationFoundationList(stationFoundationUpdateForms.stream().map(e->dozerMapper.map(e,StationFoundationPo.class)).collect(Collectors.toList()));
        List<StationFoundationVo> stationFoundationVos = stationFoundationUpdateForms.stream().map(e->dozerMapper.map(e,StationFoundationPo.class)).collect(Collectors.toList()).stream().
                map(e->dozerMapper.map(e,StationFoundationVo.class)).collect(Collectors.toList()).stream().sorted(Comparator.comparing(StationFoundationVo::getStationNum)).collect(Collectors.toList());
        List<StationStationFoundationVo> stationStationFoundationVos=stationStationFoundationService.queryStationDistanceFoundationList(stationFoundationVos.get(0).getLine()).stream().
                filter(e-> StringUtils.equalsIgnoreCase("0",e.getDistanceType().toString())).distinct().collect(Collectors.toList()).stream().
                sorted(Comparator.comparing(StationStationFoundationVo::getSum)).collect(Collectors.toList());
        List<String>stationCode=stationFoundationVos.stream().map(StationFoundationVo::getStationCode).distinct().collect(Collectors.toList());
        List<StationStationFoundationUpdateForm> stationStationFoundationUpdateForms= Lists.newArrayList();
        for(int i=0;i<stationStationFoundationVos.size();i++){
            StationStationFoundationUpdateForm form=dozerMapper.map(stationStationFoundationVos.get(i),StationStationFoundationUpdateForm.class);
            form.setStationDownDistance(new BigDecimal("0.000"));
            form.setStationUpDistance(new BigDecimal("0.000"));
            form.setStationUp(stationCode.get(i));
            form.setStationDown(stationCode.get(i+1));
            stationStationFoundationUpdateForms.add(form);
        }
        if(CollectionUtils.isNotEmpty(stationStationFoundationUpdateForms)){
            stationStationFoundationService.updateStationDistanceFoundationList(stationStationFoundationUpdateForms);
        }
        return stationFoundationVos;
    }


    /**
     *
     * @param mapWhere
     * @return
     */
    @Override
    public List<StationFoundationVo> queryStationListByParam(Map<String, Object> mapWhere) {

        List<StationFoundationVo> stationFoundationVos = mapper.queryStationListByParam(mapWhere);
       for(StationFoundationVo e:stationFoundationVos)
       {
            //插入线路名称
            if(commDictService.getDictMapByTypeCode("line").keySet().contains(e.getLine())) {
                e.setLineName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("line"), e.getLine()));
            }
            //插入站点类型名称
            if(commDictService.getDictMapByTypeCode("station.type").keySet().contains(e.getStationType().toString())) {
                e.setStationTypeName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("station.type"), String.valueOf(e.getStationType())));
            }
            String relationStationName="";
            if(!ObjectUtils.isEmpty(e.getRelationStation()))
            {
                List<String> stationList = new ArrayList<>(Arrays.asList(e.getRelationStation().split(",")));
                Map<String, Object> map= new HashMap<>();
                map.put("stationCodes",stationList);
                List<StationFoundationVo> stationFoundationRelation=mapper.queryStationListByParam(map);
                e.setStationFoundationRelation(stationFoundationRelation);
            }
            if("0".equals(e.getStationType().toString())){
                if(StringUtils.equalsIgnoreCase("0",e.getIsChangeStation().toString())){
                    e.setIsChangeStationName("否");
                }else{
                    e.setIsChangeStationName("是");
                }
            }
            e.setRelationStationName(relationStationName);
        };
        return stationFoundationVos;
    }
    @Override public String checkStationFoundationIsHave(String line, String stationCode,String stationType) {
        Example example = new Example(StationFoundationPo.class);
        Example.Criteria criteria = example.createCriteria();
        if(!ObjectUtils.isEmpty(line)) criteria.andEqualTo("line", line);
        if(!ObjectUtils.isEmpty(stationType)) criteria.andEqualTo("stationType", stationType);
        if(!ObjectUtils.isEmpty(stationCode)) criteria.andEqualTo("stationCode", stationCode);
        if(CollectionUtils.isNotEmpty(this.selectByExample(example).stream().map(e->dozerMapper.map(e,StationFoundationVo.class)).collect(Collectors.toList()))) return "0";else return "1";
    }
}
