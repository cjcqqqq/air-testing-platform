CREATE TABLE `hour_info` (
  `DEVICE_CODE` varchar(40) NOT NULL COMMENT '设备标识码',
  `COLLECT_TIME` datetime NOT NULL COMMENT '采集时间',
  `SIM` char(20) DEFAULT NULL COMMENT 'SIM卡号',
  `PM1_0` int(6) DEFAULT NULL COMMENT 'pm1.0 单位:微克/立方米',
  `PM2_5` int(6) DEFAULT NULL COMMENT 'pm2.5 单位:微克/立方米',
  `PM10` int(6) DEFAULT NULL COMMENT 'pm10  单位:微克/立方米',
  `FORMALDEHYDE` float(3,2) DEFAULT NULL COMMENT '甲醛 0.00-9.99',
  `TEMPERATURE` int(4) DEFAULT NULL COMMENT '温度 单位:摄氏度',
  `HUMIDITY` int(4) DEFAULT NULL COMMENT '湿度',
  `CO` int(4) DEFAULT NULL COMMENT '一氧化碳 预留',
  `CO2` int(4) DEFAULT NULL COMMENT '二氧化碳 预留',
  `NO` int(4) DEFAULT NULL COMMENT '一氧化氮 预留',
  `NO2` int(4) DEFAULT NULL COMMENT '二氧化氮 预留',
  `O3` int(4) DEFAULT NULL COMMENT '臭氧 预留',
  `SO2` int(4) DEFAULT NULL COMMENT '二氧化硫 预留',
  `TVOC` int(4) DEFAULT NULL COMMENT '有机气态物质 预留',
  `WIND_SPEED` int(4) DEFAULT NULL COMMENT '风速 预留',
  `WIND_DIRECTION` int(4) DEFAULT NULL COMMENT '风向 预留',
  `LONGITUDE` float(9,6) DEFAULT NULL COMMENT '经度 以度为单位的经度值，精确到百万分之一度',
  `LATITUDE` float(8,6) DEFAULT NULL COMMENT '纬度 以度为单位的纬度值，精确到百万分之一度',
  `ELECTRICITY` int(4) DEFAULT NULL COMMENT '太阳能电源电量',
  PRIMARY KEY (`DEVICE_CODE`,`COLLECT_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;