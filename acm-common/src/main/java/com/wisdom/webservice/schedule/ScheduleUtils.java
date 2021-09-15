package com.wisdom.webservice.schedule;


import com.wisdom.webservice.DateUtil;
import com.wisdom.webservice.PropertiesLoaderUtils;
import com.wisdom.webservice.schedule.entity.WFApplicationToDoService;
import com.wisdom.webservice.schedule.entity.WFApplicationToDoServiceSoap;
import com.wisdom.webservice.token.MyHeaderHandler;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class ScheduleUtils {

    private String userName;

    private String password;

    private ScheduleUtils() {
    }

    public static ScheduleUtils getInstance() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String userName = prop.getProperty("webservice.esb.username");
        String password = prop.getProperty("webservice.esb.password");
        return new ScheduleUtils(userName, password);
    }

    private ScheduleUtils(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * 获取调用对象
     *
     * @return
     */
    private WFApplicationToDoServiceSoap getService() {
        WFApplicationToDoService wfApplicationToDoService = new WFApplicationToDoService();
        WFApplicationToDoServiceSoap port = null;
        try {
            //添加Header信息
            wfApplicationToDoService.setHandlerResolver(new HandlerResolver() {
                @Override
                public List<Handler> getHandlerChain(PortInfo portInfo) {
                    List<Handler> handlerList = new ArrayList<>();

                    handlerList.add(new MyHeaderHandler(userName, password));
                    return handlerList;
                }
            });

            port = wfApplicationToDoService.getWFApplicationToDoServiceSoap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

    /**
     * 保存待办信息
     *
     * @param systemID 系统ID
     * @param procName
     * @param impoLevel
     * @param alias
     * @param passerName
     * @param receTime
     * @param loginID
     * @param systemURL
     * @param procID
     * @param taskID
     * @param appName
     * @param creatorName
     * @return
     */
    public boolean saveApplicationInfo(Integer systemID, String procName, Integer impoLevel, String alias, String passerName, String receTime,
                                       String loginID, String systemURL, String procID, Integer taskID, String appName, String creatorName) {
        return getService().saveWFApplicationInfo(systemID, procName, impoLevel, alias, passerName, DateUtil.convertToXMLGregorianCalendar(receTime,DateUtil.DATETIME_DEFAULT_FORMAT),
                loginID, systemURL, procID, taskID, appName, creatorName);
    }

    /**
     * 根据systemID 和 流程ID删除代办信息
     *
     * @param systemID
     * @param procID
     * @return
     */
    public boolean delApplicationInfo(Integer systemID, String procID) {
        return getService().deleteWFApplicationInfo(systemID, procID);
    }

    /**
     * 根据systemID 和 流程ID 和 流程步骤 删除代办信息
     *
     * @param systemID
     * @param procID
     * @param taskID
     * @return
     */
    public boolean deleteApplicationInfoByTaskID(Integer systemID, String procID, Integer taskID) {
        return getService().deleteWFApplicationInfoByTaskID(systemID, procID, taskID);
    }

    public static void main(String[] args) {

//        if (ScheduleUtils.getInstance("IEMPAdmin", "111111").delApplicationInfo(1, "0828测试wqd")) {
//            System.out.println("del by SystemID and ProcID success");
//        }
//
//        isSuccess =
//                ScheduleUtils.getInstance("IEMPAdmin", "111111").saveApplicationInfo(1, "0828测试", 1, "test", "test", "2018-11-05", "0828测试wqd", "0828测试wqd", "0828测试wqd", 828, "0828测试wqd", "0828测试wqd");
//        if (isSuccess) {
//            System.out.println("save ok");
//        }

        if (ScheduleUtils.getInstance().deleteApplicationInfoByTaskID(38, "szxmJsumtGcjs_16001",16009)) {
            System.out.println("del by SystemID and ProcID and taskid success");
        }
        else
        {
            System.out.println("del by SystemID and ProcID and taskid error");
        }
//
        if (ScheduleUtils.getInstance().delApplicationInfo(100, "34529")) {
            System.out.println("del by SystemID and ProcID and taskid success");
        }
        else
        {
            System.out.println("del by SystemID and ProcID and taskid error");
        }
//        System.out.println("over");
    }
}
