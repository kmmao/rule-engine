#!/bin/bash/
nohup java -jar -server -Xms512m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -Dfile.encoding=UTF-8 rule-engine-monitor-1.0-SNAPSHOT.jar --server.port=8011 &
