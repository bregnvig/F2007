-- MySQL dump 10.11
--
-- Host: localhost    Database: f2007
-- ------------------------------------------------------
-- Server version	5.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Account`
--

DROP TABLE IF EXISTS `Account`;
CREATE TABLE `Account` (
  `id` bigint(20) NOT NULL auto_increment,
  `balance` decimal(19,2) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Account`
--

LOCK TABLES `Account` WRITE;
/*!40000 ALTER TABLE `Account` DISABLE KEYS */;
INSERT INTO `Account` VALUES (1,'100.00'),(2,'0.00'),(3,'0.00');
/*!40000 ALTER TABLE `Account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PersistentRole`
--

DROP TABLE IF EXISTS `PersistentRole`;
CREATE TABLE `PersistentRole` (
  `id` bigint(20) NOT NULL auto_increment,
  `role` int(11) default NULL,
  `player_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK7ED22BCD872AC06E` (`player_id`),
  CONSTRAINT `FK7ED22BCD872AC06E` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `PersistentRole`
--

LOCK TABLES `PersistentRole` WRITE;
/*!40000 ALTER TABLE `PersistentRole` DISABLE KEYS */;
INSERT INTO `PersistentRole` VALUES (1,2,1),(2,1,1),(3,0,1),(4,0,2),(5,0,3),(6,1,3);
/*!40000 ALTER TABLE `PersistentRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WBC`
--

DROP TABLE IF EXISTS `WBC`;
CREATE TABLE `WBC` (
  `id` bigint(20) NOT NULL auto_increment,
  `previousRace_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK14ED847545097` (`previousRace_id`),
  CONSTRAINT `FK14ED847545097` FOREIGN KEY (`previousRace_id`) REFERENCES `race` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `WBC`
--

LOCK TABLES `WBC` WRITE;
/*!40000 ALTER TABLE `WBC` DISABLE KEYS */;
INSERT INTO `WBC` VALUES (1,NULL);
/*!40000 ALTER TABLE `WBC` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WBCEntry`
--

DROP TABLE IF EXISTS `WBCEntry`;
CREATE TABLE `WBCEntry` (
  `id` bigint(20) NOT NULL auto_increment,
  `points` int(11) NOT NULL,
  `race_id` bigint(20) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  `wbc_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK6736507AAFDCD6F0` (`wbc_id`),
  KEY `FK6736507A872AC06E` (`player_id`),
  KEY `FK6736507A51EAFF6E` (`race_id`),
  CONSTRAINT `FK6736507A51EAFF6E` FOREIGN KEY (`race_id`) REFERENCES `race` (`id`),
  CONSTRAINT `FK6736507A872AC06E` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK6736507AAFDCD6F0` FOREIGN KEY (`wbc_id`) REFERENCES `wbc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `WBCEntry`
--

LOCK TABLES `WBCEntry` WRITE;
/*!40000 ALTER TABLE `WBCEntry` DISABLE KEYS */;
/*!40000 ALTER TABLE `WBCEntry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_entry`
--

DROP TABLE IF EXISTS `account_entry`;
CREATE TABLE `account_entry` (
  `DTYPE` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL auto_increment,
  `amount` decimal(19,2) NOT NULL,
  `date` datetime NOT NULL,
  `message` varchar(50) default NULL,
  `from_account_id` bigint(20) default NULL,
  `to_account_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKDFDBDFE07EE6EBBA` (`from_account_id`),
  KEY `FKDFDBDFE0D4A93849` (`to_account_id`),
  CONSTRAINT `FKDFDBDFE07EE6EBBA` FOREIGN KEY (`from_account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKDFDBDFE0D4A93849` FOREIGN KEY (`to_account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account_entry`
--

LOCK TABLES `account_entry` WRITE;
/*!40000 ALTER TABLE `account_entry` DISABLE KEYS */;
INSERT INTO `account_entry` VALUES ('deposit',1,'10.00','2007-10-29 22:24:29','Test',NULL,NULL),('deposit',2,'50.00','2007-10-29 22:24:50','Test 2',NULL,NULL),('deposit',3,'15.00','2007-12-27 10:34:31','Test many',NULL,NULL),('deposit',4,'15.00','2007-12-27 10:34:36','Test many',NULL,NULL),('deposit',5,'15.00','2007-12-27 10:34:38','Test many',NULL,NULL),('deposit',6,'15.00','2007-12-27 10:34:39','Test many',NULL,NULL),('deposit',7,'15.00','2007-12-27 10:34:40','Test many',NULL,NULL),('deposit',8,'15.00','2007-12-27 10:34:41','Test many',NULL,NULL),('deposit',9,'15.00','2007-12-27 10:34:42','Test many',NULL,NULL),('deposit',10,'15.00','2007-12-27 10:34:43','Test many',NULL,NULL),('deposit',11,'15.00','2007-12-27 10:34:44','Test many',NULL,NULL),('deposit',12,'15.00','2007-12-27 10:34:45','Test many',NULL,NULL),('deposit',13,'15.00','2007-12-27 10:34:46','Test many',NULL,NULL),('deposit',14,'15.00','2007-12-27 10:34:47','Test many',NULL,NULL),('deposit',15,'15.00','2007-12-27 10:34:48','Test many',NULL,NULL),('deposit',16,'15.00','2007-12-27 10:34:50','Test many',NULL,NULL),('deposit',17,'15.00','2007-12-27 10:34:51','Test many',NULL,NULL),('deposit',18,'15.00','2007-12-27 10:34:52','Test many',NULL,NULL),('deposit',19,'15.00','2007-12-27 10:34:53','Test many',NULL,NULL),('deposit',20,'15.00','2007-12-27 10:34:54','Test many',NULL,NULL),('deposit',21,'15.00','2007-12-27 10:34:54','Test many',NULL,NULL),('deposit',22,'15.00','2007-12-27 10:34:56','Test many',NULL,NULL),('deposit',23,'15.00','2007-12-27 10:34:57','Test many',NULL,NULL),('deposit',24,'15.00','2007-12-27 10:34:58','Test many',NULL,NULL),('deposit',25,'15.00','2007-12-27 10:34:59','Test many',NULL,NULL);
/*!40000 ALTER TABLE `account_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_entry_link`
--

DROP TABLE IF EXISTS `account_entry_link`;
CREATE TABLE `account_entry_link` (
  `Account_id` bigint(20) NOT NULL,
  `entries_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`Account_id`,`entries_id`),
  KEY `FKDD6274F92A653C05` (`Account_id`),
  KEY `FKDD6274F944377F70` (`entries_id`),
  CONSTRAINT `FKDD6274F92A653C05` FOREIGN KEY (`Account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKDD6274F944377F70` FOREIGN KEY (`entries_id`) REFERENCES `account_entry` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account_entry_link`
--

LOCK TABLES `account_entry_link` WRITE;
/*!40000 ALTER TABLE `account_entry_link` DISABLE KEYS */;
INSERT INTO `account_entry_link` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25);
/*!40000 ALTER TABLE `account_entry_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
CREATE TABLE `bid` (
  `id` bigint(20) NOT NULL auto_increment,
  `polePositionTimeMillis` int(11) NOT NULL,
  `grid_id` bigint(20) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  `podium_id` bigint(20) NOT NULL,
  `fastestLap_id` bigint(20) NOT NULL,
  `selectedDriver_id` bigint(20) NOT NULL,
  `firstCrash_id` bigint(20) NOT NULL,
  `race_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `selectedDriver_id` (`selectedDriver_id`),
  UNIQUE KEY `firstCrash_id` (`firstCrash_id`),
  UNIQUE KEY `grid_id` (`grid_id`),
  UNIQUE KEY `podium_id` (`podium_id`),
  UNIQUE KEY `fastestLap_id` (`fastestLap_id`),
  KEY `FK17CFD588C2E5A` (`podium_id`),
  KEY `FK17CFD7A12CC2C` (`selectedDriver_id`),
  KEY `FK17CFD872AC06E` (`player_id`),
  KEY `FK17CFD51EAFF6E` (`race_id`),
  KEY `FK17CFD829F02C6` (`grid_id`),
  KEY `FK17CFD9C9F5404` (`firstCrash_id`),
  KEY `FK17CFD8CBBEA10` (`fastestLap_id`),
  CONSTRAINT `FK17CFD51EAFF6E` FOREIGN KEY (`race_id`) REFERENCES `race` (`id`),
  CONSTRAINT `FK17CFD588C2E5A` FOREIGN KEY (`podium_id`) REFERENCES `bid_podium` (`id`),
  CONSTRAINT `FK17CFD7A12CC2C` FOREIGN KEY (`selectedDriver_id`) REFERENCES `bid_select_driver` (`id`),
  CONSTRAINT `FK17CFD829F02C6` FOREIGN KEY (`grid_id`) REFERENCES `bid_grid` (`id`),
  CONSTRAINT `FK17CFD872AC06E` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK17CFD8CBBEA10` FOREIGN KEY (`fastestLap_id`) REFERENCES `bid_fastest_lap` (`id`),
  CONSTRAINT `FK17CFD9C9F5404` FOREIGN KEY (`firstCrash_id`) REFERENCES `bid_first_crash` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid_fastest_lap`
--

DROP TABLE IF EXISTS `bid_fastest_lap`;
CREATE TABLE `bid_fastest_lap` (
  `id` bigint(20) NOT NULL auto_increment,
  `pointsDriver` int(11) NOT NULL,
  `driver_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK496CB744912DE70E` (`driver_id`),
  CONSTRAINT `FK496CB744912DE70E` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bid_fastest_lap`
--

LOCK TABLES `bid_fastest_lap` WRITE;
/*!40000 ALTER TABLE `bid_fastest_lap` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_fastest_lap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid_first_crash`
--

DROP TABLE IF EXISTS `bid_first_crash`;
CREATE TABLE `bid_first_crash` (
  `DTYPE` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL auto_increment,
  `pointsCrash1` int(11) NOT NULL,
  `crash3_id` bigint(20) default NULL,
  `crash2_id` bigint(20) default NULL,
  `crash1_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKE6081D1650A5CE8C` (`crash1_id`),
  KEY `FKE6081D1650A6B74A` (`crash3_id`),
  KEY `FKE6081D1650A642EB` (`crash2_id`),
  CONSTRAINT `FKE6081D1650A5CE8C` FOREIGN KEY (`crash1_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FKE6081D1650A642EB` FOREIGN KEY (`crash2_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FKE6081D1650A6B74A` FOREIGN KEY (`crash3_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bid_first_crash`
--

LOCK TABLES `bid_first_crash` WRITE;
/*!40000 ALTER TABLE `bid_first_crash` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_first_crash` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid_grid`
--

DROP TABLE IF EXISTS `bid_grid`;
CREATE TABLE `bid_grid` (
  `DTYPE` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL auto_increment,
  `pointsPosition1` int(11) NOT NULL,
  `pointsPosition2` int(11) NOT NULL,
  `pointsPosition3` int(11) NOT NULL,
  `pointsPosition4` int(11) NOT NULL,
  `pointsPosition5` int(11) NOT NULL,
  `pointsPosition6` int(11) NOT NULL,
  `position5_id` bigint(20) NOT NULL,
  `position4_id` bigint(20) NOT NULL,
  `position6_id` bigint(20) NOT NULL,
  `position2_id` bigint(20) NOT NULL,
  `position7_id` bigint(20) default NULL,
  `position3_id` bigint(20) NOT NULL,
  `position1_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK26B15D48E495E76D` (`position2_id`),
  KEY `FK26B15D48E4965BCC` (`position3_id`),
  KEY `FK26B15D48E497B8E9` (`position6_id`),
  KEY `FK26B15D48E496D02B` (`position4_id`),
  KEY `FK26B15D48E4982D48` (`position7_id`),
  KEY `FK26B15D48E495730E` (`position1_id`),
  KEY `FK26B15D48E497448A` (`position5_id`),
  CONSTRAINT `FK26B15D48E495730E` FOREIGN KEY (`position1_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK26B15D48E495E76D` FOREIGN KEY (`position2_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK26B15D48E4965BCC` FOREIGN KEY (`position3_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK26B15D48E496D02B` FOREIGN KEY (`position4_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK26B15D48E497448A` FOREIGN KEY (`position5_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK26B15D48E497B8E9` FOREIGN KEY (`position6_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK26B15D48E4982D48` FOREIGN KEY (`position7_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bid_grid`
--

LOCK TABLES `bid_grid` WRITE;
/*!40000 ALTER TABLE `bid_grid` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_grid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid_podium`
--

DROP TABLE IF EXISTS `bid_podium`;
CREATE TABLE `bid_podium` (
  `DTYPE` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL auto_increment,
  `pointsPosition1` int(11) NOT NULL,
  `pointsPosition2` int(11) NOT NULL,
  `pointsPosition3` int(11) NOT NULL,
  `position2_id` bigint(20) NOT NULL,
  `position1_id` bigint(20) NOT NULL,
  `position3_id` bigint(20) NOT NULL,
  `position4_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK4EFE5DDEE495E76D` (`position2_id`),
  KEY `FK4EFE5DDEE4965BCC` (`position3_id`),
  KEY `FK4EFE5DDEE496D02B` (`position4_id`),
  KEY `FK4EFE5DDEE495730E` (`position1_id`),
  CONSTRAINT `FK4EFE5DDEE495730E` FOREIGN KEY (`position1_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK4EFE5DDEE495E76D` FOREIGN KEY (`position2_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK4EFE5DDEE4965BCC` FOREIGN KEY (`position3_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK4EFE5DDEE496D02B` FOREIGN KEY (`position4_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bid_podium`
--

LOCK TABLES `bid_podium` WRITE;
/*!40000 ALTER TABLE `bid_podium` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_podium` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid_select_driver`
--

DROP TABLE IF EXISTS `bid_select_driver`;
CREATE TABLE `bid_select_driver` (
  `id` bigint(20) NOT NULL auto_increment,
  `endPosition` int(11) NOT NULL,
  `pointsEndPosition` int(11) NOT NULL,
  `pointsStartPosition` int(11) NOT NULL,
  `startPosition` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bid_select_driver`
--

LOCK TABLES `bid_select_driver` WRITE;
/*!40000 ALTER TABLE `bid_select_driver` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_select_driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
CREATE TABLE `driver` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` bit(1) NOT NULL,
  `name` varchar(255) default NULL,
  `number` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'','Kimi Raikonen',6),(2,'','Felipe Massa',5),(3,'','Lewis Hamilton',2),(4,'','Wanker Alonso',1),(5,'','Nick Heidfeld',7),(6,'','Robert Kubica',8),(7,'','Jenson Button',19);
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` bigint(20) NOT NULL auto_increment,
  `emailAddress` varchar(40) default NULL,
  `firstName` varchar(25) NOT NULL,
  `lastName` varchar(25) NOT NULL,
  `password` varchar(20) NOT NULL,
  `playername` varchar(15) NOT NULL,
  `sms` varchar(8) default NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `playername` (`playername`),
  UNIQUE KEY `account_id` (`account_id`),
  KEY `FKC53E9AE12A653C05` (`account_id`),
  CONSTRAINT `FKC53E9AE12A653C05` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (1,'flemming@bregnvig.dk','Flemming','lastflb','flb','flb','28712234',1),(2,'flemming@bregnvig.dk','firstmba','lastmba','mypassword','mba','28712234',2),(3,'flemming@bregnvig.dk','firstttp','lastttp','mypassword','ttp','28712234',3);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `race`
--

DROP TABLE IF EXISTS `race`;
CREATE TABLE `race` (
  `id` bigint(20) NOT NULL auto_increment,
  `close` datetime NOT NULL,
  `completed` bit(1) NOT NULL,
  `name` varchar(25) NOT NULL,
  `open` datetime NOT NULL,
  `season_id` bigint(20) NOT NULL,
  `selectedDriver_id` bigint(20) NOT NULL,
  `raceResult_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK354AD1B52FB2D3` (`selectedDriver_id`),
  KEY `FK354AD1A4EC036E` (`raceResult_id`),
  KEY `FK354AD167D9A92E` (`season_id`),
  CONSTRAINT `FK354AD167D9A92E` FOREIGN KEY (`season_id`) REFERENCES `season` (`id`),
  CONSTRAINT `FK354AD1A4EC036E` FOREIGN KEY (`raceResult_id`) REFERENCES `race_result` (`id`),
  CONSTRAINT `FK354AD1B52FB2D3` FOREIGN KEY (`selectedDriver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `race`
--

LOCK TABLES `race` WRITE;
/*!40000 ALTER TABLE `race` DISABLE KEYS */;
INSERT INTO `race` VALUES (1,'2008-01-29 22:15:29','\0','Spa','2007-10-29 22:24:29',1,2,NULL),(2,'2008-10-29 22:21:29','\0','Monza','2008-01-29 22:19:29',1,1,NULL);
/*!40000 ALTER TABLE `race` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `race_result`
--

DROP TABLE IF EXISTS `race_result`;
CREATE TABLE `race_result` (
  `id` bigint(20) NOT NULL auto_increment,
  `polePositionTimeMillis` int(11) NOT NULL,
  `fastestLap_id` bigint(20) NOT NULL,
  `podium_id` bigint(20) NOT NULL,
  `firstCrash_id` bigint(20) NOT NULL,
  `grid_id` bigint(20) NOT NULL,
  `selectedDriver_id` bigint(20) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `selectedDriver_id` (`selectedDriver_id`),
  UNIQUE KEY `podium_id` (`podium_id`),
  UNIQUE KEY `grid_id` (`grid_id`),
  UNIQUE KEY `firstCrash_id` (`firstCrash_id`),
  UNIQUE KEY `fastestLap_id` (`fastestLap_id`),
  KEY `FK99B6DD4BF6D0B21C` (`podium_id`),
  KEY `FK99B6DD4B7A12CC2C` (`selectedDriver_id`),
  KEY `FK99B6DD4B872AC06E` (`player_id`),
  KEY `FK99B6DD4B75205F9C` (`grid_id`),
  KEY `FK99B6DD4BF0CF155C` (`firstCrash_id`),
  KEY `FK99B6DD4B8CBBEA10` (`fastestLap_id`),
  CONSTRAINT `FK99B6DD4B75205F9C` FOREIGN KEY (`grid_id`) REFERENCES `bid_grid` (`id`),
  CONSTRAINT `FK99B6DD4B7A12CC2C` FOREIGN KEY (`selectedDriver_id`) REFERENCES `bid_select_driver` (`id`),
  CONSTRAINT `FK99B6DD4B872AC06E` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK99B6DD4B8CBBEA10` FOREIGN KEY (`fastestLap_id`) REFERENCES `bid_fastest_lap` (`id`),
  CONSTRAINT `FK99B6DD4BF0CF155C` FOREIGN KEY (`firstCrash_id`) REFERENCES `bid_first_crash` (`id`),
  CONSTRAINT `FK99B6DD4BF6D0B21C` FOREIGN KEY (`podium_id`) REFERENCES `bid_podium` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `race_result`
--

LOCK TABLES `race_result` WRITE;
/*!40000 ALTER TABLE `race_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `race_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season`
--

DROP TABLE IF EXISTS `season`;
CREATE TABLE `season` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `WBC_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKC9FA6AE3AFDCD6F0` (`WBC_id`),
  CONSTRAINT `FKC9FA6AE3AFDCD6F0` FOREIGN KEY (`WBC_id`) REFERENCES `wbc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `season`
--

LOCK TABLES `season` WRITE;
/*!40000 ALTER TABLE `season` DISABLE KEYS */;
INSERT INTO `season` VALUES (1,'F1 2007',1);
/*!40000 ALTER TABLE `season` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season_driver`
--

DROP TABLE IF EXISTS `season_driver`;
CREATE TABLE `season_driver` (
  `season_id` bigint(20) NOT NULL,
  `drivers_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`season_id`,`drivers_id`),
  KEY `FK9491B504617DB0B` (`drivers_id`),
  KEY `FK9491B50467D9A92E` (`season_id`),
  CONSTRAINT `FK9491B504617DB0B` FOREIGN KEY (`drivers_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK9491B50467D9A92E` FOREIGN KEY (`season_id`) REFERENCES `season` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `season_driver`
--

LOCK TABLES `season_driver` WRITE;
/*!40000 ALTER TABLE `season_driver` DISABLE KEYS */;
INSERT INTO `season_driver` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6);
/*!40000 ALTER TABLE `season_driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season_player`
--

DROP TABLE IF EXISTS `season_player`;
CREATE TABLE `season_player` (
  `season_id` bigint(20) NOT NULL,
  `players_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`season_id`,`players_id`),
  KEY `FKA8B3B7BD73B9DBFD` (`players_id`),
  KEY `FKA8B3B7BD67D9A92E` (`season_id`),
  CONSTRAINT `FKA8B3B7BD67D9A92E` FOREIGN KEY (`season_id`) REFERENCES `season` (`id`),
  CONSTRAINT `FKA8B3B7BD73B9DBFD` FOREIGN KEY (`players_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `season_player`
--

LOCK TABLES `season_player` WRITE;
/*!40000 ALTER TABLE `season_player` DISABLE KEYS */;
INSERT INTO `season_player` VALUES (1,1),(1,2),(1,3);
/*!40000 ALTER TABLE `season_player` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2007-12-27 10:35:40
