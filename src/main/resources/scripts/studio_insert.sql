/*
  The Beatles
*/

INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (1,'John Lennon','380956660066');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (2,'Paul McCartney','380635667800');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (3,'Ringo Starr','380508337538');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (4,'George Harrison','380956395627');

/*
  Marilyn Manson
*/

INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (5,'Marilyn Manson','380956660066');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (6,'Daisy Berkowitz','380635667800');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (7,'Madonna Wayne Gacy','380508337538');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (8,'Sara Lee Lucas','380956395627');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (9,'Gidget Gein','380987362574');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (10,'Twiggy Ramirez','380508336454');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (11,'Ginger Fish','380508358854');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (12,'Zim Zum','380508358854');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (13,'John Five','380508111854');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (14,'Fred Sablan','380507366854');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (15,'Chris Vrenna','380501113322');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (16,'Tyler Bates','380501115522');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (17,'Gil Sharone','380500003322');

/*
  U2
*/

INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (18,'Bono','380507366854');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (19,'Larry Mullen','380501113322');
INSERT INTO `musician` (`id`,`name`,`phone`) VALUES (20,'Adam Clayton','380501115522');

/*
  The Beatles
*/

INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (1,'With the Beatles','2000-11-23',10,0.5,0.05,1);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (2,'Help!','2001-05-11',10,0.5,0.05,1);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (3,'Rubber Soul','2002-01-12',10,0.5,0.05,1);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (4,'The White Album','2004-10-12',10,0.4,0.05,1);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (5,'Yellow Submarine','2006-03-14',10,0.5,0.05,1);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (6,'Abbey Road','2007-01-03',10,0.6,0.05,1);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (7,'Let It Be','2007-12-12',10,0.5,0.05,1);

/*
  Marilyn Manson
*/

INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (8,'Portrait of an American Family','2001-02-02',15,0.5,0.05,5);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (9,'Antichrist Superstar','2003-08-10',15,0.4,0.05,5);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (10,'Mechanical Animals','2004-09-15',15,0.5,0.05,5);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (11,'Holy Wood','2005-11-14',15,0.6,0.05,5);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (12,'The Golden Age of Grotesque','2006-05-13',15,0.7,0.05,5);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (13,'Born Villain','2012-04-25',15,0.5,0.05,5);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (14,'The Pale Emperor','2015-01-20',15,0.3,0.05,5);

/*
  U2
*/

INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (15,'Boy','2016-04-25',12,0.5,0.05,18);
INSERT INTO `album` (`id`,`title`,`record_date`,`price`,`gonorar_percent`,`chief_part`,`chief`) VALUES (16,'October','2017-01-20',12,0.6,0.05,18);

/*
  album 1: With the Beatles
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (1,'Lennon–McCartney','It Wont Be Long',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (2,'Lennon–McCartney','All Ive Got to Do',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (3,'Lennon–McCartney','All My Loving',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (4,'George Harrison','Dont Bother Me',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (5,'Lennon–McCartney','Little Child',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (6,'Meredith Willson','Till There Was You',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (7,'Georgia Dobbins','Please Mister Postman',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (8,'Chuck Berry','Roll Over Beethoven',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (9,'Lennon–McCartney','Hold Me Tight',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (10,'Smokey Robinson','You Really Got a Hold on Me',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (11,'Lennon–McCartney','I Wanna Be Your Man',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (12,'Richard Drapkin','Devil in Her Heart',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (13,'Lennon–McCartney','Not a Second Time',1);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (14,'Janie Bradford','Money (Thats What I Want)',1);

/*
  album 2: Help!
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (15,'Lennon–McCartney','Help!',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (16,'Lennon–McCartney','The Night Before',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (17,'Lennon–McCartney','You Have Got to Hide Your Love Away',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (18,'George Harrison','I Need You',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (19,'Lennon–McCartney','Another Girl',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (20,'Lennon–McCartney','You Are Going to Lose That Girl',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (21,'Lennon–McCartney','Ticket to Ride',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (22,'Johnny Russell','Act Naturally',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (23,'Lennon–McCartney','It Is Only Love',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (24,'George Harrison','You Like Me Too Much',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (25,'Lennon–McCartney','Tell Me What You See',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (26,'Lennon–McCartney','I Have Just Seen a Face',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (27,'Lennon–McCartney','Yesterday',2);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (28,'Lennon–McCartney','Dizzy Miss Lizzy',2);

/*
  album 3: Rubber Soul
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (29,'Lennon–McCartney','Drive My Car',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (30,'Lennon–McCartney','Norwegian Wood',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (31,'Lennon–McCartney','You Wont See Me',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (32,'Lennon–McCartney','Nowhere Man',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (33,'George Harrison','Think for Yourself',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (34,'Lennon–McCartney','The Word',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (35,'Lennon–McCartney','Michelle',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (36,'Lennon–McCartney','What Goes On',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (37,'Lennon–McCartney','Girl',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (38,'Lennon–McCartney','Im Looking Through You',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (39,'Lennon–McCartney','In My Life',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (40,'Lennon–McCartney','Wait',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (41,'George Harrison','If I Needed Someone',3);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (42,'Lennon–McCartney','Run for Your Life',3);

/*
  album 4: The White Album
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (43,'Lennon–McCartney','Back in the U.S.S.R.',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (44,'Lennon–McCartney','Dear Prudence',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (45,'Lennon–McCartney','Glass Onion',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (46,'Lennon–McCartney','Ob-La-Di, Ob-La-Da',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (47,'Lennon–McCartney','Wild Honey Pie',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (48,'Lennon–McCartney','The Continuing Story of Bungalow Bill',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (49,'George Harrison','While My Guitar Gently Weeps',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (50,'Lennon–McCartney','Happiness Is a Warm Gun',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (51,'Lennon–McCartney','Martha My Dear',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (52,'Lennon–McCartney','Im So Tired',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (53,'Lennon–McCartney','Blackbird',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (54,'George Harrison','Piggies',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (55,'Lennon–McCartney','Rocky Raccoon',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (56,'Richard Starkey','Dont Pass Me By',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (57,'Lennon–McCartney','Why Dont We Do It in the Road?',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (58,'Lennon–McCartney','I Will',4);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (59,'Lennon–McCartney','Julia',4);

/*
  album 5: Yellow Submarine
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (60,'Lennon–McCartney','Yellow Submarine',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (61,'George Harrison','Only a Northern Song',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (62,'Lennon–McCartney','All Together Now',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (63,'Lennon–McCartney','Hey Bulldog',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (64,'George Harrison','Its All Too Much',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (65,'Lennon–McCartney','All You Need Is Love',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (66,'George Martin','Pepperland',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (67,'George Martin','Sea of Time',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (68,'George Martin','Sea of Holes',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (69,'George Martin','Sea of Monsters',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (70,'George Martin','March of the Meanies',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (71,'George Martin','Pepperland Laid Waste',5);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (72,'Lennon–McCartney','Yellow Submarine in Pepperland',5);

/*
  album 6: Abbey Road
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (73,'Lennon–McCartney','Come Together',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (74,'George Harrison','Something',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (75,'Lennon–McCartney','Maxwells Silver Hammer',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (76,'Lennon–McCartney','Oh! Darling',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (77,'Richard Starkey','Octopus Garden',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (78,'Lennon–McCartney','I Want You',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (79,'George Harrison','Here Comes the Sun',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (80,'Lennon–McCartney','Because',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (81,'Lennon–McCartney','You Never Give Me Your Money',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (82,'Lennon–McCartney','Sun King',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (83,'Lennon–McCartney','Mean Mr. Mustard',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (84,'Lennon–McCartney','Polythene Pam',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (85,'Lennon–McCartney','She Came in Through the Bathroom Window',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (86,'Lennon–McCartney','Golden Slumbers',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (87,'Lennon–McCartney','Carry That Weight',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (88,'Lennon–McCartney','The End',6);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (89,'Lennon–McCartney','Her Majesty',6);

/*
  album 7: Let It Be
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (90,'Lennon–McCartney','Two of Us',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (91,'Lennon–McCartney','Dig a Pony',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (92,'Lennon–McCartney','Across the Universe',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (93,'George Harrison','I Me Mine',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (94,'Lennon–McCartney','Dig It',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (95,'Lennon–McCartney','Let It Be',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (96,'Lennon–McCartney','Maggie Mae',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (97,'Lennon–McCartney','Ive Got a Feeling',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (98,'Lennon–McCartney','One After 909',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (99,'Lennon–McCartney','The Long and Winding Road',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (100,'George Harrison','For You Blue',7);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (101,'Lennon–McCartney','Get Back',7);

/*
  album 8: Portrait of an American Family
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (102,'Marilyn Manson','Prelude',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (103,'Daisy Berkowitz','Cake and Sodomy',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (104,'Daisy Berkowitz','Lunchbox',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (105,'Gidget Gein','Organ Grinder',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (106,'Daisy Berkowitz','Cyclops',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (107,'Marilyn Manson','Dope Hat',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (108,'Marilyn Manson','Get Your Gunn',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (109,'Daisy Berkowitz','Wrapped in Plastic',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (110,'Daisy Berkowitz','Dogma',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (111,'Marilyn Manson','Sweet Tooth',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (112,'Gidget Gein','Snake Eyes',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (113,'Daisy Berkowitz','My Monkey',8);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (114,'Gidget Gein','Misery Machine',8);

/*
  album 9: Antichrist Superstar
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (115,'Daisy Berkowitz, Madonna Wayne Gacy','Irresponsible Hate Anthem',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (116,'Twiggy Ramirez','The Beautiful People',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (117,'Marilyn Manson, Twiggy Ramirez','Dried Up, Tied and Dead to the World',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (118,'Daisy Berkowitz, Twiggy Ramirez','Tourniquet',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (119,'Twiggy Ramirez, Trent Renzor','Little Horn',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (120,'Madonna Wayne Gacy','Cryptochild',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (121,'Twiggy Ramirez, Trent Renzor','Demography',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (122,'Daisy Berkowitz, Twiggy Ramirez','Wormboy',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (123,'Twiggy Ramirez','Mister Superstar',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (124,'Marilyn Manson, Twiggy Ramirez, Wayne Gacy','Angel with the Scabbed Wings',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (125,'Twiggy Ramirez, Wayne Gacy','Kinderfeld',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (126,'Twiggy Ramirez, Wayne Gacy','Antichrist Superstar',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (127,'Twiggy Ramirez','1996',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (128,'Marilyn Manson','Minute of Decay',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (129,'Twiggy Ramirez, Trent Renzor','The Reflecting God',9);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (130,'Twiggy Ramirez, Marilyn Manson, Wayne Gacy, Daisy Berkowitz','Man That Tou Fear',9);

/*
  album 10: Mechanical Animals
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (131,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','Great Big White World',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (132,'Twiggy Ramirez','The Dope Show',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (133,'Twiggy Ramirez, Zim Zum','Mechanical Animals',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (134,'Twiggy Ramirez, Madonna Wayne Gacy','Rock Is Dead',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (135,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','Disassociative',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (136,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','The Speed of Pain',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (137,'Twiggy Ramirez, Madonna Wayne Gacy','Posthuman',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (138,'Twiggy Ramirez','I Want to Disappear',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (139,'Twiggy Ramirez, Zim Zum','I Dont Like the Drugs',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (140,'Twiggy Ramirez, Marilyn Manson','New Model No. 15',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (141,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','User Friendly',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (142,'Madonna Wayne Gacy, Zim Zum','Fundamentally Loathsome',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (143,'Marilyn Manson, Madonna Wayne Gacy, Twiggy Ramirez','The Last Dat on Earth',10);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (144,'Twiggy Ramirez, Madonna Wayne Gacy, Zim Zum','Coma White',10);

/*
  album 11: Holy Wood
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (145,'Marilyn Manson','GodEatGod',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (146,'Twiggy Ramirez, John Five','The Love Song',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (147,'John Five','The Fight Song',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (148,'John Five, Twiggy Ramirez','Disposable Teens',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (149,'Twiggy Ramirez, John Five','Target Audience',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (150,'Twiggy Ramirez, John Five','President Dead',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (151,'Twiggy Ramirez','In the Shadow of the Valley of Death',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (152,'Twiggy Ramirez','Cruci-Fiction in Space',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (153,'John Five','A Place in the Dirt',11);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (154,'Marilyn Manson','The Nobodies',11);

/*
  album 12: The Golden Age of Grotesque
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (155,'Madonna Wayne Gacy, Tim Skoeld','Thaeter',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (156,'John Five','This Is the New Shit',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (157,'Marilyn Manson','Mobscene',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (158,'John Five','Doll-Dagga Buzz-Buzz Ziggety-Zag',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (159,'John Five','Use Your Fist and Not Your Mouth',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (160,'Marilyn Manson','The Golden Age of Grotesque',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (161,'John Five','Saint',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (162,'John Five','Ka-Boom Ka-Boom',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (163,'John Five','Slutgarden',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (164,'John Five','Spade',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (165,'Marilyn Manson','Para-noir',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (166,'John Five','The Bright Young Things',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (167,'Marilyn Manson','Better of Two Evils',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (168,'John Five','Vodevil',12);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (169,'Marilyn Manson','Obsequey (The Death of Art)',12);

/*
  album 13: Born Villain
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (170,'Chris Vrenna','Hey, Cruel World...',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (171,'Marilyn Manson','No Reflection',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (172,'Marilyn Manson','Pistol Whipped',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (173,'Marilyn Manson','Overneath the Path of Misery',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (174,'Marilyn Manson','Slo-Mo-Tion',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (175,'Chris Vrenna','The Gardener',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (176,'Chris Vrenna','The Flowers of Evil',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (177,'Marilyn Manson','Children of Cain',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (178,'Marilyn Manson','Disengaged',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (179,'Twiggy','Lay Down Your Goddamn Arms',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (180,'Twiggy','Murderers Are Getting Prettier Every Day',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (181,'Marilyn Manson','Born Villain',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (182,'Marilyn Manson','Breaking the Same Old Ground',13);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (183,'Marilyn Manson','You Are So Vain',13);

/*
  album 14: The Pale Emperor
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (184,'Marilyn Manson','Killing Strangers',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (185,'Marilyn Manson','Deep Six',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (186,'Marilyn Manson','Third Day of a Seven Day Binge',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (187,'Marilyn Manson','The Mephistopheles of Los Angeles',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (188,'Marilyn Manson','Warship My Wreck',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (189,'Marilyn Manson','Slave Only Dreams to Be King',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (190,'Marilyn Manson','The Devil Beneath My Feet',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (191,'Marilyn Manson','Birds of Hell Awaiting',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (192,'Marilyn Manson','Cupid Carries a Gun',14);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (193,'Marilyn Manson','Odds of Even',14);

/*
  album 15: Boy
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (194,'U2','I Will Follow',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (195,'U2','Twilight',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (196,'U2','An Cat Dubh',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (197,'U2','Into the Heart',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (198,'U2','Out of Control',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (199,'U2','Stories for Boys',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (200,'U2','The Ocean',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (201,'U2','A Day Without Me',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (202,'U2','Another Time, Another Place',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (203,'U2','"The Electric Co',15);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (204,'U2','Shadows and Tall Trees',15);

/*
  album 16: October
*/

INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (205,'U2','Gloria',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (206,'U2','I Fall Down',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (207,'U2','I Threw a Brick Through a Window',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (208,'U2','Rejoice',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (209,'U2','Fire',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (210,'U2','Tomorrow',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (211,'U2','October',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (212,'U2','With a Shout (Jerusalem)',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (213,'U2','Stranger in a Strange Land',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (214,'U2','Scarlet',16);
INSERT INTO `song` (`id`,`author`,`title`,`album`) VALUES (215,'U2','Is That All',16);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (1, 'Sully Prudhomme', '2015-04-14', 1);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (1, 1, 1);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (2, 'Theodor Mommsen', '2015-04-17', 4);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (2, 1, 2);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (3, 'Henryk Sienkiewicz', '2015-04-22', 6);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (1, 100.0, 10, 3);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (4, 'Giosue Carducci', '2015-05-01', 5);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (3, 5, 4);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (5, 'Rudyard Kipling', '2015-05-12', 11);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (2, 75.50, 10, 5);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (6, 'Rudolf Christoph Eucken', '2015-05-13', 2);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (4, 2, 6);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (7, 'Selma Lagerloef', '2015-05-21', 9);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (3, 150.0, 12, 7);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (8, 'Paul von Heyse', '2015-05-24', 3);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (4, 130.0, 12, 8);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (9, 'Maurice Maeterlinck', '2015-05-29', 5);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (5, 1, 9);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (10, 'Gerhart Hauptmann', '2015-06-01', 8);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (6, 2, 10);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (11, 'Rabindranath Tagore', '2015-06-02', 10);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (7, 3, 11);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (12, 'Romain Rolland', '2015-06-12', 13);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (5, 110.0, 12, 12);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (13, 'Verner von Heidenstam', '2015-06-13', 12);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (8, 2, 13);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (14, 'Karl Adolph Gjellerup', '2015-06-14', 14);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (9, 1, 14);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (15, 'Henrik Pontoppidan', '2015-06-18', 15);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (6, 90.0, 8, 15);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (16, 'Carl Spitteler', '2015-06-22', 6);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (7, 112.0, 12, 16);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (17, 'Knut Hamsun', '2015-07-09', 1);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (10, 1, 17);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (18, 'Anatole France', '2015-07-09', 9);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (11, 2, 18);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (19, 'Jacinto Benavente', '2015-07-11', 5);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (8, 130.0, 14, 19);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (20, 'William Butler Yeats', '2015-07-14', 7);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (12, 1, 20);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (21, 'Wladyslaw Reymont', '2015-07-16', 1);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (9, 125.5, 10, 21);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (22, 'George Bernard Shaw', '2015-07-19', 4);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (10, 115.0, 9, 22);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (23, 'Grazia Deledda', '2015-08-01', 8);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (11, 92.0, 9, 23);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (24, 'Henri Bergson', '2015-08-20', 11);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (12, 150.0, 12, 24);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (25, 'Sigrid Undset', '2015-08-23', 16);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (13, 1, 25);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (26, 'Thomas Mann', '2015-09-02', 12);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (13, 120.0, 11, 26);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (27, 'Sinclair Lewis', '2015-09-14', 10);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (14, 110.0, 8, 27);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (28, 'Erik Axel Karlfeldt', '2015-09-21', 4);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (14, 1, 28);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (29, 'John Galsworthy', '2015-09-25', 3);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (15, 1, 29);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (30, 'Ivan Bunin', '2015-10-01', 2);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (15, 60.0, 5, 30);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (31, 'Wladyslaw Reymont', '2015-10-01', 9);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (16, 100.0, 10, 31);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (32, 'Luigi Pirandello', '2015-10-04', 5);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (17, 120.0, 12, 32);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (33, 'Grazia Deledda', '2015-10-12', 10);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (16, 2, 33);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (34, 'Roger Martin du Gard', '2015-10-21', 1);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (18, 75.5, 7, 34);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (35, 'Sigrid Undset', '2015-11-03', 2);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (17, 1, 35);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (36, 'Johannes Vilhelm Jensen', '2015-12-01', 13);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (19, 55.5, 4, 36);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (37, 'Gabriela Mistral', '2015-12-04', 7);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (20, 120.0, 12, 37);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (38, 'Hermann Hesse', '2015-12-06', 4);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (21, 110.0, 11, 38);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (39, 'John Galsworthy', '2015-12-21', 5);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (22, 125.50, 12, 39);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (40, 'William Faulkner', '2015-12-26', 8);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (23, 120.0, 12, 40);

/*

 */

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (41, 'Bertrand Russell', '2016-01-12', 3);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (24, 100.0, 10, 41);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (42, 'Ernest Hemingway', '2016-02-02', 6);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (25, 90.5, 9, 42);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (43, 'Albert Camus', '2016-02-13', 14);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (26, 120.0, 11, 43);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (44, 'Boris Pasternak', '2016-02-15', 15);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (18, 1, 44);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (45, 'Salvatore Quasimodo', '2016-02-21', 1);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (27, 120.0, 12, 45);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (46, 'Saint-John Perse', '2016-03-10', 6);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (28, 150.0, 12, 46);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (47, 'John Steinbeck', '2016-03-15', 5);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (29, 85.5, 6, 47);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (48, 'Giorgos Seferis', '2016-03-21', 7);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (30, 120.0, 12, 48);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (49, 'Jean-Paul Sartre', '2016-03-28', 9);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (31, 110.0, 10, 49);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (50, 'Mikhail Sholokhov', '2016-04-11', 12);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (19, 10, 50);

/*

 */

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (51, 'Shmuel Yosef Agnon', '2016-05-11', 1);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (32, 100.0, 10, 51);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (52, 'Nelly Sachs', '2016-05-12', 6);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (33, 120.0, 12, 52);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (53, 'Yasunari Kawabata', '2016-05-15', 10);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (34, 55.5, 4, 53);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (54, 'Samuel Beckett', '2016-05-28', 1);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (35, 75.5, 7, 54);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (55, 'Aleksandr Solzhenitsyn', '2016-06-04', 2);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (36, 55.5, 4, 55);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (56, 'Pablo Neruda', '2016-06-16', 13);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (37, 55.5, 4, 56);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (57, 'Patrick White', '2016-06-14', 7);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (38, 120.0, 12, 57);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (58, 'Eyvind Johnson', '2016-07-13', 4);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (39, 110.0, 11, 58);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (59, 'Harry Martinson', '2016-07-17', 5);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (40, 125.50, 12, 59);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (60, 'Eugenio Montale', '2016-07-21', 8);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (41, 120.0, 12, 60);

/*

 */

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (61, 'Saul Bellow', '2016-08-12', 3);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (42, 100.0, 10, 61);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (62, 'Vicente Aleixandre', '2016-09-01', 6);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (43, 90.5, 9, 62);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (63, 'Isaac Bashevis Singer', '2016-09-21', 14);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (44, 120.0, 11, 63);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (64, 'Odysseas Elytis', '2016-09-29', 15);
INSERT INTO `record` (`id`,`quantity`,`selling_id`) VALUES (20, 1, 64);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (65, 'Elias Canetti', '2016-10-12', 2);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (45, 120.0, 12, 65);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (66, 'William Golding', '2016-11-11', 8);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (46, 150.0, 12, 66);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (67, 'Claude Simon', '2016-12-12', 5);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (47, 85.5, 6, 67);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (68, 'Wole Soyinka', '2017-01-04', 7);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (48, 120.0, 12, 68);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (69, 'Joseph Brodsky', '2017-01-07', 9);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (49, 110.0, 10, 69);

INSERT INTO `sellings` (`id`,`client`,`sell_date`,`album_id`) VALUES (70, 'Toni Morrison', '2016-01-21', 12);
INSERT INTO `license` (`id`,`price`,`period`, `selling_id`) VALUES (50, 110.0, 10, 70);