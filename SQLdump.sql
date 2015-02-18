 drop table if exists macRulesTable;

CREATE TABLE IF NOT EXISTS `macRulesTable` (
`Index` integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
`mac` bigint(228) NOT NULL,
`block` tinyint(1) DEFAULT '0',
`user_total` bigint(9) DEFAULT '60',
`total_all` bigint(9) DEFAULT '60',
`start_time` time DEFAULT '12:00:00',
`stop_time` time DEFAULT '08:00:00'
)
