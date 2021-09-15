package com.wisdom.acm.activiti.util;

import com.wisdom.base.common.util.JsonUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.wf.WfActivityVo;

import java.util.Arrays;

public class ExprUtil {

    public static boolean equals(Object objs, Object obj){
        return Tools.toString(objs).equals(Tools.toString(obj));
    }

    public static boolean equalsIgnoreCase(Object objs, Object obj){
        return Tools.toString(objs).equalsIgnoreCase(Tools.toString(obj));
    }

    public static boolean contains(Object objs, Object obj){
        return Tools.toString(objs).contains(Tools.toString(obj));
    }

    public static boolean startsWith(Object objs, Object obj){
        return Tools.toString(objs).startsWith(Tools.toString(obj));
    }

    public static boolean endsWith(Object objs, Object obj){
        return Tools.toString(objs).endsWith(Tools.toString(obj));
    }

    public static boolean matches(Object objs, Object obj){
        return Tools.toString(objs).matches(Tools.toString(obj));
    }

    public static boolean split(Object objs, Object obj1, Object obj2){
        return Arrays.asList(Tools.toString(objs).split(Tools.toString(obj1))).contains(obj2);
    }

    public static int indexOf(Object objs, Object obj){
        return Tools.toString(objs).indexOf(Tools.toString(obj));
    }

    public static int lastIndexOf(Object objs, Object obj){
        return Tools.toString(objs).lastIndexOf(Tools.toString(obj));
    }

    public static boolean existActivity(Object objs, Object obj){
        if(!Tools.isEmpty(objs)){
            WfActivityVo[] nextActParts = JsonUtil.readValue(Tools.toString(objs), WfActivityVo[].class);
            for(WfActivityVo act : nextActParts){
                if(Tools.toString(obj).equals(act.getId())){
                    return true;
                }
            }
        }
        return false;
    }
}
