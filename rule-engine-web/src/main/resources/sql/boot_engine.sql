
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rule_engine_condition
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_condition`;
CREATE TABLE `rule_engine_condition`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `left_type` tinyint(4) NULL DEFAULT NULL,
  `left_value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `left_value_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `right_type` tinyint(4) NULL DEFAULT NULL,
  `right_value_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `right_value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `symbol` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_condition_name_index`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_condition
-- ----------------------------
INSERT INTO `rule_engine_condition` VALUES (46, '测试集合条件2', 'text', 1, '34', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-08-26 17:54:18', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_condition` VALUES (47, 'true', NULL, 2, 'true', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-09-04 20:50:46', '2020-08-24 15:57:57', 0);
INSERT INTO `rule_engine_condition` VALUES (48, 'false', NULL, 2, 'true', 'BOOLEAN', 2, 'BOOLEAN', 'false', 'EQ', '2020-08-24 16:26:51', '2020-08-24 16:26:51', 0);
INSERT INTO `rule_engine_condition` VALUES (51, '年龄', NULL, 0, '66', 'NUMBER', 2, 'NUMBER', '20', 'GE', '2020-08-27 00:15:15', '2020-08-27 00:15:15', 0);
INSERT INTO `rule_engine_condition` VALUES (52, 'vip等级', NULL, 0, '67', 'NUMBER', 2, 'NUMBER', '3', 'GE', '2020-08-27 00:15:35', '2020-08-27 00:15:35', 0);
INSERT INTO `rule_engine_condition` VALUES (53, '消费金额', NULL, 0, '68', 'NUMBER', 2, 'NUMBER', '30000', 'GE', '2020-08-27 00:17:05', '2020-08-27 00:17:05', 0);
INSERT INTO `rule_engine_condition` VALUES (54, '特殊省份', NULL, 2, '北京,河南,上海', 'COLLECTION', 0, 'COLLECTION', '69', 'CONTAIN', '2020-08-27 01:05:05', '2020-08-27 01:05:05', 0);
INSERT INTO `rule_engine_condition` VALUES (55, '文化程度', NULL, 2, '博士', 'STRING', 0, 'STRING', '70', 'EQ', '2020-08-27 01:08:04', '2020-08-27 01:08:04', 0);
INSERT INTO `rule_engine_condition` VALUES (56, '特殊市区', NULL, 2, '北京市,商丘市', 'COLLECTION', 0, 'STRING', '71', 'CONTAIN', '2020-08-27 01:14:08', '2020-08-27 01:13:05', 0);
INSERT INTO `rule_engine_condition` VALUES (57, '审批人测试', NULL, 1, '35', 'COLLECTION', 2, 'NUMBER', '123', 'CONTAIN', '2020-08-27 12:35:57', '2020-08-27 12:35:50', 0);
INSERT INTO `rule_engine_condition` VALUES (58, '测试条件-绝对值', NULL, 1, '52', 'NUMBER', 2, 'NUMBER', '123', 'EQ', '2020-08-28 14:48:48', '2020-08-28 14:48:48', 0);
INSERT INTO `rule_engine_condition` VALUES (60, '集合在.测试', NULL, 0, '61', 'COLLECTION', 2, 'COLLECTION', '1,2', 'IN', '2020-09-10 18:12:49', '2020-08-28 16:25:44', 0);
INSERT INTO `rule_engine_condition` VALUES (64, '年龄范围条件', NULL, 1, '70', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-08-30 18:53:37', '2020-08-30 18:53:37', 0);
INSERT INTO `rule_engine_condition` VALUES (65, '年龄大于等于20条件', NULL, 0, '66', 'NUMBER', 2, 'NUMBER', '20', 'GE', '2020-08-30 21:25:26', '2020-08-30 21:24:51', 0);
INSERT INTO `rule_engine_condition` VALUES (66, '年龄小于等于26条件', NULL, 0, '66', 'NUMBER', 2, 'NUMBER', '26', 'LE', '2020-09-04 20:56:36', '2020-08-30 21:25:18', 0);
INSERT INTO `rule_engine_condition` VALUES (69, '号码开头', NULL, 0, '77', 'STRING', 2, 'STRING', '137', 'STARTS_WITH', '2020-09-10 18:08:13', '2020-09-10 18:08:13', 0);
INSERT INTO `rule_engine_condition` VALUES (70, '限制号码长度', NULL, 1, '76', 'NUMBER', 2, 'NUMBER', '17', 'LE', '2020-09-10 20:08:15', '2020-09-10 18:11:41', 0);
INSERT INTO `rule_engine_condition` VALUES (71, '排除号码在白名单的', NULL, 1, '77', 'COLLECTION', 0, 'STRING', '77', 'CONTAIN', '2020-09-10 23:44:25', '2020-09-10 18:15:27', 0);
INSERT INTO `rule_engine_condition` VALUES (73, '验证是否为手机号码条件', NULL, 1, '79', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-09-16 14:57:38', '2020-09-16 14:57:38', 0);
INSERT INTO `rule_engine_condition` VALUES (74, 'vip等级大于4以上', NULL, 0, '67', 'NUMBER', 2, 'NUMBER', '4', 'GT', '2020-11-13 19:14:00', '2020-11-13 19:14:00', 0);
INSERT INTO `rule_engine_condition` VALUES (75, '消费金额大于3000', NULL, 0, '68', 'NUMBER', 2, 'NUMBER', '3000', 'GT', '2020-11-13 19:14:33', '2020-11-13 19:14:33', 0);

-- ----------------------------
-- Table structure for rule_engine_condition_group
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_condition_group`;
CREATE TABLE `rule_engine_condition_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rule_id` int(11) NULL DEFAULT NULL,
  `order_no` int(11) NOT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_condition_group_rule_id_index`(`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1080 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_condition_group
-- ----------------------------
INSERT INTO `rule_engine_condition_group` VALUES (499, '条件组r12sdfasdf', 30, 10, '2020-08-28 21:29:23', '2020-08-28 21:29:23', 0);
INSERT INTO `rule_engine_condition_group` VALUES (500, '条件组', 30, 11, '2020-08-28 21:29:23', '2020-08-28 21:29:23', 0);
INSERT INTO `rule_engine_condition_group` VALUES (573, '条件组', 35, 1, '2020-08-30 18:18:29', '2020-08-30 18:18:29', 0);
INSERT INTO `rule_engine_condition_group` VALUES (582, '条件组', 37, 1, '2020-08-30 19:01:02', '2020-08-30 19:01:02', 0);
INSERT INTO `rule_engine_condition_group` VALUES (620, '条件组', 27, 1, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO `rule_engine_condition_group` VALUES (621, '条件组', 27, 2, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO `rule_engine_condition_group` VALUES (623, '条件组', 42, 1, '2020-09-01 14:02:42', '2020-09-01 14:02:42', 0);
INSERT INTO `rule_engine_condition_group` VALUES (734, '条件组1', 41, 1, '2020-09-04 19:39:17', '2020-09-04 19:39:17', 0);
INSERT INTO `rule_engine_condition_group` VALUES (781, '条件组', 28, 1, '2020-09-07 14:36:48', '2020-09-07 14:36:48', 0);
INSERT INTO `rule_engine_condition_group` VALUES (785, '条件组', 39, 1, '2020-09-07 21:32:17', '2020-09-07 21:32:17', 0);
INSERT INTO `rule_engine_condition_group` VALUES (793, '条件组', 34, 2, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO `rule_engine_condition_group` VALUES (794, '条件组', 34, 3, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO `rule_engine_condition_group` VALUES (796, '条件组', 29, 2, '2020-09-07 22:35:58', '2020-09-07 22:35:58', 0);
INSERT INTO `rule_engine_condition_group` VALUES (870, '条件组', 44, 1, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO `rule_engine_condition_group` VALUES (871, '条件组1', 44, 2, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO `rule_engine_condition_group` VALUES (886, '条件组', 46, 1, '2020-09-09 21:40:25', '2020-09-09 21:40:25', 0);
INSERT INTO `rule_engine_condition_group` VALUES (950, '测试条件组1', 21, 12, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO `rule_engine_condition_group` VALUES (951, '条件组', 21, 16, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO `rule_engine_condition_group` VALUES (985, '条件组', 48, 1, '2020-10-27 18:25:41', '2020-10-27 18:25:41', 0);
INSERT INTO `rule_engine_condition_group` VALUES (1028, '条件组', 51, 1, '2020-11-14 10:02:16', '2020-11-14 10:02:16', 0);
INSERT INTO `rule_engine_condition_group` VALUES (1050, '消费金额满足30000', 24, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO `rule_engine_condition_group` VALUES (1051, '如果学历为博士', 24, 2, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO `rule_engine_condition_group` VALUES (1052, '特殊市区条件组', 24, 4, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO `rule_engine_condition_group` VALUES (1075, '条件组', 36, 1, '2020-11-15 19:07:59', '2020-11-15 19:07:59', 0);
INSERT INTO `rule_engine_condition_group` VALUES (1078, '先执行简单校验', 47, 1, '2020-11-15 19:15:11', '2020-11-15 19:15:11', 0);
INSERT INTO `rule_engine_condition_group` VALUES (1079, '查询数据库验证', 47, 2, '2020-11-15 19:15:12', '2020-11-15 19:15:12', 0);

-- ----------------------------
-- Table structure for rule_engine_condition_group_condition
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_condition_group_condition`;
CREATE TABLE `rule_engine_condition_group_condition`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `condition_id` int(11) NOT NULL,
  `condition_group_id` int(11) NOT NULL,
  `order_no` int(11) NOT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_condition_group_condition_condition_group_id_index`(`condition_group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1589 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_condition_group_condition
-- ----------------------------
INSERT INTO `rule_engine_condition_group_condition` VALUES (40, 45, 21, 1, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (41, 46, 21, 2, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (840, 47, 499, 1, '2020-08-28 21:29:23', '2020-08-28 21:29:23', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (927, 63, 573, 1, '2020-08-30 18:18:29', '2020-08-30 18:18:29', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (937, 64, 582, 1, '2020-08-30 19:01:02', '2020-08-30 19:01:02', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (938, 56, 582, 2, '2020-08-30 19:01:02', '2020-08-30 19:01:02', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (979, 58, 620, 1, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (980, 59, 621, 1, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (982, 47, 623, 1, '2020-09-01 14:02:42', '2020-09-01 14:02:42', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1128, 47, 734, 1, '2020-09-04 19:39:17', '2020-09-04 19:39:17', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1198, 47, 781, 1, '2020-09-07 14:36:48', '2020-09-07 14:36:48', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1203, 66, 785, 1, '2020-09-07 21:32:17', '2020-09-07 21:32:17', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1204, 65, 785, 2, '2020-09-07 21:32:17', '2020-09-07 21:32:17', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1212, 47, 793, 1, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1213, 59, 794, 1, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1215, 60, 796, 1, '2020-09-07 22:35:58', '2020-09-07 22:35:58', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1280, 47, 870, 1, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1281, 48, 871, 1, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1294, 68, 886, 1, '2020-09-09 21:40:26', '2020-09-09 21:40:26', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1393, 46, 950, 1, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1394, 45, 950, 2, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1395, 57, 951, 1, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1396, 53, 951, 2, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1450, 59, 985, 1, '2020-10-27 18:25:42', '2020-10-27 18:25:42', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1520, 74, 1028, 1, '2020-11-14 10:02:16', '2020-11-14 10:02:16', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1521, 75, 1028, 2, '2020-11-14 10:02:16', '2020-11-14 10:02:16', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1543, 53, 1050, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1544, 55, 1051, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1545, 56, 1052, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1580, 64, 1075, 1, '2020-11-15 19:07:59', '2020-11-15 19:07:59', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1585, 73, 1078, 1, '2020-11-15 19:15:12', '2020-11-15 19:15:12', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1586, 70, 1078, 2, '2020-11-15 19:15:12', '2020-11-15 19:15:12', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1587, 69, 1078, 3, '2020-11-15 19:15:12', '2020-11-15 19:15:12', 0);
INSERT INTO `rule_engine_condition_group_condition` VALUES (1588, 71, 1079, 1, '2020-11-15 19:15:12', '2020-11-15 19:15:12', 0);

-- ----------------------------
-- Table structure for rule_engine_element
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_element`;
CREATE TABLE `rule_engine_element`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `value_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_element_name_code_index`(`name`, `code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_element
-- ----------------------------
INSERT INTO `rule_engine_element` VALUES (59, '布尔元素', 'ele-boolean', 'BOOLEAN', NULL, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_element` VALUES (60, '字符串元素', 'ele-string', 'STRING', NULL, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_element` VALUES (61, '集合元素', 'ele-collection', 'COLLECTION', NULL, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_element` VALUES (66, '年龄', 'age', 'NUMBER', NULL, '2020-08-27 00:14:03', '2020-08-27 00:14:03', 0);
INSERT INTO `rule_engine_element` VALUES (67, 'vip等级', 'vipLevel', 'NUMBER', NULL, '2020-08-27 00:14:34', '2020-08-27 00:14:34', 0);
INSERT INTO `rule_engine_element` VALUES (68, '消费金额', 'AmountOfConsumption', 'NUMBER', NULL, '2020-08-27 00:16:35', '2020-08-27 00:16:35', 0);
INSERT INTO `rule_engine_element` VALUES (69, '家庭地址（省份）', 'HomeAddressProvince', 'STRING', NULL, '2020-08-27 01:01:40', '2020-08-27 01:05:12', 0);
INSERT INTO `rule_engine_element` VALUES (70, '学历', 'education', 'STRING', NULL, '2020-08-27 01:07:30', '2020-08-27 01:07:30', 0);
INSERT INTO `rule_engine_element` VALUES (71, '家庭地址（市区）', 'HomeAddressCityArea', 'STRING', NULL, '2020-08-27 01:13:40', '2020-08-27 01:17:20', 0);
INSERT INTO `rule_engine_element` VALUES (72, '数值', 'number', 'NUMBER', NULL, '2020-08-28 14:47:06', '2020-08-28 14:47:06', 0);
INSERT INTO `rule_engine_element` VALUES (73, '邮件内容', 'youjianneirong', 'STRING', NULL, '2020-08-29 01:33:44', '2020-08-29 01:33:44', 0);
INSERT INTO `rule_engine_element` VALUES (74, '邮件标题', 'youjianbiaoti', 'STRING', '123', '2020-08-29 01:34:12', '2020-08-30 11:46:01', 0);
INSERT INTO `rule_engine_element` VALUES (76, '身份证', 'identityCard', 'STRING', NULL, '2020-09-02 13:36:00', '2020-09-02 13:36:00', 0);
INSERT INTO `rule_engine_element` VALUES (77, '号码', 'phone', 'STRING', NULL, '2020-09-10 18:07:45', '2020-09-10 18:07:45', 0);
INSERT INTO `rule_engine_element` VALUES (81, '注册时间（时间戳）', 'registrationTime', 'NUMBER', NULL, '2020-11-13 19:04:31', '2020-11-13 19:04:31', 0);
INSERT INTO `rule_engine_element` VALUES (82, '号码', 'mailRecipient', 'COLLECTION', NULL, '2020-11-13 19:23:02', '2020-11-14 11:14:43', 0);

-- ----------------------------
-- Table structure for rule_engine_function
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_function`;
CREATE TABLE `rule_engine_function`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `executor` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `return_value_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_function
-- ----------------------------
INSERT INTO `rule_engine_function` VALUES (1, '是否为邮箱', '是否为邮箱函数', 'isEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-16 13:00:43', 0);
INSERT INTO `rule_engine_function` VALUES (2, '是否为空集合', '是否为空集合函数', 'isEmptyCollectionFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-19 18:54:10', 0);
INSERT INTO `rule_engine_function` VALUES (3, '发送邮件', '发送邮件函数', 'sendEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-18 17:06:45', 0);
INSERT INTO `rule_engine_function` VALUES (4, '求集合大小', NULL, 'collectionSizeFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:39:39', 0);
INSERT INTO `rule_engine_function` VALUES (5, '执行规则(函数开发中)', '', 'executeRuleFunction', 'STRING', '2020-09-11 20:26:14', '2020-08-28 14:41:53', 0);
INSERT INTO `rule_engine_function` VALUES (6, '是否为空字符串', '', 'isEmptyFunction', 'BOOLEAN', '2020-11-15 00:28:25', '2020-08-28 14:43:52', 0);
INSERT INTO `rule_engine_function` VALUES (7, '求绝对值', NULL, 'mathAbsFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:45:04', 0);
INSERT INTO `rule_engine_function` VALUES (8, '返回集合中第几个元素', '不存在则返回null', 'getCollectionElementsFunction', 'STRING', '2020-09-11 20:26:14', '2020-08-30 02:05:37', 0);
INSERT INTO `rule_engine_function` VALUES (9, '在..之间', '', 'isBetweenFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-30 02:16:51', 0);
INSERT INTO `rule_engine_function` VALUES (10, '求平均值', '集合中必须为number类型的值', 'avgFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:41:44', 0);
INSERT INTO `rule_engine_function` VALUES (11, '集合中最大值', '集合中必须为number类型的值', 'collectionMaxFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:48:32', 0);
INSERT INTO `rule_engine_function` VALUES (12, '集合中最小值', '集合中必须为number类型的值', 'collectionMinFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:49:12', 0);
INSERT INTO `rule_engine_function` VALUES (13, '字符串的长度', '', 'stringLengthFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:50:13', 0);
INSERT INTO `rule_engine_function` VALUES (14, '字符串去除前后空格', NULL, 'stringTrimFunction', 'STRING', '2020-09-11 20:26:14', '2020-09-01 13:51:14', 0);
INSERT INTO `rule_engine_function` VALUES (15, '求和', '集合中必须为number类型的值', 'sumFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:52:08', 0);
INSERT INTO `rule_engine_function` VALUES (16, '验证是否为手机号码', NULL, 'isMobileFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:53:17', 0);
INSERT INTO `rule_engine_function` VALUES (17, '是否为身份证', NULL, 'isCitizenIdFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:54:34', 0);
INSERT INTO `rule_engine_function` VALUES (18, '字符串转为集合', NULL, 'stringToCollectionFunction', 'COLLECTION', '2020-09-11 20:26:14', '2020-09-01 14:33:48', 0);

-- ----------------------------
-- Table structure for rule_engine_function_param
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_function_param`;
CREATE TABLE `rule_engine_function_param`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `function_id` int(11) NOT NULL,
  `param_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `param_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_function_param_function_id_index`(`function_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_function_param
-- ----------------------------
INSERT INTO `rule_engine_function_param` VALUES (1, 1, '普通参数', 'value', 'STRING', '2020-08-27 17:43:54', '2020-07-16 13:01:21', 0);
INSERT INTO `rule_engine_function_param` VALUES (2, 2, '集合', 'list', 'COLLECTION', '2020-08-27 17:43:53', '2020-07-19 18:54:39', 0);
INSERT INTO `rule_engine_function_param` VALUES (3, 3, '服务器地址', 'mailSmtpHost', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:05', 0);
INSERT INTO `rule_engine_function_param` VALUES (4, 3, '发送人', 'user', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:20', 0);
INSERT INTO `rule_engine_function_param` VALUES (5, 3, '发送人密码', 'password', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:44', 0);
INSERT INTO `rule_engine_function_param` VALUES (6, 3, '邮件接收人', 'tos', 'COLLECTION', '2020-08-27 17:43:54', '2020-08-18 17:10:07', 0);
INSERT INTO `rule_engine_function_param` VALUES (7, 3, '邮件标题', 'title', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:33', 0);
INSERT INTO `rule_engine_function_param` VALUES (8, 3, '邮件内容', 'text', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:50', 0);
INSERT INTO `rule_engine_function_param` VALUES (9, 4, '集合', 'list', 'COLLECTION', '2020-08-28 14:40:29', '2020-08-28 14:40:31', 0);
INSERT INTO `rule_engine_function_param` VALUES (10, 5, '规则入参', 'info', 'STRING', '2020-08-28 14:42:56', '2020-08-28 14:42:57', 0);
INSERT INTO `rule_engine_function_param` VALUES (11, 6, '字符串', 'value', 'STRING', '2020-08-28 14:44:22', '2020-08-28 14:44:24', 0);
INSERT INTO `rule_engine_function_param` VALUES (12, 7, '数值', 'value', 'NUMBER', '2020-08-28 14:45:28', '2020-08-28 14:45:30', 0);
INSERT INTO `rule_engine_function_param` VALUES (13, 3, '端口号', 'mailSmtpPort', 'NUMBER', '2020-08-29 01:36:00', '2020-08-29 01:36:02', 0);
INSERT INTO `rule_engine_function_param` VALUES (14, 8, '集合', 'list', 'COLLECTION', '2020-08-30 02:06:06', '2020-08-30 02:06:07', 0);
INSERT INTO `rule_engine_function_param` VALUES (15, 8, '索引', 'index', 'NUMBER', '2020-08-30 02:06:24', '2020-08-30 02:06:25', 0);
INSERT INTO `rule_engine_function_param` VALUES (16, 9, '数值', 'value', 'NUMBER', '2020-08-30 02:17:18', '2020-08-30 02:17:19', 0);
INSERT INTO `rule_engine_function_param` VALUES (17, 9, '最小', 'min', 'NUMBER', '2020-08-30 02:17:37', '2020-08-30 02:17:39', 0);
INSERT INTO `rule_engine_function_param` VALUES (18, 9, '最大', 'max', 'NUMBER', '2020-08-30 02:17:57', '2020-08-30 02:17:58', 0);
INSERT INTO `rule_engine_function_param` VALUES (19, 10, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:42:10', 0);
INSERT INTO `rule_engine_function_param` VALUES (20, 10, '小树位', 'scale', 'NUMBER', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO `rule_engine_function_param` VALUES (21, 11, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO `rule_engine_function_param` VALUES (22, 12, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:49:34', 0);
INSERT INTO `rule_engine_function_param` VALUES (23, 13, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:50:41', 0);
INSERT INTO `rule_engine_function_param` VALUES (24, 14, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:51:30', 0);
INSERT INTO `rule_engine_function_param` VALUES (25, 15, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:39', '2020-09-01 13:46:31', 0);
INSERT INTO `rule_engine_function_param` VALUES (26, 16, '手机号', 'mobile', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:53:56', 0);
INSERT INTO `rule_engine_function_param` VALUES (27, 17, '身份证号', 'citizenId', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:54:59', 0);
INSERT INTO `rule_engine_function_param` VALUES (28, 18, '字符串', 'value', 'STRING', '2020-09-01 14:34:22', '2020-09-01 14:34:24', 0);
INSERT INTO `rule_engine_function_param` VALUES (29, 18, '分隔符', 'regex', 'STRING', '2020-09-01 14:34:51', '2020-09-01 14:34:52', 0);

-- ----------------------------
-- Table structure for rule_engine_function_value
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_function_value`;
CREATE TABLE `rule_engine_function_value`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `function_id` int(11) NOT NULL,
  `variable_id` int(11) NOT NULL,
  `param_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `param_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '0:元素，1:变量,2:固定值',
  `value_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_function_value_function_id_variable_id_index`(`function_id`, `variable_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 379 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_function_value
-- ----------------------------
INSERT INTO `rule_engine_function_value` VALUES (38, 1, 33, NULL, 'value', 0, 'STRING', '60', '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_function_value` VALUES (39, 2, 34, NULL, 'list', 0, 'COLLECTION', '61', '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_function_value` VALUES (42, 2, 50, '集合', 'list', 2, 'COLLECTION', '123，3', '2020-08-27 22:28:49', '2020-08-27 22:28:49', 0);
INSERT INTO `rule_engine_function_value` VALUES (50, 1, 51, '普通参数', 'value', 0, 'STRING', '60', '2020-08-27 23:27:06', '2020-08-27 23:27:06', 0);
INSERT INTO `rule_engine_function_value` VALUES (51, 7, 52, '数值', 'value', 0, 'NUMBER', '72', '2020-08-28 14:47:28', '2020-08-28 14:47:28', 0);
INSERT INTO `rule_engine_function_value` VALUES (53, 4, 54, '集合', 'list', 1, 'COLLECTION', '35', '2020-08-28 15:22:10', '2020-08-28 15:22:10', 0);
INSERT INTO `rule_engine_function_value` VALUES (54, 7, 55, '数值', 'value', 1, 'NUMBER', '53', '2020-08-28 15:26:00', '2020-08-28 15:26:00', 0);
INSERT INTO `rule_engine_function_value` VALUES (56, 4, 35, '集合', 'list', 2, 'COLLECTION', '123,a,v', '2020-08-28 16:02:37', '2020-08-28 16:02:37', 0);
INSERT INTO `rule_engine_function_value` VALUES (60, 4, 53, '集合', 'list', 0, 'COLLECTION', '61', '2020-08-28 16:22:59', '2020-08-28 16:22:59', 0);
INSERT INTO `rule_engine_function_value` VALUES (61, 5, 57, '规则入参', 'info', 0, 'STRING', '60', '2020-08-28 21:29:09', '2020-08-28 21:29:09', 0);
INSERT INTO `rule_engine_function_value` VALUES (64, 4, 58, '集合', 'list', 2, 'COLLECTION', '123,123', '2020-08-29 01:28:39', '2020-08-29 01:28:39', 0);
INSERT INTO `rule_engine_function_value` VALUES (113, 8, 61, '集合', 'list', 0, 'COLLECTION', '61', '2020-08-30 12:35:49', '2020-08-30 12:35:49', 0);
INSERT INTO `rule_engine_function_value` VALUES (114, 8, 61, '索引', 'index', 2, 'NUMBER', '12', '2020-08-30 12:35:49', '2020-08-30 12:35:49', 0);
INSERT INTO `rule_engine_function_value` VALUES (115, 3, 63, '服务器地址', 'mailSmtpHost', 0, 'STRING', '60', '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO `rule_engine_function_value` VALUES (116, 3, 63, '发送人', 'user', 2, 'STRING', '123', '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO `rule_engine_function_value` VALUES (117, 3, 63, '发送人密码', 'password', NULL, 'STRING', NULL, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO `rule_engine_function_value` VALUES (118, 3, 63, '邮件接收人', 'tos', NULL, 'COLLECTION', NULL, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO `rule_engine_function_value` VALUES (119, 3, 63, '邮件标题', 'title', NULL, 'STRING', NULL, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO `rule_engine_function_value` VALUES (120, 3, 63, '邮件内容', 'text', NULL, 'STRING', NULL, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO `rule_engine_function_value` VALUES (121, 3, 63, '端口号', 'mailSmtpPort', 2, 'NUMBER', '0', '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO `rule_engine_function_value` VALUES (122, 3, 64, '服务器地址', 'mailSmtpHost', 2, 'STRING', '123', '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO `rule_engine_function_value` VALUES (123, 3, 64, '发送人', 'user', 2, 'STRING', '123123123', '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO `rule_engine_function_value` VALUES (124, 3, 64, '发送人密码', 'password', NULL, 'STRING', NULL, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO `rule_engine_function_value` VALUES (125, 3, 64, '邮件接收人', 'tos', NULL, 'COLLECTION', NULL, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO `rule_engine_function_value` VALUES (126, 3, 64, '邮件标题', 'title', NULL, 'STRING', NULL, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO `rule_engine_function_value` VALUES (127, 3, 64, '邮件内容', 'text', NULL, 'STRING', NULL, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO `rule_engine_function_value` VALUES (128, 3, 64, '端口号', 'mailSmtpPort', 2, 'NUMBER', '12', '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO `rule_engine_function_value` VALUES (136, 6, 67, '字符串', 'value', 2, 'STRING', '123', '2020-08-30 13:20:35', '2020-08-30 13:20:35', 0);
INSERT INTO `rule_engine_function_value` VALUES (270, 3, 65, '服务器地址', 'mailSmtpHost', 2, 'STRING', '123', '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO `rule_engine_function_value` VALUES (271, 3, 65, '发送人', 'user', 2, 'STRING', NULL, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO `rule_engine_function_value` VALUES (272, 3, 65, '发送人密码', 'password', 2, 'STRING', NULL, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO `rule_engine_function_value` VALUES (273, 3, 65, '邮件接收人', 'tos', 2, 'COLLECTION', NULL, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO `rule_engine_function_value` VALUES (274, 3, 65, '邮件标题', 'title', 2, 'STRING', NULL, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO `rule_engine_function_value` VALUES (275, 3, 65, '邮件内容', 'text', 2, 'STRING', NULL, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO `rule_engine_function_value` VALUES (276, 3, 65, '端口号', 'mailSmtpPort', 2, 'NUMBER', '21', '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO `rule_engine_function_value` VALUES (286, 9, 70, '数值', 'value', 0, 'NUMBER', '66', '2020-08-30 23:52:20', '2020-08-30 23:52:20', 0);
INSERT INTO `rule_engine_function_value` VALUES (287, 9, 70, '最小', 'min', 2, 'NUMBER', '20', '2020-08-30 23:52:20', '2020-08-30 23:52:20', 0);
INSERT INTO `rule_engine_function_value` VALUES (288, 9, 70, '最大', 'max', 2, 'NUMBER', '26', '2020-08-30 23:52:20', '2020-08-30 23:52:20', 0);
INSERT INTO `rule_engine_function_value` VALUES (291, 15, 72, '集合', 'list', 2, 'COLLECTION', '1,3', '2020-09-01 14:04:50', '2020-09-01 14:04:50', 0);
INSERT INTO `rule_engine_function_value` VALUES (305, 17, 71, '身份证号', 'citizenId', 0, 'STRING', '76', '2020-09-04 19:38:53', '2020-09-04 19:38:53', 0);
INSERT INTO `rule_engine_function_value` VALUES (306, 11, 73, '集合', 'list', 2, 'COLLECTION', '1,19,20,5', '2020-09-08 12:19:25', '2020-09-08 12:19:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (307, 8, 74, '集合', 'list', 2, 'COLLECTION', '123,34,33', '2020-09-08 12:23:25', '2020-09-08 12:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (308, 8, 74, '索引', 'index', 2, 'NUMBER', '0', '2020-09-08 12:23:25', '2020-09-08 12:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (309, 10, 75, '集合', 'list', 2, 'COLLECTION', '4,10', '2020-09-08 12:25:40', '2020-09-08 12:25:40', 0);
INSERT INTO `rule_engine_function_value` VALUES (310, 10, 75, '小树位', 'scale', 2, 'NUMBER', '2', '2020-09-08 12:25:40', '2020-09-08 12:25:40', 0);
INSERT INTO `rule_engine_function_value` VALUES (311, 13, 76, '字符串', 'value', 0, 'STRING', '77', '2020-09-10 18:11:06', '2020-09-10 18:11:06', 0);
INSERT INTO `rule_engine_function_value` VALUES (314, 16, 79, '手机号', 'mobile', 0, 'STRING', '77', '2020-09-18 19:08:19', '2020-09-18 19:08:19', 0);
INSERT INTO `rule_engine_function_value` VALUES (322, 3, 80, '服务器地址', 'mailSmtpHost', 2, 'STRING', '略', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (323, 3, 80, '发送人', 'user', 2, 'STRING', '略', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (324, 3, 80, '发送人密码', 'password', 2, 'STRING', '略', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (325, 3, 80, '邮件接收人', 'tos', 0, 'COLLECTION', '82', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (326, 3, 80, '邮件标题', 'title', 2, 'STRING', '恭喜你获得**优惠卷', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (327, 3, 80, '邮件内容', 'text', 2, 'STRING', '优惠券链接', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (328, 3, 80, '端口号', 'mailSmtpPort', 2, 'NUMBER', '21', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 0);
INSERT INTO `rule_engine_function_value` VALUES (368, 8, 87, '集合', 'list', 2, 'COLLECTION', '123123', '2020-11-15 00:26:14', '2020-11-15 00:26:14', 0);
INSERT INTO `rule_engine_function_value` VALUES (369, 8, 87, '索引', 'index', 0, 'NUMBER', '83', '2020-11-15 00:26:14', '2020-11-15 00:26:14', 0);
INSERT INTO `rule_engine_function_value` VALUES (374, 1, 84, '普通参数', 'value', 1, 'STRING', '88', '2020-11-15 01:08:50', '2020-11-15 01:08:50', 0);
INSERT INTO `rule_engine_function_value` VALUES (377, 8, 88, '集合', 'list', 2, 'COLLECTION', '123', '2020-11-15 01:10:16', '2020-11-15 01:10:16', 0);

-- ----------------------------
-- Table structure for rule_engine_menu
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_menu`;
CREATE TABLE `rule_engine_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL,
  `type` tinyint(1) NOT NULL COMMENT '1导航栏菜单,2侧边栏菜单',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `menu_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单路径',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_menu
-- ----------------------------

-- ----------------------------
-- Table structure for rule_engine_role
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_role`;
CREATE TABLE `rule_engine_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '名称',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '此角色的父级',
  `role_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色路径',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_role
-- ----------------------------
INSERT INTO `rule_engine_role` VALUES (1, '系统管理员', 'admin', NULL, NULL, '1', '2020-09-25 22:18:42', '2020-09-25 22:18:42', 0);
INSERT INTO `rule_engine_role` VALUES (2, '用户', 'user', '', 1, '1@2', '2020-09-25 22:19:30', '2020-09-25 22:19:31', 0);

-- ----------------------------
-- Table structure for rule_engine_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_role_menu`;
CREATE TABLE `rule_engine_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '用户id',
  `menu_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for rule_engine_rule
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_rule`;
CREATE TABLE `rule_engine_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_user_id` int(11) NULL DEFAULT NULL,
  `create_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `action_value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `action_type` tinyint(4) NULL DEFAULT NULL,
  `action_value_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `enable_default_action` tinyint(4) NULL DEFAULT NULL,
  `default_action_value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `default_action_type` tinyint(4) NULL DEFAULT NULL,
  `default_action_value_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `abnormal_alarm` json NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_rule_name_index`(`name`) USING BTREE,
  INDEX `rule_engine_rule_code_index`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_rule
-- ----------------------------
INSERT INTO `rule_engine_rule` VALUES (22, '获取规则执行接口Url', 'getRuleExeInterfaceUrl', NULL, NULL, 'admin', 2, '36', 1, 'STRING', 1, '', 2, 'STRING', '{\"email\": [\"\"], \"enable\": false}', '2020-08-25 02:53:32', '2020-09-04 20:50:47', 0);
INSERT INTO `rule_engine_rule` VALUES (36, '测试范围函数', 'test008d', NULL, NULL, 'admin', 1, 'true', 2, 'BOOLEAN', 0, '123', 2, 'STRING', '{\"email\": [\"761945125@qq.com\"], \"enable\": true}', '2020-08-30 18:37:51', '2020-08-30 18:37:51', 0);
INSERT INTO `rule_engine_rule` VALUES (47, '号码规则测试', 'phoneRuletest', NULL, NULL, NULL, 1, 'true', 2, 'BOOLEAN', 0, 'false', 2, 'BOOLEAN', '{\"email\": [\"761945125@qq.com\"], \"enable\": true}', '2020-09-10 18:07:05', '2020-11-14 11:39:40', 0);

-- ----------------------------
-- Table structure for rule_engine_rule_publish
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_rule_publish`;
CREATE TABLE `rule_engine_rule_publish`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_id` int(11) NOT NULL,
  `rule_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data` json NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_rule_publish_rule_code_index`(`rule_code`) USING BTREE,
  INDEX `rule_engine_rule_publish_rule_id_index`(`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_rule_publish
-- ----------------------------
INSERT INTO `rule_engine_rule_publish` VALUES (63, 36, 'test008d', '{\"id\": 36, \"code\": \"test008d\", \"name\": \"测试范围函数\", \"@type\": \"com.engine.core.rule.Rule\", \"actionValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"true\", \"dataType\": \"BOOLEAN\", \"valueType\": \"com.engine.core.value.Constant\"}, \"conditionSet\": {\"conditionGroups\": [{\"id\": 1059, \"orderNo\": 1, \"conditions\": [{\"id\": 64, \"name\": \"年龄范围条件\", \"orderNo\": 1, \"operator\": \"EQ\", \"leftValue\": {\"name\": \"年龄在20-26之间的\", \"@type\": \"com.engine.core.value.Variable\", \"dataType\": \"BOOLEAN\", \"valueType\": \"com.engine.core.value.Variable\", \"variableId\": 70}, \"rightValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"true\", \"dataType\": \"BOOLEAN\", \"valueType\": \"com.engine.core.value.Constant\"}}]}]}, \"abnormalAlarm\": {\"email\": [\"761945125@qq.com\"], \"enable\": true}, \"defaultActionValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"123\", \"dataType\": \"STRING\", \"valueType\": \"com.engine.core.value.Constant\"}}', '2020-11-15 19:07:41', '2020-11-15 19:07:41', 0);
INSERT INTO `rule_engine_rule_publish` VALUES (64, 47, 'phoneRuletest', '{\"id\": 47, \"code\": \"phoneRuletest\", \"name\": \"号码规则测试\", \"@type\": \"com.engine.core.rule.Rule\", \"actionValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"true\", \"dataType\": \"BOOLEAN\", \"valueType\": \"com.engine.core.value.Constant\"}, \"conditionSet\": {\"conditionGroups\": [{\"id\": 1073, \"orderNo\": 1, \"conditions\": [{\"id\": 73, \"name\": \"验证是否为手机号码条件\", \"orderNo\": 1, \"operator\": \"EQ\", \"leftValue\": {\"name\": \"验证是否为手机号码\", \"@type\": \"com.engine.core.value.Variable\", \"dataType\": \"BOOLEAN\", \"valueType\": \"com.engine.core.value.Variable\", \"variableId\": 79}, \"rightValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"true\", \"dataType\": \"BOOLEAN\", \"valueType\": \"com.engine.core.value.Constant\"}}, {\"id\": 70, \"name\": \"限制号码长度\", \"orderNo\": 2, \"operator\": \"LE\", \"leftValue\": {\"name\": \"号码长度\", \"@type\": \"com.engine.core.value.Variable\", \"dataType\": \"NUMBER\", \"valueType\": \"com.engine.core.value.Variable\", \"variableId\": 76}, \"rightValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"17\", \"dataType\": \"NUMBER\", \"valueType\": \"com.engine.core.value.Constant\"}}, {\"id\": 69, \"name\": \"号码开头\", \"orderNo\": 3, \"operator\": \"STARTS_WITH\", \"leftValue\": {\"@type\": \"com.engine.core.value.Element\", \"dataType\": \"STRING\", \"elementId\": 77, \"valueType\": \"com.engine.core.value.Element\", \"elementCode\": \"phone\", \"elementName\": \"号码\"}, \"rightValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"137\", \"dataType\": \"STRING\", \"valueType\": \"com.engine.core.value.Constant\"}}]}, {\"id\": 1074, \"orderNo\": 2, \"conditions\": [{\"id\": 71, \"name\": \"排除号码在白名单的\", \"orderNo\": 1, \"operator\": \"CONTAIN\", \"leftValue\": {\"name\": \"全局白名单\", \"@type\": \"com.engine.core.value.Variable\", \"dataType\": \"COLLECTION\", \"valueType\": \"com.engine.core.value.Variable\", \"variableId\": 77}, \"rightValue\": {\"@type\": \"com.engine.core.value.Element\", \"dataType\": \"STRING\", \"elementId\": 77, \"valueType\": \"com.engine.core.value.Element\", \"elementCode\": \"phone\", \"elementName\": \"号码\"}}]}]}, \"abnormalAlarm\": {\"email\": [\"761945125@qq.com\"], \"enable\": true}, \"defaultActionValue\": {\"@type\": \"com.engine.core.value.Constant\", \"value\": \"false\", \"dataType\": \"BOOLEAN\", \"valueType\": \"com.engine.core.value.Constant\"}}', '2020-11-15 19:07:45', '2020-11-15 19:07:45', 0);
INSERT INTO `rule_engine_rule_publish` VALUES (65, 22, 'getRuleExeInterfaceUrl', '{\"id\": 22, \"code\": \"getRuleExeInterfaceUrl\", \"name\": \"获取规则执行接口Url\", \"@type\": \"com.engine.core.rule.Rule\", \"actionValue\": {\"name\": \"获取规则执行接口地址\", \"@type\": \"com.engine.core.value.Variable\", \"dataType\": \"STRING\", \"valueType\": \"com.engine.core.value.Variable\", \"variableId\": 36}, \"conditionSet\": {\"conditionGroups\": []}, \"abnormalAlarm\": {\"email\": [\"\"], \"enable\": false}}', '2020-11-15 19:07:49', '2020-11-15 19:07:49', 0);

-- ----------------------------
-- Table structure for rule_engine_system_log
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_system_log`;
CREATE TABLE `rule_engine_system_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL,
  `description` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '请求ip',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '浏览器',
  `browser_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '浏览器版本',
  `system` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '请求者系统',
  `detailed` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '请求者系统详情',
  `mobile` tinyint(1) NULL DEFAULT NULL COMMENT '是否为移动平台',
  `ages` mediumtext CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '请求参数',
  `request_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '请求url',
  `end_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '请求结束时间',
  `running_time` bigint(10) NULL DEFAULT NULL COMMENT '运行时间',
  `return_value` mediumtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `exception` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '异常',
  `request_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 702 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_system_log
-- ----------------------------

-- ----------------------------
-- Table structure for rule_engine_user
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_user`;
CREATE TABLE `rule_engine_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '规则引擎用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_user
-- ----------------------------
INSERT INTO `rule_engine_user` VALUES (1, 'dqw', '5f329d3ac22671f7b214c461e58c27f3', '761945125@qq.com', '2020-09-25 21:49:50', '2020-09-25 21:49:51', 0);
INSERT INTO `rule_engine_user` VALUES (2, 'lq', '5f329d3ac22671f7b214c461e58c27f3', NULL, '2020-09-25 23:05:06', '2020-09-25 23:05:07', 0);

-- ----------------------------
-- Table structure for rule_engine_user_role
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_user_role`;
CREATE TABLE `rule_engine_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_user_role
-- ----------------------------
INSERT INTO `rule_engine_user_role` VALUES (3, 1, 1, '2020-09-25 22:20:31', '2020-09-25 22:20:32', 0);
INSERT INTO `rule_engine_user_role` VALUES (4, 2, 2, '2020-09-25 23:05:20', '2020-09-25 23:05:21', 0);

-- ----------------------------
-- Table structure for rule_engine_variable
-- ----------------------------
DROP TABLE IF EXISTS `rule_engine_variable`;
CREATE TABLE `rule_engine_variable`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` tinyint(4) NULL DEFAULT NULL,
  `value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rule_engine_variable_name_index`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 90 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rule_engine_variable
-- ----------------------------
INSERT INTO `rule_engine_variable` VALUES (34, '是否为空集合', NULL, 'BOOLEAN', 3, '2', '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO `rule_engine_variable` VALUES (35, '审批人', NULL, 'COLLECTION', 2, '123123,234234', '2020-08-24 22:12:53', '2020-08-28 16:10:07', 0);
INSERT INTO `rule_engine_variable` VALUES (36, '获取规则执行接口地址', NULL, 'STRING', 2, 'http://49.234.81.14:8010/ruleEngine/execute', '2020-08-25 18:53:36', '2020-11-14 02:02:56', 0);
INSERT INTO `rule_engine_variable` VALUES (51, '测试变量', NULL, 'BOOLEAN', 3, '1', '2020-08-27 23:27:06', '2020-08-27 23:27:06', 0);
INSERT INTO `rule_engine_variable` VALUES (52, '求绝对值', NULL, 'NUMBER', 3, '7', '2020-08-28 14:47:28', '2020-08-28 14:47:28', 0);
INSERT INTO `rule_engine_variable` VALUES (54, '高级配置测试', NULL, 'NUMBER', 3, '4', '2020-08-28 15:22:10', '2020-08-28 15:22:10', 0);
INSERT INTO `rule_engine_variable` VALUES (56, '是否开启', NULL, 'BOOLEAN', 2, 'true', '2020-08-28 16:10:31', '2020-08-28 16:10:31', 0);
INSERT INTO `rule_engine_variable` VALUES (57, '执行规则', NULL, 'STRING', 3, '5', '2020-08-28 21:29:09', '2020-08-28 21:29:09', 0);
INSERT INTO `rule_engine_variable` VALUES (58, '求集合的大小', NULL, 'NUMBER', 3, '4', '2020-08-29 01:22:06', '2020-08-29 01:28:39', 0);
INSERT INTO `rule_engine_variable` VALUES (70, '年龄在20-26之间的', NULL, 'BOOLEAN', 3, '9', '2020-08-30 18:34:53', '2020-08-30 23:52:20', 0);
INSERT INTO `rule_engine_variable` VALUES (71, '是否为身份证', NULL, 'BOOLEAN', 3, '17', '2020-09-01 13:58:26', '2020-09-04 19:38:53', 0);
INSERT INTO `rule_engine_variable` VALUES (73, '集合中最大值测试', NULL, 'NUMBER', 3, '11', '2020-09-08 12:19:25', '2020-09-08 12:19:25', 0);
INSERT INTO `rule_engine_variable` VALUES (75, '平均值', NULL, 'NUMBER', 3, '10', '2020-09-08 12:25:40', '2020-09-08 12:25:40', 0);
INSERT INTO `rule_engine_variable` VALUES (76, '号码长度', NULL, 'NUMBER', 3, '13', '2020-09-10 18:11:06', '2020-09-10 18:11:06', 0);
INSERT INTO `rule_engine_variable` VALUES (77, '全局白名单', NULL, 'COLLECTION', 2, '1343493849384,1371728378123', '2020-09-10 18:14:43', '2020-09-10 20:12:29', 0);
INSERT INTO `rule_engine_variable` VALUES (79, '验证是否为手机号码', NULL, 'BOOLEAN', 3, '16', '2020-09-16 14:57:18', '2020-09-18 19:08:19', 0);
INSERT INTO `rule_engine_variable` VALUES (80, '发送优惠券', NULL, 'BOOLEAN', 3, '3', '2020-11-13 19:22:26', '2020-11-13 19:23:25', 0);

SET FOREIGN_KEY_CHECKS = 1;
