/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : microservice_mall

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 03/08/2022 20:34:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_address
-- ----------------------------
DROP TABLE IF EXISTS `tb_address`;
CREATE TABLE `tb_address`  (
  `address_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `street_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_default` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`address_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_address
-- ----------------------------
INSERT INTO `tb_address` VALUES (3, 63, 'test', '18782059038', '四川省成都市青羊区百花中心站对面', 1);
INSERT INTO `tb_address` VALUES (5, 62, 'admin', '18782059038', '上海青浦区汇联路', 0);
INSERT INTO `tb_address` VALUES (7, 62, 'tom', '13100000000', '湖南省长沙市雨花区xxx', 1);
INSERT INTO `tb_address` VALUES (8, 64, 'wangshuguo', '18888888888', '黑龙江省哈尔滨市西大直街', 0);
INSERT INTO `tb_address` VALUES (9, 83, '张三', '123456789', '安徽省-合肥市-包河区-省政府', 1);

-- ----------------------------
-- Table structure for tb_express
-- ----------------------------
DROP TABLE IF EXISTS `tb_express`;
CREATE TABLE `tb_express`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '快递ID',
  `express_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `sort_order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `created` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品描述表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_express
-- ----------------------------
INSERT INTO `tb_express` VALUES (1, '京东快递', 1, '2018-05-31 11:45:10', NULL);

-- ----------------------------
-- Table structure for tb_item
-- ----------------------------
DROP TABLE IF EXISTS `tb_item`;
CREATE TABLE `tb_item`  (
  `id` bigint(20) NOT NULL COMMENT '商品id，同时也是商品编号',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品标题',
  `sell_point` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品卖点',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '商品价格',
  `num` int(11) NULL DEFAULT NULL COMMENT '库存数量',
  `limit_num` int(11) NULL DEFAULT NULL COMMENT '售卖数量限制',
  `image` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `cid` bigint(11) NULL DEFAULT NULL COMMENT '所属分类',
  `status` int(1) NULL DEFAULT 1 COMMENT '商品状态 1正常 0下架',
  `created` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `cid`(`cid`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `updated`(`updated`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_item
-- ----------------------------
INSERT INTO `tb_item` VALUES (100023501, 'Smartisan 双口 & 快充车载充电器', '铝合金机身、双口 & 快充、智能调节', 79.00, 100, 100, 'https://resource.smartisan.com/resource/d4480234a2f24b0ff5acd98288fd902d.jpg,https://resource.smartisan.com/resource/69ebf4ca620e6d5a1bb7cb54741e24d3.jpg,https://resource.smartisan.com/resource/214a422b7d250333bec4398d47eac601.jpg,https://resource.smartisan.com/resource/f512a3c4b97d204555f864d4aa17e7e9.jpg,https://resource.smartisan.com/resource/fc8a5d50ed260d9798cfba39ff5234d0.jpg', 221, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100026701, 'Smartisan 原装快充充电器 18W', '18W 安全快充、支持主流 QC3.0, MTK PE+2.0 快充协议', 49.00, 100, 100, 'https://resource.smartisan.com/resource/dc53bd870ee64d2053ecc51750ece43a.jpg,https://resource.smartisan.com/resource/83ab82fa6d9637d29d6af79d912ee572.jpg,https://resource.smartisan.com/resource/47461596fad00d37cb7a032a03d79286.jpg,https://resource.smartisan.com/resource/f4f6346bea727862087b4761fc8b01d2.jpg,https://resource.smartisan.com/resource/0286c84dba36577f37591f1af2b97402.jpg', 215, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100026801, 'Smartisan 耳机转接头', '即插即用、全面兼容', 19.00, 100, 100, 'https://resource.smartisan.com/resource/45312fb748d54aa2e58a8f4d637e9e65.jpg,https://resource.smartisan.com/resource/1dddddf6488ba89d592a37e9db93ffa2.jpg,https://resource.smartisan.com/resource/31b291594192c568b9fff9190a0d8f44.jpg,https://resource.smartisan.com/resource/561c002e74f6a5982dfaf3b4a44c9af4.jpg', 214, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100036501, '畅呼吸除霾除甲醛高效复合滤芯', '精选双层防护材质，过滤更精细，去味更有效', 699.00, 100, 100, 'https://resource.smartisan.com/resource/00eee903962f17d75950397843117e6e.jpg,https://resource.smartisan.com/resource/7a1f7380f2f2851fe133bd84115c42fe.jpg,https://resource.smartisan.com/resource/e2cd33328fe96214c2bff3ef0652350a.jpg', 228, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100039702, 'Smartisan 帆布鞋', '用于支付测试使用', 1.00, 100, 100, 'https://resource.smartisan.com/resource/578116bddf1d170c89e9af7ba5073fb6.jpg,https://resource.smartisan.com/resource/ebb01298315bf2ebdb6b21ee2c8e4237.jpg,https://resource.smartisan.com/resource/bd634d820859032b4c0f7a521eda486d.jpg,https://resource.smartisan.com/resource/51958a0a771f24e405f1b5de98108528.jpg,https://resource.smartisan.com/resource/e8791dd06c1e964d89436407f8827fe4.jpg', 236, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100040501, '畅呼吸智能空气净化器 · 超级除甲醛版', '800CADR 超强空气净化能力，400CADR超强除甲醛能力，app远程操控，多种专业滤芯可供选择', 2999.00, 100, 100, 'https://resource.smartisan.com/resource/71432ad30288fb860a4389881069b874.png,https://resource.smartisan.com/resource/6ff92d05a3bfab4fad489ca04d3eea5a.png', 226, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100040607, '坚果 3', '坚果 3 意外碎屏保修服务（碎屏险）', 2999.00, 100, 100, 'https://resource.smartisan.com/resource/13e91511f6ba3227ca5378fd2e93c54b.png,https://resource.smartisan.com/resource/fac4130efc39ed4db697cc8d137890e9.png,https://resource.smartisan.com/resource/91dc3f577960e30ca11b632e7b6ebd0f.png,https://resource.smartisan.com/resource/61586b59793ac16bd973010aecad2ca9.png', 210, 1, '2019-08-12 13:06:26', '2019-08-12 13:06:28');
INSERT INTO `tb_item` VALUES (100042203, '坚果“电池形电池”移动电源', 'Type-C 接口、轻巧便携、多重电路保护', 49.00, 100, 100, 'https://resource.smartisan.com/resource/33954b3f6a2f1614c5482ef130af9cc8.jpg,https://resource.smartisan.com/resource/1910dba5f999debab84c97c55845c74d.jpg,https://resource.smartisan.com/resource/3e62068911a78fb4b7c4ac20520a5216.jpg,https://resource.smartisan.com/resource/0329e3f7d4fd64659b36a9f3726ccf37.jpg', 218, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100042801, 'Smartisan 半入耳式耳机', '经典配色、专业调音、高品质麦克风', 59.00, 100, 100, 'https://resource.smartisan.com/resource/ce632bd67465027861707ec221b37c2d.jpg,https://resource.smartisan.com/resource/10525c4b21f039fc8ccb42cd1586f5cd.jpg,https://resource.smartisan.com/resource/d14645b66ff52c2e5958cd866a7d91e5.jpg,https://resource.smartisan.com/resource/7a4257950f953d6a7048d72de374530f.jpg,https://resource.smartisan.com/resource/dbe085a6f133b944e4e23bbb515c31ff.jpg', 217, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100046401, '坚果 R1', '骁龙 845 处理器 · 光学防抖双摄像头 · 6.17 英寸压力感应屏幕 · 10W快速无线充电功能', 2999.00, 100, 100, 'https://resource.smartisan.com/resource/06c2253354096f5e9ebf0616f1af2086.png', 210, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100047001, '坚果 QuickCharge 4+ 快速充电器', '全面兼容的 18W 快速充电', 1.00, 100, 100, 'https://resource.smartisan.com/resource/a668d1a5f41b04ece82d76ded1e94d3a.jpg,https://resource.smartisan.com/resource/c2375861762d557f65cf880b00161a41.jpg,https://resource.smartisan.com/resource/630dc5c945e78c0613d872cb83222b9e.jpg', 215, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100047101, '坚果 Type-C To Type-C 数据线', 'TPE 环保材质，PTC 过温保护', 39.00, 100, 100, 'https://resource.smartisan.com/resource/8635cb91f2cdbbc5576e069c52b99412.jpg,https://resource.smartisan.com/resource/a9a02318cb09ab38562092a556d0dedc.jpg', 214, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100051701, '坚果 Pro 2S', '双系统，无限屏，骁龙 ™ 710 处理器 · 前置 1600 万像素摄像头 · 6.01 英寸全高清全面屏 · AI 通话降噪 · 人脸解锁 + 指纹解锁 ', 1798.00, 100, 100, 'https://resource.smartisan.com/resource/b07b9765e272f866da6acda4ee107d88.png', 210, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100052801, '坚果砖式蓝牙小音箱', '一款设计出色、音质出众的随身音箱', 149.00, 100, 100, 'https://resource.smartisan.com/resource/6e96ccea3bd56bdd2243eb20330cec30.jpg,https://resource.smartisan.com/resource/a99de61d502b2f29b4a6d847751cf478.jpg,https://resource.smartisan.com/resource/3f6594f3537db91a3a4d6196111429df.jpg,https://resource.smartisan.com/resource/9e45ff0ce5d60627f0b07b1df4c56ed6.jpg,https://resource.smartisan.com/resource/830389bcd0e66569acd5ce05a304a3ea.jpg', 223, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100053001, '坚果彩虹数据线', '七彩配色随机发货，为生活增添一份小小惊喜', 19.00, 100, 100, 'https://resource.smartisan.com/resource/82aab62886740f165a3631ce6cffe895.jpg', 214, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100053202, '地平线 8 号商务旅行箱', '为了野心和远方', 999.00, 100, 100, 'https://resource.smartisan.com/resource/d1dcca9144e8d13ffb33026148599d0a.png', 238, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100053312, '地平线 8 号旅行箱', '简约设计、德国拜耳 PC 箱体', 299.00, 100, 100, 'https://resource.smartisan.com/resource/db4895e45ee6f3339037dbf7200e63f2.png', 238, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100055301, 'Smartisan 快充移动电源 10000mAh', '10000mAh 双向快充、轻盈便携、高标准安全保护', 129.00, 100, 100, 'https://resource.smartisan.com/resource/b7105b0d819e610a9c38d7ca2a813e58.jpg,https://resource.smartisan.com/resource/e47687c8288b324fb997c5bd7b709e80.jpg,https://resource.smartisan.com/resource/c933dd520c84c32edd9f50f664ec53ff.jpg,https://resource.smartisan.com/resource/1ae4fda7154eb92196f78fe9efb0c25f.jpg,https://resource.smartisan.com/resource/422ec86b9924bd5e45d5caa3ba1eaf7d.jpg', 218, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100055601, '各色DNA检测套装', '国内唯一聚焦于行为-基因关联性分析的基因检测解读', 499.00, 100, 100, 'https://resource.smartisan.com/resource/9bffe702b1f0aea221b1f18ddf886958.jpg,https://resource.smartisan.com/resource/30a1fce6a4280847eebf1b412fca39b0.jpg,https://resource.smartisan.com/resource/6681f43f88b9d867a0f33639cbeb47bf.jpg,https://resource.smartisan.com/resource/4fa597703a83cf326713faf2648744ac.jpg,https://resource.smartisan.com/resource/760637b38ba5ec5792e1e99c0d893462.jpg', 263, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100057401, 'Smartisan T恤 迪特拉姆斯', '', 149.00, 100, 100, 'https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png,https://resource.smartisan.com/resource/5068afef4f8866fae065d8c0d450e244.png,https://resource.smartisan.com/resource/a8dfe8f52dfb15c17e2e5c504c7ae2c6.png,https://resource.smartisan.com/resource/d6a6c06e5b51f0c18d8bfc45318163ea.png,https://resource.smartisan.com/resource/46724a81b037d1eca31d665c223b77a1.png', 231, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100057501, 'Smartisan T恤 毕加索', '', 149.00, 100, 100, 'https://resource.smartisan.com/resource/e9cd634b62470713f6b9c5a6065f4a10.jpg,https://resource.smartisan.com/resource/2ea973de25dffab6373dbe5e343f76c8.jpg,https://resource.smartisan.com/resource/57c12d9b6788d005341fe4aefd209fab.jpg,https://resource.smartisan.com/resource/25fb00a88fe6ababcd580a2cf0a14032.jpg,https://resource.smartisan.com/resource/bab385bd6811378389a12d7b7254ed7e.jpg', 231, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100057601, 'Smartisan T恤 皇帝的新装', '', 149.00, 100, 100, 'https://resource.smartisan.com/resource/d9586f7c5bb4578e3128de77a13e4d85.png,https://resource.smartisan.com/resource/07f77245d0f5f78f8ea580e181ec3dce.jpg,https://resource.smartisan.com/resource/0c9c397c8ac68a2ad327e1da8a5cb7d0.jpg,https://resource.smartisan.com/resource/154b35897ed3c1cb8dc1c7aae7b88f1f.jpg,https://resource.smartisan.com/resource/4a1686f2fde86e0aaac49c92395d4b32.jpg', 231, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item` VALUES (100057701, 'Smartisan T恤 丑小鸭', '', 149.00, 100, 100, 'https://resource.smartisan.com/resource/c23837ddfa3de0103be11bcbbb744066.png,https://resource.smartisan.com/resource/dad3d8d5ed151ad235ca9215815bc38b.png,https://resource.smartisan.com/resource/95f78a96e20b8e697e9df1c221d585c4.png,https://resource.smartisan.com/resource/33b0c45b3036d2a4267a05d192ccc45f.png,https://resource.smartisan.com/resource/b8bb658cf5cc22f23fb81a4c2ea028ac.png', 231, 1, '2019-07-29 14:37:02', '2019-07-29 14:37:02');

-- ----------------------------
-- Table structure for tb_item_cat
-- ----------------------------
DROP TABLE IF EXISTS `tb_item_cat`;
CREATE TABLE `tb_item_cat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类目ID',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父分类ID=0时代表一级根分类',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
  `sort_order` int(4) NULL DEFAULT NULL COMMENT '排列序号',
  `is_parent` tinyint(1) NULL DEFAULT 1 COMMENT '是否为父分类 1为true 0为false',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`, `status`) USING BTREE,
  INDEX `sort_order`(`sort_order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 299 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品类目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_item_cat
-- ----------------------------
INSERT INTO `tb_item_cat` VALUES (1, 1, '官方配件', 1, 0, 1, 'https://www.smartisan.com/category/185?type=shop', NULL, '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (2, 1, '耳机', 1, 0, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (3, 1, '手机充电配件', 1, 1, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (4, 1, '自拍杆', 1, 2, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (5, 1, '音箱', 1, 3, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (6, 1, '车载配件', 1, 4, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (7, 1, '服饰箱包', 1, 1, 1, 'https://www.smartisan.com/category/157', NULL, '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (8, 7, '服饰', 1, 0, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (9, 7, '鞋', 1, 1, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (10, 7, '箱包', 1, 2, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (11, 1, '畅呼吸', 1, 2, 1, 'https://www.smartisan.com/category/296?type=shop', NULL, '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (12, 11, '生活电器', 1, 0, 0, '', 'shop', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (202, 8, 'T恤', 1, 0, 0, 'https://resource.smartisan.com/resource/d01dcb91b34b26cda7064a3c9bf655c4.png', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (204, 8, 'POLO衫', 1, 1, 0, 'https://resource.smartisan.com/resource/daa975651d6d700c0f886718c520ee19.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (206, 9, '帆布鞋', 1, 0, 0, 'https://resource.smartisan.com/resource/1db1a8bfee3623fded41c9115b5a5335.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (207, 10, '拉杆箱', 1, 0, 0, 'https://resource.smartisan.com/resource/9960e83a55544fbf7b046013a6f7f414.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (231, 3, '数据线', 1, 0, 0, 'https://resource.smartisan.com/resource/c79a73ffc6f8e782160d978f49f543dc.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (232, 3, '充电器', 1, 1, 0, 'https://resource.smartisan.com/resource/dc53bd870ee64d2053ecc51750ece43a.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (234, 2, '线控耳机', 1, 0, 0, 'https://resource.smartisan.com/resource/72e4c31e4e623f5c37c6489f6d91efc6.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (235, 3, '移动电源', 1, 2, 0, 'https://resource.smartisan.com/resource/afcec520933673b8e03a867e6502f6e0.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (236, 4, '自拍杆', 1, 0, 0, 'https://resource.smartisan.com/resource/b9e61c6d93464454fa2e382632e34cee.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (238, 6, '车载充电器', 1, 0, 0, 'https://resource.smartisan.com/resource/d4480234a2f24b0ff5acd98288fd902d.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (252, 5, '音箱', 1, 0, 0, 'https://resource.smartisan.com/resource/c44f0ab4da5591fc3d0f82b7ac0f4f65.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (292, 10, '包', 1, 1, 0, 'https://resource.smartisan.com/resource/cef4a5433eb95cbdf242d3c1bf5617f8.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (297, 12, '空气净化器', 1, 0, 0, 'https://resource.smartisan.com/resource/71432ad30288fb860a4389881069b874.png', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');
INSERT INTO `tb_item_cat` VALUES (298, 12, '生活电器配件', 1, 1, 0, 'https://resource.smartisan.com/resource/4d83d72c5ecc288e8d5ddd9d06b80f99.jpg', '', '2019-07-29 14:01:59', '2019-07-29 14:01:59');

-- ----------------------------
-- Table structure for tb_item_desc
-- ----------------------------
DROP TABLE IF EXISTS `tb_item_desc`;
CREATE TABLE `tb_item_desc`  (
  `item_id` bigint(20) NOT NULL COMMENT '商品ID',
  `item_desc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述',
  `created` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品描述表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_item_desc
-- ----------------------------
INSERT INTO `tb_item_desc` VALUES (100023501, '<img src=\"https://resource.smartisan.com/resource/1a4b3d159317fe26377b2db040aaa2dc.jpg\" style=\"width:880px;height:7966px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100026701, '<img src=\"https://resource.smartisan.com/resource/4a7b87fe01ec8339985702ee922d205a.jpg\" style=\"width:1220px;height:4526px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100026801, '<img src=\"https://resource.smartisan.com/resource/586f7bfcf88f14935918943bb0421c26.jpg\" style=\"width:1220px;height:4715px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100036501, '<img src=\"https://resource.smartisan.com/resource/55191eb9aeb1367eefcb6d946fdc0546.jpg\" style=\"width:1220px;height:9823px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100039702, '<img src=\"https://resource.smartisan.com/resource/88cfe3dc879813dd6f60e57405a58cc1.jpg\" style=\"width:1220px;height:8863px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100040501, '<img src=\"https://resource.smartisan.com/resource/cf9cd35a37fed165577358b5cf72df7a.jpg\" style=\"width:1220px;height:12671px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100040607, '<img src=\"https://resource.smartisan.com/resource/d5a1453ad115e15bd79579e8bdb20ba0.jpg\" style=\"width:1220px;height:18021px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100042203, '<img src=\"https://resource.smartisan.com/resource/7d9e5c72d2af941602228d78c7b8904f.jpg\" style=\"width:1220px;height:8173px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100042801, '<img src=\"https://resource.smartisan.com/resource/234a5b125db07de264b5467d9ac15f1b.jpg\" style=\"width:1220px;height:8701px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100046401, '<img src=\"https://resource.smartisan.com/resource/79217e13f5f8d55cfb036edd93b2ce11.jpg\" style=\"width:1220px;height:24948px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100047001, '<img src=\"https://resource.smartisan.com/resource/a115f37d26c5ce4a99c0b5569cb736ab.jpg\" style=\"width:1220px;height:27384px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100047101, '<img src=\"https://resource.smartisan.com/resource/c6796a8fbe89a6ee92ede798be60c7ac.jpg\" style=\"width:1220px;height:5513px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100051701, '<img src=\"https://resource.smartisan.com/resource/a115f37d26c5ce4a99c0b5569cb736ab.jpg\" style=\"width:1220px;height:27384px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100052801, '<img src=\"https://resource.smartisan.com/resource/79217e13f5f8d55cfb036edd93b2ce11.jpg\" style=\"width:1220px;height:24948px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100053001, '<img src=\"https://resource.smartisan.com/resource/11e577bdce56fcf2048cd9a59a1498ad.jpg\" style=\"width:1220px;height:8475px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100053202, '<img src=\"https://resource.smartisan.com/resource/4285feb829a243a566cbe53af4b85438.jpg\" style=\"width:1220px;height:11689px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100053312, '<img src=\"https://resource.smartisan.com/resource/7d9803ef381540aa1c8097d3fc0680ea.png\" style=\"width:1220px;height:9166px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100055301, '<img src=\"https://resource.smartisan.com/resource/3ac96791a0b246a88ed8007223d5a2e2.jpg\" style=\"width:1220px;height:10486px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100055601, '<img src=\"https://resource.smartisan.com/resource/13bc92fefafd8583bcab13b4ff02b4a4.jpg\" style=\"width:1220px;height:28140px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100057401, '<img src=\"https://resource.smartisan.com/resource/6406be8c9066fd0fa6d4495f7aec9bfb.jpg\" style=\"width:1220px;height:14827px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100057501, '<img src=\"https://resource.smartisan.com/resource/6406be8c9066fd0fa6d4495f7aec9bfb.jpg\" style=\"width:1220px;height:14827px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100057601, '<img src=\"https://resource.smartisan.com/resource/6406be8c9066fd0fa6d4495f7aec9bfb.jpg\" style=\"width:1220px;height:14827px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');
INSERT INTO `tb_item_desc` VALUES (100057701, '<img src=\"https://resource.smartisan.com/resource/6406be8c9066fd0fa6d4495f7aec9bfb.jpg\" style=\"width:1220px;height:14827px\"/>', '2019-07-29 14:37:02', '2019-07-29 14:37:02');

-- ----------------------------
-- Table structure for tb_member
-- ----------------------------
DROP TABLE IF EXISTS `tb_member`;
CREATE TABLE `tb_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime(0) NOT NULL,
  `updated` datetime(0) NOT NULL,
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` int(1) NULL DEFAULT 0,
  `file` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `points` int(11) NULL DEFAULT 0 COMMENT '积分',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `isverified` varchar(26) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_member
-- ----------------------------
INSERT INTO `tb_member` VALUES (71, 'cskaoyan01', '4742af48858b0d267cffd80d3b4846f6', NULL, '291136733@qq.com', '2020-05-03 16:22:13', '2020-05-03 16:22:13', NULL, NULL, 1, NULL, NULL, NULL, NULL, 'N');
INSERT INTO `tb_member` VALUES (81, 'hanxiao', 'e10adc3949ba59abbe56e057f20f883e', NULL, '1043599451@qq.com', '2022-06-16 15:55:02', '2022-06-16 15:55:02', NULL, NULL, 1, 'http://www.520touxiang.com/uploads/allimg/2017080703/vuhoxcou32w.jpg', NULL, NULL, NULL, 'Y');
INSERT INTO `tb_member` VALUES (82, 'admin', 'e10adc3949ba59abbe56e057f20f883e', NULL, '1043599451@qq.com', '2022-06-17 14:53:11', '2022-06-17 14:53:11', NULL, NULL, 1, 'http://www.520touxiang.com/uploads/allimg/2017080703/bmn3cxnqdj2.jpg', NULL, NULL, NULL, 'Y');
INSERT INTO `tb_member` VALUES (83, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', NULL, '1043599451@qq.com', '2022-06-22 05:51:59', '2022-06-22 05:51:59', NULL, NULL, 1, 'http://localhost:8080/image/83_1658221297982.png', NULL, NULL, NULL, 'Y');

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单id',
  `payment` decimal(10, 2) NULL DEFAULT NULL COMMENT '实付金额',
  `payment_type` int(1) NULL DEFAULT NULL COMMENT '支付类型 1在线支付 2货到付款',
  `post_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '邮费',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 0未付款 1已付款 2未发货 3已发货 4交易成功 5交易关闭 6交易失败 7-已退款',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '订单更新时间',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '付款时间',
  `consign_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime(0) NULL DEFAULT NULL COMMENT '交易关闭时间',
  `shipping_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '物流名称',
  `shipping_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '物流单号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `buyer_message` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '买家留言',
  `buyer_nick` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '买家昵称',
  `buyer_comment` tinyint(1) NULL DEFAULT NULL COMMENT '买家是否已经评价',
  `unique_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '唯一键',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `buyer_nick`(`buyer_nick`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `payment_type`(`payment_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES ('19081913521928018', 0.10, NULL, NULL, 0, '2019-08-19 05:52:19', '2019-08-19 05:52:19', NULL, NULL, NULL, NULL, NULL, NULL, 62, NULL, 'test', NULL, NULL);
INSERT INTO `tb_order` VALUES ('20041912580013939', 149.00, NULL, NULL, 0, '2020-04-19 04:58:01', '2020-04-19 04:58:01', NULL, NULL, NULL, NULL, NULL, NULL, 62, NULL, 'test', NULL, NULL);
INSERT INTO `tb_order` VALUES ('20041914021310366', 149.00, NULL, NULL, 0, '2020-04-19 06:02:13', '2020-04-19 06:02:13', NULL, NULL, NULL, NULL, NULL, NULL, 62, NULL, 'test', NULL, NULL);
INSERT INTO `tb_order` VALUES ('20041914031730466', 149.00, NULL, NULL, 0, '2020-04-19 06:03:17', '2020-04-19 06:03:17', NULL, NULL, NULL, NULL, NULL, NULL, 62, NULL, 'test', NULL, NULL);
INSERT INTO `tb_order` VALUES ('20041914034866739', 149.00, NULL, NULL, 0, '2020-04-19 06:03:49', '2020-04-19 06:03:49', NULL, NULL, NULL, NULL, NULL, NULL, 62, NULL, 'test', NULL, NULL);
INSERT INTO `tb_order` VALUES ('20041914114388454', 149.00, NULL, NULL, 0, '2020-04-19 06:11:44', '2020-04-19 06:11:44', NULL, NULL, NULL, NULL, NULL, NULL, 62, NULL, 'test', NULL, NULL);
INSERT INTO `tb_order` VALUES ('20050402540122568', 199.00, NULL, NULL, 0, '2020-05-03 18:54:01', '2020-05-03 18:54:01', NULL, NULL, NULL, NULL, NULL, NULL, 71, NULL, 'cskaoyan01', NULL, NULL);

-- ----------------------------
-- Table structure for tb_order_item
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_item`;
CREATE TABLE `tb_order_item`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `item_id` bigint(20) NOT NULL COMMENT '商品id',
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单id',
  `num` int(10) NULL DEFAULT NULL COMMENT '商品购买数量',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '商品标题',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `total_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品总金额',
  `pic_path` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '商品图片地址',
  `status` int(4) NULL DEFAULT NULL COMMENT '1库存已锁定 2库存已释放 3-库存减扣成功',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `oder_item_id`(`order_id`, `item_id`) USING BTREE COMMENT '订单商品唯一索引',
  INDEX `item_id`(`item_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order_item
-- ----------------------------
INSERT INTO `tb_order_item` VALUES ('19081913521949774', 100053202, '19081913521928018', 2, '地平线 8 号商务旅行箱', 999.00, 1998.00, 'https://resource.smartisan.com/resource/d1dcca9144e8d13ffb33026148599d0a.png', 1, '2019-01-01 00:00:00', '2019-01-01 00:00:00');
INSERT INTO `tb_order_item` VALUES ('20041912580058839', 100057401, '20041912580013939', 1, 'Smartisan T恤 迪特拉姆斯', 149.00, 149.00, 'https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png', 1, '2020-04-19 04:58:00', '2020-04-19 04:58:00');
INSERT INTO `tb_order_item` VALUES ('20041914021340394', 100057401, '20041914021310366', 1, 'Smartisan T恤 迪特拉姆斯', 149.00, 149.00, 'https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png', 1, '2020-04-19 06:02:13', '2020-04-19 06:02:13');
INSERT INTO `tb_order_item` VALUES ('20041914031735181', 100057401, '20041914031730466', 1, 'Smartisan T恤 迪特拉姆斯', 149.00, 149.00, 'https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png', 1, '2020-04-19 06:03:17', '2020-04-19 06:03:17');
INSERT INTO `tb_order_item` VALUES ('20041914034868736', 100057401, '20041914034866739', 1, 'Smartisan T恤 迪特拉姆斯', 149.00, 149.00, 'https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png', 1, '2020-04-19 06:03:48', '2020-04-19 06:03:48');
INSERT INTO `tb_order_item` VALUES ('20041914114392506', 100057401, '20041914114388454', 1, 'Smartisan T恤 迪特拉姆斯', 149.00, 149.00, 'https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png', 1, '2020-04-19 06:11:43', '2020-04-19 06:11:43');
INSERT INTO `tb_order_item` VALUES ('20050402540126736', 100052801, '20050402540122568', 1, '坚果砖式蓝牙小音箱', 199.00, 199.00, 'https://resource.smartisan.com/resource/6e96ccea3bd56bdd2243eb20330cec30.jpg', 1, '2020-05-03 18:54:01', '2020-05-03 18:54:01');

-- ----------------------------
-- Table structure for tb_order_shipping
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_shipping`;
CREATE TABLE `tb_order_shipping`  (
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单ID',
  `receiver_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人全名',
  `receiver_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `receiver_mobile` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '移动电话',
  `receiver_state` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份',
  `receiver_city` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市',
  `receiver_district` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区/县',
  `receiver_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址，如：xx路xx号',
  `receiver_zip` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政编码,如：310001',
  `created` datetime(0) NULL DEFAULT NULL,
  `updated` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order_shipping
-- ----------------------------
INSERT INTO `tb_order_shipping` VALUES ('19081913521928018', 'Mic', '18073804421', NULL, NULL, NULL, NULL, '湖南省长沙市麓谷企业广场A3栋3单元407', NULL, '2019-08-19 05:52:20', '2019-08-19 05:52:20');
INSERT INTO `tb_order_shipping` VALUES ('20041912580013939', 'admin', '18782059038', NULL, NULL, NULL, NULL, '上海青浦区汇联路', NULL, '2020-04-19 04:58:01', '2020-04-19 04:58:01');
INSERT INTO `tb_order_shipping` VALUES ('20041914021310366', 'admin', '18782059038', NULL, NULL, NULL, NULL, '上海青浦区汇联路', NULL, '2020-04-19 06:02:13', '2020-04-19 06:02:13');
INSERT INTO `tb_order_shipping` VALUES ('20041914031730466', 'admin', '18782059038', NULL, NULL, NULL, NULL, '上海青浦区汇联路', NULL, '2020-04-19 06:03:17', '2020-04-19 06:03:17');
INSERT INTO `tb_order_shipping` VALUES ('20041914034866739', 'admin', '18782059038', NULL, NULL, NULL, NULL, '上海青浦区汇联路', NULL, '2020-04-19 06:03:49', '2020-04-19 06:03:49');
INSERT INTO `tb_order_shipping` VALUES ('20041914114388454', 'admin', '18782059038', NULL, NULL, NULL, NULL, '上海青浦区汇联路', NULL, '2020-04-19 06:11:44', '2020-04-19 06:11:44');

-- ----------------------------
-- Table structure for tb_panel
-- ----------------------------
DROP TABLE IF EXISTS `tb_panel`;
CREATE TABLE `tb_panel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类目ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '板块名称',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型 0轮播图 1板块种类一 2板块种类二 3板块种类三 ',
  `sort_order` int(4) NULL DEFAULT NULL COMMENT '排列序号',
  `position` int(1) NULL DEFAULT NULL COMMENT '所属位置 0首页 1商品推荐 2我要捐赠',
  `limit_num` int(4) NULL DEFAULT NULL COMMENT '板块限制商品数量',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`status`) USING BTREE,
  INDEX `sort_order`(`sort_order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内容分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_panel
-- ----------------------------
INSERT INTO `tb_panel` VALUES (1, '热门商品', 2, 1, 0, 3, 1, '', '2018-04-18 23:49:13', '2018-04-15 19:05:16');
INSERT INTO `tb_panel` VALUES (2, '服饰箱包', 3, 3, 0, 8, 1, '', NULL, '2018-04-19 11:20:59');
INSERT INTO `tb_panel` VALUES (3, '官方精选配件', 3, 2, 0, 7, 1, '', '2018-04-18 23:49:19', '2018-04-17 18:54:15');
INSERT INTO `tb_panel` VALUES (6, '热门推荐', 2, 6, 1, 2, 1, '热门推荐', '2019-07-29 23:58:19', '2019-07-29 23:58:23');
INSERT INTO `tb_panel` VALUES (7, '轮播图', 0, 0, 0, 5, 1, 'banner板块', '2018-04-15 12:33:07', '2018-04-15 12:33:07');
INSERT INTO `tb_panel` VALUES (8, '活动版块', 1, 5, 0, 4, 1, '', '2018-04-15 19:05:00', '2018-04-15 19:05:00');
INSERT INTO `tb_panel` VALUES (10, '坚果手机及配件', 3, 4, 0, 7, 1, NULL, '2018-04-18 23:50:32', '2018-04-18 23:50:35');

-- ----------------------------
-- Table structure for tb_panel_content
-- ----------------------------
DROP TABLE IF EXISTS `tb_panel_content`;
CREATE TABLE `tb_panel_content`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `panel_id` int(11) NOT NULL COMMENT '所属板块id',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型 0关联商品 1其他链接',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '关联商品id',
  `sort_order` int(4) NULL DEFAULT NULL,
  `full_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他链接',
  `pic_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pic_url2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '3d轮播图备用',
  `pic_url3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '3d轮播图备用',
  `created` datetime(0) NULL DEFAULT NULL,
  `updated` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`panel_id`) USING BTREE,
  INDEX `updated`(`updated`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_panel_content
-- ----------------------------
INSERT INTO `tb_panel_content` VALUES (2, 3, 0, 100026701, 1, '', 'https://resource.smartisan.com/resource/dc53bd870ee64d2053ecc51750ece43a.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-09-23 00:03:02', '2018-04-20 00:23:40');
INSERT INTO `tb_panel_content` VALUES (7, 3, 0, 100023501, 2, '', 'https://resource.smartisan.com/resource/d4480234a2f24b0ff5acd98288fd902d.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-09-23 22:58:11', '2018-04-20 00:14:29');
INSERT INTO `tb_panel_content` VALUES (8, 2, 0, 100053202, 1, '', 'https://resource.smartisan.com/resource/db4895e45ee6f3339037dbf7200e63f2.png?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-09-25 17:03:48', '2018-04-19 23:23:26');
INSERT INTO `tb_panel_content` VALUES (9, 2, 0, 100053312, 2, '', 'https://resource.smartisan.com/resource/d1dcca9144e8d13ffb33026148599d0a.png?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-09-25 17:04:35', '2018-04-20 10:48:17');
INSERT INTO `tb_panel_content` VALUES (14, 2, 0, 100057401, 3, '', 'https://resource.smartisan.com/resource/c892f825c80767c2bef15081352d2aa4.png?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:14:01', '2018-04-20 10:48:29');
INSERT INTO `tb_panel_content` VALUES (15, 2, 0, 100057501, 4, '', 'https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:14:52', '2018-04-20 10:48:43');
INSERT INTO `tb_panel_content` VALUES (16, 2, 0, 100057601, 5, '', 'https://resource.smartisan.com/resource/d9586f7c5bb4578e3128de77a13e4d85.png?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:15:51', '2018-04-20 10:49:02');
INSERT INTO `tb_panel_content` VALUES (17, 2, 0, 100057701, 6, '', 'https://resource.smartisan.com/resource/e9cd634b62470713f6b9c5a6065f4a10.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:17:01', '2018-04-20 10:49:17');
INSERT INTO `tb_panel_content` VALUES (18, 3, 0, 100042801, 3, NULL, 'https://resource.smartisan.com/resource/30ac0a1ab02999667f1362c501447e58.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:22:52', '2017-10-22 22:22:52');
INSERT INTO `tb_panel_content` VALUES (19, 3, 0, 100053001, 4, '', 'https://resource.smartisan.com/resource/82aab62886740f165a3631ce6cffe895.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:23:35', '2018-04-20 11:25:38');
INSERT INTO `tb_panel_content` VALUES (20, 3, 0, 100042203, 5, '', 'https://resource.smartisan.com/resource/f5a36dfc37d52a643683f4a21247f3ff.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:24:54', '2018-04-20 00:15:03');
INSERT INTO `tb_panel_content` VALUES (21, 3, 0, 100055301, 6, '', 'https://resource.smartisan.com/resource/f319b26eb69e8ba351423abfad347eae.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:25:28', '2018-04-20 00:24:11');
INSERT INTO `tb_panel_content` VALUES (22, 1, 0, 100039702, 1, NULL, 'https://resource.smartisan.com/resource/2f9a0f5f3dedf0ed813622003f1b287b.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:26:31', '2017-10-22 22:26:31');
INSERT INTO `tb_panel_content` VALUES (23, 1, 0, 100047001, 2, '', 'https://resource.smartisan.com/resource/a668d1a5f41b04ece82d76ded1e94d3a.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2017-10-22 22:26:40', '2018-04-17 20:59:35');
INSERT INTO `tb_panel_content` VALUES (25, 8, 7, NULL, 1, 'https://gper.club/answers/7e7e7f7ff4g56gc9g6e', 'https://resource.smartisan.com/resource/6/610400xinpinpeijian.jpg', NULL, NULL, '2018-04-15 19:07:43', '2018-04-19 23:20:34');
INSERT INTO `tb_panel_content` VALUES (26, 8, 7, NULL, 2, 'https://www/gupaoedu.com', 'https://resource.smartisan.com/resource/6/610400yijiuhuanxin.jpg', NULL, NULL, '2018-04-15 19:08:00', '2018-04-19 23:20:48');
INSERT INTO `tb_panel_content` VALUES (27, 8, 7, NULL, 3, 'https://ke.qq.com/course/185189?taid=1885512117965669', 'https://resource.smartisan.com/resource/4/489673079577637073.png', NULL, NULL, '2018-04-15 19:08:24', '2018-04-19 23:21:01');
INSERT INTO `tb_panel_content` VALUES (28, 8, 7, NULL, 4, 'https://www.gupaoedu.com/course-java.html', 'https://resource.smartisan.com/resource/fe6ab43348a43152b4001b4454d206ac.jpg', NULL, NULL, '2018-04-15 19:08:58', '2018-04-19 23:21:13');
INSERT INTO `tb_panel_content` VALUES (29, 2, 2, 100053312, 0, '', 'https://resource.smartisan.com/resource/3e931e9d0d270dae19b7fe9695551b4c.png?x-oss-process=image/resize,w_609/format,jpg/quality,Q_100', NULL, NULL, '2018-04-15 20:14:35', '2018-04-20 11:41:27');
INSERT INTO `tb_panel_content` VALUES (30, 3, 2, 100052801, 0, '', 'https://resource.smartisan.com/resource/fde550fa598718a4d9661c0f1557ce55.png?x-oss-process=image/resize,w_609/format,jpg/quality,Q_100', NULL, NULL, '2018-04-15 20:15:18', '2018-04-20 11:18:03');
INSERT INTO `tb_panel_content` VALUES (32, 7, 0, 100053312, 1, '', 'https://resource.smartisan.com/resource/52427d5ed690525b545d296159ffc63b.png?x-oss-process=image/format,jpg/quality,Q_100', '', '', '2018-04-17 20:41:02', '2018-04-17 20:58:41');
INSERT INTO `tb_panel_content` VALUES (33, 7, 0, 100057401, 2, '', 'https://resource.smartisan.com/resource/l/lou1.png?x-oss-process=image/format,jpg/quality,Q_100', '', '', '2018-04-17 21:08:22', '2018-04-20 10:47:19');
INSERT INTO `tb_panel_content` VALUES (34, 7, 0, 100040501, 3, NULL, 'https://resource.smartisan.com/resource/a9b9b5083118db99a58fe65afb6167e3.png?x-oss-process=image/format,jpg/quality,Q_100', '', '', '2018-04-17 21:08:30', '2018-04-17 21:08:32');
INSERT INTO `tb_panel_content` VALUES (40, 10, 3, 100046401, 0, '', 'https://resource.smartisan.com/resource/896f85d0ee0669ee1fdd8efc19bffc57.jpg?x-oss-process=image/resize,w_609/format,jpg/quality,Q_100', NULL, NULL, '2018-04-19 00:02:53', '2018-04-20 11:15:59');
INSERT INTO `tb_panel_content` VALUES (41, 10, 0, 100051701, 1, '', 'https://resource.smartisan.com/resource/b07b9765e272f866da6acda4ee107d88.png?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2018-04-19 00:02:56', '2018-04-20 00:24:36');
INSERT INTO `tb_panel_content` VALUES (42, 10, 0, 100046401, 2, '', 'https://resource.smartisan.com/resource/17f254e6f809355d8fe66260ccb48fb0.png?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2018-04-19 00:03:00', '2018-04-20 00:25:01');
INSERT INTO `tb_panel_content` VALUES (43, 10, 0, 100053001, 3, '', 'https://resource.smartisan.com/resource/82aab62886740f165a3631ce6cffe895.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2018-04-19 00:03:04', '2018-04-20 00:25:17');
INSERT INTO `tb_panel_content` VALUES (44, 10, 0, 100047101, 4, '', 'https://resource.smartisan.com/resource/8635cb91f2cdbbc5576e069c52b99412.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2018-04-19 00:03:10', '2018-04-20 10:55:52');
INSERT INTO `tb_panel_content` VALUES (45, 10, 0, 100026801, 5, '', 'https://resource.smartisan.com/resource/45312fb748d54aa2e58a8f4d637e9e65.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2018-04-19 00:03:15', '2018-04-20 10:54:56');
INSERT INTO `tb_panel_content` VALUES (46, 10, 0, 100042801, 6, '', 'https://resource.smartisan.com/resource/8a875418797690e26b665cc0d86dffc7.jpg?x-oss-process=image/resize,w_216/format,webp', NULL, NULL, '2018-04-19 00:03:20', '2018-04-20 10:55:03');
INSERT INTO `tb_panel_content` VALUES (47, 6, 0, 100046401, 1, '', 'https://resource.smartisan.com/resource/17f254e6f809355d8fe66260ccb48fb0.png', NULL, NULL, '2018-04-19 11:15:35', '2018-04-19 11:15:35');
INSERT INTO `tb_panel_content` VALUES (48, 6, 0, 100046401, 2, NULL, 'https://resource.smartisan.com/resource/06c2253354096f5e9ebf0616f1af2086.png', NULL, NULL, '2018-04-19 11:18:16', '2018-04-19 11:18:18');
INSERT INTO `tb_panel_content` VALUES (49, 6, 0, 100051701, 3, '', 'https://resource.smartisan.com/resource/b07b9765e272f866da6acda4ee107d88.png', '', '', '2018-04-19 11:18:16', '2018-04-19 11:18:18');
INSERT INTO `tb_panel_content` VALUES (50, 6, 0, 100040607, 4, '', 'https://resource.smartisan.com/resource/718bcecced0df1cd23bbdb9cc1f70b7d.png', '', '', '2018-04-19 11:18:16', '2018-04-19 11:18:18');
INSERT INTO `tb_panel_content` VALUES (55, 0, 1, NULL, 1, '', '手机', NULL, NULL, '2018-07-27 20:48:21', '2018-07-27 22:32:51');
INSERT INTO `tb_panel_content` VALUES (58, 0, 1, NULL, 2, '', '官方配件', NULL, NULL, '2018-07-27 20:50:07', '2018-07-27 22:25:18');
INSERT INTO `tb_panel_content` VALUES (59, 0, 0, NULL, 3, '', '服饰箱包', NULL, NULL, '2018-07-27 22:25:44', '2018-07-27 22:26:54');
INSERT INTO `tb_panel_content` VALUES (60, 0, 0, NULL, 4, '', '畅呼吸', NULL, NULL, '2018-07-27 22:26:03', '2018-07-27 22:26:50');
INSERT INTO `tb_panel_content` VALUES (61, 0, 0, NULL, 5, '', '服务', NULL, NULL, '2018-07-27 22:26:21', '2018-07-27 22:26:47');

-- ----------------------------
-- Table structure for tb_payment
-- ----------------------------
DROP TABLE IF EXISTS `tb_payment`;
CREATE TABLE `tb_payment`  (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付状态',
  `order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
  `product_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `pay_no` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方返回单号',
  `trade_no` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付流水号',
  `payer_uid` int(20) NOT NULL COMMENT '付款人id',
  `payer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款人姓名',
  `payer_amount` decimal(10, 2) NOT NULL COMMENT '付款方支付金额',
  `order_amount` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `pay_way` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式',
  `pay_success_time` datetime(0) NULL DEFAULT NULL COMMENT '支付成功时间',
  `complete_time` datetime(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_payment
-- ----------------------------
INSERT INTO `tb_payment` VALUES (3, '1', '19081913521928018', '��ƽ�� 8 ������������', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-18 04:03:13', '2020-04-18 04:03:13');
INSERT INTO `tb_payment` VALUES (4, '1', '19081913521928018', '��ƽ�� 8 ������������', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-18 04:07:19', '2020-04-18 04:07:19');
INSERT INTO `tb_payment` VALUES (5, '1', '19081913521928018', '��ƽ�� 8 ������������', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-18 04:13:15', '2020-04-18 04:13:15');
INSERT INTO `tb_payment` VALUES (6, '1', '19081913521928018', '��ƽ�� 8 ������������', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-18 04:14:47', '2020-04-18 04:14:47');
INSERT INTO `tb_payment` VALUES (7, '1', '19081913521928018', '??? 8 ??????', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-18 04:22:44', '2020-04-18 04:22:44');
INSERT INTO `tb_payment` VALUES (8, '1', '19081913521928018', '??? 8 ??????', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 03:37:08', '2020-04-19 03:37:08');
INSERT INTO `tb_payment` VALUES (9, '1', '20041912580013939', 'Smartisan T�� ������ķ˹', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 05:02:29', '2020-04-19 05:02:29');
INSERT INTO `tb_payment` VALUES (10, '1', '20041912580013939', 'Smartisan T�� ������ķ˹', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 05:03:21', '2020-04-19 05:03:21');
INSERT INTO `tb_payment` VALUES (11, '1', '20041912580013939', 'Smartisan T�� ������ķ˹', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 05:05:33', '2020-04-19 05:05:33');
INSERT INTO `tb_payment` VALUES (12, '1', '20041912580013939', 'Smartisan T�� ������ķ˹', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 05:08:11', '2020-04-19 05:08:11');
INSERT INTO `tb_payment` VALUES (13, '1', '20041912580013939', '[B@558b61b1', 'wx19131318012545247e5170bb1514667200', NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 05:12:44', '2020-04-19 05:12:44');
INSERT INTO `tb_payment` VALUES (14, '1', '20041912580013939', 'Smartisan T�� ������ķ˹', NULL, NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 05:54:19', '2020-04-19 05:54:19');
INSERT INTO `tb_payment` VALUES (15, '1', '20041914021310366', 'smartisan', 'wx1914031405761765fd6f877e1335581800', NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 06:02:40', '2020-04-19 06:02:40');
INSERT INTO `tb_payment` VALUES (16, '1', '20041914031730466', 'smartisan', 'wx191403559802288fe654ee4e1131064800', NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 06:03:22', '2020-04-19 06:03:22');
INSERT INTO `tb_payment` VALUES (17, '1', '20041914034866739', 'smartisan', 'wx19140425514872bac077418c1372362300', NULL, 62, 'ciggar', 1.00, 1.00, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 06:03:52', '2020-04-19 06:03:52');
INSERT INTO `tb_payment` VALUES (18, '1', '19081913521928018', '测试数据', NULL, NULL, 66, '', 0.10, 0.10, 'ali_pay', NULL, NULL, '支付宝支付', '2020-04-19 11:32:52', '2020-04-19 11:32:52');
INSERT INTO `tb_payment` VALUES (19, '1', '19081913521928019', 'test data', 'wx1919332796012247c5c497801828456000', NULL, 66, 'ciggar', 0.10, 0.10, 'wechat_pay', NULL, NULL, '微信支付', '2020-04-19 11:32:54', '2020-04-19 11:32:54');
INSERT INTO `tb_payment` VALUES (20, '1', '19081913521928021', 'test data', 'wx062055536121422c7f15bd681724402800', NULL, 66, 'ciggar', 0.10, 0.10, 'wechat_pay', NULL, NULL, '微信支付', '2020-05-06 12:55:48', '2020-05-06 12:55:48');
INSERT INTO `tb_payment` VALUES (21, '1', '19081913521928021', 'test data', 'wx062107106788962c7f15bd681672324500', NULL, 66, 'ciggar', 0.10, 0.10, 'wechat_pay', NULL, NULL, '微信支付', '2020-05-06 13:07:05', '2020-05-06 13:07:05');
INSERT INTO `tb_payment` VALUES (22, '1', '19081913521928022', 'Ƥ��', NULL, NULL, 66, 'ciggar', 0.10, 0.10, 'wechat_pay', NULL, NULL, '微信支付', '2020-05-06 13:07:45', '2020-05-06 13:07:45');
INSERT INTO `tb_payment` VALUES (23, '1', '19081913521928023', 'csmall', 'wx06210848740876113b0858821788081200', NULL, 66, 'ciggar', 0.10, 0.10, 'wechat_pay', NULL, NULL, '微信支付', '2020-05-06 13:08:43', '2020-05-06 13:08:43');
INSERT INTO `tb_payment` VALUES (24, '0', '22071516245490004', '张三 Smartisan 半入耳式耳机', NULL, NULL, 83, 'xxx', 59.00, 59.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 08:53:25', '2022-07-15 08:53:25');
INSERT INTO `tb_payment` VALUES (25, '0', '22071516245490004', '张三 Smartisan 半入耳式耳机', NULL, NULL, 83, 'xxx', 59.00, 59.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 08:53:58', '2022-07-15 08:53:58');
INSERT INTO `tb_payment` VALUES (26, '0', '22071516570144544', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 08:57:48', '2022-07-15 08:57:48');
INSERT INTO `tb_payment` VALUES (27, '0', '22071516570144544', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 08:59:18', '2022-07-15 08:59:18');
INSERT INTO `tb_payment` VALUES (28, '0', '22071516570144544', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 08:59:50', '2022-07-15 08:59:50');
INSERT INTO `tb_payment` VALUES (29, '0', '22071516570144544', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 09:01:12', '2022-07-15 09:01:12');
INSERT INTO `tb_payment` VALUES (30, '0', '22071516570144544', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 09:03:46', '2022-07-15 09:03:46');
INSERT INTO `tb_payment` VALUES (31, '0', '22071516570144544', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 09:07:26', '2022-07-15 09:07:26');
INSERT INTO `tb_payment` VALUES (32, '0', '22071517205401434', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 09:20:56', '2022-07-15 09:20:56');
INSERT INTO `tb_payment` VALUES (33, '0', '22071517205401434', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 09:43:21', '2022-07-15 09:43:21');
INSERT INTO `tb_payment` VALUES (34, '0', '22071517434425784', '张三 坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, 'xxx', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 09:43:46', '2022-07-15 09:43:46');
INSERT INTO `tb_payment` VALUES (35, '1', '22071519082518337', 'Smartisan 帆布鞋', NULL, NULL, 83, '张三', 2.00, 2.00, 'alipay', '2022-07-15 11:14:39', '2022-07-15 11:14:39', '支付宝支付', '2022-07-15 11:08:30', '2022-07-15 11:14:39');
INSERT INTO `tb_payment` VALUES (36, '1', '22071519082518337', 'Smartisan 帆布鞋', NULL, NULL, 83, '张三', 2.00, 2.00, 'alipay', '2022-07-15 11:14:39', '2022-07-15 11:14:39', '支付宝支付', '2022-07-15 11:11:33', '2022-07-15 11:14:39');
INSERT INTO `tb_payment` VALUES (37, '1', '22071519120028778', '坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', '2022-07-15 11:14:44', '2022-07-15 11:14:44', '支付宝支付', '2022-07-15 11:12:04', '2022-07-15 11:14:44');
INSERT INTO `tb_payment` VALUES (38, '1', '22071519161882024', '坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', '2022-07-15 11:22:56', '2022-07-15 11:22:56', '支付宝支付', '2022-07-15 11:16:22', '2022-07-15 11:22:56');
INSERT INTO `tb_payment` VALUES (39, '1', '22071519223379999', '坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', '2022-07-15 11:30:42', '2022-07-15 11:30:42', '支付宝支付', '2022-07-15 11:23:14', '2022-07-15 11:30:42');
INSERT INTO `tb_payment` VALUES (40, '0', '22071519223379999', '坚果 QuickCharge 4+ 快速充电器', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-15 11:31:36', '2022-07-15 11:31:36');
INSERT INTO `tb_payment` VALUES (41, '1', '22071519315379521', 'Smartisan 帆布鞋', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', '2022-07-15 11:34:46', '2022-07-15 11:34:46', '支付宝支付', '2022-07-15 11:31:57', '2022-07-15 11:34:46');
INSERT INTO `tb_payment` VALUES (42, '1', '22071519353235826', 'Smartisan 帆布鞋', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', '2022-07-15 11:35:51', '2022-07-15 11:35:51', '支付宝支付', '2022-07-15 11:35:36', '2022-07-15 11:35:51');
INSERT INTO `tb_payment` VALUES (43, '1', '22071519445565619', 'Smartisan 帆布鞋', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', '2022-07-15 11:45:20', '2022-07-15 11:45:20', '支付宝支付', '2022-07-15 11:44:59', '2022-07-15 11:45:20');
INSERT INTO `tb_payment` VALUES (44, '1', '22071519571769005', 'Smartisan 帆布鞋', NULL, NULL, 83, '张三', 1.00, 1.00, 'alipay', '2022-07-15 11:57:51', '2022-07-15 11:57:51', '支付宝支付', '2022-07-15 11:57:21', '2022-07-15 11:57:51');
INSERT INTO `tb_payment` VALUES (45, '0', '22071717031483058', '地平线 8 号旅行箱', NULL, NULL, 83, NULL, 20.00, 20.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-17 17:04:08', '2022-07-17 17:04:08');
INSERT INTO `tb_payment` VALUES (46, '0', '22071901141923948', '坚果砖式蓝牙小音箱', NULL, NULL, 83, NULL, 30.00, 30.00, 'alipay', NULL, NULL, '支付宝支付', '2022-07-19 01:19:07', '2022-07-19 01:19:07');

-- ----------------------------
-- Table structure for tb_promo_item
-- ----------------------------
DROP TABLE IF EXISTS `tb_promo_item`;
CREATE TABLE `tb_promo_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ps_id` int(11) NOT NULL COMMENT '秒杀场次id',
  `item_id` int(11) NOT NULL COMMENT '商品id',
  `seckill_price` decimal(10, 2) NOT NULL COMMENT '商品秒杀价格',
  `item_stock` int(11) NOT NULL COMMENT '商品秒杀库存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_promo_item
-- ----------------------------
INSERT INTO `tb_promo_item` VALUES (1, 1, 100057401, 49.00, 10);
INSERT INTO `tb_promo_item` VALUES (2, 1, 100052801, 199.00, 7);
INSERT INTO `tb_promo_item` VALUES (3, 2, 100042203, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (4, 2, 100053312, 20.00, 5);
INSERT INTO `tb_promo_item` VALUES (13, 5, 100023501, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (14, 5, 100052801, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (15, 5, 100057501, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (16, 5, 100047101, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (17, 6, 100023501, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (18, 6, 100052801, 30.00, 9);
INSERT INTO `tb_promo_item` VALUES (19, 6, 100057501, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (20, 6, 100047101, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (21, 7, 100023501, 30.00, 9);
INSERT INTO `tb_promo_item` VALUES (22, 7, 100052801, 30.00, 6);
INSERT INTO `tb_promo_item` VALUES (23, 7, 100057501, 30.00, 8);
INSERT INTO `tb_promo_item` VALUES (24, 7, 100047101, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (25, 8, 100023501, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (26, 8, 100052801, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (27, 8, 100057501, 30.00, 10);
INSERT INTO `tb_promo_item` VALUES (28, 8, 100047101, 30.00, 10);

-- ----------------------------
-- Table structure for tb_promo_session
-- ----------------------------
DROP TABLE IF EXISTS `tb_promo_session`;
CREATE TABLE `tb_promo_session`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `session_id` int(4) NOT NULL COMMENT '场次 id  1:上午十点场 2:下午四点场',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `yyyymmdd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '场次日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_promo_session
-- ----------------------------
INSERT INTO `tb_promo_session` VALUES (1, 1, '2022-07-17 10:00:00', '2022-07-17 12:00:00', '20220717');
INSERT INTO `tb_promo_session` VALUES (2, 2, '2022-07-17 16:47:03', '2022-07-17 18:47:08', '20220717');
INSERT INTO `tb_promo_session` VALUES (5, 1, '2022-07-18 23:35:45', '2022-07-19 01:35:45', '20220718');
INSERT INTO `tb_promo_session` VALUES (6, 2, '2022-07-19 05:35:46', '2022-07-19 07:35:46', '20220718');
INSERT INTO `tb_promo_session` VALUES (7, 1, '2022-07-19 00:24:13', '2022-07-19 02:24:13', '20220719');
INSERT INTO `tb_promo_session` VALUES (8, 2, '2022-07-19 06:24:13', '2022-07-19 08:24:13', '20220719');

-- ----------------------------
-- Table structure for tb_refund
-- ----------------------------
DROP TABLE IF EXISTS `tb_refund`;
CREATE TABLE `tb_refund`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(2) NULL DEFAULT NULL COMMENT '1-成功 2-失败',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '退款人id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款人姓名',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台退款流水号',
  `refund_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方退款流水号',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额(元)',
  `channel` int(2) NULL DEFAULT NULL COMMENT '退款渠道 1-支付宝 2-微信',
  `gmt_create` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '退款时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `trade_no_key`(`trade_no`) USING BTREE COMMENT '平台退款流水号'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_stock
-- ----------------------------
DROP TABLE IF EXISTS `tb_stock`;
CREATE TABLE `tb_stock`  (
  `item_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商品id',
  `stock_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `lock_count` int(11) NOT NULL DEFAULT 0 COMMENT '冻结库存数量',
  `restrict_count` int(3) NULL DEFAULT 5 COMMENT '限购数量',
  `sell_id` int(6) NULL DEFAULT NULL COMMENT '售卖id',
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_stock
-- ----------------------------
INSERT INTO `tb_stock` VALUES (100023501, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100026701, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100026801, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100039702, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100042203, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100042801, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100046401, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100047001, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100047101, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100051701, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100053001, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100053202, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100053312, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100055301, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100057401, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100057501, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100057601, 10000, 0, 5, NULL);
INSERT INTO `tb_stock` VALUES (100057701, 10000, 0, 5, NULL);

-- ----------------------------
-- Table structure for tb_user_verify
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_verify`;
CREATE TABLE `tb_user_verify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(56) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `register_date` datetime(0) NULL DEFAULT NULL,
  `uuid` varchar(56) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_verify` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否验证Y已验证，N为验证',
  `is_expire` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期Y已过期，N为过期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_verify
-- ----------------------------
INSERT INTO `tb_user_verify` VALUES (1, 'cskaoyan01', '2020-05-03 16:12:31', '3f3d929b89f45f730bf430d2aa6bf23d', 'N', 'N');
INSERT INTO `tb_user_verify` VALUES (2, 'cskaoyan100', '2020-05-03 16:14:31', 'd7af0bf762442ab8b595e543b12ca99a', 'N', 'N');
INSERT INTO `tb_user_verify` VALUES (3, 'cskaoyan', '2020-05-03 16:16:38', '0590cb4a2a311f5571d774ddd7c333af', 'N', 'N');
INSERT INTO `tb_user_verify` VALUES (4, 'cskaoyan01', '2020-05-03 16:22:28', '4f07f1cbff13ca20262d63ad17fbd632', 'N', 'N');
INSERT INTO `tb_user_verify` VALUES (8, 'hanxiao', '2022-06-16 15:55:02', 'eb27d025509d4b0b36d3ee600d3da22a', 'Y', 'N');
INSERT INTO `tb_user_verify` VALUES (9, 'admin', '2022-06-17 14:53:11', '7044c01fc083e6c6175e2d3b3ed9741d', 'Y', 'N');
INSERT INTO `tb_user_verify` VALUES (10, 'zhangsan', '2022-06-22 05:51:59', '4a9ced809f8484bbb722d79e0f47a99c', 'Y', 'N');

SET FOREIGN_KEY_CHECKS = 1;
