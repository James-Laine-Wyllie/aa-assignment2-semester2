#!/bin/bash

#run the compiled code with the sample inputs provided with skeleton code
java -cp .:samplePlayer.jar BattleshipMain -v -l log.txt ./../config.txt ./../loc1.txt ./../loc2.txt sample sample
