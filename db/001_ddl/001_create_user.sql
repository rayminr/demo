DROP TABLE IF EXISTS T_USER;
CREATE TABLE T_USER (
  ID              INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  NAME            VARCHAR(100)    NOT NULL,
  PWD             VARCHAR(256)    NOT NULL,
  NICK_NAME       VARCHAR(100),
  MOBILE          VARCHAR(20),
  EMAIL           VARCHAR(200),
  TYPE            VARCHAR(10),
  SRC             VARCHAR(10),
  STATUS          CHAR(10),
  SEX             CHAR(1),
  BIRTHDAY        VARCHAR(10),
  PIC_URL         VARCHAR(200),
  LAST_LOGIN_TIME TIMESTAMP,
  USER_IP         VARCHAR(100),
  CREATED_AT      TIMESTAMP       NOT NULL,
  CREATED_BY      VARCHAR(20)     NOT NULL,
  UPDATED_AT      TIMESTAMP,
  UPDATED_BY      VARCHAR(20)
)
DEFAULT CHARSET =UTF8;

CREATE UNIQUE INDEX UK_USER_NAME ON T_USER (NAME);
CREATE UNIQUE INDEX UK_USER_MOBILE ON T_USER (MOBILE);
CREATE UNIQUE INDEX UK_USER_EMAIL ON T_USER (EMAIL);