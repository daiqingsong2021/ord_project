package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysFavoritesAddForm;
import com.wisdom.acm.sys.form.SysFavoritesDeleteForm;
import com.wisdom.acm.sys.po.SysFavoritesPo;
import com.wisdom.acm.sys.vo.SysFavoritesVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface SysFavoritesService extends CommService<SysFavoritesPo> {
    /**
     * 根据用户获取用户所有收藏信息
     *
     * @param userName
     * @return
     */
    List<SysFavoritesVo> queryFavoritesByUserId(String userName);

    /**
     * 新增收藏
     *
     * @param sysFavoritesAddForm
     * @param userId
     */
    void addFavorites(SysFavoritesAddForm sysFavoritesAddForm, Integer userId);

    /**
     * 重置收藏
     *
     * @param sysFavoritesAddForm
     * @param userId
     */
    void restFavorites(SysFavoritesAddForm sysFavoritesAddForm, Integer userId);

    /**
     * 批量重置收藏
     *
     * @param sysFavoritesAddFormList
     * @param userId
     */
    void restFavorites(List<SysFavoritesAddForm> sysFavoritesAddFormList, Integer userId);

    /**
     * 根据用户和类型删除收藏（清空）
     *
     * @param userId
     * @param bizTypes
     */
    void deleteFavoritesByUserAndBizTypes(Integer userId, List<String> bizTypes);

    /**
     * 取消收藏
     *
     * @param bizType
     * @param bizs
     * @param userId
     */
    void deleteFavorites(String bizType, List<String> bizs, Integer userId);

    /**
     * 删除收藏
     *
     * @param sysFavoritesDeleteForm
     * @param userId
     */
    void deleteFavorites(SysFavoritesDeleteForm sysFavoritesDeleteForm, Integer userId);

    /**
     * 根据用户和类型获取收藏信息
     *
     * @param userId
     * @param bizType
     * @return
     */
    SysFavoritesPo getFavoritesByUserIdAndBizType(Integer userId,String bizType);

    /**
     * 根据用户和类型读取收藏内容
     *
     * @param userId
     * @param bizType
     * @return
     */
    List<String> queryFavoritesContentByUserIdAndBizType(Integer userId,String bizType);

    /**
     *
     * @param userId
     * @param bizTypes
     * @return
     */
    List<SysFavoritesPo> queryFavoritesPoByUserIdAndBizType(Integer userId, List<String> bizTypes);

    /**
     * 根据用户和类型读取收藏内容
     *
     * @param userId
     * @param bizTypes
     * @return
     */
    Map<String,List<String>> queryFavoritesContentByUserIdAndBizType(Integer userId, List<String> bizTypes);
}
