#!/bin/bash

nohup java -jar apiLoocaTeste1-1.0-SNAPSHOT-jar-with-dependencies.jar > output.log 2>&1 &
sleep 15
nohup python SolucaoRedes.py > output.log 2>&1 &

