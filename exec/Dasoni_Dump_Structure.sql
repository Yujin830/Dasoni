CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET latin1 */;
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
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `member` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKghs2pqki2va3n90061ftfgrbv` (`member`),
  CONSTRAINT `FKghs2pqki2va3n90061ftfgrbv` FOREIGN KEY (`member`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `guide`
--

DROP TABLE IF EXISTS `guide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guide` (
  `guide_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `visible_time` int(11) NOT NULL,
  PRIMARY KEY (`guide_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_url` varchar(255) DEFAULT NULL,
  `origin_name` varchar(255) DEFAULT NULL,
  `stored_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `life`
--

DROP TABLE IF EXISTS `life`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `life` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `use_date` date DEFAULT NULL,
  `member_member_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkawomcsitsu9jq5uul9tgkem9` (`member_member_id`),
  CONSTRAINT `FKkawomcsitsu9jq5uul9tgkem9` FOREIGN KEY (`member_member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `birth` date DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `gu_gun` int(11) DEFAULT NULL,
  `isblack` bit(1) DEFAULT NULL,
  `job` varchar(20) DEFAULT NULL,
  `login_id` varchar(20) DEFAULT NULL,
  `meeting_cnt` int(11) DEFAULT NULL,
  `nickname` varchar(10) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `profile_image_src` varchar(200) DEFAULT NULL,
  `rating` bigint(20) DEFAULT NULL,
  `si_do` int(11) DEFAULT NULL,
  `is_first` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `UK_enfm5patwjqulw8k4wwuo6f60` (`login_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chat_url` varchar(255) DEFAULT NULL,
  `megi_acceptable` bit(1) DEFAULT NULL,
  `rating_limit` bigint(20) DEFAULT NULL,
  `room_type` varchar(255) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `female_party_party_id` bigint(20) DEFAULT NULL,
  `male_party_party_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK817mmx24hegh1nosl27a7fdc8` (`female_party_party_id`),
  KEY `FKkux3nlxeyq1uhi8pdyrc08h9v` (`male_party_party_id`),
  CONSTRAINT `FK817mmx24hegh1nosl27a7fdc8` FOREIGN KEY (`female_party_party_id`) REFERENCES `party` (`party_id`),
  CONSTRAINT `FKkux3nlxeyq1uhi8pdyrc08h9v` FOREIGN KEY (`male_party_party_id`) REFERENCES `party` (`party_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room_member`
--

DROP TABLE IF EXISTS `room_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_room_leader` bit(1) DEFAULT NULL,
  `is_special_user` bit(1) DEFAULT NULL,
  `score` int(11) DEFAULT '0',
  `member_id` bigint(20) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKav1svcloqr7ue4dhsgkj5t4a5` (`member_id`),
  KEY `FKlmp67erahqx7u5shbkc12p0lw` (`room_id`),
  CONSTRAINT `FKav1svcloqr7ue4dhsgkj5t4a5` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `FKlmp67erahqx7u5shbkc12p0lw` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `warn`
--

DROP TABLE IF EXISTS `warn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) DEFAULT NULL,
  `warn_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-18 11:23:58

INSERT INTO mydb.guide (content, visible_time) VALUES ("다소니에 오신 여러분 환영합니다. 처음 만난 서로에게 자기소개를 해 주세요.", 0);
INSERT INTO mydb.guide (content, visible_time) VALUES ("사랑에 빠지는 시간은 3초라고 합니다. 첫인상을 봤을 때 호감이 있는 이성에게 채팅을 보내주세요. 제한 시간은 30초 입니다.", 5);
INSERT INTO mydb.guide (content, visible_time) VALUES ("서로에 대해 더 알아보는 시간을 가지겠습니다. 곧 여러분의 나이와 직업이 공개됩니다.", 20);
INSERT INTO mydb.guide (content, visible_time) VALUES ("메기 등장!! 메기 등장!! 곧 메기가 등장합니다.", 30);
INSERT INTO mydb.guide (content, visible_time) VALUES ("이제 최종 투표의 시간입니다. 호감이 있는 이성에게 하트를 눌러 시그널을 보내주세요. 서로의 마음이 이어지길 응원합니다!", 50);

INSERT INTO mydb.question (content) VALUES ("어떤 거 좋아하세요? 어떤 취미 (혹 관심사)가 있으세요?");
INSERT INTO mydb.question (content) VALUES ("올해에 꼭 하고 싶은 (버킷리스트 같은거) 것이 있으세요?");
INSERT INTO mydb.question (content) VALUES ("올해도 끝나가는데 언제가 가장 기억이 남으세요?");
INSERT INTO mydb.question (content) VALUES ("어떤 영화를 좋아하세요?");
INSERT INTO mydb.question (content) VALUES ("가장 좋아하고 관심있는 분야는 어떤 것이세요?");
INSERT INTO mydb.question (content) VALUES ("해외 여행 가보신 곳 중에 어떤 곳이 가장 기억이 남으세요?");
INSERT INTO mydb.question (content) VALUES ("좋아하는 음식은 무엇이세요?");
INSERT INTO mydb.question (content) VALUES ("10년 후 어떤 모습이 되는 게 목표세요?");
INSERT INTO mydb.question (content) VALUES ("어떤 일을 하세요? 그 일의 어떤 부분이 좋으세요?");
INSERT INTO mydb.question (content) VALUES ("삶에 영향을 미친 사람은 누구였어요?");
INSERT INTO mydb.question (content) VALUES ("어떻게 스트레스를 푸세요?");
INSERT INTO mydb.question (content) VALUES ("술 좋아하세요? 어떤 걸 주로 드세요?");
INSERT INTO mydb.question (content) VALUES ("주말은 보통 어떻게 시간보내세요?");
INSERT INTO mydb.question (content) VALUES ("어떤 선물 받는 것을 좋아하세요?");
INSERT INTO mydb.question (content) VALUES ("서울에서 가장 좋아하는 장소가 있으세요?");
INSERT INTO mydb.question (content) VALUES ("싫어하는 것이 있나요?(음식, 행동 등)");
INSERT INTO mydb.question (content) VALUES ("좋아하는 작가나, 연예인, 배우가 있으세요?");
INSERT INTO mydb.question (content) VALUES ("관심있는 스포츠가 있으세요?");
INSERT INTO mydb.question (content) VALUES ("여태까지 해본 적은 없지만 해보고 싶은 그런게 있으세요?");
INSERT INTO mydb.question (content) VALUES ("어떤 계절을 좋아하세요?");
INSERT INTO mydb.question (content) VALUES ("남사친(여사친)과 어느 정도 사이까지 괜찮나요?");
INSERT INTO mydb.question (content) VALUES ("애인이랑 꼭 같이 가고 싶은 곳이 있다면?");
INSERT INTO mydb.question (content) VALUES ("좋아하는 동물이 있나요?");
INSERT INTO mydb.question (content) VALUES ("가장 좋아하는 TV 프로그램이나 영화가 있나요?");
INSERT INTO mydb.question (content) VALUES ("일주일 동안 평소에 어떻게 보내나요?");
INSERT INTO mydb.question (content) VALUES ("보통 어떤 스타일을 선호하시나요?");
INSERT INTO mydb.question (content) VALUES ("이상형은 어떤 타입이세요?");
INSERT INTO mydb.question (content) VALUES ("어떤 종류의 음악을 좋아하시나요?");
INSERT INTO mydb.question (content) VALUES ("휴식을 취할 때는 어떤 방식으로 쉬시나요?");
INSERT INTO mydb.question (content) VALUES ("이번 주말에 계획이 있으신가요?");
INSERT INTO mydb.question (content) VALUES ("어릴 적 꿈이 무엇이었어요?");
INSERT INTO mydb.question (content) VALUES ("현재 나의 인생에서 가장 중요한 가치는 무엇인가요?");
INSERT INTO mydb.question (content) VALUES ("이번 만남의 진지한 정도는?");
INSERT INTO mydb.question (content) VALUES ("전 애인 친구랑 사귀기 VS 친구 전 애인이랑 사귀기");
INSERT INTO mydb.question (content) VALUES ("이성친구와 1박 2일 여행 가는 애인 VS 전 애인과 밤새 술 마시는 애인");
INSERT INTO mydb.question (content) VALUES ("10억 빛이 있는 내 취향 애인 vs 10억 있는 내 취향 절대 아닌 애인");
INSERT INTO mydb.question (content) VALUES ("내 과거를 캐내는 애인 VS 과거에 무심한 애인");
INSERT INTO mydb.question (content) VALUES ("이혼 경력 3번 있는 애인 VS 숨겨진 자녀가 1명 있는 애인");
INSERT INTO mydb.question (content) VALUES ("대머리가 된 애인 VS 털복숭이가 된 애인");
INSERT INTO mydb.question (content) VALUES ("코털 긴 애인 VS 겨털 긴 애인");
INSERT INTO mydb.question (content) VALUES ("대머리가 된 애인 VS 털복숭이가 된 애인");
INSERT INTO mydb.question (content) VALUES ("게임에 미쳐버린 애인 VS 술에 미쳐버린 애인");
INSERT INTO mydb.question (content) VALUES ("애인이 간첩인걸 알고 신고하기(5억) VS 신고 안 한다");
INSERT INTO mydb.question (content) VALUES ("전 애인 썰 계속 물어보는 애인 VS 전 애인 썰만 이야기하는 애인");
INSERT INTO mydb.question (content) VALUES ("애인이랑 말없이 스킨십만 1년 VS 스킨십 없이 플라토닉 러브 1년");
INSERT INTO mydb.question (content) VALUES ("술 먹고 전 애인 이름 부르기 VS 상대방 전 애인 이름 듣기");
INSERT INTO mydb.question (content) VALUES ("모르는 사람 집에 애인 속옷 VS 애인 집에 모르는 사람 속옷");
INSERT INTO mydb.question (content) VALUES ("한 달 동안 뽀뽀 금지 VS 한 달 동안 손잡기 금지");
INSERT INTO mydb.question (content) VALUES ("입에서 발 냄새 나는 애인 VS 발에서 입냄새 나는 애인");
INSERT INTO mydb.question (content) VALUES ("무성욕(한달에한번) VS 왕성욕(하루에한번)");
INSERT INTO mydb.question (content) VALUES ("여사친 10명 VS 20년 된 여사친 1명");
INSERT INTO mydb.question (content) VALUES ("사귀기 전에 스킨쉽 어디까지 가능?");
INSERT INTO mydb.question (content) VALUES ("결혼은 언제쯤 하고 싶으세요?");
INSERT INTO mydb.question (content) VALUES ("지금까지 연애 몇 번 해보셨나요?");
INSERT INTO mydb.question (content) VALUES ("여기서 제일 이상형에 가까운 사람은?");
