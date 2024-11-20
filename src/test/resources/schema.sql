-- Create tables if they do not exist
CREATE TABLE IF NOT EXISTS `sido` (
    `no` INT NOT NULL AUTO_INCREMENT,
    `sido_code` INT NULL,
    `sido_name` VARCHAR(20) NULL,
    PRIMARY KEY (`no`)
);

CREATE TABLE IF NOT EXISTS `guguns` (
    `no` INT NOT NULL AUTO_INCREMENT,
    `sido_code` INT NOT NULL,
    `gugun_code` INT NULL,
    `gugun_name` VARCHAR(20) NULL,
    PRIMARY KEY (`no`)
);

#CREATE TABLE IF NOT EXISTS `user` (
#    `id` INT NOT NULL AUTO_INCREMENT,
#    `name` VARCHAR(10) NULL,
#    `email` VARCHAR(50) NULL,
#    `pwd` VARCHAR(15) NULL,
#    `coin` INT NULL,
#    PRIMARY KEY (`id`)
#)

CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    provider ENUM('BOTH', 'LOCAL', 'GOOGLE', 'KAKAO', 'NAVER') NOT NULL,
    role ENUM('ROLE_USER', 'ROLE_ADMIN', 'ROLE_GUEST') NOT NULL
);

CREATE TABLE IF NOT EXISTS `refresh` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    refresh VARCHAR(255) NOT NULL,
    expiration TIMESTAMP NOT NULL,
    FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS `trip_member` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `trip_id` INT NOT NULL,
    PRIMARY KEY (`id`, `user_id`, `trip_id`)
);

CREATE TABLE IF NOT EXISTS `event` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `trip_id`	INT	NOT NULL,
		`name`	VARCHAR(50)	NULL,
		`date`	DATE	NULL,
		`order`	INT	NULL,
		`memo`	VARCHAR(300)	NULL,
		`latitude`	FLOAT	NULL,
		`longitude`	FLOAT	NULL,
		`category`	VARCHAR(50)	NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `trip_gugun` (
    `trip_id` INT NOT NULL,
    `gugun_id` INT NOT NULL,
    PRIMARY KEY (`trip_id`, `gugun_id`)
);

CREATE TABLE IF NOT EXISTS `trip` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(15) NULL,
    `start_date` DATE NULL,
    `end_date` DATE NULL,
    `trip_overview` VARCHAR(100) NULL,
    `img_url` VARCHAR(50) NULL,
    `is_public` BOOLEAN NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `user_like_trip` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `trip_id` INT NOT NULL,
    PRIMARY KEY (`id`, `user_id`, `trip_id`)
);

CREATE TABLE IF NOT EXISTS `place` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NULL,
    `longitude` FLOAT NULL,
    `latitude` FLOAT NULL,
    `address` VARCHAR(50) NULL,
    `road_address` VARCHAR(50) NULL,
    `category_name` VARCHAR(100) NULL,
    `category_group_code` VARCHAR(5) NULL,
    `category_group_name` VARCHAR(10) NULL,
    `phone` VARCHAR(20) NULL,
    PRIMARY KEY (`id`)
);

-- Drop existing foreign key constraints if necessary (example names)
ALTER TABLE `guguns`
    DROP FOREIGN KEY `FK_sido_TO_guguns_1`;

ALTER TABLE `trip_member`
    DROP FOREIGN KEY `FK_user_TO_trip_member_1`;

ALTER TABLE `trip_member`
    DROP FOREIGN KEY `FK_trip_TO_trip_member_1`;

ALTER TABLE `event`
    DROP FOREIGN KEY `FK_trip_TO_event_1`;

ALTER TABLE `trip_gugun`
    DROP FOREIGN KEY `FK_trip_TO_trip_gugun_1`;

ALTER TABLE `user_like_trip`
    DROP FOREIGN KEY `FK_user_TO_user_like_trip_1`;

ALTER TABLE `user_like_trip`
    DROP FOREIGN KEY `FK_trip_TO_user_like_trip_1`;

-- Add foreign key constraints
ALTER TABLE `guguns`
    ADD CONSTRAINT `FK_sido_TO_guguns_1` FOREIGN KEY (`sido_code`) REFERENCES `sido` (`no`);

ALTER TABLE `trip_member`
    ADD CONSTRAINT `FK_user_TO_trip_member_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

ALTER TABLE `trip_member`
    ADD CONSTRAINT `FK_trip_TO_trip_member_1` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`);

ALTER TABLE `event`
    ADD CONSTRAINT `FK_trip_TO_event_1` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`);

ALTER TABLE `trip_gugun`
    ADD CONSTRAINT `FK_trip_TO_trip_gugun_1` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`);

ALTER TABLE `trip_gugun`
    ADD CONSTRAINT `FK_guguns_TO_trip_gugun_1` FOREIGN KEY (`gugun_id`) REFERENCES `guguns` (`no`);

ALTER TABLE `user_like_trip`
    ADD CONSTRAINT `FK_user_TO_user_like_trip_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

ALTER TABLE `user_like_trip`
    ADD CONSTRAINT `FK_trip_TO_user_like_trip_1` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`);
