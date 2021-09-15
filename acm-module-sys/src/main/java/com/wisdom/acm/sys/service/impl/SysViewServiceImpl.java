package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.basisrc.ValidUtil;
import com.wisdom.acm.sys.form.SysFavoritesAddForm;
import com.wisdom.acm.sys.form.SysViewAddForm;
import com.wisdom.acm.sys.form.SysViewUpdateForm;
import com.wisdom.acm.sys.mapper.SysViewMapper;
import com.wisdom.acm.sys.po.SysViewPo;
import com.wisdom.acm.sys.service.SysFavoritesService;
import com.wisdom.acm.sys.service.SysViewService;
import com.wisdom.acm.sys.vo.SysViewTreeVo;
import com.wisdom.acm.sys.vo.SysViewVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysViewServiceImpl extends BaseService<SysViewMapper, SysViewPo> implements SysViewService {

    @Autowired
    private SysFavoritesService sysFavoritesService;

    /**
     * 加载视图
     * @param user
     * @param bizType
     * @return
     */
    @Override
    public List<SysViewVo> findViewByUser(UserInfo user, String bizType) {

        // 获取全局和个人视图
        List<SysViewVo> sysViewVos = this.mapper.findAllViewByUser(user.getId(), bizType);

        SysViewVo allView = new SysViewVo();
        //-1代表全部视图
        allView.setId(-1);
        allView.setViewName("默认视图");
        allView.setViewContent("");
        allView.setViewFields("");
        allView.setViewSortcols("");
        allView.setViewWidthcols("");
        allView.setViewType("");

        sysViewVos.add(0,allView);

        //获取收藏的视图id
        List<String> bizIds = sysFavoritesService.queryFavoritesContentByUserIdAndBizType(user.getId(),"view_"+bizType);
        String bizId = !ObjectUtils.isEmpty(bizIds) ? bizIds.get(0) : "-1";
        for (SysViewVo sysViewVo : sysViewVos){
            //如果收藏目标id和返回的视图的id相同
            if (bizId.equals(String.valueOf(sysViewVo.getId()))){
                //设置该视图为收藏（默认）
                sysViewVo.setDefaultView(1);
            }
        }
        return sysViewVos;
    }


    @Override
    public List<SysViewTreeVo> querySysViewTreeByUser(UserInfo user, String bizType){
        //返回视图树形
        List<SysViewTreeVo> sysViewTreeVos = new ArrayList<>();
        //所有的视图
        List<SysViewVo> allSysViewVoList = new ArrayList<>();

        //获取所有全局视图
        List<SysViewVo> globalViewVos = this.mapper.findGlobalViewByUser(user.getId(),bizType);
        SysViewVo allView = new SysViewVo();
        //-1代表全部视图
        allView.setId(-1);
        allView.setViewName("全部");
        allView.setViewContent("");
        allView.setViewFields("");
        allView.setViewSortcols("");
        allView.setViewWidthcols("");
        allView.setViewType("");
        //全部视图，放在第一位
        globalViewVos.add(0,allView);

        //获取所有个人视图
        List<SysViewVo> selfViewVos = this.mapper.findSelfViewByUser(user.getId(),bizType);

        //获取收藏的视图id
        List<String> bizIds = sysFavoritesService.queryFavoritesContentByUserIdAndBizType(user.getId(),"view_"+bizType);
        String bizId = !ObjectUtils.isEmpty(bizIds) ? bizIds.get(0) : "-1";
        //默认还没有已收藏视图
        boolean isDefault = false;
        //先循环全局视图,确认其中有没有收藏的视图
        for (SysViewVo sysViewVo : globalViewVos){
            //如果收藏目标id和返回的视图的id相同
            if (bizId.equals(String.valueOf(sysViewVo.getId()))){
                //设置该视图为收藏（默认）
                sysViewVo.setDefaultView(1);
                //视图已收藏
                isDefault = true;
            }
        }
        //全局没有收藏的视图时 --> 再循环遍历个人试图
        if (!ObjectUtils.isEmpty(selfViewVos) && !isDefault){
            for (SysViewVo sysViewVo : selfViewVos){
                //如果收藏目标id和返回的视图的id相同
                if (bizId.equals(String.valueOf(sysViewVo.getId()))){
                    //设置该视图为收藏（默认）
                    sysViewVo.setDefaultView(1);
                }
            }
        }

        SysViewTreeVo globalViewTreeVo = new SysViewTreeVo();
        globalViewTreeVo.setId(-3);
        globalViewTreeVo.setClassifyName("global");
        globalViewTreeVo.setChildren(globalViewVos);
        sysViewTreeVos.add(globalViewTreeVo);

        SysViewTreeVo selfViewTreeVo = new SysViewTreeVo();
        selfViewTreeVo.setId(-2);
        selfViewTreeVo.setClassifyName("self");
        if(!ObjectUtils.isEmpty(selfViewVos)){
            selfViewTreeVo.setChildren(selfViewVos);
        }
        sysViewTreeVos.add(selfViewTreeVo);

        return sysViewTreeVos;
    }

    /**
     * 设置默认视图
     * @param userId
     * @param viewId
     * @param bizObj
     */
    @Override
    public void setDefaultView(Integer userId, Integer viewId,String bizObj) {
        String bizType = "view_"+bizObj;
        String bizId = viewId + "";
        SysFavoritesAddForm form = new SysFavoritesAddForm();
        form.setBizType(bizType);
        form.setBizs(ListUtil.toArrayList(bizId));
        //收藏
        sysFavoritesService.restFavorites(form,userId);

    }
    /**
     * 把个人视图转变为全局视图
     * @param viewId
     */
    @Override
    public void setUserView2GlobalView(Integer viewId) {
        SysViewPo sysViewPo = this.selectById(viewId);
        sysViewPo.setUserId(null);
        sysViewPo.setDefaultView(0);
        this.updateById(sysViewPo);

    }

    /**
     * 保存视图数据
     * @param sysViewAddForm
     */
    @Override
    public SysViewPo saveView(SysViewAddForm sysViewAddForm) {
        SysViewPo sysViewPo = dozerMapper.map(sysViewAddForm, SysViewPo.class);
        //不是默认
        sysViewPo.setDefaultView(0);

        //新增生成sort_num
        Example example = new Example(SysViewPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bizType",sysViewAddForm.getBizType());
        criteria.andEqualTo("userId",sysViewAddForm.getUserId());
        Integer sortNum = this.selectNextSortByExample(example);
        sysViewPo.setSort(sortNum);

        this.insert(sysViewPo);
        return sysViewPo;
    }

    /**
     * 修改视图
     * @param sysViewUpdateForm
     * @return
     */
    @Override
    public SysViewPo updateView(SysViewUpdateForm sysViewUpdateForm) {

        SysViewPo sysViewPo = this.selectById(sysViewUpdateForm.getId());

        if (ObjectUtils.isEmpty(sysViewPo)) {
            throw new BaseException("该视图不存在!");
        }
        dozerMapper.map(sysViewUpdateForm, sysViewPo);
        this.updateById(sysViewPo);
        return sysViewPo;
    }

    /**
     * 修改视图名称
     * @param viewId
     * @param viewName
     * @return
     */
    @Override
    public SysViewPo updateViewName(Integer viewId, String viewName) {

        SysViewPo sysViewPo = this.selectById(viewId);

        if (!ObjectUtils.isEmpty(viewName)) {
            sysViewPo.setViewName(viewName);
        }

        this.updateById(sysViewPo);
        return sysViewPo;
    }

    @Override
    public void deleteViews(List<Integer> ids){
       this.deleteByIds(ids);
    }

    @Override
    public SysViewVo getSysViewVoById(Integer viewId){
        SysViewVo sysViewVo = this.mapper.selectViewById(viewId);
        //获取收藏的视图id
        List<String> bizIds = sysFavoritesService.queryFavoritesContentByUserIdAndBizType(sysViewVo.getUserId(),"view_"+sysViewVo.getBizType());
        String bizId = !ObjectUtils.isEmpty(bizIds) ? bizIds.get(0) : "-1";
        //如果收藏目标id和返回的视图的id相同
        if (bizId.equals(String.valueOf(sysViewVo.getId()))){
            //设置该视图为收藏（默认）
            sysViewVo.setDefaultView(1);
        }
        return sysViewVo;
    }
}
