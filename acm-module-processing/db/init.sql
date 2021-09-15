-- 天气表
DROP TABLE IF EXISTS `weather_history`;
CREATE TABLE `weather_history`  (
  `id` int NOT NULL,
  `SORT_NUM` int DEFAULT NULL,
  `LAST_UPD_TIME` datetime DEFAULT NULL,
  `LAST_UPD_USER` int DEFAULT NULL,
  `CREAT_TIME` datetime DEFAULT NULL,
  `CREATOR` int DEFAULT NULL COMMENT '上报人',
  `LAST_UPD_IP` varchar(100) DEFAULT NULL,
  `WSDVER` int DEFAULT NULL,
  `crt_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `weatherdes` varchar(255)  COMMENT '天气描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
