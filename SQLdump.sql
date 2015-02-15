 phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 15, 2015 at 05:58 AM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET FOREIGN_KEY_CHECKS=0;
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `macRulesSchema`
--

-- --------------------------------------------------------

--
-- Table structure for table `macRulesTable`
--

CREATE TABLE IF NOT EXISTS `macRulesTable` (
`Index` int(11) NOT NULL,
  `mac` bigint(228) NOT NULL,
  `block` tinyint(1) DEFAULT '0',
  `user_total` bigint(9) DEFAULT '60',
  `total_all` bigint(9) DEFAULT '60',
  `start_time` time DEFAULT '12:00:00',
  `stop_time` time DEFAULT '08:00:00'
) TYPE=InnoDB AUTO_INCREMENT=134;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `macRulesTable`
--
ALTER TABLE `macRulesTable`
 ADD PRIMARY KEY (`Index`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `macRulesTable`
--
ALTER TABLE `macRulesTable`
MODIFY `Index` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=134;SET FOREIGN_KEY_CHECKS=1;
COMMIT;
