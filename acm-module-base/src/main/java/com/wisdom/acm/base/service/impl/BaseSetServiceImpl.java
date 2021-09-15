package com.wisdom.acm.base.service.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.base.form.set.BaseSetDocUpdateForm;
import com.wisdom.acm.base.form.set.BaseSetProjectUpdateForm;
import com.wisdom.acm.base.form.set.BaseSetTimeUdateForm;
import com.wisdom.acm.base.mapper.BaseSetMapper;
import com.wisdom.acm.base.po.BaseSetPo;
import com.wisdom.acm.base.service.BaseDictService;
import com.wisdom.acm.base.service.BaseSetService;
import com.wisdom.acm.base.vo.BaseBoVo;
import com.wisdom.acm.base.vo.set.BaseSetBoVo;
import com.wisdom.acm.base.vo.set.BaseSetDocVo;
import com.wisdom.acm.base.vo.set.BaseSetProjectVo;
import com.wisdom.acm.base.vo.set.BaseSetTimeVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.GeneralVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class BaseSetServiceImpl extends BaseService<BaseSetMapper, BaseSetPo> implements BaseSetService {

    @Autowired
    private BaseDictService dictService;

    @Override
    public List<BaseSetBoVo> queryBoList() {

        List<BaseSetBoVo> retVo = new ArrayList<>();

        BaseSetBoVo setBoVo = new BaseSetBoVo();
        setBoVo.setCode("project");
        setBoVo.setId(1);
        setBoVo.setName("项目设置");
        retVo.add(setBoVo);

        setBoVo = new BaseSetBoVo();
        setBoVo.setCode("time");
        setBoVo.setId(2);
        setBoVo.setName("时间设置");
        retVo.add(setBoVo);

        setBoVo = new BaseSetBoVo();
        setBoVo.setCode("doc");
        setBoVo.setId(3);
        setBoVo.setName("文档设置");
        retVo.add(setBoVo);

        return retVo;
    }

    /**
     * 修改项目全局设置
     *
     * @param baseSetProjectForm
     */
    @Override
    @AddLog(title = "修改项目设置", module = LoggerModuleEnum.BM_GLOBALD)
    public void updateProject(BaseSetProjectUpdateForm baseSetProjectForm) {

        this.updateBaseProjectSet(baseSetProjectForm, "project");
    }

    private void updateBaseProjectSet(BaseSetProjectUpdateForm baseSetProjectForm, String project) {
        //修改内容
        StringBuffer content = new StringBuffer();
        if (!ObjectUtils.isEmpty(baseSetProjectForm.getCpmFloat())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(project);
            baseSetPo.setBsKey("cpmFloat");
            baseSetPo.setBsValue(String.valueOf(baseSetProjectForm.getCpmFloat()));
            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(project,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"总浮时");
        }
        if (!ObjectUtils.isEmpty(baseSetProjectForm.getCpmType())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(project);
            baseSetPo.setBsKey("cpmType");
            baseSetPo.setBsValue(String.valueOf(baseSetProjectForm.getCpmType()));

            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(project,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"关键路径");
        }
        if (!ObjectUtils.isEmpty(baseSetProjectForm.getEnableProjectTeam())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(project);
            baseSetPo.setBsKey("enableProjectTeam");
            baseSetPo.setBsValue(String.valueOf(baseSetProjectForm.getEnableProjectTeam()));

            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(project,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"启用项目管理团队");

        }
        if (!ObjectUtils.isEmpty(baseSetProjectForm.getMessage())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(project);
            baseSetPo.setBsKey("message");
            baseSetPo.setBsValue(String.valueOf(baseSetProjectForm.getMessage()));

            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(project,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"启用消息推送");

        }
        if (!ObjectUtils.isEmpty(baseSetProjectForm.getTaskDrtnType())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(project);
            baseSetPo.setBsKey("taskDrtnType");
            baseSetPo.setBsValue(String.valueOf(baseSetProjectForm.getTaskDrtnType()));

            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(project,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"工期类型");

        }
        if (!ObjectUtils.isEmpty(baseSetProjectForm.getShareWbs())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(project);
            baseSetPo.setBsKey("shareWbs");
            baseSetPo.setBsValue(String.valueOf(baseSetProjectForm.getShareWbs()));

            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(project,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"启用WBS内部共享");
        }
        // 添加修改日志
        this.setAcmLogger(new AcmLogger("修改项目设置," + content));
    }

    //添加日志
    public void addUpdateBaseSetLog(BaseSetPo baseSetPo,List<BaseSetPo> baseSetPoList,StringBuffer content,String updateType){
        if (!ObjectUtils.isEmpty(baseSetPoList) && !baseSetPo.getBsValue().equals(baseSetPoList.get(0).getBsValue())){
            if(content.length() > 0){
                content.append(",");
            }
            content.append(updateType+"由【").append(baseSetPoList.get(0).getBsValue()).append("】修改为【").append(baseSetPo.getBsValue()).append("】");
            mapper.updateBaseSet(baseSetPo);
        }
    }

    /**
     * 修改文档全局设置
     *
     * @param baseSetDocForm
     */
    @Override
    @AddLog(title = "修改文档设置", module = LoggerModuleEnum.BM_GLOBALD)
    public void updateDoc(BaseSetDocUpdateForm baseSetDocForm) {
        this.updateDocBaseSet(baseSetDocForm, "doc");
    }

    private void updateDocBaseSet(BaseSetDocUpdateForm baseSetDocForm, String doc) {
        //修改内容
        StringBuffer content = new StringBuffer();
        if (!ObjectUtils.isEmpty(baseSetDocForm.getUploadMax())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(doc);
            baseSetPo.setBsKey("uploadMax");
            baseSetPo.setBsValue(String.valueOf(baseSetDocForm.getUploadMax()));

            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(doc,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"上传文件大小");
        }
        if (!ObjectUtils.isEmpty(baseSetDocForm.getBanFileType())) {
            StringBuffer stringBuffer = new StringBuffer("[");
            for (String value : baseSetDocForm.getBanFileType()){
                stringBuffer.append(value + ",");
            }
            int last = stringBuffer.lastIndexOf(",");
            String tet = stringBuffer.substring(0,last);
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(doc);
            baseSetPo.setBsKey("banFileType");
            baseSetPo.setBsValue(tet+"]");
            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(doc,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"禁止文件格式");
        }
        // 添加修改日志
        this.setAcmLogger(new AcmLogger("修改文档设置," + content));
    }

    /**
     * 修改时间设置
     *
     * @param baseSetTimeForm
     */
    @Override
    @AddLog(title = "修改时间设置", module = LoggerModuleEnum.BM_GLOBALD)
    public void updateTime(BaseSetTimeUdateForm baseSetTimeForm) {
        this.updateTimeBaseSet(baseSetTimeForm, "time");
    }

    @Override
    public List<BaseBoVo> getTrainTime() {
        List<BaseBoVo> list=Lists.newArrayList();
        List<BaseSetPo> queryBaseSetPoList=queryBaseSetPoList("train");
        for(BaseSetPo po:queryBaseSetPoList){
            BaseBoVo vo=new BaseBoVo();
            if(po.getBsKey().equals("line1")){
                vo.setId(1);
            }
            if(po.getBsKey().equals("line2")){
                vo.setId(2);
            }
            if(po.getBsKey().equals("line3")){
                vo.setId(3);
            }
            vo.setBoCode(po.getBsKey());
            vo.setBoName(po.getBsValue());
            list.add(vo);
        }
        return list;
    }

    /**
     * 修改客车运营时间
     * @param dates
     */
    @Override
    public void updateTrainTime(Map<String,String> dates) {
        //线路分3条
        StringBuffer content = new StringBuffer();
        if(ObjectUtils.isEmpty(dates)){//传递的是空集合，则说明是定时器执行
            List<BaseSetPo> queryBaseSetPoList=queryBaseSetPoList("train");
            for(BaseSetPo po:queryBaseSetPoList){
                po.setBsValue(String.valueOf(Integer.valueOf(po.getBsValue())+1));
                mapper.updateBaseSet(po);
            }
        }else{
                if(StringHelper.isNotNullAndEmpty(dates.get("line1"))){
                    BaseSetPo baseSetPo = new BaseSetPo();
                    baseSetPo.setBoCode("train");
                    baseSetPo.setBsKey("line1");
                    baseSetPo.setBsValue(dates.get("line1"));
                    List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey("train",baseSetPo.getBsKey());
                    //修改内容日志
                    this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"一号线");
                }
                if(StringHelper.isNotNullAndEmpty(dates.get("line2"))){
                    BaseSetPo baseSetPo = new BaseSetPo();
                    baseSetPo.setBoCode("train");
                    baseSetPo.setBsKey("line2");
                    baseSetPo.setBsValue(dates.get("line2"));
                    List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey("train",baseSetPo.getBsKey());
                    //修改内容日志
                    this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"二号线");
                }
                if(StringHelper.isNotNullAndEmpty(dates.get("line3"))){
                    BaseSetPo baseSetPo = new BaseSetPo();
                    baseSetPo.setBoCode("train");
                    baseSetPo.setBsKey("line3");
                    baseSetPo.setBsValue(dates.get("line3"));
                    List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey("train",baseSetPo.getBsKey());
                    //修改内容日志
                    this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"三号线");
                }
        }
        // 添加修改日志
        this.setAcmLogger(new AcmLogger("修改时间设置," + content));
    }

    private void updateTimeBaseSet(BaseSetTimeUdateForm baseSetTimeForm, String time) {
        //修改内容
        StringBuffer content = new StringBuffer();
        if (!ObjectUtils.isEmpty(baseSetTimeForm.getDateFormat())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(time);
            baseSetPo.setBsKey("dateFormat");
            baseSetPo.setBsValue(String.valueOf(baseSetTimeForm.getDateFormat()));
            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(time,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"日期格式");
        }
        if (!ObjectUtils.isEmpty(baseSetTimeForm.getDrtnUnit())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(time);
            baseSetPo.setBsKey("drtnUnit");
            baseSetPo.setBsValue(String.valueOf(baseSetTimeForm.getDrtnUnit()));
            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(time,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"工期单位");
        }
        if (!ObjectUtils.isEmpty(baseSetTimeForm.getTimeUnit())) {
            BaseSetPo baseSetPo = new BaseSetPo();
            baseSetPo.setBoCode(time);
            baseSetPo.setBsKey("timeUnit");
            baseSetPo.setBsValue(String.valueOf(baseSetTimeForm.getTimeUnit()));
            List<BaseSetPo> baseSetPoList = this.queryBaseSetPoByCodeAndKey(time,baseSetPo.getBsKey());
            //修改内容日志
            this.addUpdateBaseSetLog(baseSetPo,baseSetPoList,content,"工时单位");
        }
        // 添加修改日志
        this.setAcmLogger(new AcmLogger("修改时间设置," + content));
    }

    /**
     * 项目全局设置信息
     *
     * @return
     */
    @Override
    public BaseSetProjectVo getProjectInfo() {
        //获得所有属性的每个的BaseSetPo
        BaseSetPo taskDrtnTypePo = this.getBaseSetPoByBoCodeAndKey("project", "taskDrtnType");
        BaseSetPo cpmTypePo = this.getBaseSetPoByBoCodeAndKey("project", "cpmType");
        BaseSetPo cpmFloatPo = this.getBaseSetPoByBoCodeAndKey("project", "cpmFloat");
        BaseSetPo enableProjectTeamPo = this.getBaseSetPoByBoCodeAndKey("project", "enableProjectTeam");
        BaseSetPo shareWbsPo = this.getBaseSetPoByBoCodeAndKey("project", "shareWbs");
        BaseSetPo messagePo = this.getBaseSetPoByBoCodeAndKey("project", "message");

        //新建返回BaseSetProjectVo
        BaseSetProjectVo baseSetProjectVo = new BaseSetProjectVo();

        List<String> dictList = new ArrayList<>();
        dictList.add("plan.project.taskdrtntype");
        dictList.add("plan.project.cpmtype");
        DictionarysMap dictionarysMap  = dictService.getDictMapByTypeCode(dictList);
        //赋值
        if (!ObjectUtils.isEmpty(taskDrtnTypePo.getBsValue())) {
            String name = dictionarysMap.getDictionaryName("plan.project.taskdrtntype", taskDrtnTypePo.getBsValue());
            GeneralVo generalVo = new GeneralVo();
            generalVo.setCode(taskDrtnTypePo.getBsValue());
            generalVo.setName(name);
            baseSetProjectVo.setTaskDrtnType(generalVo);
        }
        if (!ObjectUtils.isEmpty(cpmTypePo.getBsValue())) {
            String name = dictionarysMap.getDictionaryName("plan.project.cpmtype", cpmTypePo.getBsValue());
            GeneralVo generalVo = new GeneralVo();
            generalVo.setCode(cpmTypePo.getBsValue());
            generalVo.setName(name);
            baseSetProjectVo.setCpmType(generalVo);
        }
        baseSetProjectVo.setCpmFloat(Double.valueOf(cpmFloatPo.getBsValue()));
        baseSetProjectVo.setEnableProjectTeam(Integer.valueOf(enableProjectTeamPo.getBsValue()));
        baseSetProjectVo.setShareWbs(Integer.valueOf(shareWbsPo.getBsValue()));
        baseSetProjectVo.setMessage(Integer.valueOf(messagePo.getBsValue()));

        return baseSetProjectVo;
    }

    /**
     * 文档全局设置信息
     *
     * @return
     */
    @Override
    public BaseSetDocVo getDocInfo() {
        //获得所有属性的每个的BaseSetPo
        BaseSetPo uploadMaxPo = this.getBaseSetPoByBoCodeAndKey("doc", "uploadMax");
        BaseSetPo banFileTypePo = this.getBaseSetPoByBoCodeAndKey("doc", "banFileType");
        //新建
        BaseSetDocVo baseSetDocVo = new BaseSetDocVo();
        //赋值
        baseSetDocVo.setUploadMax(Double.valueOf(uploadMaxPo.getBsValue()));
        String str = banFileTypePo.getBsValue().replace("[", "").replace("]", "");
        List<String> lis = Arrays.asList(str.split(","));
        baseSetDocVo.setBanFileType(lis);
        return baseSetDocVo;
    }

    /**
     * 时间全局设置信息
     *
     * @return
     */
    @Override
    public BaseSetTimeVo getTimeInfo() {
        //获得所有属性的每个的BaseSetPo
        BaseSetPo timeUnitPo = this.getBaseSetPoByBoCodeAndKey("time", "timeUnit");
        BaseSetPo drtnUnitPo = this.getBaseSetPoByBoCodeAndKey("time", "drtnUnit");
        BaseSetPo dateFormatPo = this.getBaseSetPoByBoCodeAndKey("time", "dateFormat");
        //新建
        BaseSetTimeVo baseSetTimeVo = new BaseSetTimeVo();

        List<String> dictList = new ArrayList<>();
        dictList.add("plan.project.timeunit");
        dictList.add("plan.project.drtnunit");
        dictList.add("base.date.formate");
        DictionarysMap dictionarysMap  = dictService.getDictMapByTypeCode(dictList);
        //赋值
        if (!ObjectUtils.isEmpty(timeUnitPo.getBsValue())) {
            String name = dictionarysMap.getDictionaryName("plan.project.timeunit", timeUnitPo.getBsValue());
            GeneralVo generalVo = new GeneralVo();
            generalVo.setCode(timeUnitPo.getBsValue());
            generalVo.setName(name);
            baseSetTimeVo.setTimeUnit(generalVo);
        }
        if (!ObjectUtils.isEmpty(timeUnitPo.getBsValue())) {
            String name = dictionarysMap.getDictionaryName("plan.project.drtnunit", drtnUnitPo.getBsValue());
            GeneralVo generalVo = new GeneralVo();
            generalVo.setCode(drtnUnitPo.getBsValue());
            generalVo.setName(name);
            baseSetTimeVo.setDrtnUnit(generalVo);
        }
        if (!ObjectUtils.isEmpty(timeUnitPo.getBsValue())) {
            String name = dictionarysMap.getDictionaryName("base.date.formate", dateFormatPo.getBsValue());
            GeneralVo generalVo = new GeneralVo();
            generalVo.setCode(dateFormatPo.getBsValue());
            generalVo.setName(name);
            baseSetTimeVo.setDateFormat(generalVo);
        }
        return baseSetTimeVo;
    }


    /**
     * 修改全局设置
     *
     * @param
     */
    public void updateBaseSet(Object object, String boCode) {

//        /**
//         *  获取属性名(name)
//         */
//        List<String> list = this.getFiledsName(object);
//        for (int i = 0; i < list.size(); i++) {
//            //根据boCode和key值获取要修改的baseSetPo
//            BaseSetPo baseSetPo = this.getBaseSetPoByBoCodeAndKey(boCode, list.get(i));
//            //根据属性名获得属性值
//            Object name = this.getFieldValueByName(list.get(i), object);
//            //id
//            if (Objects.isNull(baseSetPo)) {
//                //如果是空，则为增加
//                baseSetPo = new BaseSetPo();
//                //业务编码
//                baseSetPo.setBoCode(boCode);
//                //key
//                baseSetPo.setBsKey(list.get(i));
//                //value
//                this.insert(baseSetPo);
//            } else {
//                //不为空时修改
//                //业务编码
//                baseSetPo.setBoCode(boCode);
//                //key
//                baseSetPo.setBsKey(list.get(i));
//                //value
//                if ("banFileType".equals(list.get(i))) {
//                    baseSetPo.setBsValue(String.valueOf(name).replace(" ", ""));
//                } else {
//                    baseSetPo.setBsValue(String.valueOf(name));
//                }
//                System.out.println("================================================================>" + baseSetPo.toString());
//                //保存修改
//                this.updateById(baseSetPo);
//            }
//        }
    }

    /**
     * 获取属性名(key)的list
     */
    private List<String> getFiledsName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List list = new ArrayList();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            list.add(fieldName);
        }
        return list;
    }

    /**
     * 根据属性名获取属性值(value)
     */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取基础设置集合
     *
     * @param boCode
     * @return
     */
    public List<BaseSetPo> queryBaseSetPoList(String boCode) {
        Example example = new Example(BaseSetPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode", boCode);
        return this.selectByExample(example);
    }

    /**
     * 根据bocode和key查询baseSetPo
     *
     * @param boCode
     * @param key
     * @return
     */
    @Override
    public BaseSetPo getBaseSetPoByBoCodeAndKey(String boCode, String key) {
        Example example = new Example(BaseSetPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode", boCode);
        criteria.andEqualTo("bsKey", key);
        return this.selectOneByExample(example);
    }

    /**
     * 根据boCode 和 bskey 查询全局设置po
     * @param boCode
     * @param bsKey
     * @return
     */
    @Override
    public List<BaseSetPo> queryBaseSetPoByCodeAndKey(String boCode,String bsKey){
        Example example = new Example(BaseSetPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode",boCode);
        criteria.andEqualTo("bsKey",bsKey);
        List<BaseSetPo> baseSetPos = this.selectByExample(example);
        return ObjectUtils.isEmpty(baseSetPos) ? null : baseSetPos;
    }
}
