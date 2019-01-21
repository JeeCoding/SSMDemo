/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50556
Source Host           : localhost:3306
Source Database       : ssm_demo

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2019-01-22 00:12:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for k_dept
-- ----------------------------
DROP TABLE IF EXISTS `k_dept`;
CREATE TABLE `k_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `birdate` datetime DEFAULT NULL COMMENT '出生日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of k_dept
-- ----------------------------
INSERT INTO `k_dept` VALUES ('1', '小明', '20', '2018-12-22 00:00:00');
INSERT INTO `k_dept` VALUES ('2', '小明', '18', '2027-04-23 00:00:00');
INSERT INTO `k_dept` VALUES ('3', '小明', '18', '2027-04-23 00:00:00');
INSERT INTO `k_dept` VALUES ('4', '小明', '20', '2018-12-24 00:00:00');
INSERT INTO `k_dept` VALUES ('5', '小明', '20', '2019-01-16 00:00:00');
INSERT INTO `k_dept` VALUES ('6', '小明', '20', '2019-01-17 00:00:00');
INSERT INTO `k_dept` VALUES ('7', '小明', '20', '2019-01-17 00:19:08');
