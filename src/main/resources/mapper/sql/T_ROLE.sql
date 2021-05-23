/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : db2021

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 23/05/2021 22:16:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for T_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `T_ROLE`;
CREATE TABLE `T_ROLE` (
  `ID` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `NAME` varchar(32) COLLATE utf8_german2_ci DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_german2_ci;

-- ----------------------------
-- Records of T_ROLE
-- ----------------------------
BEGIN;
INSERT INTO `T_ROLE` VALUES ('02a2f0cf401b4c2bb36ae5d7eef4dee6', 'user', '2021-05-22 17:24:37', '2021-05-22 17:24:40');
INSERT INTO `T_ROLE` VALUES ('347432919a5d4ee5bf13e7b3927e69d6', 'admin', '2021-05-22 17:16:47', '2021-05-22 17:16:47');
INSERT INTO `T_ROLE` VALUES ('39f4c2a527144625982223f22d39ffbd', 'visitor', '2021-05-22 18:02:17', '2021-05-22 18:02:19');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
