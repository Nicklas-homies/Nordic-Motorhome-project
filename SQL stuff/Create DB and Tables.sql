SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

DROP SCHEMA IF EXISTS `NMH_company` ;
CREATE SCHEMA IF NOT EXISTS `NMH_company` DEFAULT CHARACTER SET utf8 ;
USE `NMH_company` ;

DROP TABLE IF EXISTS `NMH_company`.`motorhomeType` ;
CREATE TABLE IF NOT EXISTS `NMH_company`.`motorhomeType` (
  `typeId` INT NOT NULL,
  `capacity` INT NOT NULL,
  `price` INT NOT NULL,
  PRIMARY KEY (`typeId`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `NMH_company`.`motorhomes` ;
CREATE TABLE IF NOT EXISTS `NMH_company`.`motorhomes` (
  `motorhomeId` INT NOT NULL AUTO_INCREMENT,
  `brand` VARCHAR(45) NOT NULL,
  `model` VARCHAR(45) NOT NULL,
  `timesUsed` INT NULL,
  `kmDriven` INT NULL,
  `extraPrice` DOUBLE NULL,
  `typeId` INT NOT NULL,
  PRIMARY KEY (`motorhomeId`),
  INDEX `typeId_idx` (`typeId` ASC) VISIBLE,
  CONSTRAINT `typeId`
    FOREIGN KEY (`typeId`)
    REFERENCES `NMH_company`.`motorhomeType` (`typeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DROP TABLE IF EXISTS `NMH_company`.`customers` ;
CREATE TABLE IF NOT EXISTS `NMH_company`.`customers` (
  `customerId` INT NOT NULL AUTO_INCREMENT,
  `cName` VARCHAR(70) NOT NULL,
  `number` INT NOT NULL,
  PRIMARY KEY (`customerId`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `NMH_company`.`custUseMotor` ;
CREATE TABLE IF NOT EXISTS `NMH_company`.`custUseMotor` (
  `rentId` INT NOT NULL AUTO_INCREMENT,
  `startDate` DATE NOT NULL,
  `endDate` DATE NOT NULL,
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

DROP TABLE IF EXISTS `NMH_company`.`season` ;
CREATE TABLE IF NOT EXISTS `NMH_company`.`season` (
  `seasonId` INT NOT NULL,
  `startDate` DATE NOT NULL,
  `endDate` DATE NOT NULL,
  `type` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`seasonId`))
ENGINE = InnoDB;

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

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;