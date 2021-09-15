package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.po.SysOrgPo;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.SysOrgInfoVo;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface CommOrgService {

    /**
     *根据用户ID获取用户主组织对象
     * @param userId
     * @return
     */
    default SysOrgInfoVo getUserOrgInfo(Integer userId){
        ApiResult<SysOrgInfoVo> apiResult = this.getUserOrgInfo_(userId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     *
     * @param orgIds
     * @return
     */
    default Map<Integer, OrgVo> getOrgVoMapByOrgs(List<Integer> orgIds){
        ApiResult<Map<Integer, OrgVo>> apiResult = this.getOrgVoByOrgs(orgIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     *
     * @param orgId
     * @return
     */
    default OrgVo getOrgVoByOrgId(Integer orgId){
        ApiResult<Map<Integer, OrgVo>> apiResult = this.getOrgVoByOrgs(ListUtil.toArrayList(orgId));
        if(apiResult.getStatus() == 200){
            return apiResult.getData().get(orgId);
        }
        return null;
    }

    /**
     *
     * @param orgIds
     * @return
     */
    default List<SysOrgPo> queryOrgPosByOrgIds(List<Integer> orgIds){
        ApiResult<List<SysOrgPo>> apiResult = this.queryOrgPosByIds(orgIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     *
     * @param userId
     * @return
     */
    default Integer getMainOrg(Integer userId){
        ApiResult<SysOrgInfoVo> apiResult = this.getMainOrgByUserId(userId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData().getId();
        }
        return null;
    }

    /**
     * 根据用户ID获取用户主组织对象
     * @param userId
     * @return
     */
    @RequestMapping(value = "/org/{userId}/orgInfo", method = RequestMethod.GET)
    ApiResult<SysOrgInfoVo> getUserOrgInfo_(@PathVariable("userId") Integer userId);

    /**
     * 根据组织id获取组织对象
     *
     * @param orgIds
     * @return
     */
    @RequestMapping(value = "/org/orgvo/maps", method = RequestMethod.POST)
    ApiResult<Map<Integer, OrgVo>> getOrgVoByOrgs(@RequestBody List<Integer> orgIds);

    /**
     *  根据orgids获取所有组织
     */
    @RequestMapping(value = "/org/orgids/list", method = RequestMethod.POST)
    ApiResult<List<SysOrgInfoVo>> queryOrgList(@RequestBody List<Integer> orgIds);

    /**
     *  根据orgids获取所有组织
     */
    @RequestMapping(value = "/org/orgpo/list", method = RequestMethod.POST)
    ApiResult<List<SysOrgPo>> queryOrgPosByIds(@RequestBody List<Integer> orgIds);

    /**
     *  根据userId获取主部门id
     */
    @RequestMapping(value = "/org/{userId}/mainorg", method = RequestMethod.GET)
    ApiResult<SysOrgInfoVo> getMainOrgByUserId(@PathVariable("userId") Integer userId);

}
