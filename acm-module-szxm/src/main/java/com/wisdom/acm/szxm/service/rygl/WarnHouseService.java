package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.WarnHouseAddForm;
import com.wisdom.acm.szxm.form.rygl.WarnHouseUpdateForm;
import com.wisdom.acm.szxm.po.rygl.WarnHousePo;
import com.wisdom.acm.szxm.vo.rygl.WarnHouseVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface WarnHouseService extends CommService<WarnHousePo>
{

    /**
     * 查询仓库信息
     * @param projInfoId 项目基础信息ID
     * @param pageSize 一页多少
     * @param currentPageNum 最近多少页
     * @return
     */
    PageInfo<WarnHouseVo> selectWarnHouseList(Integer projInfoId, Integer pageSize, Integer currentPageNum);

    /**
     * 查询仓库信息
     * @return
     */
    List<WarnHouseVo> selectWarnHouseListByProjId(Map<String, Object> mapWhere);


    /**
     * 增加仓库
     * @param warnHouseAddForm
     * @return
     */
    WarnHouseVo addWarnHouse(WarnHouseAddForm warnHouseAddForm);

    /**
     * 修改仓库
     * @param warnHouseUpdateForm
     * @return
     */
    WarnHouseVo updateWarnHouse(WarnHouseUpdateForm warnHouseUpdateForm);

    /**
     * 删除人员
     * @param ids
     */
    void deleteWarnHouse(List<Integer> ids);

    String uploadWarnHouseFile(MultipartFile file, Map<String, Object> paramMap);
}
