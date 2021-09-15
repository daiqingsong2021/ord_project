package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysSearchIptForm;
import com.wisdom.acm.sys.po.SysIptPo;
import com.wisdom.acm.sys.po.SysUserIptPo;
import com.wisdom.acm.sys.vo.SysIptInfoVo;
import com.wisdom.acm.sys.vo.SysIptVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface IptMapper extends CommMapper<SysIptPo> {

    /**
     * 搜索ipt列表
     * @param searchMap
     * @return
     */
    List<Integer> selectIptsBySearch(@Param("search") SysSearchIptForm searchMap);

    /**
     * 获取ipt列表
     * @param iptId
     * @return
     */
    List<SysIptVo> selectIptsAllByPid(@Param("parentId") Integer iptId);

    void addIptList(@Param("iptList") List<SysIptPo> sysIptPos);

    List<SysIptPo> selectAddIptsFromOrg();

    void updateIptImportOver();

    void updateIptImportTopIpt(@Param("topOrgId") int topOrgId,@Param("iptId") int iptId);

    void addIptUserRelations(@Param("iptUsers") List<SysUserIptPo> sysIptUserPos);

    void updateIptRelationFromOrg(@Param("iptId") int iptId,@Param("parentId") int parentId);

    /**
     * 获取ipt信息
     * @param iptId
     * @return
     */
    SysIptInfoVo selectIptById(@Param("iptId") Integer iptId);

    /**
     * 获取多个ipt信息
     * @param iptIds
     * @return
     */
    List<SysIptInfoVo> selectIptsByIds(@Param("iptIds") List<Integer> iptIds);

    /**
     * 获取ipt清单
     * @return
     */
    List<SysIptVo> selectIptList();
}
