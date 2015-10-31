#!/bin/sh

./release.sh

/usr/apache/apache-tomcat-7.0.47/bin/catalina.sh stop

rm -rf /usr/apache/apache-tomcat-7.0.47/webapps/mytime*
cp build/deploy/*.war /usr/apache/apache-tomcat-7.0.47/webapps

/usr/apache/apache-tomcat-7.0.47/bin/catalina.sh start
tail -fn 1000   /usr/apache/apache-tomcat-7.0.47/logs/catalina.out


