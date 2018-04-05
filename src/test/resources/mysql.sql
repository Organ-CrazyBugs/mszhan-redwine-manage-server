CREATE DATABASE `mszhan_redwine_manage` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

CREATE TABLE `user_login` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;


INSERT INTO `mszhan_redwine_manage`.`user_login`(`id`, `user_name`, `password`) VALUES (1, 'admin', 'ceb4f32325eda6142bd65215f4c0f371');



