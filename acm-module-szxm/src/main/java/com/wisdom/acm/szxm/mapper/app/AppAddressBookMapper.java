package com.wisdom.acm.szxm.mapper.app;

import com.wisdom.acm.szxm.form.app.CommonlyUsedUserAddForm;
import com.wisdom.acm.szxm.vo.app.ContactsUserVo;
import com.wisdom.acm.szxm.vo.app.OrgInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppAddressBookMapper
{
    /**
     * 增加 or 更新常用联系人
     * @param commonlyUsedUserAddForm
     */
    void mergeIntoCommonUserTable(CommonlyUsedUserAddForm commonlyUsedUserAddForm) ;

    /**
     * 查询常用联系人
     * @param userId
     * @return
     */
    List<ContactsUserVo> queryCommonlyUsedUser(@Param("userId") Integer userId);

    /**
     * 查询外部单位 内部通讯录人员
     * @param mapWhere
     * @return
     */
    List<ContactsUserVo> selectWbInnerPeople(Map<String,Object> mapWhere);

    /**
     * 查询业主 内部通讯录人员
     * @param mapWhere
     * @return
     */
    List<ContactsUserVo> selectYzInnerPeople(Map<String,Object> mapWhere);

    /**
     * 查询 外部单位 外部通讯录组织信息
     * @param mapWhere
     * @return
     */
    List<OrgInfoVo> selectWbOuterOrg(Map<String, Object> mapWhere);

    /**
     * 查询这个人所属标段的专业
     * @return
     */
    String selectSectionProfession(@Param("userId") Integer userId,@Param("projectId") Integer projectId);

    /**
     * 非业主查询人员列表
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    List<ContactsUserVo> selectFyzUnionAll(@Param("projectId")Integer projectId, @Param("userId") Integer userId,@Param("searcher") String searcher);

    /**
     * 业主查询人员列表
     * @param projectId
     * @param searcher
     * @return
     */
    List<ContactsUserVo> selectYzUnionAll(@Param("projectId") Integer projectId, @Param("searcher")  String searcher);
}
