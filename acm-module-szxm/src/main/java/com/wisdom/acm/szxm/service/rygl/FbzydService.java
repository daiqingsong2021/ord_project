package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.FbzydAddForm;
import com.wisdom.acm.szxm.form.rygl.FbzydUpdateForm;
import com.wisdom.acm.szxm.po.rygl.FbzydPo;
import com.wisdom.acm.szxm.vo.rygl.FbzydVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FbzydService extends CommService<FbzydPo>
{

    /**
     * 查询分包作业队信息
     * @param projInfoId 项目基础信息ID
     * @param pageSize 一页多少
     * @param currentPageNum 最近多少页
     * @return
     */
    PageInfo<FbzydVo> selectFbzydList(Integer projInfoId, Integer pageSize, Integer currentPageNum);


    /**
     * 增加分包作业队
     * @param fbzydAddForm
     * @return
     */
    FbzydVo addFbzyd(FbzydAddForm fbzydAddForm);

    /**
     * 修改分包作业队
     * @param fbzydUpdateForm
     * @return
     */
    FbzydVo updateFbzyd(FbzydUpdateForm fbzydUpdateForm);

    /**
     * 删除仓库
     * @param ids
     */
    void deleteFbzyd(List<Integer> ids);

    String uploadFbzydFile(MultipartFile file, Map<String, Object> paramMap);

    /**
     * 查询分包作业队信息
     * @param
     * @param pageSize 一页多少
     * @param currentPageNum 最近多少页
     * @return
     */
    PageInfo<FbzydVo> selectSectionWorkteamList(Map<String, Object> paramMap, Integer pageSize, Integer currentPageNum);

}
