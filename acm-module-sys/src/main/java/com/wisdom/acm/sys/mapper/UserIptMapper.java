package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysIptUserSearchForm;
import com.wisdom.acm.sys.po.SysUserIptPo;
import com.wisdom.acm.sys.vo.SysIptUserVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserIptMapper extends CommMapper<SysUserIptPo> {


    Integer deleteUserByIptId(@Param("iptIds") List<Integer> iptId);

    /**
     * 获取useript关系
     * @param iptIds
     * @return
     */
    List<SysUserIptPo> queryUserIptRelation(@Param("iptIds") List<Integer> iptIds);

    SysUserIptPo selectUserIptRelationExist(@Param("userId") Integer userId,@Param("iptId") Integer iptId);

    List<SysIptUserVo> selectUserByIptId(@Param("searchForm") SysIptUserSearchForm sysIptUserSearchForm,@Param("iptId") Integer iptId);

    void deleteIptUserByUserIdAndIptId(@Param("userIds") List<Integer> userIds,@Param("iptId") Integer iptId);

    void deleteUserIptRelationByUserIds(@Param("userIds") List<Integer> userIds);
}
