//package com.wisdom.acm.leaf.service.impl;
//
//import com.wisdom.acm.leaf.enums.Constants;
//import com.wisdom.acm.leaf.exception.InitException;
//import com.wisdom.leaf.IDGen;
//import com.wisdom.leaf.common.PropertyFactory;
//import com.wisdom.leaf.common.Result;
//import com.wisdom.leaf.common.ZeroIDGen;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.util.Properties;
//
//@Service("SnowflakeService")
//public class SnowflakeService {
//    private Logger logger = LoggerFactory.getLogger(SnowflakeService.class);
//    IDGen idGen;
//    public SnowflakeService() throws InitException {
//        Properties properties = PropertyFactory.getProperties();
//        boolean flag = Boolean.parseBoolean(properties.getProperty(Constants.LEAF_SNOWFLAKE_ENABLE, "true"));
//        if (flag) {
//            String zkAddress = properties.getProperty(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS);
//            int port = Integer.parseInt(properties.getProperty(Constants.LEAF_SNOWFLAKE_PORT));
//            idGen = new SnowflakeIDGenImpl(zkAddress, port);
//            if(idGen.init()) {
//                logger.info("Snowflake Service Init Successfully");
//            } else {
//                throw new InitException("Snowflake Service Init Fail");
//            }
//        } else {
//            idGen = new ZeroIDGen();
//            logger.info("Zero ID Gen Service Init Successfully");
//        }
//    }
//    public Result getId(String key) {
//        return idGen.get(key);
//    }
//}
