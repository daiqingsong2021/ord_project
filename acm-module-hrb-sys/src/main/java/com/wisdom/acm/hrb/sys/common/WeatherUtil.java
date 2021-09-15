package com.wisdom.acm.hrb.sys.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zll
 * 2020/8/17/017 10:30
 * Description:<查询指定城市天气情况>
 */
public class WeatherUtil {
    public static void main(String[] args) {
        try {
            //测试获取实时天气1(包含风况，湿度)
            //Map<String, Object> map = getTodayWeather("101190101");
            Map<String, Object> map = getTodayWeather("南京");
            System.out.println(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getTodayWeather(String url1)
            throws IOException, NullPointerException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            URLConnection connectionData = null;
            // 连接和风天气的API      3c3fa198cacc4152b94b20def11b2455
            URL url = new URL(url1);
            connectionData = url.openConnection();
            connectionData.setConnectTimeout(1000);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connectionData.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                sb.append(line);
            String datas = sb.toString();
            JSONObject jsonData = JSONObject.parseObject(datas);
            JSONArray info = jsonData.getJSONArray("HeWeather6");
            JSONObject jsonObject = (JSONObject) info.get(0);
            if (null == jsonObject.getString("now")) return map;
            JSONObject nowData = JSONObject.parseObject(jsonObject.getString("now").toString());

            JSONArray dailyForecast = jsonObject.getJSONArray("daily_forecast");
            JSONObject todayTmp = (JSONObject) dailyForecast.get(0);
            map.put("tmpMax", todayTmp.getString("tmp_max")); //当天最高气温
            map.put("tmpMin", todayTmp.getString("tmp_min")); //当天最低气温
            map.put("condTxt", todayTmp.getString("cond_txt_d"));  //当天天气
            map.put("date", todayTmp.getString("date")); //当天日期
            map.put("realTimeTmp", nowData.getString("tmp")); //实时温度
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时！！");
        } catch (FileNotFoundException e) {
            System.out.println("加载文件出错！！");
        } catch (UnknownHostException e) {
            System.out.println("外部主机不可达，请检查网络！！");
        }finally {
        //关闭流
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}