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

 Date: 23/05/2021 22:15:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for T_USER
-- ----------------------------
DROP TABLE IF EXISTS `T_USER`;
CREATE TABLE `T_USER` (
  `ID` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `USER_NAME` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `PASS_WORD` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `SALT` varchar(32) COLLATE utf8_german2_ci DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `UPDATED_DATE` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_german2_ci;

-- ----------------------------
-- Records of T_USER
-- ----------------------------
BEGIN;
INSERT INTO `T_USER` VALUES ('0179ce08a04c4c2f9784d8e8049e837c', 'moko', '4ca16cef5dac24d6dfbac0ddd1b12ae0', '10f9c', '2021-05-20 22:36:33.409000', '2021-05-20 22:36:33.896000');
INSERT INTO `T_USER` VALUES ('dcf92bc0c44b40ff8d88bb4321cbdfba', 'Daisy', '0c33ccb2c9400a39b7c5d32241b32dc3', '60a75', '2021-05-17 22:19:35.592000', '2021-05-17 22:19:35.592000');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
