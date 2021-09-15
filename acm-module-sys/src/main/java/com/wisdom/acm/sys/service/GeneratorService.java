package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.basisrc.GeneratorUtils;
import com.wisdom.acm.sys.mapper.GeneratorMapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author Mr.AG
 * @email 463540703@qq.com
 * @date 2017年08月25日
 */
@Service
public class GeneratorService {
    @Autowired
    private GeneratorMapper generatorMapper;

    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        int offset = Integer.parseInt(map.get("offset").toString());
        int limit = Integer.parseInt(map.get("limit").toString());
        map.put("offset", offset);
        map.put("limit", limit);
        return generatorMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return generatorMapper.queryTotal(map);
    }

    public Map<String, String> queryTable(String tableName) {
        return generatorMapper.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorMapper.queryColumns(tableName);
    }

    public byte[] generatorCode(List<String> tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GeneratorUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    public void reactCode(Map<String, String> sqlMap) {

        String sql = sqlMap.get("sql");
        String mudole = sqlMap.get("mudole");
        String table = sqlMap.get("table");

        //查询列信息
        Map<String, String> columns = queryReactColumns(sql);
        List<Map<String, String>> vmList = new ArrayList<>();
        Map<String, String> vmMap = null;
        Iterator<String> iter = columns.keySet().iterator();
        while (iter.hasNext()) {
            vmMap = new HashMap<>();
            String key = iter.next();
            vmMap.put("dataIndex", key);
            vmMap.put("key", key);
            vmMap.put("title", "wsd.i18n." + mudole + "." + table + "." + key.toLowerCase() + "");
            vmList.add(vmMap);
        }
        //生成代码
        GeneratorUtils.generatorReactCode(vmList, table);

    }

    private LinkedHashMap<String, String> queryReactColumns(String sql) {
        return generatorMapper.queryReactColumns(sql);
    }


    public byte[] generatorJavaCode(String tableName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

//		for(String tableName : tableNames){
        //查询表信息
        Map<String, String> table = queryTable(tableName);
        //查询列信息
        List<Map<String, String>> columns = queryColumns(tableName);
        //生成代码
        GeneratorUtils.generatorJavaCode(table, columns, zip);
//		}
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    public void formCode(Map<String, String> sqlMap) {
        String sql = sqlMap.get("sql");
        String mudole = sqlMap.get("mudole");
        String table = sqlMap.get("table");

        //查询列信息
        Map<String, String> columns = queryReactColumns(sql);
        List<Map<String, Object>> vmList = new ArrayList<>();
        Map<String, Object> vmMap = null;
        Iterator<String> iter = columns.keySet().iterator();
        while (iter.hasNext()) {
            vmMap = new HashMap<>();
            String key = iter.next();
            if (key.toLowerCase().contains("type") || key.equals("iptName") || key.equals("userName") || key.equals("categoryCode") || key.toLowerCase().contains("level")) {
                vmMap.put("xiala", 1);
                vmMap.put("riqi", 2);
                vmMap.put("dataIndex", key);
                vmMap.put("key", key);
                vmMap.put("message", "wsd.i18n." + mudole + "." + table + "." + key.toLowerCase() + ".message");
                vmMap.put("title", "wsd.i18n." + mudole + "." + table + "." + key.toLowerCase() + "");
                vmList.add(vmMap);
            } else if (key.toLowerCase().contains("time")) {
                vmMap.put("riqi", 1);
                vmMap.put("xiala", 2);
                vmMap.put("dataIndex", key);
                vmMap.put("key", key);
                vmMap.put("message", "wsd.i18n." + mudole + "." + table + "." + key.toLowerCase() + ".message");
                vmMap.put("title", "wsd.i18n." + mudole + "." + table + "." + key.toLowerCase() + "");
                vmList.add(vmMap);
            } else {
                vmMap.put("xiala", 2);
                vmMap.put("riqi", 2);
                vmMap.put("dataIndex", key);
                vmMap.put("key", key);
                vmMap.put("message", "wsd.i18n." + mudole + "." + table + "." + key.toLowerCase() + ".message");
                vmMap.put("title", "wsd.i18n." + mudole + "." + table + "." + key.toLowerCase() + "");
                vmList.add(vmMap);
            }

        }
        //生成代码
        GeneratorUtils.generatorFormCode(vmList, table);
    }
}
