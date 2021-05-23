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

 Date: 23/05/2021 22:17:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for T_ROLE_PERM
-- ----------------------------
DROP TABLE IF EXISTS `T_ROLE_PERM`;
CREATE TABLE `T_ROLE_PERM` (
  `ID` varchar(32) COLLATE utf8_german2_ci NOT NULL,
  `ROLE_ID` varchar(32) COLLATE utf8_german2_ci DEFAULT NULL,
  `PERM_ID` varchar(32) COLLATE utf8_german2_ci DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_german2_ci;

-- ----------------------------
-- Records of T_ROLE_PERM
-- ----------------------------
BEGIN;
INSERT INTO `T_ROLE_PERM` VALUES ('1592dae8df8e45b4903e83988bf6b83b', '02a2f0cf401b4c2bb36ae5d7eef4dee6', '84d604bef3744c53b3b62ab2648db30e', '2021-05-23 17:12:21', '2021-05-23 17:12:23');
INSERT INTO `T_ROLE_PERM` VALUES ('79963bca8dd74c68bdfff61346ea02cf', '347432919a5d4ee5bf13e7b3927e69d6', '29a9851a5b0649079e0ae602d75f2c42', '2021-05-23 17:08:02', '2021-05-23 17:08:04');
INSERT INTO `T_ROLE_PERM` VALUES ('84d604bef3744c53b3b62ab2648db30e', '347432919a5d4ee5bf13e7b3927e69d6', '84d604bef3744c53b3b62ab2648db30e', '2021-05-23 17:16:01', '2021-05-23 17:16:03');
INSERT INTO `T_ROLE_PERM` VALUES ('de6d7b620ab84a49b7663f822fcf76cc', '347432919a5d4ee5bf13e7b3927e69d6', '3bc5c99332634f23bdb0d9e29f73de97', '2021-05-23 17:14:26', '2021-05-23 17:14:29');
INSERT INTO `T_ROLE_PERM` VALUES ('f83d6222772e433e8b7924d1ec1e3390', '39f4c2a527144625982223f22d39ffbd', '3bc5c99332634f23bdb0d9e29f73de97', '2021-05-23 17:10:10', '2021-05-23 17:10:12');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
