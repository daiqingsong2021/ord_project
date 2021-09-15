package com.wisdom.acm.sys.service.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.sys.basisrc.DateUtils;
import com.wisdom.acm.sys.form.SysOrgAddForm;
import com.wisdom.acm.sys.form.SysOrgUpdateForm;
import com.wisdom.acm.sys.form.SysUserAddFrom;
import com.wisdom.acm.sys.form.SysUserUpdateFrom;
import com.wisdom.acm.sys.mapper.UserMapper;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.service.SysOrgService;
import com.wisdom.acm.sys.service.SysUserService;
import com.wisdom.acm.sys.service.UUVService;
import com.wisdom.acm.sys.vo.ADDepartmentInfoVo;
import com.wisdom.acm.sys.vo.ADDepartmentVo;
import com.wisdom.acm.sys.vo.SysOrgVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.webservice.DateUtil;
import com.wisdom.webservice.internalstaff.InstaffServiceUtils;
import com.wisdom.webservice.internalstaff.entity.ADDepartment;
import com.wisdom.webservice.internalstaff.entity.ADUser;
import com.wisdom.webservice.outorg.OutOrgUtils;
import com.wisdom.webservice.outorg.entity.ADDepartmentInfo;
import com.wisdom.webservice.outstaff.OutstaffServiceUtils;
import com.wisdom.webservice.outstaff.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-09-16 15:24
 * Description：<描述>
 */
@Service("uuvService")
public class UUVServiceImpl extends BaseService<UserMapper, SysUserPo> implements UUVService {
    private static Logger logger = LoggerFactory.getLogger(UUVServiceImpl.class);

    @Autowired
    private SysOrgService orgService;

    @Autowired
    private SysUserService userServce;

    @Autowired
    private UUVServiceUtils uuvServiceUtils;

    @Autowired(required = false)
    private CommDictService commDictService;

    private final String STATUS_ENABLED = "enabled";
    private final String STATUS_DISABLED = "disabled";
    private final String SEX_MAN = "True";

    /**
     * 获取本地组织结构
     *
     * @return
     */
    private List<SysOrgVo> getLocalOrg() {
        List<SysOrgVo> sysOrgVos = orgService.queryOrgTree();

        if (ObjectUtils.isEmpty(sysOrgVos)) {
            logger.info("No organization was queried");
            return null;
        }
        List<SysOrgVo> orgVoList = new ArrayList<>();
        for (SysOrgVo orgVo : sysOrgVos) {
            uuvServiceUtils.getChildren(orgVo, orgVoList);
        }
        return orgVoList;
    }

    @Override
    @Async
    @AddLog(title = "同步UUV组织", module = LoggerModuleEnum.SM_ORG, initContent = true)
    public ApiResult syncUUVOrg(String startUpdateTime, String endUpdateTime) {
        Date startTime = new Date();
        logger.info("同步uuv组织开始执行，开始时间：{}", startTime);

        try {
            ApiResult<String> innerResult = tbInnerOrg();
            ApiResult<String> outResult = tbOutOrg(startUpdateTime, endUpdateTime);
            super.setAcmLogger(new AcmLogger("同步内部组织结果，" + innerResult.getData() + "；同步外部组织结果，" + outResult.getData()));
        } catch (Exception e) {
            super.setAcmLogger(new AcmLogger("同步uuv组织异常"));
            logger.info("同步uuv组织异常,原因为：", e);
        }

        Date endTime = new Date();
        long difTime = (endTime.getTime() - startTime.getTime()) / 1000 + 1;
        logger.info("同步uuv组织结束，结束时间：{}, 同步组织耗时：{}秒", endTime, difTime);
        return ApiResult.success();
    }

    @Override
    @Async
    // 通过@Async注解方法表明这个方法是一个异步方法，如果注解在类级别，则表名该类的所有方法都是异步的，
    // 而这里的方法自动被注入使用ThreadPoolTaskExecutor作为TaskExecutor
    @AddLog(title = "同步UUV人员", module = LoggerModuleEnum.SM_USER, initContent = true)
    public ApiResult syncUUVUser(String startUpdateTime, String endUpdateTime) {
        Date startTime = new Date();
        logger.info("同步uuv人员开始执行，开始时间：{}", startTime);
        try {
            ApiResult<String> innerResult = tbInnerUser();
            ApiResult<String> outResult = tbOutUser(startUpdateTime, endUpdateTime);
            super.setAcmLogger(new AcmLogger("同步内部人员结果，" + innerResult.getData() + "；同步外部人员结果，" + outResult.getData()));
        } catch (Exception e) {
            super.setAcmLogger(new AcmLogger("同步uuv人员异常"));
            logger.info("同步uuv人员异常,原因为：", e);
        }

        Date endTime = new Date();
        long difTime = (endTime.getTime() - startTime.getTime()) / 1000 + 1;
        logger.info("同步uuv人员结束，结束时间：{}， 同步人员耗时：{}秒", endTime, difTime);
        return ApiResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @AddLog(title = "同步UUV定时任务", module = LoggerModuleEnum.SM_ORG, initContent = true)
    public void tbUuvTask() {
        Date startTime = new Date();
        logger.info("定时任务 uuv数据同步任务开始执行，开始时间：{}", startTime);
        try {
            ApiResult<String> innerOrgResult = tbInnerOrg();

            //获取昨天和明天时间
            Calendar zuoTianCalendar = new GregorianCalendar();
            zuoTianCalendar.setTime(new Date());
            zuoTianCalendar.add(Calendar.DATE, -1);
            Calendar mingTianCalendar = new GregorianCalendar();
            mingTianCalendar.setTime(new Date());
            mingTianCalendar.add(Calendar.DATE, 1);
            //增量同步外部组织 时间为（昨天 -- 今天有更新的组织）
            ApiResult<String> outOrgResult = tbOutOrg(DateUtils.format(zuoTianCalendar.getTime()), DateUtils.format(mingTianCalendar.getTime()));

            ApiResult<String> innerUserResult = tbInnerUser();

            //增量同步外部人员 时间为（昨天 -- 今天有更新的人员）
            ApiResult<String> outUserResult = tbOutUser(DateUtils.format(zuoTianCalendar.getTime()), DateUtils.format(mingTianCalendar.getTime()));
            super.setAcmLogger(new AcmLogger("定时任务同步内部组织结果，" + innerOrgResult.getData() +
                    "；定时任务同步外部组织结果，" + outOrgResult.getData() + "；定时任务同步内部人员结果，" + innerUserResult.getData() +
                    "；定时任务同步外部人员结果，" + outUserResult.getData()));
        } catch (Exception e) {
            super.setAcmLogger(new AcmLogger("定时任务同步uuv数据异常"));
            logger.info("定时任务同步uuv数据异常,原因为：", e);
        }

        Date endTime = new Date();
        long difTime = (endTime.getTime() - startTime.getTime()) / 1000 + 1;
        logger.info("定时任务 uuv数据同步任务结束，结束时间：{}, 同步耗时:{}秒", endTime, difTime);
    }

    @Override
    public ApiResult<String> tbInnerOrg() {
        List<SysOrgVo> orgVoList = getLocalOrg();
        Map<String, Integer> parentIds = new HashMap<>();
        List<ADDepartment> departMentLists = InstaffServiceUtils.getInstance().getDepartMentList();

        ApiResult<String> result = ApiResult.success();
        List<String> needCodes = new ArrayList<>();
        //查找需要同步的组织编码
        Map<String, DictionaryVo> needCodeMap = commDictService.getDictMapByTypeCode("base.uuv.needcode");
        if (!ObjectUtils.isEmpty(needCodeMap)) {
            needCodes.addAll(needCodeMap.keySet());
        } else {
            needCodes.add("108000000");
            needCodes.add("199000000");
            needCodes.add("201000000");
        }

        if (!ObjectUtils.isEmpty(departMentLists)) {
            List<ADDepartmentVo> departmentVos = sortDepartmentVos(departMentLists, needCodes);
            List<ADDepartmentVo> screenDepartmentVos = Lists.newArrayList();
            List<ADDepartmentVo> screenDepartmentVoList = Lists.newArrayList();

            filterNeedOrg(needCodes, departmentVos, screenDepartmentVos, screenDepartmentVoList);

            if (ObjectUtils.isEmpty(screenDepartmentVos)) {
                logger.error("获取内部组织失败");
                result.setData("获取内部组织失败");
                return result;
            }

            for (ADDepartmentVo adDepartmentVo : screenDepartmentVos) {
                List<ADDepartmentVo> adDepartmentVos = Lists.newArrayList();
                uuvServiceUtils.getChildren(adDepartmentVo, adDepartmentVos);
                screenDepartmentVoList.addAll(adDepartmentVos);
            }
            // 有效个数
            int total = 0;
            //本次待更新、新增数量
            int waitChangesNum = 0;
            //本次更新、新增数量
            int changesNum = 0;
            for (ADDepartmentVo department : screenDepartmentVoList) {
                String deptCode = department.getDeptCode();

                //uuv组织状态为disable 则不同步该条数据
                if (STATUS_DISABLED.equalsIgnoreCase(department.getStatus())) {
                    logger.info("内部组织已失效, 组织编码是 {}, 组织名称是 {}", deptCode, department.getDeptName());
                    continue;
                }

                if (ObjectUtils.isEmpty(orgVoList)) {
                    waitChangesNum++;
                    SysOrgAddForm sysOrgAddForm = getSysOrgAddForm(parentIds, department);
                    SysOrgPo sysOrgPo = orgService.xinZengOrg(sysOrgAddForm);
                    parentIds.put(sysOrgPo.getOrgCode(), sysOrgPo.getId());
                    changesNum++;
                    total++;
                    logger.info("内部组织添加成功, 组织编码是 {}, 组织ID是 {}, 组织名称是 {}", deptCode, sysOrgPo.getId(), sysOrgPo.getOrgName());
                } else {
                    for (int i = 0; i < orgVoList.size(); i++) {
                        SysOrgVo allOrg = orgVoList.get(i);
                        if (allOrg.getOrgCode().equals(deptCode)) {
                            //在orgcode相同时 根据时间判断是否需要更新
                            Date oddUpdateTime = allOrg.getUpdateTime();
                            Date newUpdateTime = DateUtil.convertToDate(department.getUpdateTime());
                            long time = -1L;
                            if (!ObjectUtils.isEmpty(newUpdateTime)) {
                                time = newUpdateTime.getTime() - oddUpdateTime.getTime();
                            }
                            if (time > 0) {
                                waitChangesNum++;
                                SysOrgUpdateForm sysOrgUpdateForm = getSysOrgUpdateForm(parentIds, department, allOrg);
                                SysOrgPo sysOrgPo = orgService.gengXinOrg(sysOrgUpdateForm);
                                changesNum++;
                                logger.info("内部组织更新成功, 组织编码是 {}, 组织ID是 {}, 组织名称是 {}", deptCode, sysOrgPo.getId(), sysOrgPo.getOrgName());
                            }
                            parentIds.put(allOrg.getOrgCode(), allOrg.getId());
                            total++;
                            break;
                        }
                        if (i == orgVoList.size() - 1) {
                            waitChangesNum++;
                            SysOrgAddForm sysOrgAddForm = getSysOrgAddForm(parentIds, department);
                            SysOrgPo sysOrgPo = orgService.xinZengOrg(sysOrgAddForm);
                            parentIds.put(sysOrgPo.getOrgCode(), sysOrgPo.getId());
                            total++;
                            changesNum++;
                            logger.info("内部组织添加成功, 组织编码是 {}, 组织ID是 {}, 组织名称是 {}", deptCode, sysOrgPo.getId(), sysOrgPo.getOrgName());
                        }
                    }
                }
            }
            logger.info("内部组织同步成功, 同步成功组织数量 {}, 所有待同步组织数量 {}", total, screenDepartmentVoList.size());
            result.setData("内部组织同步成功, 内部组织数量: " + total + ",待更新: " + waitChangesNum +
                    ",成功: " + changesNum + ",失败: " + (waitChangesNum - changesNum));
        } else {

            logger.info("获取内部组织为空");
            result.setData("获取内部组织为空");
        }
        return result;
    }

    /**
     * 过滤出所需要的组织
     *
     * @param needCodes              所需要的组织code
     * @param departmentVos          所有组织列表
     * @param screenDepartmentVos    过滤后的组织
     * @param screenDepartmentVoList 过滤后的所有组织
     */
    private void filterNeedOrg(List<String> needCodes, List<ADDepartmentVo> departmentVos, List<ADDepartmentVo> screenDepartmentVos, List<ADDepartmentVo> screenDepartmentVoList) {
        for (ADDepartmentVo adDepartmentVo : departmentVos) {
            if ("100000000".equals(adDepartmentVo.getDeptCode())) {
                screenDepartmentVoList.add(adDepartmentVo);
            }
            if (needCodes.contains(adDepartmentVo.getDeptCode())) {
                screenDepartmentVos.add(adDepartmentVo);
            }
        }
    }

    @Override
    public ApiResult<String> tbOutOrg(String startUpdateTime, String endUpdateTime) {
        List<SysOrgVo> orgVoList = getLocalOrg();
        Map<String, Integer> parentIds = new HashMap<>();
        List<ADDepartmentInfo> departmentInfos = OutOrgUtils.getInstance().getUUVPortalOrgList("1970-01-01", endUpdateTime);
        ApiResult<String> result = ApiResult.success();

        if (!ObjectUtils.isEmpty(departmentInfos)) {
            List<ADDepartmentInfoVo> departmentInfoVos = sortDepartmentInfoVos(departmentInfos);
            // 有效个数
            int total = 0;
            //本次待更新、新增数量
            int waitChangesNum = 0;
            //本次更新、新增数量
            int changesNum = 0;
            for (ADDepartmentInfoVo department : departmentInfoVos) {
                String deptCode = department.getDeptCode();

                //uuv组织状态为disable 则不同步该条数据
                if (STATUS_DISABLED.equalsIgnoreCase(department.getStatus())) {
                    logger.info("外部组织已失效, 组织编码是 {}, 组织名称是 {}", deptCode, department.getDeptName());
                    continue;
                }
                if (ObjectUtils.isEmpty(orgVoList)) {
                    waitChangesNum++;
                    SysOrgAddForm sysOrgAddForm = getSysOrgAddForm(parentIds, department);
                    SysOrgPo sysOrgPo = orgService.xinZengOrg(sysOrgAddForm);
                    logger.info("外部组织添加成功, 组织编码是 {}, 组织ID是 {}, 组织名称是 {}", deptCode, sysOrgPo.getId(), sysOrgPo.getOrgName());
                    total++;
                    changesNum++;
                    parentIds.put(sysOrgPo.getOrgCode(), sysOrgPo.getId());
                } else {
                    for (int i = 0; i < orgVoList.size(); i++) {
                        SysOrgVo orgVo = orgVoList.get(i);
                        //在orgcode相同时 根据时间判断是否需要更新 新的时间大于老的更新时间则需要更新
                        Date oddUpdateTime = orgVo.getUpdateTime();
                        Date newUpdateTime = DateUtil.convertToDate(department.getUpdateTime());
                        long time = -1L;
                        if (!ObjectUtils.isEmpty(newUpdateTime)) {
                            time = newUpdateTime.getTime() - oddUpdateTime.getTime();
                        }
                        if (orgVo.getOrgCode().equals(deptCode)) {
                            if (time > 0) {
                                waitChangesNum++;
                                SysOrgUpdateForm sysOrgUpdateForm = getSysOrgUpdateForm(parentIds, department, orgVo);
                                SysOrgPo sysOrgPo = orgService.gengXinOrg(sysOrgUpdateForm);
                                changesNum++;
                                logger.info("外部组织更新成功, 组织编码是 {}, 组织ID是 {}, 组织名称是 {}", deptCode, sysOrgPo.getId(), sysOrgPo.getOrgName());
                            }
                            parentIds.put(orgVo.getOrgCode(), orgVo.getId());
                            total++;
                            break;
                        }
                        if (i == orgVoList.size() - 1) {
                            waitChangesNum++;
                            SysOrgAddForm sysOrgAddForm = getSysOrgAddForm(parentIds, department);
                            SysOrgPo sysOrgPo = orgService.xinZengOrg(sysOrgAddForm);
                            logger.info("外部组织添加成功, 组织编码是 {}, 组织ID是 {}, 组织名称是 {}", deptCode, sysOrgPo.getId(), sysOrgPo.getOrgName());
                            total++;
                            changesNum++;
                            parentIds.put(sysOrgPo.getOrgCode(), sysOrgPo.getId());
                        }
                    }
                }
            }
            logger.info("外部组织同步成功, 同步成功组织数量 {}, 所有待同步组织数量 {}", total, departmentInfoVos.size());
            result.setData("外部组织同步成功, 外部组织数量: " + total + ",待更新: " + waitChangesNum +
                    ",成功: " + changesNum + ",失败: " + (waitChangesNum - changesNum));
        } else {
            logger.info("获取外部组织为空, ");
            result.setData("获取外部组织为空");
        }
        return result;
    }

    @Override
    public ApiResult<String> tbInnerUser() {
        List<SysOrgVo> localOrg = getLocalOrg();
        List<SysUserVo> sysAllUserVos = userServce.queryAllUser();
        ApiResult<String> result = ApiResult.success();
        List<ADUser> adUsers = InstaffServiceUtils.getInstance().getADUser(false);
        if (!ObjectUtils.isEmpty(adUsers)) {
            List<String> syncErrorList = Lists.newArrayList();
            // 有效个数
            int total = 0;
            //本次待更新、新增数量
            int waitChangesNum = 0;
            //本次更新、新增数量
            int changesNum = 0;
            for (ADUser adUser : adUsers) {
                String userCode = adUser.getUserCode();
                if (ObjectUtils.isEmpty(sysAllUserVos)) {
                    if ("admin".equals(adUser.getAccountName()) || ObjectUtils.isEmpty(userCode)) {
                        continue;
                    }
                    //新增
                    SysUserAddFrom innerAddUser = getInnerAddUser(adUser, localOrg);
                    if (ObjectUtils.isEmpty(innerAddUser)) {
                        continue;
                    }
                    waitChangesNum++;
                    SysUserPo sysUserPo = null;
                    try {
                        sysUserPo = userServce.xinZengUser(innerAddUser);
                    } catch (Exception e) {
                        syncErrorList.add(adUser.getAccountName());
                        logger.error("新增内部人员失败，人员编码为 {}, 人员登录名是{}， 人员名称是 {}, 原因为",
                                userCode, innerAddUser.getUserName(), innerAddUser.getActuName(), e);
                        continue;
                    }
                    total++;
                    changesNum++;
                    logger.info("内部人员添加成功, 人员ID 是 {}, 人员编码是 {}, 人员登录名是{}， 人员名称是 {}",
                            sysUserPo.getId(), userCode, sysUserPo.getUserName(), sysUserPo.getActuName());
                } else {
                    for (int i = 0; i < sysAllUserVos.size(); i++) {
                        if ("admin".equals(adUser.getAccountName()) || ObjectUtils.isEmpty(userCode)) {
                            break;
                        }
                        SysUserVo sysAllUserVo = sysAllUserVos.get(i);

                        //在orgcode相同时 根据时间判断是否需要更新 新的时间大于老的更新时间则需要更新
                        Date oddUpdateTime = sysAllUserVo.getUpdateTime();
                        Date newUpdateTime = DateUtil.convertToDate(adUser.getUpdateTime());
                        long time = -1L;
                        if (!ObjectUtils.isEmpty(newUpdateTime)) {
                            time = newUpdateTime.getTime() - oddUpdateTime.getTime();
                        }
                        if (userCode.equals(sysAllUserVo.getUserCode())) {
                            if (time > 0) {
                                waitChangesNum++;
                                //更新
                                SysUserUpdateFrom sysUserUpdateFrom = new SysUserUpdateFrom();
                                sysUserUpdateFrom.setId(sysAllUserVo.getId());
                                sysUserUpdateFrom = getInnerUpdateUser(adUser, localOrg, sysUserUpdateFrom);
                                if (ObjectUtils.isEmpty(sysUserUpdateFrom)) {
                                    syncErrorList.add(adUser.getAccountName());
                                    logger.info("内部人员更新失败,未匹配上所属部门。 人员编码 是 {}, 人员名称是 {}, 部门编码是{}",
                                            userCode, adUser.getAccountName(), adUser.getDeptCode());
                                    break;
                                }
                                SysUserPo sysUserPo = null;
                                try {
                                    sysUserPo = userServce.xiuGaiUser(sysUserUpdateFrom);
                                } catch (Exception e) {
                                    syncErrorList.add(adUser.getAccountName());
                                    logger.error("更新内部人员失败，人员编码为 {}, 人员登录名是{}， 人员名称是 {}, 原因为",
                                            userCode, sysUserUpdateFrom.getUserName(), sysUserUpdateFrom.getActuName(), e);
                                    break;
                                }
                                changesNum++;
                                logger.info("内部人员更新成功, 人员ID 是 {}, 人员编码是 {}, 人员登录名是{}， 人员名称是 {}",
                                        sysUserPo.getId(), userCode, sysUserPo.getUserName(), sysUserPo.getActuName());
                            }
                            total++;
                            break;
                        }
                        if (sysAllUserVos.size() == i + 1) {
                            //新增
                            SysUserAddFrom innerAddUser = getInnerAddUser(adUser, localOrg);
                            if (ObjectUtils.isEmpty(innerAddUser)) {
                                break;
                            }
                            waitChangesNum++;
                            SysUserPo sysUserPo = null;
                            try {
                                sysUserPo = userServce.xinZengUser(innerAddUser);
                            } catch (Exception e) {
                                syncErrorList.add(adUser.getAccountName());
                                logger.error("新增内部人员失败，人员编码为 {}, 人员登录名是{}， 人员名称是 {}, 原因为",
                                        userCode, innerAddUser.getUserName(), innerAddUser.getActuName(), e);
                                break;
                            }
                            total++;
                            changesNum++;
                            logger.info("内部人员添加成功, 人员ID 是 {}, 人员编码是 {}, 人员登录名是{}， 人员名称是 {}",
                                    sysUserPo.getId(), userCode, sysUserPo.getUserName(), sysUserPo.getActuName());
                        }
                    }
                }
            }
            if (!ObjectUtils.isEmpty(syncErrorList)) {
                logger.info("内部人员同步结束, 同步错误人员为 {}, 同步成功数量为 {}, 所有待同步数量为 {}", syncErrorList, total, adUsers.size());
                result.setData("内部人员同步结束, 同步错误人员为:" + syncErrorList + ",内部人员数量: " + total + ",待更新: " + waitChangesNum +
                        ",成功: " + changesNum + ",失败: " + (waitChangesNum - changesNum));
                return result;
            }
            logger.info("内部人员同步成功, 同步成功数量为 {}, 所有待同步数量为 {}", total, adUsers.size());
            result.setData("内部人员同步成功, 内部人员数量: " + total + ",待更新: " + waitChangesNum +
                    ",成功: " + changesNum + ",失败: " + (waitChangesNum - changesNum));
        } else {
            logger.info("获取所有待同步内部人员结果为空");
            result.setData("获取所有待同步内部人员结果为空");
        }
        return result;
    }

    /**
     * 获取内部更新人员对象
     *
     * @param adUser
     * @param sysOrgVos
     * @param sysUserUpdateFrom
     * @return
     */
    private SysUserUpdateFrom getInnerUpdateUser(ADUser adUser, List<SysOrgVo> sysOrgVos, SysUserUpdateFrom sysUserUpdateFrom) {
        sysUserUpdateFrom.setActuName(adUser.getChsName());
        sysUserUpdateFrom.setUserName(adUser.getAccountName());
        int status = 0;
        if (STATUS_ENABLED.equals(adUser.getStatus())) {
            status = 1;
        }
        sysUserUpdateFrom.setStaffStatus(status);
        sysUserUpdateFrom.setUserCode(adUser.getUserCode());
        sysUserUpdateFrom.setEmail(adUser.getEmailAddress());

        XMLGregorianCalendar birthday = adUser.getBirthday();
        if (!ObjectUtils.isEmpty(birthday)) {
            sysUserUpdateFrom.setBirth(DateUtil.convertToDate(birthday));
        }

        String userSex = adUser.getSex();
        if (!ObjectUtils.isEmpty(userSex)) {
            int sex = 0;
            if (SEX_MAN.equalsIgnoreCase(userSex)) {
                sex = 1;
            }
            sysUserUpdateFrom.setSex(sex);
        }
        sysUserUpdateFrom.setPhone(adUser.getMobilePhone());

        String joinDate = adUser.getJoinDate();
        if (!ObjectUtils.isEmpty(joinDate)) {
            Date dateFormat = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy");
                dateFormat = format.parse(joinDate);
            } catch (ParseException e) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dateFormat = format.parse(joinDate);
                } catch (ParseException e1) {
                    logger.error("时间转换错误, 入参为 {}， 错误原因为 {}", joinDate, e);
                }
            }
            sysUserUpdateFrom.setEntryDate(dateFormat);
        }

        String identityID = adUser.getIdentityID();
        if (!ObjectUtils.isEmpty(identityID)) {
            sysUserUpdateFrom.setCardNum(identityID);
            sysUserUpdateFrom.setCardType("1");
        }

        String deptCode = adUser.getDeptCode();
        if (!ObjectUtils.isEmpty(deptCode) && !ObjectUtils.isEmpty(sysOrgVos)) {
            for (int i = 0; i < sysOrgVos.size(); i++) {
                SysOrgVo sysOrgVo = sysOrgVos.get(i);
                if (sysOrgVo.getOrgCode().equals(deptCode)) {
                    sysUserUpdateFrom.setOrgId(sysOrgVo.getId());
                    break;
                }
                if (sysOrgVos.size() == i + 1) {
                    return null;
                }

            }
        }

        return sysUserUpdateFrom;
    }

    /**
     * 获取外部更新人员对象
     *
     * @param userInfo
     * @param sysOrgVos
     * @param sysUserUpdateFrom
     * @return
     */
    private SysUserUpdateFrom getOutUpdateUser(UserInfo userInfo, List<SysOrgVo> sysOrgVos, SysUserUpdateFrom sysUserUpdateFrom) {
        sysUserUpdateFrom.setActuName(userInfo.getChsName());
        sysUserUpdateFrom.setUserName(userInfo.getAccountName());

        int status = 0;
        if (STATUS_ENABLED.equals(userInfo.getStatus())) {
            status = 1;
        }
        sysUserUpdateFrom.setStaffStatus(status);
        sysUserUpdateFrom.setUserCode(userInfo.getUserCode());
        sysUserUpdateFrom.setEmail(userInfo.getEmailAddress());

        XMLGregorianCalendar birthday = userInfo.getBirthday();
        if (!ObjectUtils.isEmpty(birthday)) {
            sysUserUpdateFrom.setBirth(DateUtil.convertToDate(birthday));
        }

        //如果返回数据没有sex字段 默认为 女 0 当前外部人员为U
        String infoSex = userInfo.getSex();
        if (!ObjectUtils.isEmpty(infoSex)) {
            int sex = 0;
            if (SEX_MAN.equalsIgnoreCase(infoSex)) {
                sex = 1;
            }
            sysUserUpdateFrom.setSex(sex);
        }
        sysUserUpdateFrom.setPhone(userInfo.getMobilePhone());

        String joinDate = userInfo.getJoinDate();
        if (!ObjectUtils.isEmpty(joinDate)) {
            Date dateFormat = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat = format.parse(joinDate);
            } catch (Exception e) {
                logger.error("时间转换错误, 入参为 {}， 错误原因为 {}", joinDate, e);
            }
            sysUserUpdateFrom.setEntryDate(dateFormat);
        }
        String identityID = userInfo.getIdentityID();
        if (!ObjectUtils.isEmpty(identityID)) {
            sysUserUpdateFrom.setCardNum(identityID);
            sysUserUpdateFrom.setCardType("1");
        }

        String deptCode = userInfo.getDeptCode();
        if (!ObjectUtils.isEmpty(deptCode) && !ObjectUtils.isEmpty(sysOrgVos)) {
            for (int i = 0; i < sysOrgVos.size(); i++) {
                SysOrgVo sysOrgVo = sysOrgVos.get(i);
                if (sysOrgVo.getOrgCode().equals(deptCode)) {
                    sysUserUpdateFrom.setOrgId(sysOrgVo.getId());
                    break;
                }
                if (sysOrgVos.size() == i + 1) {
                    return null;
                }
            }
        }

        return sysUserUpdateFrom;
    }

    /**
     * 获取内部新增人员对象
     *
     * @param adUser
     * @param sysOrgVos
     * @return
     */
    private SysUserAddFrom getInnerAddUser(ADUser adUser, List<SysOrgVo> sysOrgVos) {
        SysUserAddFrom sysUserAddFrom = new SysUserAddFrom();
        sysUserAddFrom.setActuName(adUser.getChsName());
        sysUserAddFrom.setPassword("12345678");
        sysUserAddFrom.setUserName(adUser.getAccountName());
        sysUserAddFrom.setUserCode(adUser.getUserCode());
        int status = 0;
        if (STATUS_ENABLED.equals(adUser.getStatus())) {
            status = 1;
        }
        sysUserAddFrom.setStaffStatus(status);
        sysUserAddFrom.setEmail(adUser.getEmailAddress());

        XMLGregorianCalendar birthday = adUser.getBirthday();
        if (!ObjectUtils.isEmpty(birthday)) {
            sysUserAddFrom.setBirth(DateUtil.convertToDate(birthday));
        }

        //如果返回数据没有sex字段 默认为 女 0
        String userSex = adUser.getSex();
        if (!ObjectUtils.isEmpty(userSex)) {
            int sex = 0;
            if (SEX_MAN.equalsIgnoreCase(userSex)) {
                sex = 1;
            }
            sysUserAddFrom.setSex(sex);
        }
        sysUserAddFrom.setPhone(adUser.getMobilePhone());

        String joinDate = adUser.getJoinDate();
        if (!ObjectUtils.isEmpty(joinDate)) {
            Date dateFormat = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy");
                dateFormat = format.parse(joinDate);
            } catch (ParseException e) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dateFormat = format.parse(joinDate);
                } catch (ParseException e1) {
                    logger.error("时间转换错误, 入参为 {}， 错误原因为 {}", joinDate, e);
                }
            }
            sysUserAddFrom.setEntryDate(dateFormat);
        }

        String identityID = adUser.getIdentityID();
        if (!ObjectUtils.isEmpty(identityID)) {
            sysUserAddFrom.setCardNum(identityID);
            sysUserAddFrom.setCardType("1");
        }

        String deptCode = adUser.getDeptCode();
        if (!ObjectUtils.isEmpty(deptCode) && !ObjectUtils.isEmpty(sysOrgVos)) {
            for (int i = 0; i < sysOrgVos.size(); i++) {
                SysOrgVo sysOrgVo = sysOrgVos.get(i);
                if (sysOrgVo.getOrgCode().equals(deptCode)) {
                    sysUserAddFrom.setOrgId(sysOrgVo.getId());
                    break;
                }
                if (sysOrgVos.size() == i + 1) {
                    return null;
                }
            }
        }

        return sysUserAddFrom;
    }

    /**
     * 获取外部新增人员对象
     *
     * @param userInfo
     * @param sysOrgVos
     * @return
     */
    private SysUserAddFrom getOutAddUser(UserInfo userInfo, List<SysOrgVo> sysOrgVos) {
        SysUserAddFrom sysUserAddFrom = new SysUserAddFrom();
        sysUserAddFrom.setActuName(userInfo.getChsName());
        sysUserAddFrom.setUserCode(userInfo.getUserCode());
        sysUserAddFrom.setPassword("123456");
        sysUserAddFrom.setUserName(userInfo.getAccountName());
        int status = 0;
        if (STATUS_ENABLED.equals(userInfo.getStatus())) {
            status = 1;
        }
        sysUserAddFrom.setStaffStatus(status);
        sysUserAddFrom.setEmail(userInfo.getEmailAddress());

        XMLGregorianCalendar birthday = userInfo.getBirthday();
        if (!ObjectUtils.isEmpty(birthday)) {
            sysUserAddFrom.setBirth(DateUtil.convertToDate(birthday));
        }

        //如果返回数据没有sex字段 默认为 女 0 当前外部人员 返回U
        String infoSex = userInfo.getSex();
        if (!ObjectUtils.isEmpty(infoSex)) {
            int sex = 0;
            if (SEX_MAN.equalsIgnoreCase(userInfo.getSex())) {
                sex = 1;
            }
            sysUserAddFrom.setSex(sex);
        }
        sysUserAddFrom.setPhone(userInfo.getMobilePhone());

        String joinDate = userInfo.getJoinDate();
        if (!ObjectUtils.isEmpty(joinDate)) {
            Date dateFormat = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat = format.parse(joinDate);
            } catch (ParseException e) {
                logger.error("时间转换错误, 入参为 {}， 错误原因为 {}", joinDate, e);
            }
            sysUserAddFrom.setEntryDate(dateFormat);
        }

        String identityID = userInfo.getIdentityID();
        if (!ObjectUtils.isEmpty(identityID)) {
            sysUserAddFrom.setCardNum(identityID);
            sysUserAddFrom.setCardType("1");
        }

        //设置部门编码
        String deptCode = userInfo.getDeptCode();
        if (!ObjectUtils.isEmpty(deptCode) && !ObjectUtils.isEmpty(sysOrgVos)) {
            for (int i = 0; i < sysOrgVos.size(); i++) {
                SysOrgVo sysOrgVo = sysOrgVos.get(i);
                if (sysOrgVo.getOrgCode().equals(deptCode)) {
                    sysUserAddFrom.setOrgId(sysOrgVo.getId());
                    break;
                }
                if (sysOrgVos.size() == i + 1) {
                    return null;
                }
            }
        }

        return sysUserAddFrom;
    }

    @Override
    public ApiResult<String> tbOutUser(String startUpdateTime, String endUpdateTime) {
        List<SysOrgVo> localOrg = getLocalOrg();
        List<SysUserVo> sysAllUserVos = userServce.queryAllUser();
        ApiResult<String> result = ApiResult.success();
        List<UserInfo> userInfos = OutstaffServiceUtils.getInstance().getUUVPortalUserList(false, startUpdateTime, endUpdateTime);
        if (!ObjectUtils.isEmpty(userInfos)) {
            // 有效个数
            int total = 0;
            //本次待更新、新增数量
            int waitChangesNum = 0;
            //本次更新、新增数量
            int changesNum = 0;
            List<String> syncErrorList = Lists.newArrayList();
            for (UserInfo userInfo : userInfos) {
                String userCode = userInfo.getUserCode();
                if (ObjectUtils.isEmpty(sysAllUserVos)) {
                    if ("admin".equals(userInfo.getAccountName()) || ObjectUtils.isEmpty(userCode)) {
                        continue;
                    }
                    //新增
                    SysUserAddFrom innerAddUser = getOutAddUser(userInfo, localOrg);
                    if (ObjectUtils.isEmpty(innerAddUser)) {
                        continue;
                    }
                    waitChangesNum++;
                    SysUserPo sysUserPo = null;
                    try {
                        sysUserPo = userServce.xinZengUser(innerAddUser);
                    } catch (Exception e) {
                        syncErrorList.add(userInfo.getAccountName());
                        logger.error("新增外部人员失败，人员编码为 {}, 人员登录名是{}， 人员名称是 {}, 原因为",
                                userCode, innerAddUser.getUserName(), innerAddUser.getActuName(), e);
                        continue;
                    }
                    total++;
                    changesNum++;
                    logger.info("新增外部人员成功, 人员ID为{}, 人员编码为 {}, 人员登录名是{}， 人员名称是 {}",
                            sysUserPo.getId(), userCode, sysUserPo.getUserName(), sysUserPo.getActuName());
                } else {
                    for (int i = 0; i < sysAllUserVos.size(); i++) {
                        SysUserVo sysAllUserVo = sysAllUserVos.get(i);

                        //在orgcode相同时 根据时间判断是否需要更新  新的时间大于老的更新时间则需要更新
                        Date oddUpdateTime = sysAllUserVo.getUpdateTime();
                        Date newUpdateTime = DateUtil.convertToDate(userInfo.getUpdateTime());
                        long time = -1L;
                        if (!ObjectUtils.isEmpty(newUpdateTime)) {
                            time = newUpdateTime.getTime() - oddUpdateTime.getTime();
                        }
                        if (userCode.equals(sysAllUserVo.getUserCode())) {
                            if (time > 0) {
                                //更新
                                waitChangesNum++;
                                SysUserUpdateFrom sysUserUpdateFrom = new SysUserUpdateFrom();
                                sysUserUpdateFrom.setId(sysAllUserVo.getId());
                                sysUserUpdateFrom = getOutUpdateUser(userInfo, localOrg, sysUserUpdateFrom);
                                if (ObjectUtils.isEmpty(sysUserUpdateFrom)) {
                                    syncErrorList.add(userInfo.getAccountName());
                                    logger.info("外部人员更新失败,未匹配上所属部门。 人员编码 是 {}, 人员名称是 {}, 部门编码是{}",
                                            userCode, userInfo.getAccountName(), userInfo.getDeptCode());
                                    break;
                                }
                                SysUserPo sysUserPo = null;
                                try {
                                    sysUserPo = userServce.xiuGaiUser(sysUserUpdateFrom);
                                } catch (Exception e) {
                                    syncErrorList.add(userInfo.getAccountName());
                                    logger.error("更新外部人员失败，人员编码为 {}, 人员登录名是{}， 人员名称是 {}, 原因为",
                                            userCode, sysUserUpdateFrom.getUserName(), sysUserUpdateFrom.getActuName(), e);
                                    break;
                                }
                                changesNum++;
                                logger.info("外部人员更新成功, 人员ID为{}, 人员编码为 {}, 人员登录名是{}， 人员名称是 {}",
                                        sysUserPo.getId(), userCode, sysUserPo.getUserName(), sysUserPo.getActuName());
                            }
                            total++;
                            break;
                        }
                        if (sysAllUserVos.size() == i + 1) {
                            //新增
                            SysUserAddFrom innerAddUser = getOutAddUser(userInfo, localOrg);
                            if (ObjectUtils.isEmpty(innerAddUser)) {
                                break;
                            }
                            waitChangesNum++;
                            SysUserPo sysUserPo = null;
                            try {
                                sysUserPo = userServce.xinZengUser(innerAddUser);
                            } catch (Exception e) {
                                syncErrorList.add(userInfo.getAccountName());
                                logger.error("新增外部人员失败，人员编码为 {}, 人员登录名是{}， 人员名称是 {}, 原因为",
                                        userCode, innerAddUser.getUserName(), innerAddUser.getActuName(), e);
                                break;
                            }
                            total++;
                            changesNum++;
                            logger.info("新增外部人员成功, 人员ID为{}, 人员编码为 {}, 人员登录名是{}， 人员名称是 {}",
                                    sysUserPo.getId(), userCode, sysUserPo.getUserName(), sysUserPo.getActuName());
                        }
                    }
                }
            }
            if (!ObjectUtils.isEmpty(syncErrorList)) {
                logger.info("外部人员同步结束, 同步错误的人员为 {}, 同步数量为 {}, 所有待同步人员数量为 {}", syncErrorList, total, userInfos.size());
                result.setData("外部人员同步结束, 同步错误人员为: " + syncErrorList + ",外部人员数量: " + total + ",待更新:" + waitChangesNum +
                        ",成功:" + changesNum + ",失败:" + (waitChangesNum - changesNum));
                return result;
            }
            logger.info("外部人员同步成功, 同步数量为 {}, 所有待同步人员数量为 {}", total, userInfos.size());
            result.setData("外部人员同步成功, 外部人员数量: " + total + ",待更新: " + waitChangesNum +
                    ",成功: " + changesNum + ",失败: " + (waitChangesNum - changesNum));
        } else {
            logger.info("获取所有待同步外部人员结果为空");
            result.setData("获取所有待同步外部人员结果为空");
        }
        return result;
    }

    /**
     * 内部组织 排序
     *
     * @param departMentLists
     * @return
     */
    private List<ADDepartmentVo> sortDepartmentVos(List<ADDepartment> departMentLists, List<String> needCodes) {
        List<ADDepartmentVo> adDepartmentVos = copyToDepartmentVo(departMentLists);
        adDepartmentVos = generateInnerTree(adDepartmentVos);
        List<ADDepartmentVo> departmentVos = Lists.newArrayList();
        for (ADDepartmentVo adDepartmentVo : adDepartmentVos) {
            getChildren(adDepartmentVo, departmentVos, needCodes);
        }
        return departmentVos;
    }

    private void getChildren(ADDepartmentVo orgVo, List<ADDepartmentVo> orgVoList, List<String> needCodes) {
        orgVoList.add(orgVo);
        if (!ObjectUtils.isEmpty(orgVo.getChildren())) {
            List<ADDepartmentVo> childrens = orgVo.getChildren();
            for (ADDepartmentVo sysOrgVo : childrens) {
                if (needCodes.contains(sysOrgVo.getDeptCode())) {
                    orgVoList.add(sysOrgVo);
                }
            }
        }
    }

    /**
     * copy生成内部组织树对象
     *
     * @param departMentLists
     * @return
     */
    private List<ADDepartmentVo> copyToDepartmentVo(List<ADDepartment> departMentLists) {
        List<ADDepartmentVo> adDepartmentVos = Lists.newArrayList();
        for (ADDepartment department : departMentLists) {
            ADDepartmentVo adDepartmentVo = new ADDepartmentVo();
            adDepartmentVo.setColumn1(department.getColumn1());
            adDepartmentVo.setColumn2(department.getColumn2());
            adDepartmentVo.setColumn3(department.getColumn3());
            adDepartmentVo.setColumn4(department.getColumn4());
            adDepartmentVo.setColumn5(department.getColumn5());
            adDepartmentVo.setCreateTime(department.getCreateTime());
            adDepartmentVo.setCreator(department.getCreator());
            adDepartmentVo.setDeptCode(department.getDeptCode());
            adDepartmentVo.setDeptduty(department.getDeptduty());
            adDepartmentVo.setDeptFullName(department.getDeptFullName());
            adDepartmentVo.setLine(department.getLine());
            adDepartmentVo.setDeptID(department.getDeptID());
            adDepartmentVo.setDeptName(department.getDeptName());
            adDepartmentVo.setFax(department.getFax());
            adDepartmentVo.setGroupMailBox(department.getGroupMailBox());
            adDepartmentVo.setGroupName(department.getGroupName());
            adDepartmentVo.setIsVOrg(department.getIsVOrg());
            adDepartmentVo.setLevel(department.getLevel());
            adDepartmentVo.setOrderNo(department.getOrderNo());
            adDepartmentVo.setParentDeptCode(department.getParentDeptCode());
            adDepartmentVo.setParentDeptID(department.getParentDeptID());
            adDepartmentVo.setPosCount(department.getPosCount());
            adDepartmentVo.setPost(department.getPost());
            adDepartmentVo.setRegion(department.getRegion());
            adDepartmentVo.setSecondLevelDeptFullName(department.getSecondLevelDeptFullName());
            adDepartmentVo.setSimpleNameCode(department.getSimpleNameCode());
            adDepartmentVo.setStatus(department.getStatus());
            adDepartmentVo.setStreet(department.getStreet());
            adDepartmentVo.setTelephone(department.getTelephone());
            adDepartmentVo.setUpdateBy(department.getUpdateBy());
            adDepartmentVo.setUpdateTime(department.getUpdateTime());
            adDepartmentVo.setUserCount(department.getUserCount());

            adDepartmentVos.add(adDepartmentVo);
        }
        return adDepartmentVos;
    }

    /**
     * 外部组织 排序
     *
     * @param departMentLists
     * @return
     */
    private List<ADDepartmentInfoVo> sortDepartmentInfoVos(List<ADDepartmentInfo> departMentLists) {
        List<ADDepartmentInfoVo> adDepartmentVos = copyToDepartmentInfoVo(departMentLists);
        adDepartmentVos = generateOutTree(adDepartmentVos);
        List<ADDepartmentInfoVo> departmentVos = Lists.newArrayList();
        for (ADDepartmentInfoVo adDepartmentVo : adDepartmentVos) {
            uuvServiceUtils.getChildren(adDepartmentVo, departmentVos);
        }
        return departmentVos;
    }

    /**
     * copy生成树对象
     *
     * @param departMentLists
     * @return
     */
    private List<ADDepartmentInfoVo> copyToDepartmentInfoVo(List<ADDepartmentInfo> departMentLists) {
        List<ADDepartmentInfoVo> adDepartmentVos = Lists.newArrayList();
        for (ADDepartmentInfo department : departMentLists) {
            ADDepartmentInfoVo departmentInfoVo = new ADDepartmentInfoVo();
            departmentInfoVo.setColumn1(department.getColumn1());
            departmentInfoVo.setColumn2(department.getColumn2());
            departmentInfoVo.setColumn3(department.getColumn3());
            departmentInfoVo.setColumn4(department.getColumn4());
            departmentInfoVo.setColumn5(department.getColumn5());
            departmentInfoVo.setCreateTime(department.getCreateTime());
            departmentInfoVo.setCreator(department.getCreator());
            departmentInfoVo.setDeptCode(department.getDeptCode());
            departmentInfoVo.setDeptduty(department.getDeptduty());
            departmentInfoVo.setDeptFullName(department.getDeptFullName());
            departmentInfoVo.setLine(department.getLine());
            departmentInfoVo.setDeptID(department.getDeptID());
            departmentInfoVo.setDeptName(department.getDeptName());
            departmentInfoVo.setFax(department.getFax());
            departmentInfoVo.setGroupMailBox(department.getGroupMailBox());
            departmentInfoVo.setSecondLevelDeptCode(department.getSecondLevelDeptCode());
            departmentInfoVo.setGroupName(department.getGroupName());
            departmentInfoVo.setIsVOrg(department.getIsVOrg());
            departmentInfoVo.setLevel(department.getLevel());
            departmentInfoVo.setOrderNo(department.getOrderNo());
            departmentInfoVo.setParentDeptCode(department.getParentDeptCode());
            departmentInfoVo.setParentDeptID(department.getParentDeptID());
            departmentInfoVo.setPosCount(department.getPosCount());
            departmentInfoVo.setPost(department.getPost());
            departmentInfoVo.setRegion(department.getRegion());
            departmentInfoVo.setSecondLevelDeptFullName(department.getSecondLevelDeptFullName());
            departmentInfoVo.setGroupAlias(department.getGroupAlias());
            departmentInfoVo.setDeptType(department.getDeptType());
            departmentInfoVo.setLastUpdateDate(department.getLastUpdateDate());
            departmentInfoVo.setDataSource(department.getDataSource());
            departmentInfoVo.setPriority(department.getPriority());
            departmentInfoVo.setSimpleNameCode(department.getSimpleNameCode());
            departmentInfoVo.setStatus(department.getStatus());
            departmentInfoVo.setStreet(department.getStreet());
            departmentInfoVo.setTelephone(department.getTelephone());
            departmentInfoVo.setFax(department.getFax());
            departmentInfoVo.setUpdateBy(department.getUpdateBy());
            departmentInfoVo.setUpdateTime(department.getUpdateTime());
            departmentInfoVo.setUserCount(department.getUserCount());

            adDepartmentVos.add(departmentInfoVo);
        }
        return adDepartmentVos;
    }


    /**
     * 根据列表产生树 内部组织
     *
     * @param treeNodes
     * @return
     */
    public List<ADDepartmentVo> generateInnerTree(List<ADDepartmentVo> treeNodes) {
        List<ADDepartmentVo> retList = Lists.newArrayList();
        if (!ObjectUtils.isEmpty(treeNodes)) {
            List<String> idList = ListUtil.toValueList(treeNodes, "parentDeptCode", String.class);
            //去除 父级parentcode 为空
            idList.remove("");
            List<ADDepartmentVo> rootNodes = Lists.newArrayList();//根节点初始化
            for (ADDepartmentVo treeNode : treeNodes) {
                if (!idList.contains(treeNode.getParentDeptCode())) {
                    //如不包含，则说明，该节点为根节点
                    rootNodes.add(treeNode);
                }
            }
            for (ADDepartmentVo rootNode : rootNodes) {//遍历根节点，在根节点下拼装树形数据
                //寻找子节点
                Map<String, List<ADDepartmentVo>> childrenMap = toInnerChildrenMap(treeNodes);
                List<ADDepartmentVo> childrenList = uuvServiceUtils.bulidInnerChildren(childrenMap, rootNode.getDeptCode());
                rootNode.setChildren(childrenList);
                retList.add(rootNode);
            }
        }
        return retList;
    }

    /**
     * 根据列表产生树 外部组织
     *
     * @param treeNodes
     * @return
     */
    public List<ADDepartmentInfoVo> generateOutTree(List<ADDepartmentInfoVo> treeNodes) {
        List<ADDepartmentInfoVo> retList = Lists.newArrayList();
        if (!ObjectUtils.isEmpty(treeNodes)) {
            List<String> idList = ListUtil.toValueList(treeNodes, "parentDeptCode", String.class);
            //去除 外部组织的父节点
            idList.remove("100000000");
            List<ADDepartmentInfoVo> rootNodes = Lists.newArrayList();//根节点初始化
            for (ADDepartmentInfoVo treeNode : treeNodes) {
                if (!idList.contains(treeNode.getParentDeptCode())) {
                    //如不包含，则说明，该节点为根节点
                    rootNodes.add(treeNode);
                }
            }
            for (ADDepartmentInfoVo rootNode : rootNodes) {//遍历根节点，在根节点下拼装树形数据
                //寻找子节点
                Map<String, List<ADDepartmentInfoVo>> childrenMap = toOutChildrenMap(treeNodes);
                List<ADDepartmentInfoVo> childrenList = this.bulidOutChildren(childrenMap, rootNode.getDeptCode());
                rootNode.setChildren(childrenList);
                retList.add(rootNode);
            }
        }
        return retList;
    }

    /**
     * 外部 子节点
     *
     * @param treeNodes
     * @return
     */
    public Map<String, List<ADDepartmentInfoVo>> toOutChildrenMap(List<ADDepartmentInfoVo> treeNodes) {
        Map<String, List<ADDepartmentInfoVo>> childrenMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(treeNodes)) {
            for (ADDepartmentInfoVo t : treeNodes) {

                if (childrenMap.get(t.getParentDeptCode()) == null) {
                    List<ADDepartmentInfoVo> l = new ArrayList<>();
                    l.add(t);
                    childrenMap.put(t.getParentDeptCode(), l);
                } else {
                    childrenMap.get(t.getParentDeptCode()).add(t);
                }
            }
        }
        return childrenMap;
    }

    /**
     * 递归计算子节点  外部
     *
     * @param childrenMap
     * @param parentCode
     * @return
     */
    private List<ADDepartmentInfoVo> bulidOutChildren(Map<String, List<ADDepartmentInfoVo>> childrenMap, String parentCode) {
        List<ADDepartmentInfoVo> list = childrenMap.get(parentCode);
        if (!ObjectUtils.isEmpty(list)) {
            for (ADDepartmentInfoVo t : list) {
                // 递归查询子节点
                List<ADDepartmentInfoVo> children = bulidOutChildren(childrenMap, t.getDeptCode());
                t.setChildren(null);
                if (!ObjectUtils.isEmpty(children)) {
                    t.setChildren(children);
                }
            }
        }

        return list;
    }

    /**
     * 内部部 子节点
     *
     * @param treeNodes
     * @return
     */
    public Map<String, List<ADDepartmentVo>> toInnerChildrenMap(List<ADDepartmentVo> treeNodes) {
        Map<String, List<ADDepartmentVo>> childrenMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(treeNodes)) {
            for (ADDepartmentVo t : treeNodes) {
                if (ObjectUtils.isEmpty(t.getDeptCode())) {
                    logger.info("该组织无单位编码，组织为{}，父级编码为{}",t.getDeptName(),t.getParentDeptCode());
                    continue;
                }
                if (childrenMap.get(t.getParentDeptCode()) == null) {
                    List<ADDepartmentVo> l = new ArrayList<>();
                    l.add(t);
                    childrenMap.put(t.getParentDeptCode(), l);
                } else {
                    childrenMap.get(t.getParentDeptCode()).add(t);
                }
            }
        }
        return childrenMap;
    }

    /**
     * 内部组织 更新
     *
     * @param parentIds
     * @param department
     * @param orgVo
     * @return
     */
    private SysOrgUpdateForm getSysOrgUpdateForm(Map<String, Integer> parentIds, ADDepartmentVo department, SysOrgVo orgVo) {
        SysOrgUpdateForm sysOrgUpdateForm = new SysOrgUpdateForm();
        sysOrgUpdateForm.setId(orgVo.getId());

        sysOrgUpdateForm.setOrgCode(department.getDeptCode());
        sysOrgUpdateForm.setOrgName(department.getDeptName());
        String parentDeptCode = department.getParentDeptCode();
        if (ObjectUtils.isEmpty(parentDeptCode)) {
            sysOrgUpdateForm.setParentId(0);
        } else {
            sysOrgUpdateForm.setParentId(parentIds.get(parentDeptCode));
        }
        // uuv内部组织更新时 忽略组织类型字段
        // if (0 == sysOrgUpdateForm.getParentId()) {
        //     //内部组织 没有父级 为公司 其余为部门
        //     sysOrgUpdateForm.setOrgType(0);
        // } else {
        //     sysOrgUpdateForm.setOrgType(1);
        // }
        sysOrgUpdateForm.setOrgLevel(department.getLevel() + "");
        int status = 0;
        if ("enabled".equals(department.getStatus())) {
            status = 1;
        }
        sysOrgUpdateForm.setStatus(status);
        sysOrgUpdateForm.setOrgEmail(department.getGroupMailBox());
        return sysOrgUpdateForm;
    }

    /**
     * 内部组织 新增
     *
     * @param parentIds
     * @param department
     * @return
     */
    private SysOrgAddForm getSysOrgAddForm(Map<String, Integer> parentIds, ADDepartmentVo department) {
        SysOrgAddForm sysOrgAddForm = new SysOrgAddForm();

        String deptCode = department.getDeptCode();
        String deptName = department.getDeptName();
        String parentDeptCode = department.getParentDeptCode();
        String orgLevel = department.getLevel() + "";
        String statusStr = department.getStatus();
        String groupMailBox = department.getGroupMailBox();
        orgAdd(true, parentIds, sysOrgAddForm, deptCode, deptName, parentDeptCode, orgLevel, statusStr, groupMailBox);
        return sysOrgAddForm;
    }

    /**
     * 外部组织更新
     *
     * @param parentIds
     * @param departmentInfo
     * @param orgVo
     * @return
     */
    private SysOrgUpdateForm getSysOrgUpdateForm(Map<String, Integer> parentIds, ADDepartmentInfoVo departmentInfo, SysOrgVo orgVo) {
        SysOrgUpdateForm sysOrgUpdateForm = new SysOrgUpdateForm();
        sysOrgUpdateForm.setId(orgVo.getId());
        sysOrgUpdateForm.setOrgCode(departmentInfo.getDeptCode());
        sysOrgUpdateForm.setOrgName(departmentInfo.getDeptName());
        String parentDeptCode = departmentInfo.getParentDeptCode();
        if ("100000000".equals(parentDeptCode)) {
            sysOrgUpdateForm.setParentId(0);
        } else {
            sysOrgUpdateForm.setParentId(parentIds.get(parentDeptCode));
        }
        sysOrgUpdateForm.setOrgType(0);
        sysOrgUpdateForm.setOrgLevel(departmentInfo.getLevel() + "");
        int status = 0;
        if ("enabled".equals(departmentInfo.getStatus())) {
            status = 1;
        }
        sysOrgUpdateForm.setStatus(status);
        sysOrgUpdateForm.setOrgEmail(departmentInfo.getGroupMailBox());
        return sysOrgUpdateForm;
    }

    /**
     * 外部组织 新增
     *
     * @param parentIds
     * @param departmentInfo
     * @return
     */
    private SysOrgAddForm getSysOrgAddForm(Map<String, Integer> parentIds, ADDepartmentInfoVo departmentInfo) {
        SysOrgAddForm sysOrgAddForm = new SysOrgAddForm();
        String deptCode = departmentInfo.getDeptCode();
        String deptName = departmentInfo.getDeptName();
        String parentDeptCode = departmentInfo.getParentDeptCode();
        String orgLevel = departmentInfo.getLevel() + "";
        String statusStr = departmentInfo.getStatus();
        String groupMailBox = departmentInfo.getGroupMailBox();

        orgAdd(false, parentIds, sysOrgAddForm, deptCode, deptName, parentDeptCode, orgLevel, statusStr, groupMailBox);

        return sysOrgAddForm;
    }

    private void orgAdd(boolean inner, Map<String, Integer> parentIds, SysOrgAddForm sysOrgAddForm, String deptCode, String deptName, String parentDeptCode, String orgLevel, String statusStr, String groupMailBox) {
        sysOrgAddForm.setOrgCode(deptCode);
        sysOrgAddForm.setOrgName(deptName);
        boolean isParent = (inner && ObjectUtils.isEmpty(parentDeptCode)) || (!inner && "100000000".equals(parentDeptCode));
        if (isParent) {
            sysOrgAddForm.setParentId(0);
        } else {
            sysOrgAddForm.setParentId(parentIds.get(parentDeptCode));
        }
        if (inner && 0 == sysOrgAddForm.getParentId()) {
            //内部组织 没有父级 为公司 其余为部门
            sysOrgAddForm.setOrgType(0);
        } else if (inner) {
            sysOrgAddForm.setOrgType(1);
        } else {
            sysOrgAddForm.setOrgType(0);
        }
        sysOrgAddForm.setOrgLevel(orgLevel);
        int status = 0;

        if (STATUS_ENABLED.equals(statusStr)) {
            status = 1;
        }
        sysOrgAddForm.setStatus(status);
        sysOrgAddForm.setOrgEmail(groupMailBox);
    }

    public static void main(String[] args) {
//在orgcode相同时 根据时间判断是否需要更新 新的时间大于老的更新时间则需要更新
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date oddUpdateTime = null;
        Date newUpdateTime = null;
        try {
            oddUpdateTime = sdf.parse("2018-01-09");
            newUpdateTime = sdf.parse("2018-10-09");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = -1L;
        if (!ObjectUtils.isEmpty(newUpdateTime)) {
            time = newUpdateTime.getTime() - oddUpdateTime.getTime();
        }
    }
}
