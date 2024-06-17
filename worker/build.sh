#!/bin/bash

#setup JAVA environment
#. /zserver/java/set_env.cmd

PROJECT=""
MAINCLASS=""

if [ -n "$1" ]; then
    PROJECT=$1
fi

if [ -n "$2" ]; then
    MAINCLASS=$2
fi

_DEBUG=true
_COMPRESS=true
_MAINCLASS=com.vng.zing.zgroupmediamemoryworker.app.MainApp
_DISTJAR=$PROJECT.jar

#change main class of NB project.properties
sed 's/^main.class=.*/main.class='$_MAINCLASS'/' nbproject/project.properties -i
echo "Change main class: $_MAINCLASS"

ant clean #clean first
rm -f dist/$_DISTJAR
rm -f manifest.mf

_CMD="ant jar -Djavac.debug=$_DEBUG -Djar.compress=$_COMPRESS"
$_CMD
echo Done by build command: $_CMD

#mv dist/ZGroupMediaMemoryWorker.jar dist/$_DISTJAR
echo "Main class of $_DISTJAR: $_MAINCLASS"

#rsync dist/$_DISTJAR root@10.30.58.214:/zserver/java-projects/zalo-server2/sms-activation-code-mvas/dist
# echo "RSYNC " $_DISTJAR " TO /zalo-server2/lbs-http-v2/dist"
