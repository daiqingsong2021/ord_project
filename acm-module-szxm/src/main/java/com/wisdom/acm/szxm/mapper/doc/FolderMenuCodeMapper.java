package com.wisdom.acm.szxm.mapper.doc;

import com.wisdom.acm.szxm.po.doc.DocFolderMenuIdPo;
import com.wisdom.acm.szxm.vo.doc.MenuFolderVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: szc
 * @Date: 2019/7/18 14:04
 * @Version 1.0
 */
public interface FolderMenuCodeMapper extends CommMapper<DocFolderMenuIdPo> {

    List<MenuFolderVo> queryMenuInfoByFolderIds(@Param("folderIds") List<Integer> folderIds);

    List<MenuFolderVo> selectProjectFolderByMenuIds(@Param("projectId") Integer projectId, @Param("menuIds")  List<Integer> menuIds);
}
