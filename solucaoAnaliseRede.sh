#!/bin/bash

nohup java -jar apiLoocaTeste1-1.0-SNAPSHOT-jar-with-dependencies.jar > output.log 2>&1 &
sleep 5
nohup python seu_script.py > output.log 2>&1 &
python SolucaoRedes.py

