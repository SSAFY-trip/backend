CREATE TABLE IF NOT EXISTS `guguns` (
                                        `no` INT NOT NULL AUTO_INCREMENT,
                                        `sido_code` INT NOT NULL,
                                        `gugun_code` INT NULL,
                                        `gugun_name` VARCHAR(20) NULL,
                                        PRIMARY KEY (`no`)
);

CREATE TABLE IF NOT EXISTS `sido` (
                                      `no` INT NOT NULL AUTO_INCREMENT,
                                      `sido_code` INT NULL,
                                      `sido_name` VARCHAR(20) NULL,
                                      PRIMARY KEY (`no`)
);

CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    provider ENUM('BOTH', 'LOCAL', 'GOOGLE', 'KAKAO', 'NAVER') NOT NULL,
    role ENUM('ROLE_USER', 'ROLE_ADMIN', 'ROLE_GUEST') NOT NULL
);

CREATE TABLE `trip` (
                                      `id` INT NOT NULL AUTO_INCREMENT,
                                      `name` VARCHAR(15) NULL,
                                      `start_date` DATE NULL,
                                      `end_date` DATE NULL,
                                      `trip_overview` VARCHAR(100) NULL,
                                      `img_url` VARCHAR(50) NULL,
                                      `is_public` BOOLEAN NULL,
                                      PRIMARY KEY (`id`)
);
