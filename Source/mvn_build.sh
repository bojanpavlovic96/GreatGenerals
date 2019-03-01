#!/bin/bash

echo "entering client model"
cd ./client-model

echo "ready to build"
read
mvn clean install
echo ""
echo "model done"
echo ""

echo "entering client view"
cd ../client-view

echo "ready to build"
read
mvn clean install
echo ""
echo "view done"
echo ""

echo "entering gg server"
cd ../gg-server

echo "ready to build"
read
mvn clean install
echo ""
echo "gg-server done"
echo ""

echo "entering client controller"
cd ../client-controller

echo "ready to build"
read
mvn clean install
echo ""
echo "controller done"
echo ""

echo "entering client application"
cd ../client-application

echo "ready to build"
read
mvn clean install
echo ""
echo "application done"
echo ""
