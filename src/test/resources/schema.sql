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
                                      `id` INT NOT NULL AUTO_INCREMENT,
                                      `name` VARCHAR(10) NULL,
                                      `email` VARCHAR(50) NULL,
                                      `pwd` VARCHAR(15) NULL,
                                      `coin` INT NULL,
                                      PRIMARY KEY (`id`)
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
