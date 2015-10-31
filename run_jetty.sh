#!/bin/bash
echo "log home:/tmp/logs/$(basename $(pwd))"

app_log_home="/tmp/logs/$(basename $(pwd))"

if [ ! -d "${app_log_home}" ]; then
    mkdir -p "${app_log_home}"
fi

export GRADLE_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=27002"
export root_dir=$0/..
gradle -p $root_dir jettyRun -Dprj=$(basename $(pwd)) -Dapp.name=$(basename $(pwd)) -Dapp.log.home=${app_log_home} -DhttpPort=27000 -DstopPort=27001
