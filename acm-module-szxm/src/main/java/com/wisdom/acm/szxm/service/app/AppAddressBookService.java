package com.wisdom.acm.szxm.service.app;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.app.CommonlyUsedUserAddForm;
import com.wisdom.acm.szxm.vo.app.ContactsUserVo;
import com.wisdom.acm.szxm.vo.app.OrgInfoVo;

import java.util.List;
import java.util.Map;

public interface AppAddressBookService
{
    /**
     * 插入常用联系人表
     * @param
     */
    public void mergeIntoCommonUserTable(CommonlyUsedUserAddForm commonlyUsedUserAddForm);

    /**
     * 查询常用联系人
     * @param id
     * @return
     */
    List<ContactsUserVo> queryCommonlyUsedUser(Integer id);
    /**
     * 查询内部通讯录人员
     * @param mapWhere
     * @return
     */
    List<ContactsUserVo> queryInnerPeople(Map<String, Object> mapWhere);

    /**
     * 查询外部通讯录 单位信息
     * @param mapWhere
     * @return
     */
    List<OrgInfoVo> queryOuterOrg(Map<String, Object> mapWhere);

    /**
     * 外部通讯录 根据orgId 查询通讯录人员
     * @param mapWhere
     * @return
     */
    List<ContactsUserVo> queryOuterUserByOrgId(Map<String, Object> mapWhere);

    /**
     * 查询联系人
     * @param searcher
     * @return
     */
    List<ContactsUserVo> queryOnePeople(String searcher,Integer projectId);
}
