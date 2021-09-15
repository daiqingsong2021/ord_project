package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.TsPlatAddForm;
import com.wisdom.acm.szxm.form.rygl.TsPlatUpdateForm;
import com.wisdom.acm.szxm.po.rygl.TsPlatPo;
import com.wisdom.acm.szxm.vo.rygl.TsPlatVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TsPlatService extends CommService<TsPlatPo>
{

    /**
     * 查询调试平台信息
     * @param projInfoId 项目基础信息ID
     * @param pageSize 一页多少
     * @param currentPageNum 最近多少页
     * @return
     */
    PageInfo<TsPlatVo> selectTsPlatList(Integer projInfoId, Integer pageSize, Integer currentPageNum);

    /**
     * 增加调试平台
     * @param tsPlatAddForm
     * @return
     */
    TsPlatVo addTsPlat(TsPlatAddForm tsPlatAddForm);

    /**
     * 修改仓库
     * @param tsPlatUpdateForm
     * @return
     */
    TsPlatVo updateTsPlat(TsPlatUpdateForm tsPlatUpdateForm);

    /**
     * 删除仓库
     * @param ids
     */
    void deleteTsPlat(List<Integer> ids);

    String uploadTsPlatFile(MultipartFile file, Map<String, Object> paramMap);
}
