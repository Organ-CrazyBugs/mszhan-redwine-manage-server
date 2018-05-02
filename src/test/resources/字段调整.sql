ALTER TABLE `mszhan_redwine_manage`.`order_header`
ADD COLUMN `shipping_fee` decimal(18, 6) DEFAULT NULL COMMENT '运费' AFTER `agent_name`;

ALTER TABLE `mszhan_redwine_manage`.`order_header`
MODIFY COLUMN `status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '(WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货, REMOVED 已删除)' AFTER `postal_code`;


ALTER TABLE `mszhan_redwine_manage`.`inbound_history`
ADD COLUMN `warehouse_id` int(11) AFTER `order_item_id`;

ALTER TABLE `mszhan_redwine_manage`.`order_header`
ADD COLUMN `phone_number` varchar(50) COMMENT '电话号码' AFTER `shipping_fee`;