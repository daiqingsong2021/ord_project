package com.wisdom.acm.szxm.common;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonHelper
{
    private static final Gson gson = getGson();

    private static Gson getGson()
    {
       return new Gson();
    }

    /**
     * 对象转变为JSON
     * toJsonWithGson:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 - 可选).<br/>
     * TODO(这里描述这个方法的执行流程 - 可选).<br/>
     * TODO(这里描述这个方法的使用方法 - 可选).<br/>
     * TODO(这里描述这个方法的注意事项 - 可选).<br/>
     *
     * @author wyf
     * @param obj
     * @return
     * @since JDK 1.6
     */
    public static String toJsonWithGson(Object obj)
    {

        return gson.toJson(obj);
    }
    
    public static String toJsonWithGson(Object obj, Type type)
    {
        return gson.toJson(obj, type);
    }

    /**
     * List转变为JSOn
     * toJsonWithGson:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 - 可选).<br/>
     * TODO(这里描述这个方法的执行流程 - 可选).<br/>
     * TODO(这里描述这个方法的使用方法 - 可选).<br/>
     * TODO(这里描述这个方法的注意事项 - 可选).<br/>
     *
     * @author wyf
     * @param list
     * @return
     * @since JDK 1.6
     */
    @SuppressWarnings("unchecked")
    public static String toJsonWithGson(List list)
    {
        return gson.toJson(list);
    }

    @SuppressWarnings("unchecked")
    public static String toJsonWithGson(List list, Type type)
    {
        return gson.toJson(list, type);
    }
 
    /**
     * JSON转变为对象
     * fromJsonWithGson:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 - 可选).<br/>
     * TODO(这里描述这个方法的执行流程 - 可选).<br/>
     * TODO(这里描述这个方法的使用方法 - 可选).<br/>
     * TODO(这里描述这个方法的注意事项 - 可选).<br/>
     *
     * @author wyf
     * @param jsonStr
     * @param classOfT 类对象
     * @return
     * @since JDK 1.6
     */
    public static <T> T fromJsonWithGson(String jsonStr, Class<T> classOfT)
    {
        return gson.fromJson(jsonStr, classOfT);
    }

    /**
     * JSON转变为对象
     * fromJsonWithGson:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 - 可选).<br/>
     * TODO(这里描述这个方法的执行流程 - 可选).<br/>
     * TODO(这里描述这个方法的使用方法 - 可选).<br/>
     * TODO(这里描述这个方法的注意事项 - 可选).<br/>
     *
     * @author wyf
     * @param jsonStr
     * @param typeofT  反射类型对象
     * @return
     * @since JDK 1.6
     */
    public static <T> T fromJsonWithGson(String jsonStr, Type typeofT)
    {
        return gson.fromJson(jsonStr, typeofT);
    }

    public static void main(String args[])
    {
        String jsonStr="{\"errcode\":0,\"errmsg\":\"ok\",\"checkindata\":[{\"userid\":\"ZhangYingHeng\",\"groupname\":\"移动考勤\",\"checkin_type\":\"上班打卡\",\"exception_type\":\"未打卡\",\"checkin_time\":1568680200,\"location_title\":\"\",\"location_detail\":\"\",\"wifiname\":\"\",\"notes\":\"\",\"wifimac\":\"\",\"mediaids\":[]},{\"userid\":\"HanXiaoFei\",\"groupname\":\"移动考勤\",\"checkin_type\":\"上班打卡\",\"exception_type\":\"时间异常\",\"checkin_time\":1568683685,\"location_title\":\"苏州市轨道交通集团有限公司\",\"location_detail\":\"江苏省苏州市姑苏区石路街道干将西路668号\",\"wifiname\":\"\",\"notes\":\"\",\"wifimac\":\"\",\"mediaids\":[]},{\"userid\":\"ZhangJing\",\"groupname\":\"移动考勤\",\"checkin_type\":\"上班打卡\",\"exception_type\":\"时间异常\",\"checkin_time\":1568688086,\"location_title\":\"苏州市轨道交通集团有限公司\",\"location_detail\":\"江苏省苏州市姑苏区石路街道干将西路668号\",\"wifiname\":\"\",\"notes\":\"\",\"wifimac\":\"\",\"mediaids\":[]},{\"userid\":\"HanXiaoFei\",\"groupname\":\"移动考勤\",\"checkin_type\":\"下班打卡\",\"exception_type\":\"未打卡\",\"checkin_time\":1568712600,\"location_title\":\"\",\"location_detail\":\"\",\"wifiname\":\"\",\"notes\":\"\",\"wifimac\":\"\",\"mediaids\":[]},{\"userid\":\"ZhangJing\",\"groupname\":\"移动考勤\",\"checkin_type\":\"下班打卡\",\"exception_type\":\"未打卡\",\"checkin_time\":1568712600,\"location_title\":\"\",\"location_detail\":\"\",\"wifiname\":\"\",\"notes\":\"\",\"wifimac\":\"\",\"mediaids\":[]},{\"userid\":\"ZhangYingHeng\",\"groupname\":\"移动考勤\",\"checkin_type\":\"下班打卡\",\"exception_type\":\"未打卡\",\"checkin_time\":1568712600,\"location_title\":\"\",\"location_detail\":\"\",\"wifiname\":\"\",\"notes\":\"\",\"wifimac\":\"\",\"mediaids\":[]}]}";
        JSONObject jsonObject=JSONObject.parseObject(jsonStr);
        Map<String,Object> returnMap=jsonObject.toJavaObject(Map.class);
        System.out.println(returnMap.get("errcode"));
        List<Map<String,Object>> checkindata=(List<Map<String,Object>>)returnMap.get("checkindata");
        for(Map<String,Object> userInfo:checkindata)
        {
            String checkinTimeStamp=String.valueOf(userInfo.get("checkin_time"));//uninx 时间戳
            System.out.println(checkinTimeStamp);
        }
    }
}
