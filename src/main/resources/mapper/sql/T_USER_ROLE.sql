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

 Date: 23/05/2021 22:17:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for T_USER_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `T_USER_ROLE`;
CREATE TABLE `T_USER_ROLE` (
  `ID` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `USER_ID` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `ROLE_ID` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `CREATED_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_german2_ci;

-- ----------------------------
-- Records of T_USER_ROLE
-- ----------------------------
BEGIN;
INSERT INTO `T_USER_ROLE` VALUES ('061da7f606104e97bda96c07622ec59f', '0179ce08a04c4c2f9784d8e8049e837c', '02a2f0cf401b4c2bb36ae5d7eef4dee6', '2021-05-22 17:59:44', '2021-05-22 17:59:46');
INSERT INTO `T_USER_ROLE` VALUES ('587f29f69ffc400a837b786af81d9086', 'dcf92bc0c44b40ff8d88bb4321cbdfba', '347432919a5d4ee5bf13e7b3927e69d6', '2021-05-22 18:01:13', '2021-05-22 18:01:15');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
