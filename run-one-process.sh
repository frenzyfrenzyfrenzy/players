#!/bin/bash

mvn clean package
java -jar target/players-1.0-SNAPSHOT-spring-boot.jar -mode=standalone