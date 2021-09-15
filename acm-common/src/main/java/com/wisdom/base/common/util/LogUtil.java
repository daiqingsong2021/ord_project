package com.wisdom.base.common.util;


import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.po.BasePo;
import com.wisdom.base.common.vo.log.LogContentsVo;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogUtil {

    public static String getEditLog(String name,String oldValue, String newValue){
        String log = name + "由["+ oldValue +"]修改为["+newValue+"]";
        return  log ;
    }


    public static String getValue(Method m, Object o){
        Object ret = null;
        try {
            ret = m.invoke(o,new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return FormatUtil.toString(ret);
    }

    public static Map<String,Method> getMethodMap(Method[] methods ){

        Map<String,Method> retmap = new HashMap<>();
        if(!ObjectUtils.isEmpty(methods)){
            for(Method method : methods){
                Method m = BridgeMethodResolver.findBridgedMethod(method);
                retmap.put(method.getName().toLowerCase(),method);
            }
        }

        return retmap;
    }

    /**
     *
     * @param form
     * @param po
     * @return
     */
    public static String getEditChangeLogContent(BaseForm form, BasePo po){
        StringBuffer content = new StringBuffer();
        try {
            Class<?> c = form.getClass();
            Class<?> p = po.getClass();

            Map<String, Method> formMap = getMethodMap(c.getMethods());
            Map<String, Method> poMap = getMethodMap(p.getMethods());
            //
            Field[] fields = c.getDeclaredFields();

            for (Field f : fields) {

                //获取字段中包含fieldMeta的注解
                LogParam logParam = f.getAnnotation(LogParam.class);
                System.out.println("filedName:"+ f.getName());
                if (logParam != null) {

                    String title = logParam.title();
                    System.out.println(title);
                    Method m = formMap.get("get" + f.getName().toLowerCase());
                    Method pm = poMap.get("get" + f.getName().toLowerCase());
                    if( m == null || pm == null){

                        String formValue = getValue(m, form);
                        String poValue = getValue(pm, po);
                        boolean change = !formValue.equals(poValue);
                        if (change) {
                            if (content.length() > 0) {
                                content.append(",");
                            }
                            content.append(title).append("由【").append(poValue).append("】修改为【").append(formValue).append("】");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content.toString();
    }

    /**
     *
     * @param form
     * @param po
     * @return
     */
    public static List<LogContentsVo> getEditChangeLogContent2(BaseForm form, BasePo po){

        List<LogContentsVo> retlist = new ArrayList<>();
        try {
            Class<?> c = form.getClass();
            Class<?> p = po.getClass();
            // 获取form数据
            Map<String, Method> formMap = getMethodMap(c.getMethods());
            // 获取PO数据
            Map<String, Method> poMap = getMethodMap(p.getMethods());
            // 所有字段
            Field[] fields = c.getDeclaredFields();

            for (Field f : fields) {

                //获取字段中包含LogParam的注解的字段
                LogParam logParam = f.getAnnotation(LogParam.class);
                System.out.println("filedName:"+ f.getName());
                if (logParam != null) {

                    String title = logParam.title();
                    System.out.println(title);
                    Method m = formMap.get("get" + f.getName().toLowerCase());
                    Method pm = poMap.get("get" + f.getName().toLowerCase());
                    if( m != null && pm != null){
                        // 新值
                        String formValue = getValue(m, form);
                        // 就值
                        String poValue = getValue(pm, po);
                        boolean change = !formValue.equals(poValue);
                        if (change) {
                            LogContentsVo vo = new LogContentsVo();
                            vo.setTitle(title);
                            vo.setNewValue(formValue);
                            vo.setOldValue(poValue);
                            vo.setType(logParam.type().getCode());
                            vo.setTypeValue(logParam.dictType());
                            retlist.add(vo);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retlist;
    }

    /**
     *
     * @param form
     * @return
     */
    public static List<LogContentsVo> getAddLogContent(BaseForm form){
        List<LogContentsVo> retlist = new ArrayList<>();
        try {
            Class<?> c = form.getClass();
            // 获取form数据
            Map<String, Method> formMap = getMethodMap(c.getMethods());
            // 所有字段
            Field[] fields = c.getDeclaredFields();

            for (Field f : fields) {

                //获取字段中包含LogParam的注解的字段
                LogParam logParam = f.getAnnotation(LogParam.class);
                System.out.println("filedName:"+ f.getName());
                if (logParam != null) {

                    String title = logParam.title();
                    System.out.println(title);
                    Method m = formMap.get("get" + f.getName().toLowerCase());
                    if( m != null ){
                        // 新值
                        String formValue = getValue(m, form);
                        if(!ObjectUtils.isEmpty(formValue)){
                            LogContentsVo vo = new LogContentsVo();
                            vo.setTitle(title);
                            vo.setNewValue(formValue);
                            vo.setType(logParam.type().getCode());
                            vo.setTypeValue(logParam.dictType());
                            retlist.add(vo);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retlist;
    }

    /**
     *新增form表单 日志中加入 日期 ，线路 ，状态
     * @param form
     */
    public static AcmLogger addLogger(BaseForm form){

        // 所有字段
        Class<?> p = form.getClass();
        // 获取PO数据
        Map<String, Method> poMap =LogUtil.getMethodMap(p.getMethods());
        // 所有字段
        Field[] fields = p.getDeclaredFields();
        String recordTime="";
        String line="";
        String recordId="";
        String recordStatus="INIT";
        for (Field f : fields)
        {
            Method pm = poMap.get("get" + f.getName().toLowerCase());
            String poValue = getValue(pm, form);
            if("recordtime".equals(f.getName().toLowerCase()))
            {
                recordTime= poValue;
            }
            if("recordday".equals(f.getName().toLowerCase()))
            {
                recordTime= poValue;
            }
            if("line".equals(f.getName().toLowerCase()))
            {
                line= poValue;
            }

        }

        AcmLogger logger = new AcmLogger(recordTime,line,recordId,recordStatus);
        return logger;
    }
}
