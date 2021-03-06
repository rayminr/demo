/*后面需要建成分区表*/
DROP TABLE IF EXISTS T_LOGIN_LOG;
CREATE TABLE T_LOGIN_LOG (
  ID            INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  LOGIN_SRC     VARCHAR(10)     NOT NULL,
  LOGIN_TYPE    VARCHAR(10)     NOT NULL,
  LOGIN_ACCOUNT VARCHAR(200)    NOT NULL,
  LOGIN_PWD     VARCHAR(256)    NOT NULL,
  LOGIN_TIME    TIMESTAMP       NOT NULL  DEFAULT CURRENT_TIMESTAMP,
  LOGIN_IP      VARCHAR(100),
  LOGIN_AGENT   VARCHAR(512)
)
DEFAULT CHARSET =UTF8;

CREATE INDEX IDX_LOGIN_LOG_TC ON T_LOGIN_LOG (LOGIN_TIME, LOGIN_ACCOUNT);
