package com.wisdom.acm.szxm.mapper.pfe;


import com.wisdom.acm.szxm.po.pfe.StationPo;
import com.wisdom.acm.szxm.vo.pfe.StationVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 站点数据访问
 */
public interface StationMapper extends CommMapper<StationPo>{

    /**
     * 查询站点
     * @param projectId 项目ID
     * @return 站点List<StationVo>
     */
    List<StationVo> selectByProjectId(@Param("projectId") Integer projectId);


    /**
     * 判断是否存在code（新增时用）
     * @param code
     * @return
     */
    Integer selectOneByCode(@Param("code") String code,@Param("projectId") Integer projectId);

    /**
     * 判断是否存在code（更新时用）
     * @param code
     * @param id
     * @return
     */
    Integer selectOneByCodeAndId(@Param("code") String code,@Param("id") Integer id,@Param("projectId") Integer projectId);
}
