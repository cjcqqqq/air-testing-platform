CREATE TABLE `DEVICE_INFO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID，表主键',
  `DEVICE_CODE` varchar(40) NOT NULL COMMENT '设备标识码',
  `LONGITUDE` bigint(20) DEFAULT NULL COMMENT '经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度',
  `LATITUDE` bigint(20) DEFAULT NULL COMMENT '纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度',
  `SIM` bigint(20) DEFAULT NULL COMMENT 'SIM卡号',
  `PROTOCOL` varchar(40) NOT NULL COMMENT '协议类别',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备信息表';