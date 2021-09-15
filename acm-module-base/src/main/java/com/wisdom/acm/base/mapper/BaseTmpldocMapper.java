package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseTmpldocPo;
import com.wisdom.acm.base.vo.tmpldoc.BaseTmpldocVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseTmpldocMapper extends CommMapper<BaseTmpldocPo> {

    /**
     * 文档模板分页列表
     * @return
     */
    public List<BaseTmpldocVo> selectTmplDocList(@Param("key") String key);

    /**
     * 文档模板基本信息
     * @return
     */
    public BaseTmpldocVo selectTmplDocById(Integer tmplDocId);


}