package com.wisdom.webservice.outorg;


import com.alibaba.fastjson.JSON;
import com.wisdom.webservice.DateUtil;
import com.wisdom.webservice.PropertiesLoaderUtils;
import com.wisdom.webservice.outorg.entity.ADDepartmentInfo;
import com.wisdom.webservice.outorg.entity.ArrayOfADDepartmentInfo;
import com.wisdom.webservice.outorg.entity.UUVPortalOrgServices;
import com.wisdom.webservice.outorg.entity.UUVPortalOrgServicesSoap;
import com.wisdom.webservice.token.MyHeaderHandler;
import org.springframework.util.ObjectUtils;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class OutOrgUtils {
    private String userName;

    private String password;

    private OutOrgUtils() {
    }

    private OutOrgUtils(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public static OutOrgUtils getInstance() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String userName = prop.getProperty("webservice.esb.username");
        String password = prop.getProperty("webservice.esb.password");
        return new OutOrgUtils(userName, password);
    }

    /**
     * 获取调用对象
     *
     * @return
     */
    private UUVPortalOrgServicesSoap getService() {
        UUVPortalOrgServices uuvPortalOrgServices = new UUVPortalOrgServices();

        UUVPortalOrgServicesSoap port = null;
        try {

            //添加Header信息
            uuvPortalOrgServices.setHandlerResolver(new HandlerResolver() {
                @Override
                public List<Handler> getHandlerChain(PortInfo portInfo) {
                    List<Handler> handlerList = new ArrayList<>();

                    handlerList.add(new MyHeaderHandler(userName, password));
                    return handlerList;
                }
            });

            port = uuvPortalOrgServices.getUUVPortalOrgServicesSoap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

    /**
     * 获取外部组织
     *
     * @param beginLastUpdateDate 最后更新起始时间
     * @param endLastUpdateDate   最后更新结束时间
     * @return
     */
    public List<ADDepartmentInfo> getUUVPortalOrgList(String beginLastUpdateDate, String endLastUpdateDate) {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String appId = prop.getProperty("webservice.esb.appId");

        ArrayOfADDepartmentInfo uuvPortalOrgList
                = getService().getUUVPortalOrgList(appId, DateUtil.convertToXMLGregorianCalendar(beginLastUpdateDate, DateUtil.DATE_DEFAULT_FORMAT), DateUtil.convertToXMLGregorianCalendar(endLastUpdateDate, DateUtil.DATE_DEFAULT_FORMAT));
        List<ADDepartmentInfo> adDepartmentInfo = uuvPortalOrgList.getADDepartmentInfo();
        if (ObjectUtils.isEmpty(adDepartmentInfo)) {
            return null;
        }
        return adDepartmentInfo;
    }

    /**
     * 创建外部组织
     *
     * @param orgId   组织ID
     * @param orgName 组织名称
     * @return 是否成功
     */
    public boolean createUUVPortalOrg(String orgId, String orgName) {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String appId = prop.getProperty("webservice.esb.appId");
        return getService().createUUVPortalOrg(orgId, orgName, appId);
    }

    public static void main(String[] args) {

        List<ADDepartmentInfo> uuvPortalOrgList = OutOrgUtils.getInstance().getUUVPortalOrgList("2013-01-01", "2019-01-01");
        if (!uuvPortalOrgList.isEmpty()) {
            for (ADDepartmentInfo adDepartmentInfo : uuvPortalOrgList) {
                System.out.println(JSON.toJSON(adDepartmentInfo));
            }
        }
    }

}
