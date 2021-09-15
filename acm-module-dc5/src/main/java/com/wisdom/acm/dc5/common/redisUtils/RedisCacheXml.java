package com.wisdom.acm.dc5.common.redisUtils;

import com.wisdom.base.common.util.ResourceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RedisCacheXml
{
    private static final Logger log = Logger.getLogger(RedisCacheXml.class);
    private static final String REDIS_XML = "redis.xml";
    private static String defaultDataCenter = null;
    private static Map<String, Map<Map<String, String>, TreeSet<String>>> dataCenters = new TreeMap();

    public RedisCacheXml()
    {
    }

    public static String getDefaultDataCenter()
    {
        return defaultDataCenter;
    }

    public static Map<String, Map<Map<String, String>, TreeSet<String>>> getDataCenters()
    {
        return dataCenters;
    }

    public static void setDataCenters(Map<String, Map<Map<String, String>, TreeSet<String>>> dataCenters)
    {
        dataCenters = dataCenters;
    }

    public void load()
    {
        SAXBuilder builder = new SAXBuilder();
        InputStream is = null;

        try
        {
            is = ResourceUtil.findResoureFile("redis.xml");
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            Element e = root.getChild("default-datacenter");
            if (null != e)
            {
                defaultDataCenter = e.getText().trim();
            }

            this.loadDataCenter(root);
        }
        catch (Exception e)
        {
            log.error("加载redis.xml配置文件出错!", e);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                log.error("加载redis.xml配置文件出错!", e);
            }

        }

    }

    private void loadDataCenter(Element root)
    {
        List<Element> datacenters = root.getChildren("datacenter");
        Iterator i$ = datacenters.iterator();

        while (i$.hasNext())
        {
            Element e = (Element) i$.next();
            String dataCenterName = e.getAttributeValue("name");
            if (StringUtils.isBlank(dataCenterName))
            {
                throw new NullPointerException("数据中心名称不能为空!");
            }

            this.loadMaster(dataCenterName, e);
        }

    }

    private void loadMaster(String dataCenterName, Element parent)
    {
        Map<Map<String, String>, TreeSet<String>> map = new HashMap();
        List<Element> masters = parent.getChildren("master");
        Iterator i$ = masters.iterator();

        while (i$.hasNext())
        {
            Element master = (Element) i$.next();
            TreeSet<String> set = new TreeSet();
            Map<String, String> key = new HashMap();
            String masterName = master.getAttributeValue("name");
            key.put("name", masterName);
            String requirepass = master.getAttributeValue("requirepass");
            key.put("requirepass", requirepass);
            String timeout = master.getAttributeValue("timeout");
            key.put("timeout", timeout);
            String database = master.getAttributeValue("database");
            key.put("database", database);
            String maxTotal = master.getAttributeValue("maxTotal");
            key.put("maxTotal", maxTotal);
            String maxIdle = master.getAttributeValue("maxIdle");
            key.put("maxIdle", maxIdle);
            String maxWaitMillis = master.getAttributeValue("maxWaitMillis");
            key.put("maxWaitMillis", maxWaitMillis);
            List<Element> addresses = master.getChildren("address");
            Iterator i$2 = addresses.iterator();

            while (i$2.hasNext())
            {
                Element address = (Element) i$2.next();
                String strAddress = address.getText();
                set.add(strAddress);
            }

            map.put(key, set);
        }

        dataCenters.put(dataCenterName, map);
    }

    private void loadALL(String dataCenterName, Element parent)
    {
        if (dataCenterName.equalsIgnoreCase(defaultDataCenter))
        {
            Map<Map<String, String>, TreeSet<String>> master_map = new HashMap();
            List<Element> masters = parent.getChildren("master");
            Iterator i$ = masters.iterator();

            while (i$.hasNext())
            {
                Element master = (Element) i$.next();
                TreeSet<String> set = new TreeSet();
                Map<String, String> key = new HashMap();
                String requirepass = master.getAttributeValue("requirepass");
                key.put("requirepass", requirepass);
                String timeout = master.getAttributeValue("timeout");
                key.put("timeout", timeout);
                List<Element> addresses = master.getChildren("address");
                Iterator i$2 = addresses.iterator();

                while (i$2.hasNext())
                {
                    Element address = (Element) i$2.next();
                    String strAddress = address.getText();
                    set.add(strAddress);
                }

                master_map.put(key, set);
            }

            dataCenters.put("master", master_map);
            Map<Map<String, String>, TreeSet<String>> slave_map = new HashMap();
            List<Element> slaves = parent.getChildren("slave");
            Iterator i$_ = slaves.iterator();

            while (i$_.hasNext())
            {
                Element slave = (Element) i$_.next();
                TreeSet<String> set = new TreeSet();
                Map<String, String> key = new HashMap();
                String masterauth = slave.getAttributeValue("masterauth");
                key.put("masterauth", masterauth);
                String timeout = slave.getAttributeValue("timeout");
                key.put("timeout", timeout);
                List<Element> addresses = slave.getChildren("address");
                Iterator i$2 = addresses.iterator();

                while (i$2.hasNext())
                {
                    Element address = (Element) i$2.next();
                    String strAddress = address.getText();
                    set.add(strAddress);
                }

                slave_map.put(key, set);
            }

            dataCenters.put("slave", slave_map);
        }
    }
}
