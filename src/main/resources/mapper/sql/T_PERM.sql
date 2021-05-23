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

 Date: 23/05/2021 22:17:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for T_PERM
-- ----------------------------
DROP TABLE IF EXISTS `T_PERM`;
CREATE TABLE `T_PERM` (
  `ID` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `NAME` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `URL` varchar(128) COLLATE utf8_german2_ci DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_german2_ci;

-- ----------------------------
-- Records of T_PERM
-- ----------------------------
BEGIN;
INSERT INTO `T_PERM` VALUES ('29a9851a5b0649079e0ae602d75f2c42', 'user:*:*', '/user', '2021-05-23 21:44:03', '2021-05-23 21:44:03');
INSERT INTO `T_PERM` VALUES ('3bc5c99332634f23bdb0d9e29f73de97', 'product:find:*', '/product/find', '2021-05-23 21:44:13', '2021-05-23 21:44:13');
INSERT INTO `T_PERM` VALUES ('84d604bef3744c53b3b62ab2648db30e', 'user:find:*', '/user/find', '2021-05-23 21:44:26', '2021-05-23 21:44:26');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
