package com.wisdom.acm.leaf.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.wisdom.acm.leaf.enums.Constants;
import com.wisdom.acm.leaf.exception.InitException;
import com.wisdom.base.common.util.ResourceUtil;
import com.wisdom.base.common.util.SpringBootResourceUtils;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.leaf.IDGen;
import com.wisdom.leaf.common.Result;
import com.wisdom.leaf.common.ZeroIDGen;
import com.wisdom.leaf.segment.SegmentIDGenImpl;
import com.wisdom.leaf.segment.dao.IDAllocDao;
import com.wisdom.leaf.segment.dao.impl.IDAllocDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

@Service("SegmentService")
public class SegmentService {
	private Logger logger = LoggerFactory.getLogger(SegmentService.class);

	IDGen idGen;
	DruidDataSource dataSource;

	public SegmentService() throws SQLException, InitException {
		Properties properties = this.getProperties(); //PropertyFactory.getProperties();
		boolean flag = Boolean.parseBoolean(properties.getProperty(Constants.LEAF_SEGMENT_ENABLE, "true"));
		if (flag) {
			// Config dataSource
			dataSource = new DruidDataSource();
			dataSource.setUrl(properties.getProperty(Constants.LEAF_JDBC_URL));
			dataSource.setUsername(properties.getProperty(Constants.LEAF_JDBC_USERNAME));
			dataSource.setPassword(properties.getProperty(Constants.LEAF_JDBC_PASSWORD));
			dataSource.init();

			// Config Dao
			IDAllocDao dao = new IDAllocDaoImpl(dataSource);

			// Config ID Gen
			idGen = new SegmentIDGenImpl();
			((SegmentIDGenImpl) idGen).setDao(dao);
			if (idGen.init()) {
				logger.info("Segment Service Init Successfully");
			} else {
				throw new InitException("Segment Service Init Fail");
			}
		} else {
			idGen = new ZeroIDGen();
			logger.info("Zero ID Gen Service Init Successfully");
		}
	}

	public Result getId(String key, int step) {
		return idGen.get(key, step);
	}

	public Result getId(String key) {
		return idGen.get(key);
	}

	public SegmentIDGenImpl getIdGen() {
		if (idGen instanceof SegmentIDGenImpl) {
			return (SegmentIDGenImpl) idGen;
		}
		return null;
	}

	private Properties getProperties(){
		Map<String, Object> app = SpringBootResourceUtils.getApplicationProperties();
		Properties properties = new Properties();
		properties.setProperty(Constants.LEAF_SEGMENT_ENABLE, Tools.toString(app.get(Constants.LEAF_SEGMENT_ENABLE)));
		properties.setProperty(Constants.LEAF_JDBC_URL, ResourceUtil.removeEl(Tools.toString(app.get("spring.datasource.url"))));
		properties.setProperty(Constants.LEAF_JDBC_USERNAME, Tools.toString(app.get("spring.datasource.username")));
		properties.setProperty(Constants.LEAF_JDBC_PASSWORD, Tools.toString(app.get("spring.datasource.password")));
		properties.setProperty(Constants.LEAF_SNOWFLAKE_ENABLE, Tools.toString(app.get(Constants.LEAF_SNOWFLAKE_ENABLE))); //是否开启snowflake模式false
		properties.setProperty(Constants.LEAF_SNOWFLAKE_PORT, Tools.toString(app.get(Constants.LEAF_SNOWFLAKE_PORT))); //snowflake模式下的端口
		properties.setProperty(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS, Tools.toString(app.get(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS))); //snowflake模式下的zk地址
		return properties;
	}


}
