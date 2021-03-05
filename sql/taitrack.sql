-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 05, 2021 at 06:35 PM
-- Server version: 5.7.31
-- PHP Version: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kasukupro`
--

-- --------------------------------------------------------

--
-- Table structure for table `bac_1101_attendance`
--

DROP TABLE IF EXISTS `bac_1101_attendance`;
CREATE TABLE IF NOT EXISTS `bac_1101_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  `20TH-JANUARY-2021` varchar(10) DEFAULT 'ABSENT',
  `23RD-FEBRUARY-2021` varchar(10) DEFAULT 'ABSENT',
  `4TH-MARCH-2021` varchar(10) DEFAULT 'ABSENT',
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bac_1101_attendance`
--

INSERT INTO `bac_1101_attendance` (`REGNO`, `STUDENT_NAME`, `20TH-JANUARY-2021`, `23RD-FEBRUARY-2021`, `4TH-MARCH-2021`) VALUES
('18/05866', 'tony juma', 'PRESENT', 'PRESENT', 'PRESENT'),
('19/01000', 'Kenneth Mutua', 'ABSENT', 'ABSENT', 'ABSENT'),
('17/05253', 'Fidelis Nduku', 'PRESENT', 'ABSENT', 'ABSENT');

-- --------------------------------------------------------

--
-- Table structure for table `bac_1102_attendance`
--

DROP TABLE IF EXISTS `bac_1102_attendance`;
CREATE TABLE IF NOT EXISTS `bac_1102_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  `3RD-MARCH-2021` varchar(10) DEFAULT 'ABSENT',
  `4TH-MARCH-2021` varchar(10) DEFAULT 'ABSENT',
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bac_1102_attendance`
--

INSERT INTO `bac_1102_attendance` (`REGNO`, `STUDENT_NAME`, `3RD-MARCH-2021`, `4TH-MARCH-2021`) VALUES
('18/05866', 'tony juma', 'PRESENT', 'PRESENT');

-- --------------------------------------------------------

--
-- Table structure for table `bac_1103_attendance`
--

DROP TABLE IF EXISTS `bac_1103_attendance`;
CREATE TABLE IF NOT EXISTS `bac_1103_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  `13TH-NOVEMBER-2020` varchar(10) DEFAULT 'ABSENT',
  `25TH-NOVEMBER-2020` varchar(10) DEFAULT 'ABSENT',
  `26TH-NOVEMBER-2020` varchar(10) DEFAULT 'ABSENT',
  `2ND-DECEMBER-2020` varchar(10) DEFAULT 'ABSENT',
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bac_1103_attendance`
--

INSERT INTO `bac_1103_attendance` (`REGNO`, `STUDENT_NAME`, `13TH-NOVEMBER-2020`, `25TH-NOVEMBER-2020`, `26TH-NOVEMBER-2020`, `2ND-DECEMBER-2020`) VALUES
('18/05866', 'tony juma', 'PRESENT', 'PRESENT', 'ABSENT', 'ABSENT'),
('19/01000', 'Kenneth Mutua', 'PRESENT', 'ABSENT', 'ABSENT', 'PRESENT');

-- --------------------------------------------------------

--
-- Table structure for table `bac_1104_attendance`
--

DROP TABLE IF EXISTS `bac_1104_attendance`;
CREATE TABLE IF NOT EXISTS `bac_1104_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bac_1105_attendance`
--

DROP TABLE IF EXISTS `bac_1105_attendance`;
CREATE TABLE IF NOT EXISTS `bac_1105_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bac_1106_attendance`
--

DROP TABLE IF EXISTS `bac_1106_attendance`;
CREATE TABLE IF NOT EXISTS `bac_1106_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  `12TH-FEBRUARY-2021` varchar(10) DEFAULT 'ABSENT',
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bac_1106_attendance`
--

INSERT INTO `bac_1106_attendance` (`REGNO`, `STUDENT_NAME`, `12TH-FEBRUARY-2021`) VALUES
('18/05866', 'tony juma', 'ABSENT');

-- --------------------------------------------------------

--
-- Table structure for table `bac_1107_attendance`
--

DROP TABLE IF EXISTS `bac_1107_attendance`;
CREATE TABLE IF NOT EXISTS `bac_1107_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bit_1101_attendance`
--

DROP TABLE IF EXISTS `bit_1101_attendance`;
CREATE TABLE IF NOT EXISTS `bit_1101_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  `25TH-OCTOBER-2020` varchar(10) DEFAULT 'ABSENT',
  `25TH-NOVEMBER-2020` varchar(10) DEFAULT 'ABSENT',
  `12TH-FEBRUARY-2021` varchar(10) DEFAULT 'ABSENT',
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bit_1101_attendance`
--

INSERT INTO `bit_1101_attendance` (`REGNO`, `STUDENT_NAME`, `25TH-OCTOBER-2020`, `25TH-NOVEMBER-2020`, `12TH-FEBRUARY-2021`) VALUES
('18/01000', 'Jane Kingori', 'PRESENT', 'ABSENT', 'ABSENT'),
('18/06118', 'maina mwangi', 'ABSENT', 'ABSENT', 'ABSENT');

-- --------------------------------------------------------

--
-- Table structure for table `bit_1102_attendance`
--

DROP TABLE IF EXISTS `bit_1102_attendance`;
CREATE TABLE IF NOT EXISTS `bit_1102_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  `25TH-OCTOBER-2020` varchar(10) DEFAULT 'ABSENT',
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bit_1102_attendance`
--

INSERT INTO `bit_1102_attendance` (`REGNO`, `STUDENT_NAME`, `25TH-OCTOBER-2020`) VALUES
('18/01000', 'Jane Kingori', 'ABSENT');

-- --------------------------------------------------------

--
-- Table structure for table `bit_1103_attendance`
--

DROP TABLE IF EXISTS `bit_1103_attendance`;
CREATE TABLE IF NOT EXISTS `bit_1103_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bit_1104_attendance`
--

DROP TABLE IF EXISTS `bit_1104_attendance`;
CREATE TABLE IF NOT EXISTS `bit_1104_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bit_1105_attendance`
--

DROP TABLE IF EXISTS `bit_1105_attendance`;
CREATE TABLE IF NOT EXISTS `bit_1105_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bit_1106_attendance`
--

DROP TABLE IF EXISTS `bit_1106_attendance`;
CREATE TABLE IF NOT EXISTS `bit_1106_attendance` (
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `lecturers`
--

DROP TABLE IF EXISTS `lecturers`;
CREATE TABLE IF NOT EXISTS `lecturers` (
  `STAFFID` varchar(10) NOT NULL,
  `FIRSTNAME` varchar(30) NOT NULL,
  `SECONDNAME` varchar(30) NOT NULL,
  `TITLE` varchar(10) NOT NULL,
  `EMAIL` varchar(80) NOT NULL,
  `PASSCODE` int(5) NOT NULL,
  PRIMARY KEY (`STAFFID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lecturers`
--

INSERT INTO `lecturers` (`STAFFID`, `FIRSTNAME`, `SECONDNAME`, `TITLE`, `EMAIL`, `PASSCODE`) VALUES
('EMP001', 'SIMON', 'MWENDIA ', 'Dr.', 'mwendia@kca.ac.ke', 1234),
('EMP100', 'Jane', 'Okoth', 'Mrs.', 'jokoth@kca.ac.ke', 1234),
('EMP150', 'MARTIN', 'JUMA', 'MR.', 'mjuma@kca.ac.ke', 1234),
('EMP200', 'Barbara', 'Karendi', 'Mrs.', 'karendi@kca.ac.ke', 1234),
('9136335', 'Patrick', 'ogao', 'Prof.', 'ogao@kca.ac.ke', 1234);

-- --------------------------------------------------------

--
-- Table structure for table `lec_units`
--

DROP TABLE IF EXISTS `lec_units`;
CREATE TABLE IF NOT EXISTS `lec_units` (
  `NUMBER` int(20) NOT NULL AUTO_INCREMENT,
  `STAFFID` varchar(10) NOT NULL,
  `FULLNAME` varchar(50) NOT NULL,
  `UNIT_CODE` varchar(10) NOT NULL,
  `UNIT_NAME` varchar(100) NOT NULL,
  `COURSE_NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`NUMBER`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lec_units`
--

INSERT INTO `lec_units` (`NUMBER`, `STAFFID`, `FULLNAME`, `UNIT_CODE`, `UNIT_NAME`, `COURSE_NAME`) VALUES
(27, 'EMP001', 'Dr. SIMON MWENDIA ', 'BAC 1101', 'Computer Organization & Applications', 'Applied Computing'),
(23, 'EMP200', 'Mrs. Barbara Karendi', 'BAC 1102', 'Operating Systems', 'Applied Computing'),
(14, 'EMP100', 'Mrs. Jane Okoth', 'BIT 1101', 'INTERNET TECHNOLOGY', 'Information Technology'),
(15, 'EMP100', 'Mrs. Jane Okoth', 'BIT 1102', 'Business Communication Skills', 'Information Technology'),
(24, 'EMP200', 'Mrs. Barbara Karendi', 'BAC 1101', 'Computer Organization & Applications', 'Applied Computing'),
(28, 'EMP001', 'Dr. SIMON MWENDIA ', 'BAC 1102', 'Operating Systems', 'Applied Computing');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
  `REGNO` varchar(30) NOT NULL,
  `FIRSTNAME` varchar(30) NOT NULL,
  `SECONDNAME` varchar(30) NOT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `GENDER` varchar(10) NOT NULL,
  `FACULTY` varchar(250) NOT NULL,
  `COURSE` varchar(250) NOT NULL,
  `PASSCODE` int(5) NOT NULL,
  PRIMARY KEY (`REGNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`REGNO`, `FIRSTNAME`, `SECONDNAME`, `EMAIL`, `GENDER`, `FACULTY`, `COURSE`, `PASSCODE`) VALUES
('18/05866', 'tony', 'juma', 'tony@gmail.com', 'Male', 'Faculty of Computing', 'Applied Computing', 1234),
('18/01000', 'Jane', 'Kingori', 'jkingori@gmail.com', 'Male', 'Faculty of Computing', 'Information Technology', 1234),
('19/01000', 'Kenneth', 'Mutua', 'kmutua@gmail.com', 'Male', 'Faculty of Computing', 'Applied Computing', 1234),
('17/05253', 'Fidelis', 'Nduku', 'ndukufidelis72@gmail.com', 'Female', 'Faculty of Computing', 'Applied Computing', 1234),
('18/06118', 'maina', 'mwangi', 'maish19@live.com', 'Male', 'Faculty of Computing', 'Information Technology', 1234);

-- --------------------------------------------------------

--
-- Table structure for table `student_units`
--

DROP TABLE IF EXISTS `student_units`;
CREATE TABLE IF NOT EXISTS `student_units` (
  `NUMBER` int(20) NOT NULL AUTO_INCREMENT,
  `REGNO` varchar(10) NOT NULL,
  `STUDENT_NAME` varchar(50) NOT NULL,
  `UNIT_CODE` varchar(10) NOT NULL,
  `UNIT_NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`NUMBER`)
) ENGINE=MyISAM AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `student_units`
--

INSERT INTO `student_units` (`NUMBER`, `REGNO`, `STUDENT_NAME`, `UNIT_CODE`, `UNIT_NAME`) VALUES
(71, '18/05866', 'tony juma', 'BAC 1101', 'Computer Organization & Applications'),
(67, '17/05253', 'Fidelis Nduku', 'BAC 1102', 'Operating Systems'),
(66, '17/05253', 'Fidelis Nduku', 'BAC 1101', 'Computer Organization & Applications'),
(65, '19/01000', 'Kenneth Mutua', 'BAC 1101', 'Computer Organization & Applications'),
(64, '19/01000', 'Kenneth Mutua', 'BAC 1102', 'Operating Systems'),
(70, '18/06118', 'maina mwangi', 'BIT 1101', 'INTERNET TECHNOLOGY'),
(54, '18/01000', 'Jane Kingori', 'BIT 1102', 'Business Communication Skills'),
(73, '18/05866', 'tony juma', 'BAC 1102', 'Operating Systems'),
(53, '18/01000', 'Jane Kingori', 'BIT 1101', 'INTERNET TECHNOLOGY');

-- --------------------------------------------------------

--
-- Table structure for table `units`
--

DROP TABLE IF EXISTS `units`;
CREATE TABLE IF NOT EXISTS `units` (
  `CODE` varchar(10) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `COURSE_NAME` varchar(100) NOT NULL,
  `COURSE_CODE` varchar(30) NOT NULL,
  `LATITUDE` double DEFAULT '0',
  `LONGITUDE` double DEFAULT '0',
  PRIMARY KEY (`CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `units`
--

INSERT INTO `units` (`CODE`, `NAME`, `COURSE_NAME`, `COURSE_CODE`, `LATITUDE`, `LONGITUDE`) VALUES
('BAC 1101', 'Computer Organization & Applications', 'APPLIED COMPUTING', 'BAC', -1.2190461, 36.8959022),
('BAC 1102', 'Operating Systems', 'APPLIED COMPUTING', 'BAC', -1.2190461, 36.8959022),
('BAC 1103', 'Mathematics For Sciences', 'APPLIED COMPUTING', 'BAC', -1.2190267, 36.8959217),
('BAC 1104', 'Business Communication Skills', 'APPLIED COMPUTING', 'BAC', 0, 0),
('BAC 1105', 'Installation and Customization', 'APPLIED COMPUTING', 'BAC', 0, 0),
('BAC 1106', 'HIV/AIDS and Information Literacy', 'APPLIED COMPUTING', 'BAC', -1.2190282, 36.895923),
('BAC 1107', 'Internet Technologies and the Web', 'APPLIED COMPUTING', 'BAC', 0, 0),
('BIT 1101', 'INTERNET TECHNOLOGY', 'INFORMATION TECHNOLOGY', 'BIT', -1.253486, 36.8604478),
('BIT 1102', 'Business Communication Skills', 'INFORMATION TECHNOLOGY', 'BIT', -1.2186796, 36.8958139),
('BIT 1103', 'Computer Organization and Architecture\r\n', 'INFORMATION TECHNOLOGY', 'BIT', 0, 0),
('BIT 1104', 'OPERATING SYSTEMS', 'INFORMATION TECHNOLOGY', 'BIT', 0, 0),
('BIT 1105', 'Mathematics for Sciences', 'INFORMATION TECHNOLOGY', 'BIT', 0, 0),
('BIT 1106', 'HIV and AIDS and Information Literacy', 'INFORMATION TECHNOLOGY', 'BIT', 0, 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
