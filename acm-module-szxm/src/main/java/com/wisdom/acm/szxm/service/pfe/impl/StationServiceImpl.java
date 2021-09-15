package com.wisdom.acm.szxm.service.pfe.impl;


import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.pfe.StationAddForm;
import com.wisdom.acm.szxm.form.pfe.StationUpdateForm;
import com.wisdom.acm.szxm.mapper.pfe.StationMapper;
import com.wisdom.acm.szxm.po.pfe.StationPo;
import com.wisdom.acm.szxm.service.pfe.StationService;
import com.wisdom.acm.szxm.vo.pfe.StationVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.DictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 站点业务逻辑处理
 */
@Service
public class StationServiceImpl extends BaseService<StationMapper, StationPo> implements StationService {

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Override
    public StationVo getStationInfo(Integer id) {
        if(!ObjectUtils.isEmpty(id)){
            Example example = new Example(StationPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",id);
            StationPo stationPo = this.selectOneByExample(example);
            if(stationPo != null){
                StationVo stationVo = new StationVo();

                stationVo.setId(stationPo.getId());
                stationVo.setName(stationPo.getName());
                stationVo.setCode(stationPo.getCode());
                stationVo.setRemark(stationPo.getRemark());

                String dictName = szxmCommonUtil.getDictionaryName("proj.station.type",stationPo.getStationType());
                DictionaryVo dictionaryVo = new DictionaryVo();
                dictionaryVo.setId(stationPo.getStationType());
                dictionaryVo.setName(dictName);
                stationVo.setStationType(dictionaryVo);

                return stationVo;
            }
        }
        return null;
    }

    @Override
    public List<StationVo> queryStationByProjectId(Integer projectId) {
        return this.mapper.selectByProjectId(projectId);
    }

    @Override
    public StationVo addStationByForm(StationAddForm stationAddForm) {

        StationPo stationPo = new StationPo();
        stationPo.setName(stationAddForm.getName());

        Integer id = mapper.selectOneByCode(stationAddForm.getCode(),stationAddForm.getProjectId());
        if(!ObjectUtils.isEmpty(id)){
            throw new BaseException("输入的编码已存在，请重新输入");
        }

        stationPo.setCode(stationAddForm.getCode());
        stationPo.setProjectId(stationAddForm.getProjectId());
        stationPo.setStationType(stationAddForm.getStationType());
        stationPo.setRemark(stationAddForm.getRemark());
        this.insert(stationPo);

        StationVo stationVo = dozerMapper.map(stationPo,StationVo.class);

        String dictName = szxmCommonUtil.getDictionaryName("proj.station.type",stationPo.getStationType());

        DictionaryVo dictionaryVo = new DictionaryVo();
        dictionaryVo.setId(stationPo.getStationType());
        dictionaryVo.setName(dictName);

        stationVo.setStationType(dictionaryVo);
        return stationVo;
    }

    @Override
    public StationVo updateStationByForm(StationUpdateForm stationUpdateForm) {

        Integer id = stationUpdateForm.getId();
        if(!ObjectUtils.isEmpty(id)){

           Integer existId =  mapper.selectOneByCodeAndId(stationUpdateForm.getCode(),id,stationUpdateForm.getProjectId());
           if(!ObjectUtils.isEmpty(existId)){
               throw new BaseException("修改的编码已存在，请重新输入");
           }

            StationPo stationPo =  this.selectById(id);
            if(stationPo != null){
                stationPo.setCode(stationUpdateForm.getCode());
                stationPo.setName(stationUpdateForm.getName());
                stationPo.setStationType(stationUpdateForm.getStationType());
                stationPo.setRemark(stationUpdateForm.getRemark());

                this.updateById(stationPo);
            }
            StationVo stationVo = dozerMapper.map(stationPo,StationVo.class);

            String dictName = szxmCommonUtil.getDictionaryName("proj.station.type",stationPo.getStationType());

            DictionaryVo dictionaryVo = new DictionaryVo();
            dictionaryVo.setId(stationPo.getStationType());
            dictionaryVo.setName(dictName);

            stationVo.setStationType(dictionaryVo);

            return stationVo;
        }
        return null;
    }

    @Override
    public void deleteStation(List<Integer> ids) {
        if(!ObjectUtils.isEmpty(ids)){
            List<StationPo> stationPos =  this.selectByIds(ids);
            if(!ObjectUtils.isEmpty(stationPos)){
                for (StationPo stationPo : stationPos) {
                    this.delete(stationPo);
                }
            }
        }
    }
}
