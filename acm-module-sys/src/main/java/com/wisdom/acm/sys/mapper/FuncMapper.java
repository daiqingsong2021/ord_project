package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysFuncPo;
import com.wisdom.acm.sys.vo.SysFuncVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface FuncMapper extends CommMapper<SysFuncPo> {

    /**
     * 获取功能信息
     * @param id
     * @return
     */
    SysFuncPo selectFuncPoById(@Param("funcId") Integer id);

    /**
     * 获取权限配置信息
     * @param funcId
     * @return
     */
    SysFuncVo selectFuncVoOneById(@Param("funcId")Integer funcId);


    /**
     * 获取权限配置列表
     * @param menuId
     * @return
     */
    List<SysFuncVo> selectFuncVoById(@Param("menuId") Integer menuId);


    void deleteFuncByMenuIds(@Param("menuIds") List<Integer> menuIds);

    List<SysFuncVo> selectFuncByIds(@Param("funcIds") List<Integer> funcIds);

    List<SysFuncVo> selectFuncVoByFuncId(@Param("funcId") Integer funcId);


    List<SysFuncPo> selectByFuncCodes(@Param("funcCodes")  List<String> funcCodes);
}
