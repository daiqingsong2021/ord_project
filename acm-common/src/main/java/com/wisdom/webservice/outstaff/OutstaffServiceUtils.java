package com.wisdom.webservice.outstaff;


import com.alibaba.fastjson.JSON;
import com.wisdom.webservice.DateUtil;
import com.wisdom.webservice.PropertiesLoaderUtils;
import com.wisdom.webservice.outstaff.entity.ArrayOfUserInfo;
import com.wisdom.webservice.outstaff.entity.UUVPortalUserServices;
import com.wisdom.webservice.outstaff.entity.UUVPortalUserServicesSoap;
import com.wisdom.webservice.outstaff.entity.UserInfo;
import com.wisdom.webservice.token.MyHeaderHandler;
import org.springframework.util.ObjectUtils;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class OutstaffServiceUtils {
    private String userName;

    private String password;

    private OutstaffServiceUtils() {
    }

    private OutstaffServiceUtils(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public static OutstaffServiceUtils getInstance() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String userName = prop.getProperty("webservice.esb.username");
        String password = prop.getProperty("webservice.esb.password");
        return new OutstaffServiceUtils(userName, password);
    }

    /**
     * 获取调用对象
     *
     * @return
     */
    private UUVPortalUserServicesSoap getService() {
        UUVPortalUserServices uuvPortalUserServices = new UUVPortalUserServices();
        UUVPortalUserServicesSoap port = null;
        try {

            //添加Header信息
            uuvPortalUserServices.setHandlerResolver(new HandlerResolver() {
                @Override
                public List<Handler> getHandlerChain(PortInfo portInfo) {
                    List<Handler> handlerList = new ArrayList<>();

                    handlerList.add(new MyHeaderHandler(userName, password));
                    return handlerList;
                }
            });

            port = uuvPortalUserServices.getUUVPortalUserServicesSoap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

    /**
     * 获取外部人员信息
     *
     * @return
     */
    public List<UserInfo> getUUVPortalUserList(boolean isContainPartTimeJob, String beginLastUpdateDate, String endLastUpdateDate) {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String appId = prop.getProperty("webservice.esb.appId");

        ArrayOfUserInfo uuvPortalUserList = getService().getUUVPortalUserList(appId, isContainPartTimeJob, DateUtil.convertToXMLGregorianCalendar(beginLastUpdateDate,DateUtil.DATE_DEFAULT_FORMAT), DateUtil.convertToXMLGregorianCalendar(endLastUpdateDate,DateUtil.DATE_DEFAULT_FORMAT));
        List<UserInfo> userInfo = uuvPortalUserList.getUserInfo();
        if (ObjectUtils.isEmpty(userInfo)) {
            return null;
        }
        return userInfo;
    }

    /**
     * 创建外部人员信息
     *
     * @return
     */
    public boolean createUUVPortalUser(String userId, String orgId, String userName, String loginId, String sex, String mobilePhone, String email, String idCard) {
        return getService().createUUVPortalUser(userId, orgId, userName, loginId, sex, mobilePhone, email, idCard);
    }

    /**
     * 修改外部人员密码
     *
     * @return
     */
    public boolean modifyPassword(String accountName, String newPassword) {
        return getService().modifyPassword(accountName, newPassword);
    }

    public static void main(String[] args) {
        List<UserInfo> uuvPortalUserList = OutstaffServiceUtils.getInstance().getUUVPortalUserList(true, "2013-01-01", "2019-01-01");
        if (!uuvPortalUserList.isEmpty()) {
            for (UserInfo userInfo : uuvPortalUserList) {
                System.out.println(JSON.toJSON(userInfo));
            }
        }
    }
}
