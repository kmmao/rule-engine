#!/bin/bash/
nohup java -jar -server -Xms2024m -Xmx2024m -Xss512k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -Dfile.encoding=UTF-8 rule-engine-web-1.0-SNAPSHOT.jar --server.port=80 &
