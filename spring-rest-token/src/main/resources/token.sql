-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.73-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema test
--

CREATE DATABASE IF NOT EXISTS test;
USE test;

--
-- Definition of table `store_sms_details`
--

DROP TABLE IF EXISTS `store_sms_details`;
CREATE TABLE `store_sms_details` (
  `MSG_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `MSG_TYPE` varchar(18) NOT NULL,
  `RECIPIENT_NUMBER` int(15) DEFAULT NULL,
  `MSG_DESC` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MSG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `store_sms_details`
--

/*!40000 ALTER TABLE `store_sms_details` DISABLE KEYS */;
INSERT INTO `store_sms_details` (`MSG_ID`,`MSG_TYPE`,`RECIPIENT_NUMBER`,`MSG_DESC`) VALUES 
 (1,'test',21,'test'),
 (2,'test',21,'test'),
 (5,'test',21,'test'),
 (10,'test',21,'test'),
 (11,'test',21,'test'),
 (12,'test',21,'test'),
 (15,'mq',21,'mq');
/*!40000 ALTER TABLE `store_sms_details` ENABLE KEYS */;


--
-- Definition of table `userdetails`
--

DROP TABLE IF EXISTS `userdetails`;
CREATE TABLE `userdetails` (
  `USERID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(20) NOT NULL,
  `PASSWORD` varchar(20) NOT NULL,
  PRIMARY KEY (`USERID`),
  UNIQUE KEY `UN_USERNAME` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userdetails`
--

/*!40000 ALTER TABLE `userdetails` DISABLE KEYS */;
INSERT INTO `userdetails` (`USERID`,`USERNAME`,`PASSWORD`) VALUES 
 (1,'naveen','naveen');
/*!40000 ALTER TABLE `userdetails` ENABLE KEYS */;


--
-- Definition of table `usertokendetails`
--

DROP TABLE IF EXISTS `usertokendetails`;
CREATE TABLE `usertokendetails` (
  `USERNAME` varchar(20) NOT NULL,
  `TOKEN` varchar(64) NOT NULL,
  `CREATEDBY` varchar(18) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `MODIFIEDBY` varchar(18) DEFAULT NULL,
  `MODIFIEDDATE` datetime DEFAULT NULL,
  `TOKENEXPTIME` datetime NOT NULL,
  `ISUSERLOGGEDIN` varchar(1) NOT NULL,
  PRIMARY KEY (`USERNAME`),
  UNIQUE KEY `UN_TOKEN` (`TOKEN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `usertokendetails`
--

/*!40000 ALTER TABLE `usertokendetails` DISABLE KEYS */;
INSERT INTO `usertokendetails` (`USERNAME`,`TOKEN`,`CREATEDBY`,`CREATEDDATE`,`MODIFIEDBY`,`MODIFIEDDATE`,`TOKENEXPTIME`,`ISUSERLOGGEDIN`) VALUES 
 ('naveen','r9ocdko0u6joc5n3spv9tqu08j','naveen','2015-03-19 14:20:19','naveen','2015-04-10 12:34:35','2015-04-10 13:04:35','Y');
/*!40000 ALTER TABLE `usertokendetails` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
