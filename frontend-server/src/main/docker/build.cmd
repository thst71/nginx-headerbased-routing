@echo off

cd ..\..\..
copy target\nginx*.jar src\main\docker
docker build -t frontend-server src\main\docker