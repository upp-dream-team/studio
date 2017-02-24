CREATE DATABASE  IF NOT EXISTS `projman9` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `projman9`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: projman9
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `record_date` date NOT NULL,
  `price` float unsigned NOT NULL,
  `gonorar_percent` float unsigned NOT NULL,
  `chief_part` float unsigned NOT NULL,
  `chief` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_album` (`chief`),
  CONSTRAINT `fk_album` FOREIGN KEY (`chief`) REFERENCES `musician` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album`
--

LOCK TABLES `album` WRITE;
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
INSERT INTO `album` VALUES (1,'With the Beatles','2000-11-23',10,0.5,0.05,1),(2,'Help!','2001-05-11',10,0.5,0.05,1),(3,'Rubber Soul','2002-01-12',10,0.5,0.05,1),(4,'The White Album','2004-10-12',10,0.4,0.05,1),(5,'Yellow Submarine','2006-03-14',10,0.5,0.05,1),(6,'Abbey Road','2007-01-03',10,0.6,0.05,1),(7,'Let It Be','2007-12-12',10,0.5,0.05,1),(8,'Portrait of an American Family','2001-02-02',15,0.5,0.05,5),(9,'Antichrist Superstar','2003-08-10',15,0.4,0.05,5),(10,'Mechanical Animals','2004-09-15',15,0.5,0.05,5),(11,'Holy Wood','2005-11-14',15,0.6,0.05,5),(12,'The Golden Age of Grotesque','2006-05-13',15,0.7,0.05,5),(13,'Born Villain','2012-04-25',15,0.5,0.05,5),(14,'The Pale Emperor','2015-01-20',15,0.3,0.05,5),(15,'Boy','2016-04-25',12,0.5,0.05,18),(16,'October','2017-01-20',12,0.6,0.05,18);
/*!40000 ALTER TABLE `album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrument`
--

DROP TABLE IF EXISTS `instrument`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrument` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrument`
--

LOCK TABLES `instrument` WRITE;
/*!40000 ALTER TABLE `instrument` DISABLE KEYS */;
/*!40000 ALTER TABLE `instrument` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `license`
--

DROP TABLE IF EXISTS `license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `license` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `price` float unsigned NOT NULL,
  `period` int(10) unsigned NOT NULL,
  `selling_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_license` (`selling_id`),
  CONSTRAINT `fk_license` FOREIGN KEY (`selling_id`) REFERENCES `sellings` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `license`
--

LOCK TABLES `license` WRITE;
/*!40000 ALTER TABLE `license` DISABLE KEYS */;
INSERT INTO `license` VALUES (1,100,10,3),(2,75.5,10,5),(3,150,12,7),(4,130,12,8),(5,110,12,12),(6,90,8,15),(7,112,12,16),(8,130,14,19),(9,125.5,10,21),(10,115,9,22),(11,92,9,23),(12,150,12,24),(13,120,11,26),(14,110,8,27),(15,60,5,30),(16,100,10,31),(17,120,12,32),(18,75.5,7,34),(19,55.5,4,36),(20,120,12,37),(21,110,11,38),(22,125.5,12,39),(23,120,12,40),(24,100,10,41),(25,90.5,9,42),(26,120,11,43),(27,120,12,45),(28,150,12,46),(29,85.5,6,47),(30,120,12,48),(31,110,10,49),(32,100,10,51),(33,120,12,52),(34,55.5,4,53),(35,75.5,7,54),(36,55.5,4,55),(37,55.5,4,56),(38,120,12,57),(39,110,11,58),(40,125.5,12,59),(41,120,12,60),(42,100,10,61),(43,90.5,9,62),(44,120,11,63),(45,120,12,65),(46,150,12,66),(47,85.5,6,67),(48,120,12,68),(49,110,10,69),(50,110,10,70);
/*!40000 ALTER TABLE `license` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `musician`
--

DROP TABLE IF EXISTS `musician`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `musician` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `musician`
--

LOCK TABLES `musician` WRITE;
/*!40000 ALTER TABLE `musician` DISABLE KEYS */;
INSERT INTO `musician` VALUES (1,'John Lennon','380956660066'),(2,'Paul McCartney','380635667800'),(3,'Ringo Starr','380508337538'),(4,'George Harrison','380956395627'),(5,'Marilyn Manson','380956660066'),(6,'Daisy Berkowitz','380635667800'),(7,'Madonna Wayne Gacy','380508337538'),(8,'Sara Lee Lucas','380956395627'),(9,'Gidget Gein','380987362574'),(10,'Twiggy Ramirez','380508336454'),(11,'Ginger Fish','380508358854'),(12,'Zim Zum','380508358854'),(13,'John Five','380508111854'),(14,'Fred Sablan','380507366854'),(15,'Chris Vrenna','380501113322'),(16,'Tyler Bates','380501115522'),(17,'Gil Sharone','380500003322'),(18,'Bono','380507366854'),(19,'Larry Mullen','380501113322'),(20,'Adam Clayton','380501115522');
/*!40000 ALTER TABLE `musician` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `musician_instrument`
--

DROP TABLE IF EXISTS `musician_instrument`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `musician_instrument` (
  `musician_id` bigint(20) unsigned NOT NULL,
  `instrument_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`musician_id`,`instrument_id`),
  UNIQUE KEY `pk_musician_instrument_0` (`musician_id`),
  UNIQUE KEY `pk_musician_instrument_1` (`instrument_id`),
  CONSTRAINT `fk_musician_instrument` FOREIGN KEY (`musician_id`) REFERENCES `musician` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_musician_instrument_0` FOREIGN KEY (`instrument_id`) REFERENCES `instrument` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `musician_instrument`
--

LOCK TABLES `musician_instrument` WRITE;
/*!40000 ALTER TABLE `musician_instrument` DISABLE KEYS */;
/*!40000 ALTER TABLE `musician_instrument` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `quantity` int(10) unsigned NOT NULL,
  `selling_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_record` (`selling_id`),
  CONSTRAINT `fk_record` FOREIGN KEY (`selling_id`) REFERENCES `sellings` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record`
--

LOCK TABLES `record` WRITE;
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
INSERT INTO `record` VALUES (1,1,1),(2,1,2),(3,5,4),(4,2,6),(5,1,9),(6,2,10),(7,3,11),(8,2,13),(9,1,14),(10,1,17),(11,2,18),(12,1,20),(13,1,25),(14,1,28),(15,1,29),(16,2,33),(17,1,35),(18,1,44),(19,10,50),(20,1,64);
/*!40000 ALTER TABLE `record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rozpodil`
--

DROP TABLE IF EXISTS `rozpodil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rozpodil` (
  `musician_id` bigint(20) unsigned NOT NULL,
  `song_id` bigint(20) unsigned NOT NULL,
  `gonorar_chastka` float unsigned NOT NULL,
  PRIMARY KEY (`musician_id`,`song_id`),
  KEY `idx_rozpodil` (`song_id`),
  KEY `idx_rozpodil_0` (`musician_id`),
  CONSTRAINT `fk_rozpodil` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_rozpodil_0` FOREIGN KEY (`musician_id`) REFERENCES `musician` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rozpodil`
--

LOCK TABLES `rozpodil` WRITE;
/*!40000 ALTER TABLE `rozpodil` DISABLE KEYS */;
/*!40000 ALTER TABLE `rozpodil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sellings`
--

DROP TABLE IF EXISTS `sellings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sellings` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `client` varchar(255) NOT NULL,
  `sell_date` date NOT NULL,
  `album_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sellings` (`album_id`),
  CONSTRAINT `fk_sellings` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sellings`
--

LOCK TABLES `sellings` WRITE;
/*!40000 ALTER TABLE `sellings` DISABLE KEYS */;
INSERT INTO `sellings` VALUES (1,'Sully Prudhomme','2015-04-14',1),(2,'Theodor Mommsen','2015-04-17',4),(3,'Henryk Sienkiewicz','2015-04-22',6),(4,'Giosue Carducci','2015-05-01',5),(5,'Rudyard Kipling','2015-05-12',11),(6,'Rudolf Christoph Eucken','2015-05-13',2),(7,'Selma Lagerloef','2015-05-21',9),(8,'Paul von Heyse','2015-05-24',3),(9,'Maurice Maeterlinck','2015-05-29',5),(10,'Gerhart Hauptmann','2015-06-01',8),(11,'Rabindranath Tagore','2015-06-02',10),(12,'Romain Rolland','2015-06-12',13),(13,'Verner von Heidenstam','2015-06-13',12),(14,'Karl Adolph Gjellerup','2015-06-14',14),(15,'Henrik Pontoppidan','2015-06-18',15),(16,'Carl Spitteler','2015-06-22',6),(17,'Knut Hamsun','2015-07-09',1),(18,'Anatole France','2015-07-09',9),(19,'Jacinto Benavente','2015-07-11',5),(20,'William Butler Yeats','2015-07-14',7),(21,'Wladyslaw Reymont','2015-07-16',1),(22,'George Bernard Shaw','2015-07-19',4),(23,'Grazia Deledda','2015-08-01',8),(24,'Henri Bergson','2015-08-20',11),(25,'Sigrid Undset','2015-08-23',16),(26,'Thomas Mann','2015-09-02',12),(27,'Sinclair Lewis','2015-09-14',10),(28,'Erik Axel Karlfeldt','2015-09-21',4),(29,'John Galsworthy','2015-09-25',3),(30,'Ivan Bunin','2015-10-01',2),(31,'Wladyslaw Reymont','2015-10-01',9),(32,'Luigi Pirandello','2015-10-04',5),(33,'Grazia Deledda','2015-10-12',10),(34,'Roger Martin du Gard','2015-10-21',1),(35,'Sigrid Undset','2015-11-03',2),(36,'Johannes Vilhelm Jensen','2015-12-01',13),(37,'Gabriela Mistral','2015-12-04',7),(38,'Hermann Hesse','2015-12-06',4),(39,'John Galsworthy','2015-12-21',5),(40,'William Faulkner','2015-12-26',8),(41,'Bertrand Russell','2016-01-12',3),(42,'Ernest Hemingway','2016-02-02',6),(43,'Albert Camus','2016-02-13',14),(44,'Boris Pasternak','2016-02-15',15),(45,'Salvatore Quasimodo','2016-02-21',1),(46,'Saint-John Perse','2016-03-10',6),(47,'John Steinbeck','2016-03-15',5),(48,'Giorgos Seferis','2016-03-21',7),(49,'Jean-Paul Sartre','2016-03-28',9),(50,'Mikhail Sholokhov','2016-04-11',12),(51,'Shmuel Yosef Agnon','2016-05-11',1),(52,'Nelly Sachs','2016-05-12',6),(53,'Yasunari Kawabata','2016-05-15',10),(54,'Samuel Beckett','2016-05-28',1),(55,'Aleksandr Solzhenitsyn','2016-06-04',2),(56,'Pablo Neruda','2016-06-16',13),(57,'Patrick White','2016-06-14',7),(58,'Eyvind Johnson','2016-07-13',4),(59,'Harry Martinson','2016-07-17',5),(60,'Eugenio Montale','2016-07-21',8),(61,'Saul Bellow','2016-08-12',3),(62,'Vicente Aleixandre','2016-09-01',6),(63,'Isaac Bashevis Singer','2016-09-21',14),(64,'Odysseas Elytis','2016-09-29',15),(65,'Elias Canetti','2016-10-12',2),(66,'William Golding','2016-11-11',8),(67,'Claude Simon','2016-12-12',5),(68,'Wole Soyinka','2017-01-04',7),(69,'Joseph Brodsky','2017-01-07',9),(70,'Toni Morrison','2016-01-21',12);
/*!40000 ALTER TABLE `sellings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `author` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `album` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_song` (`album`),
  CONSTRAINT `fk_song` FOREIGN KEY (`album`) REFERENCES `album` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song`
--

LOCK TABLES `song` WRITE;
/*!40000 ALTER TABLE `song` DISABLE KEYS */;
INSERT INTO `song` VALUES (1,'Lennon–McCartney','It Wont Be Long',1),(2,'Lennon–McCartney','All Ive Got to Do',1),(3,'Lennon–McCartney','All My Loving',1),(4,'George Harrison','Dont Bother Me',1),(5,'Lennon–McCartney','Little Child',1),(6,'Meredith Willson','Till There Was You',1),(7,'Georgia Dobbins','Please Mister Postman',1),(8,'Chuck Berry','Roll Over Beethoven',1),(9,'Lennon–McCartney','Hold Me Tight',1),(10,'Smokey Robinson','You Really Got a Hold on Me',1),(11,'Lennon–McCartney','I Wanna Be Your Man',1),(12,'Richard Drapkin','Devil in Her Heart',1),(13,'Lennon–McCartney','Not a Second Time',1),(14,'Janie Bradford','Money (Thats What I Want)',1),(15,'Lennon–McCartney','Help!',2),(16,'Lennon–McCartney','The Night Before',2),(17,'Lennon–McCartney','You Have Got to Hide Your Love Away',2),(18,'George Harrison','I Need You',2),(19,'Lennon–McCartney','Another Girl',2),(20,'Lennon–McCartney','You Are Going to Lose That Girl',2),(21,'Lennon–McCartney','Ticket to Ride',2),(22,'Johnny Russell','Act Naturally',2),(23,'Lennon–McCartney','It Is Only Love',2),(24,'George Harrison','You Like Me Too Much',2),(25,'Lennon–McCartney','Tell Me What You See',2),(26,'Lennon–McCartney','I Have Just Seen a Face',2),(27,'Lennon–McCartney','Yesterday',2),(28,'Lennon–McCartney','Dizzy Miss Lizzy',2),(29,'Lennon–McCartney','Drive My Car',3),(30,'Lennon–McCartney','Norwegian Wood',3),(31,'Lennon–McCartney','You Wont See Me',3),(32,'Lennon–McCartney','Nowhere Man',3),(33,'George Harrison','Think for Yourself',3),(34,'Lennon–McCartney','The Word',3),(35,'Lennon–McCartney','Michelle',3),(36,'Lennon–McCartney','What Goes On',3),(37,'Lennon–McCartney','Girl',3),(38,'Lennon–McCartney','Im Looking Through You',3),(39,'Lennon–McCartney','In My Life',3),(40,'Lennon–McCartney','Wait',3),(41,'George Harrison','If I Needed Someone',3),(42,'Lennon–McCartney','Run for Your Life',3),(43,'Lennon–McCartney','Back in the U.S.S.R.',4),(44,'Lennon–McCartney','Dear Prudence',4),(45,'Lennon–McCartney','Glass Onion',4),(46,'Lennon–McCartney','Ob-La-Di, Ob-La-Da',4),(47,'Lennon–McCartney','Wild Honey Pie',4),(48,'Lennon–McCartney','The Continuing Story of Bungalow Bill',4),(49,'George Harrison','While My Guitar Gently Weeps',4),(50,'Lennon–McCartney','Happiness Is a Warm Gun',4),(51,'Lennon–McCartney','Martha My Dear',4),(52,'Lennon–McCartney','Im So Tired',4),(53,'Lennon–McCartney','Blackbird',4),(54,'George Harrison','Piggies',4),(55,'Lennon–McCartney','Rocky Raccoon',4),(56,'Richard Starkey','Dont Pass Me By',4),(57,'Lennon–McCartney','Why Dont We Do It in the Road?',4),(58,'Lennon–McCartney','I Will',4),(59,'Lennon–McCartney','Julia',4),(60,'Lennon–McCartney','Yellow Submarine',5),(61,'George Harrison','Only a Northern Song',5),(62,'Lennon–McCartney','All Together Now',5),(63,'Lennon–McCartney','Hey Bulldog',5),(64,'George Harrison','Its All Too Much',5),(65,'Lennon–McCartney','All You Need Is Love',5),(66,'George Martin','Pepperland',5),(67,'George Martin','Sea of Time',5),(68,'George Martin','Sea of Holes',5),(69,'George Martin','Sea of Monsters',5),(70,'George Martin','March of the Meanies',5),(71,'George Martin','Pepperland Laid Waste',5),(72,'Lennon–McCartney','Yellow Submarine in Pepperland',5),(73,'Lennon–McCartney','Come Together',6),(74,'George Harrison','Something',6),(75,'Lennon–McCartney','Maxwells Silver Hammer',6),(76,'Lennon–McCartney','Oh! Darling',6),(77,'Richard Starkey','Octopus Garden',6),(78,'Lennon–McCartney','I Want You',6),(79,'George Harrison','Here Comes the Sun',6),(80,'Lennon–McCartney','Because',6),(81,'Lennon–McCartney','You Never Give Me Your Money',6),(82,'Lennon–McCartney','Sun King',6),(83,'Lennon–McCartney','Mean Mr. Mustard',6),(84,'Lennon–McCartney','Polythene Pam',6),(85,'Lennon–McCartney','She Came in Through the Bathroom Window',6),(86,'Lennon–McCartney','Golden Slumbers',6),(87,'Lennon–McCartney','Carry That Weight',6),(88,'Lennon–McCartney','The End',6),(89,'Lennon–McCartney','Her Majesty',6),(90,'Lennon–McCartney','Two of Us',7),(91,'Lennon–McCartney','Dig a Pony',7),(92,'Lennon–McCartney','Across the Universe',7),(93,'George Harrison','I Me Mine',7),(94,'Lennon–McCartney','Dig It',7),(95,'Lennon–McCartney','Let It Be',7),(96,'Lennon–McCartney','Maggie Mae',7),(97,'Lennon–McCartney','Ive Got a Feeling',7),(98,'Lennon–McCartney','One After 909',7),(99,'Lennon–McCartney','The Long and Winding Road',7),(100,'George Harrison','For You Blue',7),(101,'Lennon–McCartney','Get Back',7),(102,'Marilyn Manson','Prelude',8),(103,'Daisy Berkowitz','Cake and Sodomy',8),(104,'Daisy Berkowitz','Lunchbox',8),(105,'Gidget Gein','Organ Grinder',8),(106,'Daisy Berkowitz','Cyclops',8),(107,'Marilyn Manson','Dope Hat',8),(108,'Marilyn Manson','Get Your Gunn',8),(109,'Daisy Berkowitz','Wrapped in Plastic',8),(110,'Daisy Berkowitz','Dogma',8),(111,'Marilyn Manson','Sweet Tooth',8),(112,'Gidget Gein','Snake Eyes',8),(113,'Daisy Berkowitz','My Monkey',8),(114,'Gidget Gein','Misery Machine',8),(115,'Daisy Berkowitz, Madonna Wayne Gacy','Irresponsible Hate Anthem',9),(116,'Twiggy Ramirez','The Beautiful People',9),(117,'Marilyn Manson, Twiggy Ramirez','Dried Up, Tied and Dead to the World',9),(118,'Daisy Berkowitz, Twiggy Ramirez','Tourniquet',9),(119,'Twiggy Ramirez, Trent Renzor','Little Horn',9),(120,'Madonna Wayne Gacy','Cryptochild',9),(121,'Twiggy Ramirez, Trent Renzor','Demography',9),(122,'Daisy Berkowitz, Twiggy Ramirez','Wormboy',9),(123,'Twiggy Ramirez','Mister Superstar',9),(124,'Marilyn Manson, Twiggy Ramirez, Wayne Gacy','Angel with the Scabbed Wings',9),(125,'Twiggy Ramirez, Wayne Gacy','Kinderfeld',9),(126,'Twiggy Ramirez, Wayne Gacy','Antichrist Superstar',9),(127,'Twiggy Ramirez','1996',9),(128,'Marilyn Manson','Minute of Decay',9),(129,'Twiggy Ramirez, Trent Renzor','The Reflecting God',9),(130,'Twiggy Ramirez, Marilyn Manson, Wayne Gacy, Daisy Berkowitz','Man That Tou Fear',9),(131,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','Great Big White World',10),(132,'Twiggy Ramirez','The Dope Show',10),(133,'Twiggy Ramirez, Zim Zum','Mechanical Animals',10),(134,'Twiggy Ramirez, Madonna Wayne Gacy','Rock Is Dead',10),(135,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','Disassociative',10),(136,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','The Speed of Pain',10),(137,'Twiggy Ramirez, Madonna Wayne Gacy','Posthuman',10),(138,'Twiggy Ramirez','I Want to Disappear',10),(139,'Twiggy Ramirez, Zim Zum','I Dont Like the Drugs',10),(140,'Twiggy Ramirez, Marilyn Manson','New Model No. 15',10),(141,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','User Friendly',10),(142,'Madonna Wayne Gacy, Zim Zum','Fundamentally Loathsome',10),(143,'Marilyn Manson, Madonna Wayne Gacy, Twiggy Ramirez','The Last Dat on Earth',10),(144,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','Coma White',10),(145,'Marilyn Manson','GodEatGod',11),(146,'Twiggy Ramirez, John Five','The Love Song',11),(147,'John Five','The Fight Song',11),(148,'John Five, Twiggy Ramirez','Disposable Teens',11),(149,'Twiggy Ramirez, John Five','Target Audience',11),(150,'Twiggy Ramirez, John Five','President Dead',11),(151,'Twiggy Ramirez','In the Shadow of the Valley of Death',11),(152,'Twiggy Ramirez','Cruci-Fiction in Space',11),(153,'John Five','A Place in the Dirt',11),(154,'Marilyn Manson','The Nobodies',11),(155,'Madonna Wayne Gacy, Tim Skoeld','Thaeter',12),(156,'John Five','This Is the New Shit',12),(157,'Marilyn Manson','Mobscene',12),(158,'John Five','Doll-Dagga Buzz-Buzz Ziggety-Zag',12),(159,'John Five','Use Your Fist and Not Your Mouth',12),(160,'Marilyn Manson','The Golden Age of Grotesque',12),(161,'John Five','Saint',12),(162,'John Five','Ka-Boom Ka-Boom',12),(163,'John Five','Slutgarden',12),(164,'John Five','Spade',12),(165,'Marilyn Manson','Para-noir',12),(166,'John Five','The Bright Young Things',12),(167,'Marilyn Manson','Better of Two Evils',12),(168,'John Five','Vodevil',12),(169,'Marilyn Manson','Obsequey (The Death of Art)',12),(170,'Chris Vrenna','Hey, Cruel World...',13),(171,'Marilyn Manson','No Reflection',13),(172,'Marilyn Manson','Pistol Whipped',13),(173,'Marilyn Manson','Overneath the Path of Misery',13),(174,'Marilyn Manson','Slo-Mo-Tion',13),(175,'Chris Vrenna','The Gardener',13),(176,'Chris Vrenna','The Flowers of Evil',13),(177,'Marilyn Manson','Children of Cain',13),(178,'Marilyn Manson','Disengaged',13),(179,'Twiggy','Lay Down Your Goddamn Arms',13),(180,'Twiggy','Murderers Are Getting Prettier Every Day',13),(181,'Marilyn Manson','Born Villain',13),(182,'Marilyn Manson','Breaking the Same Old Ground',13),(183,'Marilyn Manson','You Are So Vain',13),(184,'Marilyn Manson','Killing Strangers',14),(185,'Marilyn Manson','Deep Six',14),(186,'Marilyn Manson','Third Day of a Seven Day Binge',14),(187,'Marilyn Manson','The Mephistopheles of Los Angeles',14),(188,'Marilyn Manson','Warship My Wreck',14),(189,'Marilyn Manson','Slave Only Dreams to Be King',14),(190,'Marilyn Manson','The Devil Beneath My Feet',14),(191,'Marilyn Manson','Birds of Hell Awaiting',14),(192,'Marilyn Manson','Cupid Carries a Gun',14),(193,'Marilyn Manson','Odds of Even',14),(194,'U2','I Will Follow',15),(195,'U2','Twilight',15),(196,'U2','An Cat Dubh',15),(197,'U2','Into the Heart',15),(198,'U2','Out of Control',15),(199,'U2','Stories for Boys',15),(200,'U2','The Ocean',15),(201,'U2','A Day Without Me',15),(202,'U2','Another Time, Another Place',15),(203,'U2','\"The Electric Co',15),(204,'U2','Shadows and Tall Trees',15),(205,'U2','Gloria',16),(206,'U2','I Fall Down',16),(207,'U2','I Threw a Brick Through a Window',16),(208,'U2','Rejoice',16),(209,'U2','Fire',16),(210,'U2','Tomorrow',16),(211,'U2','October',16),(212,'U2','With a Shout (Jerusalem)',16),(213,'U2','Stranger in a Strange Land',16),(214,'U2','Scarlet',16),(215,'U2','Is That All',16);
/*!40000 ALTER TABLE `song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'projman9'
--

--
-- Dumping routines for database 'projman9'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-23 11:55:44
