package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysIPAccessPo;
import com.wisdom.acm.sys.vo.SysIPAccessVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IPAccRuleMapper extends CommMapper<SysIPAccessPo> {
    /**
     * 查询所有访问设置
     * @return
     */
    List<SysIPAccessVo> selectIPAccessAll();

    /**
     * 查询一条视图
     * @param IPAccId
     * @return
     */
    SysIPAccessVo getOne(Integer IPAccId);

    List<SysIPAccessVo> selectIPAccessByStartIpAndEndIp(@Param("startIp") String startIP,@Param("endIp") String endIP);

    List<SysIPAccessVo> selectIPAccByIsEffect();
}
