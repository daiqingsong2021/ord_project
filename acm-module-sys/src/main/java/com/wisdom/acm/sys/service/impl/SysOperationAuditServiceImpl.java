package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysSearchAuditForm;
import com.wisdom.acm.sys.mapper.OperationAuditMapper;
import com.wisdom.acm.sys.po.SysOperationAuditPo;
import com.wisdom.acm.sys.service.SysOperationAuditService;
import com.wisdom.acm.sys.vo.SysOperationAuditVo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class SysOperationAuditServiceImpl extends BaseService<OperationAuditMapper, SysOperationAuditPo> implements SysOperationAuditService {

    /**
     * 获取操作审计列表
     * @param searchMap
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<SysOperationAuditVo> queryOperationAudit(SysSearchAuditForm searchMap, Integer pageSize, Integer currentPageNum) {
        //获取日期查询条件
        Integer flag = (Integer) searchMap.getThisTime();
        //本年
        if (flag.equals(1)){
            String thisStartTime = DateUtil.getYearStart();
            String thisEndTime = DateUtil.getYearEnd();
            searchMap.setThisStartTime(thisStartTime);
            searchMap.setThisEndTime(thisEndTime);
        }
        //本月
        if (flag.equals(2)){
            String thisStartTime = DateUtil.getMonthStart();
            String thisEndTime = DateUtil.getMonthEnd();
            searchMap.setThisStartTime(thisStartTime);
            searchMap.setThisEndTime(thisEndTime);
        }
        //本周
        if (flag.equals(3)){
            String thisStartTime = DateUtil.getWeekStart();
            String thisEndTime = DateUtil.getWeekEnd();
            searchMap.setThisStartTime(thisStartTime);
            searchMap.setThisEndTime(thisEndTime);
        }
        //本日
        if (flag.equals(4)){
            String thisStartTime = DateUtil.getToday()+" 00:00:00 ";
            String thisEndTime = DateUtil.getToday()+" 23:59:59";
            searchMap.setThisStartTime(thisStartTime);
            searchMap.setThisEndTime(thisEndTime);
        }
        PageHelper.startPage(currentPageNum,pageSize);
        List<SysOperationAuditVo> list = mapper.selectOperationAudit(searchMap);
        for (SysOperationAuditVo sysOperationAuditVo : list ){
            if (!ObjectUtils.isEmpty(sysOperationAuditVo.getUserName())) {
                StringBuffer opuser = new StringBuffer();
                opuser.append(sysOperationAuditVo.getUserName());
                opuser.append("(" + sysOperationAuditVo.getActuName() + ")");
                sysOperationAuditVo.setOperationUser(String.valueOf(opuser));
            }else{
                sysOperationAuditVo.setOperationUser("");
            }
        }
        PageInfo<SysOperationAuditVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
