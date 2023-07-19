-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (9);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(400) NOT NULL,
  `visible_time` int NOT NULL,
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(400) NOT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'어떤 거 좋아하세요? 어떤 취미 (혹 관심사)가 있으세요?'),(2,'올해에 꼭 하고 싶은 (버킷리스트 같은거) 것이 있으세요?'),(3,'올해도 끝나가는데 언제가 가장 기억이 남으세요?'),(4,'어떤 영화를 좋아하세요?'),(5,'가장 좋아하고 관심있는 분야는 어떤 것이세요?'),(6,'해외 여행 가보신 곳 중에 어떤 곳이 가장 기억이 남으세요?'),(7,'좋아하는 음식은 무엇이세요?'),(8,'10년 후 어떤 모습이 되는 게 목표세요?'),(9,'어떤 일을 하세요? 그 일의 어떤 부분이 좋으세요?'),(10,'삶에 영향을 미친 사람은 누구였어요?'),(11,'어떻게 스트레스를 푸세요?'),(12,'술 좋아하세요? 어떤 걸 주로 드세요?'),(13,'주말은 보통 어떻게 시간보내세요?'),(14,'어떤 선물 받는 것을 좋아하세요?'),(15,'서울에서 가장 좋아하는 장소가 있으세요?'),(16,'싫어하는 것이 있나요?(음식, 행동 등)'),(17,'좋아하는 작가나, 연예인, 배우가 있으세요?'),(18,'관심있는 스포츠가 있으세요?'),(19,'여태까지 해본 적은 없지만 해보고 싶은 그런게 있으세요?'),(20,'어떤 계절을 좋아하세요?'),(21,'남사친(여사친)과 어느 정도 사이까지 괜찮나요?'),(22,'애인이랑 꼭 같이 가고 싶은 곳이 있다면?'),(23,'좋아하는 동물이 있나요?'),(24,'가장 좋아하는 TV 프로그램이나 영화가 있나요?'),(25,'일주일 동안 평소에 어떻게 보내나요?'),(26,'보통 어떤 스타일을 선호하시나요?'),(27,'이상형은 어떤 타입이세요?'),(28,'어떤 종류의 음악을 좋아하시나요?'),(29,'휴식을 취할 때는 어떤 방식으로 쉬시나요?'),(30,'이번 주말에 계획이 있으신가요?'),(31,'어릴 적 꿈이 무엇이었어요?'),(32,'현재 나의 인생에서 가장 중요한 가치는 무엇인가요?'),(33,'이번 만남의 진지한 정도는?'),(34,'전 애인 친구랑 사귀기 VS 친구 전 애인이랑 사귀기'),(35,'이성친구와 1박 2일 여행 가는 애인 VS 전 애인과 밤새 술 마시는 애인'),(36,'10억 빛이 있는 내 취향 애인 vs 10억 있는 내 취향 절대 아닌 애인'),(37,'내 과거를 캐내는 애인 VS 과거에 무심한 애인'),(38,'이혼 경력 3번 있는 애인 VS 숨겨진 자녀가 1명 있는 애인'),(39,'대머리가 된 애인 VS 털복숭이가 된 애인'),(40,'코털 긴 애인 VS 겨털 긴 애인'),(41,'대머리가 된 애인 VS 털복숭이가 된 애인'),(42,'게임에 미쳐버린 애인 VS 술에 미쳐버린 애인'),(43,'애인이 간첩인걸 알고 신고하기(5억) VS 신고 안 한다'),(44,'전 애인 썰 계속 물어보는 애인 VS 전 애인 썰만 이야기하는 애인'),(45,'애인이랑 말없이 스킨십만 1년 VS 스킨십 없이 플라토닉 러브 1년'),(46,'술 먹고 전 애인 이름 부르기 VS 상대방 전 애인 이름 듣기'),(47,'모르는 사람 집에 애인 속옷 VS 애인 집에 모르는 사람 속옷'),(48,'한 달 동안 뽀뽀 금지 VS 한 달 동안 손잡기 금지'),(49,'입에서 발 냄새 나는 애인 VS 발에서 입냄새 나는 애인'),(50,'무성욕(한달에한번) VS 왕성욕(하루에한번)'),(51,'여사친 10명 VS 20년 된 여사친 1명'),(52,'사귀기 전에 스킨쉽 어디까지 가능?'),(53,'결혼은 언제쯤 하고 싶으세요?'),(54,'지금까지 연애 몇 번 해보셨나요?'),(55,'여기서 제일 이상형에 가까운 사람은?');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `type` tinyint NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `people_cnt` int DEFAULT NULL,
  `video` int DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  `megi_setting` tinyint NOT NULL,
  `limit_rating` int NOT NULL,
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `room_id_UNIQUE` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,0,'2023-07-17 05:35:12',6,1,'방제목',0,1),(2,1,'2023-07-17 05:35:34',4,1,'방제목2',0,1);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_member`
--

DROP TABLE IF EXISTS `room_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_member` (
  `user_id` int NOT NULL,
  `room_id` int NOT NULL,
  `megi` tinyint DEFAULT NULL,
  KEY `room_id_idx` (`room_id`),
  KEY `room_member_user_id` (`user_id`),
  CONSTRAINT `room_member_room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `room_member_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_member`
--

LOCK TABLES `room_member` WRITE;
/*!40000 ALTER TABLE `room_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_q`
--

DROP TABLE IF EXISTS `room_q`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_q` (
  `question_id` int NOT NULL,
  `room_id` int NOT NULL,
  KEY `question_id_idx` (`question_id`),
  KEY `room_id_idx` (`room_id`),
  CONSTRAINT `room_q_question_id` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `room_q_room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_q`
--

LOCK TABLES `room_q` WRITE;
/*!40000 ALTER TABLE `room_q` DISABLE KEYS */;
INSERT INTO `room_q` VALUES (5,1),(35,1),(42,1);
/*!40000 ALTER TABLE `room_q` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_signal`
--

DROP TABLE IF EXISTS `room_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_signal` (
  `room_id` int NOT NULL,
  `to_user` int NOT NULL,
  `from_user` int NOT NULL,
  KEY `room_id_idx` (`room_id`),
  CONSTRAINT `room_signal_room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_signal`
--

LOCK TABLES `room_signal` WRITE;
/*!40000 ALTER TABLE `room_signal` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_signal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `login_id` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(10) DEFAULT NULL,
  `age` int NOT NULL,
  `gender` varchar(10) NOT NULL,
  `birth` date NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `isblack` tinyint NOT NULL DEFAULT '0',
  `rank` varchar(20) DEFAULT '"white"',
  `profile_image_src` varchar(200) DEFAULT NULL,
  `meeting_count` int NOT NULL DEFAULT '0',
  `roles` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'ssafy','{bcrypt}$2a$10$vz4LO3LTVmUlS.eb22RP/e70KWcjkoJ3Hxba1qOtZvg05ZbUBJsv6','김싸피',21,'male','2023-07-16','01012341234',0,'white','abc',0,NULL),(3,'ssafy4','{bcrypt}$2a$10$41.Q2j6cadIKsZ1EYiRkDODEO7QdmlmabsTU.VPnGuVwzDdvL2CUq','김싸피',21,'male','2023-07-16','01012341234',0,'white','abc',0,NULL),(4,'ssafy41','{bcrypt}$2a$10$ssTDSRNFSmHzChuNfhmcz.X92N5Z8Sn.OkMMSfPE/iAKko9Kc5b6u','김싸피',21,'male','2023-07-16','01012341234',0,'white','abc',0,NULL),(5,'ssafy412','{bcrypt}$2a$10$2bv/MufpUyg/MaE0s69yZenLI0HOvp4UyENtxN9DXoDlJYcM5StQa','김싸피',21,'male','2023-07-16','01012341234',0,'white','abc',0,NULL),(6,'ssafy4124','{bcrypt}$2a$10$aKKi44pGA7I4WV0MgAEkLueN34NGKy1VGqZKZO1y/kVyMxU7btclq','김싸피',21,'male','2023-07-16','010-1234-1234',0,'white','https://blog.kakaocdn.net/dn/bgq3u4/btrtwLG0prO/dqT7WhJlzC2PbYuubG3Pp1/img.png',12,NULL),(7,'','{bcrypt}$2a$10$gwOHvnPg83aNEaIP6AlWY./OnD9g/U./czZ29ur7.9GSK6C2fN4ky','김싸피',21,'male','2023-07-16','010-1234-1234',0,'white','https://blog.kakaocdn.net/dn/bgq3u4/btrtwLG0prO/dqT7WhJlzC2PbYuubG3Pp1/img.png',12,NULL),(8,'hi123','{bcrypt}$2a$10$b.yuf4v80AqrDyFrOHc3oOIdF5weP6YWfluQrtn/zuj8v6SrJ0IvO','김싸피',21,'male','2023-07-16','010-1234-1234',0,'white','https://blog.kakaocdn.net/dn/bgq3u4/btrtwLG0prO/dqT7WhJlzC2PbYuubG3Pp1/img.png',12,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warn`
--

DROP TABLE IF EXISTS `warn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warn` (
  `user_id` int NOT NULL,
  `when` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `warn_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warn`
--

LOCK TABLES `warn` WRITE;
/*!40000 ALTER TABLE `warn` DISABLE KEYS */;
/*!40000 ALTER TABLE `warn` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-19 17:23:57
