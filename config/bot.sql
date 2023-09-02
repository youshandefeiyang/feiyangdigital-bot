-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2023-09-02 18:23:49
-- 服务器版本： 8.0.24
-- PHP 版本： 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `bot`
--

-- --------------------------------------------------------

--
-- 表的结构 `groupinfo`
--

CREATE TABLE `groupinfo` (
  `id` int NOT NULL,
  `groupId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ownerAndAnonymousAdmins` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `groupName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `keyWords` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `keyWordsflag` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'notallow',
  `deleteKeywordFlag` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'notdelete',
  `settingTimeStamp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `intoGroupWelcomeWords` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转储表的索引
--

--
-- 表的索引 `groupinfo`
--
ALTER TABLE `groupinfo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `groupId` (`groupId`),
  ADD KEY `settingTimeStamp` (`settingTimeStamp`),
  ADD KEY `groupName` (`groupName`),
  ADD KEY `keyWordsflag` (`keyWordsflag`),
  ADD KEY `deleteKeywordFlag` (`deleteKeywordFlag`);
ALTER TABLE `groupinfo` ADD FULLTEXT KEY `ownerAndAnonymousAdmins` (`ownerAndAnonymousAdmins`);
ALTER TABLE `groupinfo` ADD FULLTEXT KEY `intoGroupWelcomeWords` (`intoGroupWelcomeWords`);
ALTER TABLE `groupinfo` ADD FULLTEXT KEY `keyWords` (`keyWords`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `groupinfo`
--
ALTER TABLE `groupinfo`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
