#!/bin/bash

# run complied Java with custom players (sample | random | greedy | prob)
# usage ./runPlayers <sample> <rand> -- or any combination of players

java -cp .:samplePlayer.jar BattleshipMain -v -l log.txt ./../config.txt ./../loc1.txt ./../loc1.txt $1 $2
