-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema NMH_company
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `NMH_company` ;

-- -----------------------------------------------------
-- Schema NMH_company
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `NMH_company` DEFAULT CHARACTER SET utf8 ;
USE `NMH_company` ;

-- -----------------------------------------------------
-- Table `NMH_company`.`motorhometype`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NMH_company`.`motorhometype` ;

CREATE TABLE IF NOT EXISTS `NMH_company`.`motorhometype` (
  `typeId` INT NOT NULL,
  `capacity` INT NOT NULL,
  `price` INT NOT NULL,
  PRIMARY KEY (`typeId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `NMH_company`.`motorhomes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NMH_company`.`motorhomes` ;

CREATE TABLE IF NOT EXISTS `NMH_company`.`motorhomes` (
  `motorhomeId` INT NOT NULL AUTO_INCREMENT,
  `brand` VARCHAR(45) NOT NULL,
  `model` VARCHAR(45) NOT NULL,
  `timesUsed` INT NULL,
  `kmDriven` INT NULL,
  `activeState` INT DEFAULT 0 NOT NULL,
  `typeId` INT NOT NULL,
  PRIMARY KEY (`motorhomeId`),
  INDEX `typeId_idx` (`typeId` ASC) VISIBLE,
  CONSTRAINT `typeId`
    FOREIGN KEY (`typeId`)
    REFERENCES `NMH_company`.`motorhometype` (`typeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `NMH_company`.`customers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NMH_company`.`customers` ;

CREATE TABLE IF NOT EXISTS `NMH_company`.`customers` (
  `customerId` INT NOT NULL AUTO_INCREMENT,
  `cName` VARCHAR(70) NOT NULL,
  `number` INT NOT NULL,
  PRIMARY KEY (`customerId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `NMH_company`.`custUseMotor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NMH_company`.`custUseMotor` ;

CREATE TABLE IF NOT EXISTS `NMH_company`.`custUseMotor` (
  `rentId` INT NOT NULL AUTO_INCREMENT,
  `startDate` DATE NOT NULL,
  `endDate` DATE NOT NULL,
  `extraPrice` DOUBLE NULL,
  `customerId` INT NOT NULL,
  `motorhomeId` INT NOT NULL,
  PRIMARY KEY (`rentId`),
  INDEX `customerId_idx` (`customerId` ASC) VISIBLE,
  INDEX `motorhomeId_idx` (`motorhomeId` ASC) VISIBLE,
  CONSTRAINT `customerId`
    FOREIGN KEY (`customerId`)
    REFERENCES `NMH_company`.`customers` (`customerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `motorhomeId`
    FOREIGN KEY (`motorhomeId`)
    REFERENCES `NMH_company`.`motorhomes` (`motorhomeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `NMH_company`.`season`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NMH_company`.`season` ;

CREATE TABLE IF NOT EXISTS `NMH_company`.`season` (
  `seasonId` INT NOT NULL AUTO_INCREMENT,
  `startMonth` INT NOT NULL,
  `endMonth` INT NOT NULL,
  `seasonTypeId` INT NOT NULL,
  PRIMARY KEY (`seasonId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `NMH_company`.`damages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NMH_company`.`damages` ;

CREATE TABLE IF NOT EXISTS `NMH_company`.`damages` (
  `damageId` INT NOT NULL AUTO_INCREMENT,
  `damageDesc` VARCHAR(500) NULL,
  `motorhomeDmgId` INT NOT NULL,
  PRIMARY KEY (`damageId`),
  INDEX `motorhomeId_idx` (`motorhomeDmgId` ASC) VISIBLE,
  CONSTRAINT `motorhomeDmgId`
    FOREIGN KEY (`motorhomeDmgId`)
    REFERENCES `NMH_company`.`motorhomes` (`motorhomeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO motorhometype (typeid, capacity, price) VALUES (1, 2, 400.00);
INSERT INTO motorhometype (typeid, capacity, price) VALUES (2, 3, 600.00);
INSERT INTO motorhometype (typeid, capacity, price) VALUES (3, 4, 800.00);
INSERT INTO motorhometype (typeid, capacity, price) VALUES (4, 5, 1000.00);
INSERT INTO motorhometype (typeid, capacity, price) VALUES (5, 6, 1200.00);
INSERT INTO motorhometype (typeid, capacity, price) VALUES (6, 8, 1600.00);
INSERT INTO motorhometype (typeid, capacity, price) VALUES (7, 10, 2000.00);
INSERT INTO motorhometype (typeid, capacity, price) VALUES (8, 12, 2400.00);

INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Skoda','citiGo',0,0,1);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Skoda','citiGo',0,0,1);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Skoda','citiGo',0,0,1);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Skoda','citiGo',0,0,1);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Volvo','45Small',0,0,2);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Volvo','45Small',0,0,2);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Volvo','45Small',0,0,2);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Ford','Ka',0,0,1);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Ford','Ka',0,0,1);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Mercedes','VeryBig',0,0,3);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Mercedes','VeryBig',0,0,3);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Mercedes','VeryBig',0,0,3);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Skoda','citiGoBig',0,0,4);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Skoda','citiGoBig',0,0,4);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Jerba','J-Pod',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Jerba','J-Pod',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Jerba','J-Pod',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Jerba','J-Pod',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Rolling homes','Vito',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Rolling homes','Vito',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Rolling homes','Vito',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Rolling homes','Vito',0,0,5);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Fiat','Alko Esential',0,0,6);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Fiat','Alko Esential',0,0,6);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Fiat','Alko Esential',0,0,6);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Concorde','Atego',0,0,6);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Concorde','Atego',0,0,6);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Globetrotter','XLi 7850',0,0,7);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Globetrotter','XLi 7850',0,0,7);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Globetrotter','XLi 7850',0,0,7);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Concorde','Atego Plus',0,0,8);
INSERT INTO motorhomes (brand, model, timesUsed, kmDriven, typeId) VALUES ('Concorde','Atego Plus',0,0,8);

INSERT INTO customers (cName, number) VALUES ('John Smith', '12345678');
INSERT INTO customers (cName, number) VALUES ('Jane Doe', '90123456');
INSERT INTO customers (cName, number) VALUES ('John Doe', '78901234');

INSERT INTO season (startMonth, endMonth, seasonTypeId) VALUES (1, 2, 1);
INSERT INTO season (startMonth, endMonth, seasonTypeId) VALUES (10, 12, 1);
INSERT INTO season (startMonth, endMonth, seasonTypeId) VALUES (3, 5, 2);
INSERT INTO season (startMonth, endMonth, seasonTypeId) VALUES (6, 9, 3);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
