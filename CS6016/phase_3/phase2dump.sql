/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.5.27-MariaDB, for Linux (x86_64)
--
-- Host: cs-db.eng.utah.edu    Database: LMS2
-- ------------------------------------------------------
-- Server version	10.11.8-MariaDB-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Administrators`
--

DROP TABLE IF EXISTS `Administrators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Administrators` (
  `uID` char(8) NOT NULL,
  `fName` varchar(100) NOT NULL,
  `lName` varchar(100) NOT NULL,
  `DOB` date NOT NULL,
  PRIMARY KEY (`uID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AssignmentCategories`
--

DROP TABLE IF EXISTS `AssignmentCategories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AssignmentCategories` (
  `CategoryID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Weight` int(10) unsigned NOT NULL,
  `InClass` int(10) unsigned NOT NULL,
  PRIMARY KEY (`CategoryID`),
  UNIQUE KEY `Name` (`Name`,`InClass`),
  KEY `AssignmentCategories_ibfk_1` (`InClass`),
  CONSTRAINT `AssignmentCategories_ibfk_1` FOREIGN KEY (`InClass`) REFERENCES `Classes` (`ClassID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Assignments`
--

DROP TABLE IF EXISTS `Assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assignments` (
  `AssignmentID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Contents` varchar(8192) NOT NULL,
  `Due` datetime NOT NULL,
  `MaxPoints` int(10) unsigned NOT NULL,
  `Category` int(10) unsigned NOT NULL,
  PRIMARY KEY (`AssignmentID`),
  UNIQUE KEY `name_unique` (`Name`,`Category`),
  KEY `Assignments_ibfk_1` (`Category`),
  CONSTRAINT `Assignments_ibfk_1` FOREIGN KEY (`Category`) REFERENCES `AssignmentCategories` (`CategoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Classes`
--

DROP TABLE IF EXISTS `Classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Classes` (
  `ClassID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Season` varchar(6) NOT NULL,
  `Year` int(10) unsigned NOT NULL,
  `Location` varchar(100) NOT NULL,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  `Listing` int(10) unsigned NOT NULL,
  `TaughtBy` char(8) DEFAULT NULL,
  PRIMARY KEY (`ClassID`),
  UNIQUE KEY `Season` (`Season`,`Year`,`Listing`),
  KEY `Classes_ibfk_1` (`Listing`),
  KEY `Taught` (`TaughtBy`),
  CONSTRAINT `Classes_ibfk_1` FOREIGN KEY (`Listing`) REFERENCES `Courses` (`CatalogID`),
  CONSTRAINT `Taught` FOREIGN KEY (`TaughtBy`) REFERENCES `Professors` (`uID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Courses`
--

DROP TABLE IF EXISTS `Courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Courses` (
  `CatalogID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Number` int(10) unsigned NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Department` varchar(4) NOT NULL,
  PRIMARY KEY (`CatalogID`),
  UNIQUE KEY `Number` (`Number`,`Department`),
  KEY `Courses_ibfk_1` (`Department`),
  CONSTRAINT `Courses_ibfk_1` FOREIGN KEY (`Department`) REFERENCES `Departments` (`Subject`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Departments`
--

DROP TABLE IF EXISTS `Departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Departments` (
  `Name` varchar(100) NOT NULL,
  `Subject` varchar(4) NOT NULL,
  PRIMARY KEY (`Subject`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Enrolled`
--

DROP TABLE IF EXISTS `Enrolled`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Enrolled` (
  `Student` char(8) NOT NULL,
  `Class` int(10) unsigned NOT NULL,
  `Grade` varchar(2) NOT NULL,
  PRIMARY KEY (`Student`,`Class`),
  KEY `Enrolled_ibfk_2` (`Class`),
  CONSTRAINT `Enrolled_ibfk_1` FOREIGN KEY (`Student`) REFERENCES `Students` (`uID`),
  CONSTRAINT `Enrolled_ibfk_2` FOREIGN KEY (`Class`) REFERENCES `Classes` (`ClassID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Professors`
--

DROP TABLE IF EXISTS `Professors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Professors` (
  `uID` char(8) NOT NULL,
  `fName` varchar(100) NOT NULL,
  `lName` varchar(100) NOT NULL,
  `DOB` date NOT NULL,
  `WorksIn` varchar(4) NOT NULL,
  PRIMARY KEY (`uID`),
  KEY `Professors_ibfk_1` (`WorksIn`),
  CONSTRAINT `Professors_ibfk_1` FOREIGN KEY (`WorksIn`) REFERENCES `Departments` (`Subject`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Students`
--

DROP TABLE IF EXISTS `Students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Students` (
  `uID` char(8) NOT NULL,
  `fName` varchar(100) NOT NULL,
  `lName` varchar(100) NOT NULL,
  `DOB` date NOT NULL,
  `Major` varchar(4) NOT NULL,
  PRIMARY KEY (`uID`),
  KEY `Students_ibfk_1` (`Major`),
  CONSTRAINT `Students_ibfk_1` FOREIGN KEY (`Major`) REFERENCES `Departments` (`Subject`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Submissions`
--

DROP TABLE IF EXISTS `Submissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Submissions` (
  `Assignment` int(10) unsigned NOT NULL,
  `Student` char(8) NOT NULL,
  `Score` int(10) unsigned NOT NULL,
  `SubmissionContents` varchar(8192) DEFAULT NULL,
  `Time` datetime NOT NULL,
  PRIMARY KEY (`Assignment`,`Student`),
  KEY `Submissions_ibfk_2` (`Student`),
  CONSTRAINT `Submissions_ibfk_1` FOREIGN KEY (`Assignment`) REFERENCES `Assignments` (`AssignmentID`),
  CONSTRAINT `Submissions_ibfk_2` FOREIGN KEY (`Student`) REFERENCES `Students` (`uID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `__EFMigrationsHistory`
--

DROP TABLE IF EXISTS `__EFMigrationsHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `__EFMigrationsHistory` (
  `MigrationId` varchar(150) NOT NULL,
  `ProductVersion` varchar(32) NOT NULL,
  PRIMARY KEY (`MigrationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-28 13:29:36
