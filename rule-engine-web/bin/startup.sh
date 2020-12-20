#!/bin/bash/
nohup java -jar -server -Xms1024m -Xmx1024m -Xss512k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -Dfile.encoding=UTF-8 rule-engine-web-1.0-SNAPSHOT.jar --server.port=80 &
