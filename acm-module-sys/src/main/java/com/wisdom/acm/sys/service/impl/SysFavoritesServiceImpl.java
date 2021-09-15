package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.form.SysFavoritesAddForm;
import com.wisdom.acm.sys.form.SysFavoritesDeleteForm;
import com.wisdom.acm.sys.mapper.SysFavoritesMapper;
import com.wisdom.acm.sys.po.SysFavoritesPo;
import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.service.SysFavoritesService;
import com.wisdom.acm.sys.service.SysUserService;
import com.wisdom.acm.sys.vo.SysFavoritesReturnVo;
import com.wisdom.acm.sys.vo.SysFavoritesVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.UserInfo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class SysFavoritesServiceImpl extends BaseService<SysFavoritesMapper, SysFavoritesPo> implements SysFavoritesService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysFavoritesVo> queryFavoritesByUserId(String userName) {
        UserInfo userInfo = sysUserService.getUserInfoByName(userName);
        List<SysFavoritesVo> favoritesVos = mapper.selectFavoritesByUserId(userInfo.getId());
        return favoritesVos;
    }


    @Override
    public SysFavoritesPo getFavoritesByUserIdAndBizType(Integer userId, String bizType) {
        Example example = new Example(SysFavoritesPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("bizType", bizType);
        return this.selectOneByExample(example);
    }


    @Override
    public List<String> queryFavoritesContentByUserIdAndBizType(Integer userId, String bizType) {
        Example example = new Example(SysFavoritesPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("bizType", bizType);
        SysFavoritesPo favoritesPo = this.selectOneByExample(example);
        List<String> bizlist = null;
        if(favoritesPo != null){
            String content = favoritesPo.getContent();
            String[] arr = null;
            if (!ObjectUtils.isEmpty(content)) {
                arr = content.split(",");
                bizlist = ListUtil.toArrayList(arr);
            }
        }

        return bizlist;
    }
    @Override
    public List<SysFavoritesPo> queryFavoritesPoByUserIdAndBizType(Integer userId, List<String> bizTypes){
        if(ObjectUtils.isEmpty(bizTypes)){
            return null;
        }
        Example example = new Example(SysFavoritesPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andIn("bizType", bizTypes);
        return this.selectByExample(example);
    }

    @Override
    public Map<String,List<String>> queryFavoritesContentByUserIdAndBizType(Integer userId, List<String> bizTypes) {

        Map<String,List<String>> retMap = new HashMap<>();
        List<SysFavoritesPo> favoritesPos = this.queryFavoritesPoByUserIdAndBizType(userId,bizTypes);

        if(!ObjectUtils.isEmpty(favoritesPos)){
            for(SysFavoritesPo favoritesPo : favoritesPos){
                String content = favoritesPo.getContent();
                List<String> bizlist = null;
                if (!ObjectUtils.isEmpty(content)) {
                    bizlist = ListUtil.toArrayList(content.split(","));
                }
                retMap.put(favoritesPo.getBizType(),bizlist);
            }
        }
        return retMap;
    }



    @Override
    public void addFavorites(SysFavoritesAddForm sysFavoritesAddForm, Integer userId) {
        SysFavoritesPo sysFavoritesPo = getFavoritesByUserIdAndBizType(userId, sysFavoritesAddForm.getBizType());
        List<String> bizs = sysFavoritesAddForm.getBizs();
        if (sysFavoritesPo != null) {
            String content = sysFavoritesPo.getContent();
            String[] arr = null;
            if (!ObjectUtils.isEmpty(content)) {
                arr = content.split(",");
            }
            List<String> bizlist = ListUtil.toArrayList(arr);
            if (!ObjectUtils.isEmpty(bizs)) {
                for (String biz : bizs) {
                    if (!ObjectUtils.isEmpty(bizlist)) {
                        if (!bizlist.contains(biz)) {
                            content = content + "," + biz;
                        }
                    }

                }
            }
            sysFavoritesPo.setContent(content);
            super.updateById(sysFavoritesPo);
        } else {
            sysFavoritesPo = dozerMapper.map(sysFavoritesAddForm, SysFavoritesPo.class);
            sysFavoritesPo.setUserId(userId);
            sysFavoritesPo.setBizType(sysFavoritesAddForm.getBizType());
            sysFavoritesPo.setSort(mapper.selectNextSort());
            if (!ObjectUtils.isEmpty(bizs)) {
                String content = ListUtil.toStr(bizs);
                sysFavoritesPo.setContent(content);
            }
            super.insert(sysFavoritesPo);
        }
    }

    @Override
    public void restFavorites(SysFavoritesAddForm sysFavoritesAddForm, Integer userId) {
        // 清空该类型的值
        this.deleteFavoritesByUserAndBizTypes(userId,ListUtil.toArrayList(sysFavoritesAddForm.getBizType()));
        List<String> bizs = sysFavoritesAddForm.getBizs();

        SysFavoritesPo sysFavoritesPo = dozerMapper.map(sysFavoritesAddForm, SysFavoritesPo.class);
        sysFavoritesPo.setUserId(userId);
        sysFavoritesPo.setBizType(sysFavoritesAddForm.getBizType());
        sysFavoritesPo.setSort(mapper.selectNextSort());
        if (!ObjectUtils.isEmpty(bizs)) {
            String content = ListUtil.toStr(bizs);
            sysFavoritesPo.setContent(content);
        }
        super.insert(sysFavoritesPo);
    }


    @Override
    public void restFavorites(List<SysFavoritesAddForm> sysFavoritesAddFormList, Integer userId) {

        if(ObjectUtils.isEmpty(sysFavoritesAddFormList)){
            return;
        }

        List<String> bizTypes = ListUtil.toValueList(sysFavoritesAddFormList, "bizType", String.class);
        // 清空该类型的值
        this.deleteFavoritesByUserAndBizTypes(userId,bizTypes);
        //
        for(SysFavoritesAddForm sysFavoritesAddForm : sysFavoritesAddFormList){
            List<String> bizs = sysFavoritesAddForm.getBizs();
            SysFavoritesPo sysFavoritesPo = dozerMapper.map(sysFavoritesAddForm, SysFavoritesPo.class);
            sysFavoritesPo.setUserId(userId);
            sysFavoritesPo.setBizType(sysFavoritesAddForm.getBizType());
            sysFavoritesPo.setSort(mapper.selectNextSort());
            if (!ObjectUtils.isEmpty(bizs)) {
                String content = ListUtil.toStr(bizs);
                sysFavoritesPo.setContent(content);
            }
            super.insert(sysFavoritesPo);
        }
    }

    @Override
    public void deleteFavoritesByUserAndBizTypes(Integer userId, List<String> bizTypes) {
        if(!ObjectUtils.isEmpty(bizTypes)){
            Example example = new Example(SysFavoritesPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            criteria.andIn("bizType", bizTypes);

            this.deleteByExample(example);
        }
    }

    @Override
    public void deleteFavorites(String bizType, List<String> bizs, Integer userId){
        SysFavoritesDeleteForm favoritesDeleteForm = new SysFavoritesDeleteForm();
        favoritesDeleteForm.setBizs(bizs);
        favoritesDeleteForm.setBizType(bizType);
        this.deleteFavorites(favoritesDeleteForm,userId);
    }

    @Override
    public void deleteFavorites(SysFavoritesDeleteForm sysFavoritesDeleteForm, Integer userId) {
        SysFavoritesPo sysFavoritesPo = getFavoritesByUserIdAndBizType(userId,sysFavoritesDeleteForm.getBizType());
        List<String> bizs = sysFavoritesDeleteForm.getBizs();
        if(sysFavoritesPo != null){
            String content = sysFavoritesPo.getContent();
            String[] arr = null;
            if(!ObjectUtils.isEmpty(content)){
                arr = content.split(",");
            }
            List<String> bizlist = ListUtil.toArrayList(arr);
            List<String> bizlistNew = new ArrayList<>(bizlist);

            if(!ObjectUtils.isEmpty(bizs)){
                for(String biz : bizs){
                    if(!ObjectUtils.isEmpty(bizlist)){
                        if(bizlist.contains(biz)){
                            bizlistNew.remove(biz);
                        }
                    }

                }
            }
            if(!ObjectUtils.isEmpty(bizlistNew)&&bizlistNew.size()>0){
                String contenta = ListUtil.toStr(bizlistNew);
                sysFavoritesPo.setContent(contenta);
                super.updateById(sysFavoritesPo);
            }else{
                super.delete(sysFavoritesPo);
            }
        }
    }
}
