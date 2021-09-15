package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysViewPo;
import com.wisdom.acm.sys.vo.SysViewVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysViewMapper extends CommMapper<SysViewPo> {


/*        List<SysViewVo> findGlobalDefaultView(@Param("bizType") String bizType);


        List<SysViewVo> findUserDefaultView(@Param("userId") Integer userId,@Param("bizType") String bizType);*/

        /**
         * 查询视图
         *
         * @param userId
         * @param bizType
         * @return
         */
        List<SysViewVo> findAllViewByUser(@Param("userId") Integer userId,@Param("bizType") String bizType);

        /**
         * 查询全局视图
         *
         * @param userId
         * @param bizType
         * @return
         */
        List<SysViewVo> findGlobalViewByUser(@Param("userId") Integer userId,@Param("bizType") String bizType);

        /**
         * 查询全局视图
         *
         * @param userId
         * @param bizType
         * @return
         */
        List<SysViewVo> findSelfViewByUser(@Param("userId") Integer userId,@Param("bizType") String bizType);

        /**
         * 根据id获取视图数据
         * @param viewId
         * @return
         */
        SysViewVo selectViewById(@Param("viewId") Integer viewId);


}
