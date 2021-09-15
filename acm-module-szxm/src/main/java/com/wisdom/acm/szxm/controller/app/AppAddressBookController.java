package com.wisdom.acm.szxm.controller.app;

import com.wisdom.acm.szxm.common.PinyinUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.form.app.CommonlyUsedUserAddForm;
import com.wisdom.acm.szxm.service.app.AppAddressBookService;
import com.wisdom.acm.szxm.vo.app.ContactsUserVo;
import com.wisdom.acm.szxm.vo.app.OrgInfoVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 *通讯录控制器
 */
@RestController
@RequestMapping("app/addressBook")
public class AppAddressBookController
{

    @Autowired
    private AppAddressBookService appAddressBookService;

    @Autowired
    private LeafService leafService;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private HttpServletRequest request;
    /**
     * 增加常用联系人
     * @return
     */
    @PostMapping(value = "/addCommonlyUsedUser")
    public ApiResult addCommonlyUsedUser(@RequestBody @Valid CommonlyUsedUserAddForm commonlyUsedUserAddForm)
    {
        UserInfo userInfo=commUserService.getLoginUser();
        commonlyUsedUserAddForm.setUserId(userInfo.getId());
        commonlyUsedUserAddForm.setId(leafService.getId());
        commonlyUsedUserAddForm.setJob(StringHelper.formattString(commonlyUsedUserAddForm.getJob()));
        commonlyUsedUserAddForm.setTelPhone(StringHelper.formattString(commonlyUsedUserAddForm.getTelPhone()));
        commonlyUsedUserAddForm.setSex(StringHelper.formattString(commonlyUsedUserAddForm.getSex()));
        commonlyUsedUserAddForm.setContactsDept(StringHelper.formattString(commonlyUsedUserAddForm.getContactsDept()));
        commonlyUsedUserAddForm.setContactsName(StringHelper.formattString(commonlyUsedUserAddForm.getContactsName()));
        appAddressBookService.mergeIntoCommonUserTable(commonlyUsedUserAddForm);
        return ApiResult.success();
    }
    /**
     * 查询常用联系人 ，前10条
     * @return
     */
    @GetMapping(value = "/queryCommonlyUsedUser")
    public ApiResult queryCommonlyUsedUser()
    {
        UserInfo userInfo=commUserService.getLoginUser();
        List<ContactsUserVo> contactsUserVoList= appAddressBookService.queryCommonlyUsedUser(userInfo.getId());
        for(ContactsUserVo contactsUserVo:contactsUserVoList)
        {
            contactsUserVo.setNamePinYin(PinyinUtil.getQuanPin(contactsUserVo.getContactsName()).toString());
        }

        //contactsUserVoList 按照姓名拼音排序
        Collections.sort(contactsUserVoList,new Comparator<ContactsUserVo>(){
            @Override public int compare(ContactsUserVo o1, ContactsUserVo o2)
            {
                return o1.getNamePinYin().compareTo(o2.getNamePinYin());
            }
        } );
        return ApiResult.success(contactsUserVoList);
    }
    /**
     * 查询内部通讯录单位人员
     * @param mapWhere 项目ID，联系人姓名
     * @return
     */
    @GetMapping(value = "/queryInnerPeople")
    public ApiResult queryInnerPeople(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        if(StringHelper.isNullAndEmpty(projectId))
        {
            throw new BaseException("项目ID不能为空!");
        }
        List<ContactsUserVo> contactsUserVoList=appAddressBookService.queryInnerPeople(mapWhere);
        for(ContactsUserVo contactsUserVo:contactsUserVoList)
        {
            contactsUserVo.setNamePinYin(PinyinUtil.getQuanPin(contactsUserVo.getContactsName()).toString());
        }
        //contactsUserVoList 按照姓名拼音排序
        Collections.sort(contactsUserVoList,new Comparator<ContactsUserVo>(){
            @Override public int compare(ContactsUserVo o1, ContactsUserVo o2)
            {
                return o1.getNamePinYin().compareTo(o2.getNamePinYin());
            }
        } );
        return ApiResult.success(contactsUserVoList);
    }
    /**
     * 查询外部通讯录单位组织机构
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/queryOuterOrg")
    public ApiResult queryOuterOrg(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        if(StringHelper.isNullAndEmpty(projectId))
        {
            throw new BaseException("项目ID不能为空!");
        }
        List<OrgInfoVo> orgInfoVoList=appAddressBookService.queryOuterOrg(mapWhere);
        return ApiResult.success(orgInfoVoList);
    }
    /**
     * 查询外部通讯录 根据组织ID查人员
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/queryOuterUserByOrgId")
    public ApiResult queryOuterUserByOrgId(@RequestParam Map<String, Object> mapWhere)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        if(StringHelper.isNullAndEmpty(projectId))
        {
            throw new BaseException("项目ID不能为空!");
        }
        String orgId = request.getParameter("orgId");//获取项目ID
        if(StringHelper.isNullAndEmpty(projectId))
        {
            throw new BaseException("组织ID不能为空!");
        }
        List<ContactsUserVo> contactsUserVoList=appAddressBookService.queryOuterUserByOrgId(mapWhere);
        for(ContactsUserVo contactsUserVo:contactsUserVoList)
        {
            contactsUserVo.setNamePinYin(PinyinUtil.getQuanPin(contactsUserVo.getContactsName()).toString());
        }

        //contactsUserVoList 按照姓名拼音排序
        Collections.sort(contactsUserVoList,new Comparator<ContactsUserVo>(){
            @Override public int compare(ContactsUserVo o1, ContactsUserVo o2)
            {
                return o1.getNamePinYin().compareTo(o2.getNamePinYin());
            }
        } );
        return ApiResult.success(contactsUserVoList);
    }

    @GetMapping(value = "/queryOnePeople/{projectId}/{searcher}")
    public ApiResult queryOnePeople(@PathVariable("searcher") String searcher,@PathVariable("projectId") Integer projectId)
    {
        if(ObjectUtils.isEmpty(projectId))
            throw new BaseException("项目ID不能为空");
          if(StringHelper.isNullAndEmpty(searcher))
              throw new BaseException("查询条件不能为空");
        List<ContactsUserVo> contactsUserVoList= appAddressBookService.queryOnePeople(StringHelper.formattString(searcher),projectId);
        for(ContactsUserVo contactsUserVo:contactsUserVoList)
        {
            contactsUserVo.setNamePinYin(PinyinUtil.getQuanPin(contactsUserVo.getContactsName()).toString());
        }
        return  ApiResult.success(contactsUserVoList);
    }
}
