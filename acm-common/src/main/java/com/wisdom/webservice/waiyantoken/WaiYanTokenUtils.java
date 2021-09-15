package com.wisdom.webservice.waiyantoken;

import com.wisdom.webservice.PropertiesLoaderUtils;
import com.wisdom.webservice.token.MyHeaderHandler;
import com.wisdom.webservice.waiyantoken.entity.GCToken;
import com.wisdom.webservice.waiyantoken.entity.GCTokenSoap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：wqd
 * Date：2019-11-11 14:24
 * Description：<描述>
 */
public class WaiYanTokenUtils {
    private static Logger logger = LoggerFactory.getLogger(WaiYanTokenUtils.class);

    private String userName;

    private String password;

    public static WaiYanTokenUtils getInstance() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String userName = prop.getProperty("webservice.esb.username");
        String password = prop.getProperty("webservice.esb.password");
        return new WaiYanTokenUtils(userName, password);
    }

    private WaiYanTokenUtils(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * 单例 私有构造
     */
    private WaiYanTokenUtils() {
    }

    /**
     * +
     * 获取调用对象
     *
     * @return
     */
    private GCTokenSoap getService() {
        GCToken gcToken = new GCToken();
        GCTokenSoap gcTokenSoap = null;
        try {
            //添加Header信息
            gcToken.setHandlerResolver(new HandlerResolver() {
                @Override
                public List<Handler> getHandlerChain(PortInfo portInfo) {
                    List<Handler> handlerList = new ArrayList<>();

                    handlerList.add(new MyHeaderHandler(userName, password));
                    return handlerList;
                }
            });
            gcTokenSoap = gcToken.getGCTokenSoap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gcTokenSoap;
    }

    /**
     * 获取合同外延登录token
     *
     * @return
     */
    public String getToken() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String token = getService().get(prop.getProperty("webservice.hetongwaiyantoken.key"));
        if (StringUtils.isBlank(token)) {
            logger.error("获取合同外延token失败");
            return null;
        }

        return token;
    }

    public static void main(String[] args) {
        String token = getInstance().getToken();
        System.out.println(token);
    }
}
