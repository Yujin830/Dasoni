CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `mydb`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.41-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `life`
--

DROP TABLE IF EXISTS `life`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `life` (
                        `member_id` bigint(20) NOT NULL,
                        `when` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        UNIQUE KEY `member_id_UNIQUE` (`member_id`),
                        CONSTRAINT `FK_MEMBER_TO_LIFE_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
                          `member_id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `login_id` varchar(20) NOT NULL,
                          `password` varchar(100) NOT NULL,
                          `gender` varchar(10) NOT NULL,
                          `birth` date NOT NULL,
                          `phone_number` varchar(20) NOT NULL,
                          `nickname` varchar(10) DEFAULT NULL,
                          `isblack` tinyint(1) DEFAULT 0,
                          `rank` int(11) DEFAULT 0,
                          `meeting_cnt` int(11) DEFAULT 0,
                          `profile_image_src` varchar(200) DEFAULT NULL,
                          `job` varchar(20) DEFAULT NULL,
                          `si_do` int(11) DEFAULT NULL,
                          `gu_gun` int(11) DEFAULT NULL,
                          PRIMARY KEY (`member_id`),
                          UNIQUE KEY `member_id_UNIQUE` (`member_id`),
                          UNIQUE KEY `login_id_UNIQUE` (`login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
                          `notice_id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `content` varchar(400) NOT NULL,
                          `visible_time` int(11) NOT NULL,
                          PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `party`
--

DROP TABLE IF EXISTS `party`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `party` (
                         `party_id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `party_gender` varchar(10) NOT NULL,
                         `avg_rating` int(11) DEFAULT NULL,
                         PRIMARY KEY (`party_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `party_member`
--

DROP TABLE IF EXISTS `party_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `party_member` (
                                `party_id` bigint(20) NOT NULL,
                                `member_id` bigint(20) NOT NULL,
                                `megi` tinyint(1) NOT NULL,
                                `party_manager` tinyint(1) NOT NULL,
                                KEY `FK_MEMBER_TO_PARTY_MEMBER_1` (`member_id`),
                                KEY `FK_PARTY_TO_PARTY_MEMBER_1` (`party_id`),
                                CONSTRAINT `FK_MEMBER_TO_PARTY_MEMBER_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                CONSTRAINT `FK_PARTY_TO_PARTY_MEMBER_1` FOREIGN KEY (`party_id`) REFERENCES `party` (`party_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `party_room`
--

DROP TABLE IF EXISTS `party_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `party_room` (
                              `room_id` bigint(20) NOT NULL,
                              `party_id` bigint(20) NOT NULL,
                              KEY `FK_PARTY_TO_PARTY_ROOM_1` (`party_id`),
                              KEY `FK_ROOM_TO_PARTY_ROOM_1` (`room_id`),
                              CONSTRAINT `FK_PARTY_TO_PARTY_ROOM_1` FOREIGN KEY (`party_id`) REFERENCES `party` (`party_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                              CONSTRAINT `FK_ROOM_TO_PARTY_ROOM_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
                            `question_id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `content` varchar(400) NOT NULL,
                            PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recent_match`
--

DROP TABLE IF EXISTS `recent_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recent_match` (
                                `member_id` bigint(20) NOT NULL,
                                `match_user_id` bigint(20) NOT NULL,
                                KEY `FK_MEMBER_TO_RECENT_MATCH_1` (`member_id`),
                                KEY `FK_MEMBER_TO_RECENT_MATCH_2` (`match_user_id`),
                                CONSTRAINT `FK_MEMBER_TO_RECENT_MATCH_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                CONSTRAINT `FK_MEMBER_TO_RECENT_MATCH_2` FOREIGN KEY (`match_user_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
                        `room_id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `type` tinyint(1) NOT NULL,
                        `start_time` timestamp NULL DEFAULT NULL,
                        `people_cnt` int(11) DEFAULT NULL,
                        `video` int(11) DEFAULT NULL,
                        `title` varchar(20) DEFAULT NULL,
                        `megi_setting` tinyint(1) NOT NULL,
                        `limit_rating` int(11) NOT NULL,
                        PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room_q`
--

DROP TABLE IF EXISTS `room_q`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_q` (
                          `room_id` bigint(20) NOT NULL,
                          `question_id` bigint(20) NOT NULL,
                          KEY `FK_QUESTION_TO_ROOM_Q_1` (`question_id`),
                          KEY `FK_ROOM_TO_ROOM_Q_1` (`room_id`),
                          CONSTRAINT `FK_QUESTION_TO_ROOM_Q_1` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT `FK_ROOM_TO_ROOM_Q_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room_signal`
--

DROP TABLE IF EXISTS `room_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_signal` (
                               `room_id` bigint(20) NOT NULL,
                               `to_user` bigint(20) NOT NULL,
                               `from_user` bigint(20) NOT NULL,
                               KEY `FK_ROOM_TO_ROOM_SIGNAL_1` (`room_id`),
                               CONSTRAINT `FK_ROOM_TO_ROOM_SIGNAL_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `warn`
--

DROP TABLE IF EXISTS `warn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warn` (
                        `member_id` bigint(20) NOT NULL,
                        `when` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        KEY `FK_MEMBER_TO_WARN_1` (`member_id`),
                        CONSTRAINT `FK_MEMBER_TO_WARN_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-24 16:54:24
