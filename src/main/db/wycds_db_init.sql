-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.1.73-community-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 mydb 的数据库结构 
DROP DATABASE IF EXISTS `wycds`;
CREATE DATABASE IF NOT EXISTS `wycds` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `wycds`;

-- 导出  表 mydb.app 结构
DROP TABLE IF EXISTS `cds_session`;
CREATE TABLE IF NOT EXISTS `cds_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `session_id` varchar(1000) NOT NULL COMMENT 'session_id,非空',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `last_access_time` datetime NOT NULL COMMENT '最后一次访问时间',
  `ip` varchar(30) NOT NULL COMMENT 'server的ip',
  `status` varchar(45) DEFAULT NULL COMMENT '状态',
  `attributes` varchar(45) DEFAULT NULL COMMENT 'session属性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='session信息表';

-- 数据导出被取消选择。

-- 导出  表 mydb.app 结构
DROP TABLE IF EXISTS `app`;
CREATE TABLE IF NOT EXISTS `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `app_name` varchar(60) NOT NULL COMMENT '应用名,非空',
  `app_key` varchar(60) NOT NULL COMMENT '应用key,非空',
  `owner` varchar(45) NOT NULL COMMENT '负责人,非空',
  `phone` varchar(45) DEFAULT NULL COMMENT '联系电话,可空',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱,可空',
  `attribute` varchar(800) DEFAULT NULL COMMENT '关联属性,可空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用信息表';

-- 数据导出被取消选择。


-- 导出  表 mydb.column_info 结构
DROP TABLE IF EXISTS `column_info`;
CREATE TABLE IF NOT EXISTS `column_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `splitting_key_id` int(11) NOT NULL COMMENT '分离键id,非空',
  `table_name` varchar(60) NOT NULL COMMENT '表名,非空',
  `column_name` varchar(60) NOT NULL COMMENT '字段,非空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `cluster_id` int(11) DEFAULT NULL COMMENT '在那个集群中定义的？',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字段信息表';

-- 数据导出被取消选择。


-- 导出  表 mydb.data_source 结构
DROP TABLE IF EXISTS `data_source`;
CREATE TABLE IF NOT EXISTS `data_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL COMMENT '数据源名称,非空',
  `min_connections` int(11) NOT NULL COMMENT '最小连接数,非空',
  `max_connections` int(11) NOT NULL COMMENT '最大连接数,非空',
  `user_name` varchar(40) NOT NULL COMMENT '用户名,非空',
  `pass_word` varchar(30) DEFAULT NULL,
  `checkout_timeout_milli_sec` int(11) DEFAULT NULL COMMENT '等待超时时间,可空',
  `idle_timeout_sec` varchar(45) DEFAULT NULL COMMENT '连接最大空闲时间,可空',
  `check_statement` varchar(400) DEFAULT NULL COMMENT '检测连接是否可用的查询语句,可空',
  `max_statements` int(11) DEFAULT NULL COMMENT '池中最大普通语句数,可空',
  `max_pre_statements` int(11) DEFAULT NULL COMMENT '池中最大预编译语句数,可空',
  `print_sql` varchar(10) DEFAULT NULL COMMENT '记录SQL语句及执行时间,可空',
  `verbose` varchar(10) DEFAULT NULL COMMENT '记录除SQL语句及执行时间外的其他信息,可空',
  `connection_info` varchar(400) DEFAULT NULL COMMENT '驱动支持的其他参数,多个参数用&分割,可空,可空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `url` varchar(400) NOT NULL,
  `driver` varchar(400) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源信息表';

-- 数据导出被取消选择。


-- 导出  表 mydb.data_source_monitor 结构
DROP TABLE IF EXISTS `data_source_monitor`;
CREATE TABLE IF NOT EXISTS `data_source_monitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_source_id` int(11) NOT NULL COMMENT '数据源id,非空',
  `app_id` int(11) NOT NULL COMMENT '应用id,非空',
  `connections` int(11) NOT NULL COMMENT '当前连接数,非空',
  `max_connections` int(11) NOT NULL COMMENT '最大连接数,非空',
  `wait_connections` int(11) NOT NULL COMMENT 'waitConnections,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库连接池状态监控表';

-- 数据导出被取消选择。


-- 导出  表 mydb.db_cluster 结构
DROP TABLE IF EXISTS `db_cluster`;
CREATE TABLE IF NOT EXISTS `db_cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cluster_name` varchar(45) DEFAULT NULL,
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据簇表';

-- 数据导出被取消选择。


-- 导出  表 mydb.db_host_group 结构
DROP TABLE IF EXISTS `db_host_group`;
CREATE TABLE IF NOT EXISTS `db_host_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(60) DEFAULT NULL COMMENT '业务主名称,非空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `db_type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库群组表';



-- 数据导出被取消选择。

-- 导出  表 mydb.db_unit 结构
DROP TABLE IF EXISTS `db_unit`;
CREATE TABLE IF NOT EXISTS `db_unit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `db_host_group_id` int(11) NOT NULL,
  `ip` varchar(20) NOT NULL COMMENT 'ip地址,非空',
  `port` int(11) NOT NULL COMMENT '端口号,非空',
  `db_server_id` int(32) DEFAULT NULL COMMENT 'MySQL服务器ServerId',
  `db_name` varchar(60) NOT NULL COMMENT '数据库名称',
  `user_name` varchar(60) NOT NULL COMMENT '数据库密码',
  `passwd` varchar(60) NOT NULL COMMENT '数据库密码',
  `db_type` varchar(20) NOT NULL,
  `master_or_slave` varchar(20) NOT NULL COMMENT 'Master/Slave主备,非空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库基本信息表';

-- 数据导出被取消选择。


-- 导出  表 mydb.depots_table_rules 结构
DROP TABLE IF EXISTS `depots_table_rules`;
CREATE TABLE IF NOT EXISTS `depots_table_rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规则id,非空',
  `rule_type` varchar(10) NOT NULL COMMENT '规则类型(哈希,范围),非空',
  `db_group_id` int(11) NOT NULL COMMENT '群组id,非空',
  `table_suffix` varchar(45) NOT NULL COMMENT '表后缀,非空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `upper_limit` int(20) DEFAULT NULL,
  `lower_limit` int(20) DEFAULT NULL,
  `hash_value` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分库分表规则表';

-- 数据导出被取消选择。


-- 导出  表 mydb.r_data_source_app 结构
DROP TABLE IF EXISTS `r_data_source_app`;
CREATE TABLE IF NOT EXISTS `r_data_source_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date_source_id` int(11) NOT NULL COMMENT '数据源id',
  `app_id` int(11) NOT NULL COMMENT '应用id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源与应用信息关联表';

-- 数据导出被取消选择。


-- 导出  表 mydb.rdb_cluster_db_group 结构
DROP TABLE IF EXISTS `rdb_cluster_db_group`;
CREATE TABLE IF NOT EXISTS `rdb_cluster_db_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `db_cluster_id` int(11) NOT NULL,
  `db_group_id` int(11) NOT NULL,
  `group_type` varchar(10) NOT NULL COMMENT '群组类型(全局组,工作组),非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群组和数据簇关系表';

-- 数据导出被取消选择。


-- 导出  表 mydb.r_rules_splitting_key 结构
DROP TABLE IF EXISTS `r_rules_splitting_key`;
CREATE TABLE IF NOT EXISTS `r_rules_splitting_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `splitting_key_id` int(11) DEFAULT NULL COMMENT '分离键id,非空',
  `depots_table_rules_id` int(11) DEFAULT NULL COMMENT '规则id,非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分离键与分库分表规则关联表';

-- 数据导出被取消选择。


-- 导出  表 mydb.splittingkey 结构
DROP TABLE IF EXISTS `splitting_key`;
CREATE TABLE IF NOT EXISTS `splitting_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '切分健id',
  `split_name` varchar(45) DEFAULT NULL COMMENT '切分健名称,可空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `cluster_id` int(11) DEFAULT NULL COMMENT '所属集群',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='切分键表';

-- 数据导出被取消选择。


-- 导出  表 mydb.sqlperformancemonitor 结构
DROP TABLE IF EXISTS `sql_performance_monitor`;
CREATE TABLE IF NOT EXISTS `sql_performance_monitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_source_id` int(11) NOT NULL COMMENT '数据源id',
  `app_id` int(11) NOT NULL COMMENT '应用id',
  `average_time` int(11) NOT NULL COMMENT '平均执行时间',
  `error_num` int(11) NOT NULL COMMENT '错误次数',
  `creation_date` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SQL执行性能监控表';

-- 数据导出被取消选择。


-- 导出  表 mydb.test_splitting 结构
DROP TABLE IF EXISTS `test_splitting`;
CREATE TABLE IF NOT EXISTS `test_splitting` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--  监控相关
-- 导出  表 mydb.db_monitor_group 结构
DROP TABLE IF EXISTS `db_monitor_group`;
CREATE TABLE IF NOT EXISTS `db_monitor_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_name` varchar(20)  NOT NULL COMMENT '监控组名称',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `db_type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库基本信息表';

-- 导出  表 mydb.dbinfo 结构
DROP TABLE IF EXISTS `db_info`;
CREATE TABLE IF NOT EXISTS `db_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `db_monitor_group_id` int(11) NOT NULL,
  `ip` varchar(20) NOT NULL COMMENT 'ip地址,非空',
  `port` int(11) NOT NULL COMMENT '端口号,非空',
  `db_server_id` int(32) DEFAULT NULL COMMENT 'MySQL服务器ServerId',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `db_type` varchar(20) NOT NULL,
  `master_or_slave` varchar(20) NOT NULL COMMENT 'Master/Slave主备,非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库基本信息表';


/*Table structure for table `db_monitor` */

DROP TABLE IF EXISTS `db_monitor`;

CREATE TABLE `db_monitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `db_monitor_group_Id` int(11) NOT NULL COMMENT '数据库组id,非空',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人,非空',
  `modification_date` datetime NOT NULL COMMENT '最后修改时间,非空',
  `delete_status` varchar(10) NOT NULL DEFAULT 'false' COMMENT '删除状态(true已删除,false未删除),非空',
  `monitor_item` varchar(50) NOT NULL COMMENT '监控项',
  `monitor_item_desc` varchar(500) COMMENT '监控项描述',
  `monitor_script_type` varchar(500) COMMENT '监控脚本类型(python、shell)',
  `monitor_script_path` varchar(500) COMMENT '监控脚本下载路径',
  `check_interval` int(8) NOT NULL COMMENT '检测间隔时间单位s',
  `error_num_upper` int(2) NOT NULL COMMENT '允许最大连续错误次数',
  `check_times` int(2) NOT NULL COMMENT '监控次数',
  `threshold_upper` int(8) COMMENT '阈值上限',
  `threshold_lower` int(8) COMMENT '阈值下限',
  `power` DOUBLE(3,3) NOT NULL COMMENT '权重',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库监控配置表';

DROP TABLE IF EXISTS `db_event`;

CREATE TABLE `db_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `event_type` varchar(50) NOT NULL COMMENT '事件类型',
  `db_monitor_group_id` int(11) NOT NULL COMMENT '监控组id,非空',
  `db_info_Id` int(11) NOT NULL COMMENT '监控数据库id,非空',
  `db_monitor_Id` int(11) NOT NULL COMMENT '监控项id,非空',
  `ip` varchar(20) NOT NULL COMMENT 'ip地址,非空',
  `port` int(11) NOT NULL COMMENT '端口号,非空',
  `db_type` varchar(50) NOT NULL COMMENT '事件类型',
  `create_by` varchar(20) NOT NULL COMMENT '创建人,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件表';


/*Table structure for table `db_monitor_instance` */

DROP TABLE IF EXISTS `db_monitor_instance`;
CREATE TABLE `db_monitor_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `db_minitor_Id` int(11) NOT NULL COMMENT '数据库组id,非空',
  `creation_date` datetime NOT NULL COMMENT '创建时间,非空',
  `monitor_item` varchar(50) NOT NULL COMMENT '监控项',
  `status` varchar(20) NOT NULL COMMENT '状态：OK（正常）、WARNING（警告）、CRITICALl（宕机）',
  `monitor_value` int(11) NOT NULL COMMENT '监控值',
  `integral` int(3) NOT NULL COMMENT '积分（总分100）',
  `error_num` int(2) NOT NULL COMMENT '连续错误次数',
  `alarm_msg` varchar(200)  COMMENT '报警内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='监控实例表';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
