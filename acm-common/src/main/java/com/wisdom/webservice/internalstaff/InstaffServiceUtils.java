package com.wisdom.webservice.internalstaff;

import com.alibaba.fastjson.JSON;
import com.wisdom.webservice.PropertiesLoaderUtils;
import com.wisdom.webservice.internalstaff.entity.ADDepartment;
import com.wisdom.webservice.internalstaff.entity.ADUser;
import com.wisdom.webservice.internalstaff.entity.ArrayOfADDepartment;
import com.wisdom.webservice.internalstaff.entity.ArrayOfADUser;
import com.wisdom.webservice.internalstaff.entity.UUVGet;
import com.wisdom.webservice.internalstaff.entity.UUVGetSoap;
import com.wisdom.webservice.token.MyHeaderHandler;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class InstaffServiceUtils {
    private String userName;

    private String password;

    public static InstaffServiceUtils getInstance() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String userName = prop.getProperty("webservice.esb.username");
        String password = prop.getProperty("webservice.esb.password");
        return new InstaffServiceUtils(userName, password);
    }

    private InstaffServiceUtils(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * 单例 私有构造
     */
    private InstaffServiceUtils() {
    }

    /**
     * +
     * 获取调用对象
     *
     * @return
     */
    private UUVGetSoap getService() {
        UUVGet uuvGet = new UUVGet();
        UUVGetSoap uuvGetSoap = null;
        try {
            //添加Header信息
            uuvGet.setHandlerResolver(new HandlerResolver() {
                @Override
                public List<Handler> getHandlerChain(PortInfo portInfo) {
                    List<Handler> handlerList = new ArrayList<>();

                    handlerList.add(new MyHeaderHandler(userName, password));
                    return handlerList;
                }
            });
            uuvGetSoap = uuvGet.getUUVGetSoap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuvGetSoap;
    }

    /**
     * 内部人员接口
     *
     * @param isContainPartTimeJob
     * @return
     */
    public List<ADUser> getADUser(boolean isContainPartTimeJob) {
        ArrayOfADUser userList = getService().getUserList(isContainPartTimeJob);
        return userList.getADUser();
    }

    /**
     * 部门信息
     *
     * @return
     */
    public List<ADDepartment> getDepartMentList() {
        ArrayOfADDepartment departMentList = getService().getDepartMentList();
        return departMentList.getADDepartment();
    }

    public static void main(String[] args) {

        List<ADUser> adUsers = InstaffServiceUtils.getInstance().getADUser(true);
        if (!adUsers.isEmpty()) {
            for (ADUser adUser : adUsers) {
                System.out.println(JSON.toJSON(adUser));
            }
        }

        // List<ADDepartment> departMentLists = InstaffServiceUtils.getInstance().getDepartMentList();
        // if (!departMentLists.isEmpty()) {
        //     for (ADDepartment adDepartment : departMentLists) {
        //         System.out.println(JSON.toJSON(adDepartment));
        //     }
        // }
    }
}
