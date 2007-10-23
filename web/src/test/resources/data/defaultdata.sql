-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.41


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


USE f2007;
INSERT INTO `f2007`.`Account` (`id`,`balance`) VALUES 
 (1,'0.00'),
 (2,'0.00'),
 (3,'0.00');
INSERT INTO `f2007`.`WBC` (`id`,`previousRace_id`) VALUES 
 (1,NULL);
INSERT INTO `f2007`.`driver` (`id`,`active`,`name`,`number`) VALUES 
 (1,0x01,'Kimi Raikonen',6),
 (2,0x01,'Felipe Massa',5),
 (3,0x01,'Lewis Hamilton',2),
 (4,0x01,'Wanker Alonso',1),
 (5,0x01,'Nick Heidfeld',7),
 (6,0x01,'Robert Kubica',8),
 (7,0x01,'Jenson Button',19);
INSERT INTO `f2007`.`player` (`id`,`emailAddress`,`firstName`,`lastName`,`playername`,`sms`,`account_id`) VALUES 
 (1,'flemming@bregnvig.dk','firstflb','lastflb','flb','28712234',1),
 (2,'flemming@bregnvig.dk','firstmba','lastmba','mba','28712234',2),
 (3,'flemming@bregnvig.dk','firstttp','lastttp','ttp','28712234',3);
INSERT INTO `f2007`.`race` (`id`,`close`,`completed`,`name`,`open`,`raceResult_id`,`selectedDriver_id`,`season_id`) VALUES 
 (1,'2007-10-20 14:46:41',0x00,'Spa','2007-10-20 14:44:41',NULL,2,1),
 (2,'2007-10-20 14:41:41',0x00,'Monza','2007-10-20 14:39:41',NULL,1,1);
INSERT INTO `f2007`.`season` (`id`,`name`,`WBC_id`) VALUES 
 (1,'F1 2007',1);
INSERT INTO `f2007`.`season_driver` (`season_id`,`drivers_id`) VALUES 
 (1,1),
 (1,2),
 (1,3),
 (1,4),
 (1,5),
 (1,6);
INSERT INTO `f2007`.`season_player` (`season_id`,`players_id`) VALUES 
 (1,1),
 (1,2),
 (1,3);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
