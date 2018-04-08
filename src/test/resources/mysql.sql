
-- CREATE DATABASE `mszhan_redwine_manage` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';


/*
Navicat MySQL Data Transfer

Source Server         : wineSales
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : mszhan_redwine_manage

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-04-06 21:45:05
*/

-- ----------------------------
-- Table structure for `agents`
-- ----------------------------
DROP TABLE IF EXISTS `agents`;
CREATE TABLE `agents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `tel` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `balance` decimal(18,6) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `updator` int(11) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agents
-- ----------------------------

-- ----------------------------
-- Table structure for `agent_price_history`
-- ----------------------------
DROP TABLE IF EXISTS `agent_price_history`;
CREATE TABLE `agent_price_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `price` decimal(18,6) DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL COMMENT '(RECHARGE充值，SPEND花费)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agent_price_history
-- ----------------------------

-- ----------------------------
-- Table structure for `bill`
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(100) DEFAULT NULL,
  `price` decimal(18,6) DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bill
-- ----------------------------

-- ----------------------------
-- Table structure for `inbound_history`
-- ----------------------------
DROP TABLE IF EXISTS `inbound_history`;
CREATE TABLE `inbound_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL COMMENT '(TRANSTER_INBOUND调拨出库，SALES_INBOUND销售出库，OTHER_INBOUND其他)',
  `product_id` int(11) DEFAULT NULL,
  `sku` varchar(200) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `creator_name` varchar(100) DEFAULT NULL,
  `order_id` varchar(100) DEFAULT NULL,
  `order_item_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inbound_history
-- ----------------------------

-- ----------------------------
-- Table structure for `inventory`
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ware_house_id` int(11) DEFAULT NULL COMMENT '仓库id',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `sku` varchar(200) DEFAULT NULL COMMENT 'sku',
  `quantity` int(11) DEFAULT NULL COMMENT '库存数量',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `updator` int(11) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inventory
-- ----------------------------

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order_header`;
CREATE TABLE `order_header` (
  `order_id` varchar(100) NOT NULL COMMENT '订单号',
  `total_amount` decimal(10,0) DEFAULT NULL COMMENT '订单总金额',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` int(11) DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `client_name` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `agtent_id` int(11) DEFAULT NULL COMMENT '代理id',
  `address` varchar(500) DEFAULT NULL COMMENT '客户地址',
  `postal_code` varchar(50) DEFAULT NULL COMMENT '邮政编码',
  `status` varchar(50) DEFAULT NULL COMMENT '(WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货)',
  `payment_status` varchar(50) DEFAULT NULL COMMENT '(PAID已付款，UNPAID未付款)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for `order_delivery_person`
-- ----------------------------
DROP TABLE IF EXISTS `order_delivery_person`;
CREATE TABLE `order_delivery_person` (
  `id` int(11) DEFAULT NULL,
  `order_id` varchar(100) DEFAULT NULL,
  `order_item_id` int(11) DEFAULT NULL,
  `delivery_person` varchar(100) DEFAULT NULL,
  `tel` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_delivery_person
-- ----------------------------

-- ----------------------------
-- Table structure for `order_item`
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(100) DEFAULT NULL COMMENT '订单号',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `sku` varchar(200) DEFAULT NULL COMMENT 'sku',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `shipping_fee` decimal(18,6) DEFAULT NULL COMMENT '运费',
  `packaging_fee` decimal(18,6) DEFAULT NULL COMMENT '包装费',
  `unit_price` decimal(18,6) DEFAULT NULL COMMENT '单价',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `updator` datetime DEFAULT NULL COMMENT '更新人',
  `agent_id` int(11) DEFAULT NULL COMMENT '代理id',
  `type` varchar(50) DEFAULT NULL COMMENT '(GIFT赠品,SALES销售商品)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_item
-- ----------------------------

-- ----------------------------
-- Table structure for `outbound_history`
-- ----------------------------
DROP TABLE IF EXISTS `outbound_history`;
CREATE TABLE `outbound_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL COMMENT '(TRANSTER_OUTBOUND调拨出库，SALES_OUTBOUND销售出库，OTHER其他)',
  `product_id` int(11) DEFAULT NULL,
  `sku` varchar(200) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `creator_name` varchar(100) DEFAULT NULL,
  `order_id` varchar(100) DEFAULT NULL,
  `order_item_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of outbound_history
-- ----------------------------

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(500) DEFAULT NULL COMMENT '产品名称',
  `cost` decimal(18,6) unsigned zerofill DEFAULT NULL COMMENT '成本',
  `general_gent_ price` decimal(18,6) DEFAULT NULL COMMENT '总代理价',
  `gent_price` decimal(18,6) DEFAULT NULL COMMENT '代理价',
  `wholesale_price` decimal(18,6) DEFAULT NULL COMMENT '批发价',
  `retail_price` decimal(18,6) DEFAULT NULL COMMENT '零售价',
  `unit` varchar(5) DEFAULT NULL COMMENT '单位',
  `specification` varchar(50) DEFAULT NULL COMMENT '规格',
  `level` varchar(30) DEFAULT NULL COMMENT '级别',
  `production_area` varchar(200) DEFAULT NULL COMMENT '产区',
  `product_url` varchar(200) DEFAULT NULL COMMENT '图片地址',
  `sku` varchar(200) DEFAULT NULL COMMENT 'sku',
  `creator` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `updator` int(11) DEFAULT NULL,
  `back_remark` varchar(500) DEFAULT NULL COMMENT '背景',
  `brand_name` varchar(200) DEFAULT NULL COMMENT '品牌',
  `alcohol_content` decimal(10,4) DEFAULT NULL COMMENT '酒精度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login`
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_login
-- ----------------------------
INSERT INTO `user_login` VALUES ('1', 'admin', 'ceb4f32325eda6142bd65215f4c0f371');

-- ----------------------------
-- Table structure for `warehouse`
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `principal` varchar(100) DEFAULT NULL COMMENT '负责人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `updator` int(11) DEFAULT NULL COMMENT '更新人',
  `tel` varchar(100) DEFAULT NULL COMMENT '手机号码',
  `phone` varchar(100) DEFAULT NULL COMMENT '电话号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------


ALTER TABLE `warehouse` ADD COLUMN `status` char(10) NOT NULL DEFAULT 'ENABLED' COMMENT '状态, ENABLED, DISABLED';
ALTER TABLE `warehouse` ADD COLUMN `remark` varchar(500) NULL COMMENT '备注' AFTER `status`;

INSERT INTO `warehouse`(`id`, `name`, `address`, `principal`, `create_date`, `creator`, `update_date`, `updator`, `tel`, `phone`, `status`) VALUES (1, '深圳测试仓库', '广东省深圳市龙岗区山窝窝', '小名', '2018-04-07 11:49:27', NULL, '2018-04-07 11:49:35', NULL, '85512344321', '13544445555', 'ENABLED');
INSERT INTO `warehouse`(`id`, `name`, `address`, `principal`, `create_date`, `creator`, `update_date`, `updator`, `tel`, `phone`, `status`) VALUES (2, '香港测试仓库', '香港山窝窝', '小名', '2018-04-07 11:49:27', NULL, '2018-04-07 11:49:35', NULL, '85512344321', '13544445555', 'ENABLED');
