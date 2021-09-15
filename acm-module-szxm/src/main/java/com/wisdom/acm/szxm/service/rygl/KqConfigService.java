package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.*;
import com.wisdom.acm.szxm.po.rygl.KqConfigPo;
import com.wisdom.acm.szxm.vo.rygl.KqConfigVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface KqConfigService extends CommService<KqConfigPo>
{

    PageInfo<KqConfigVo> selectKqConfigList(Map<String, Object> mapWhere, List<String> sectionList,
            Integer pageSize, Integer currentPageNum);

    KqConfigVo addKqConfig(KqConfigAddForm kqConfigAddForm);

    void deleteKqConfig(List<Integer> ids);

    KqConfigVo updateKqConfig(KqConfigUpdateForm kqConfigUpdateForm);

    KqConfigVo selectByKqConfigId(Integer id);

    KqConfigVo saveAllKqConfig(AllKqConfigForm allKqConfigForm);

    KqConfigVo getAllKqConfig();
}
