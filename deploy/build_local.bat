@echo off

REM Check for docker
WHERE docker

IF %ERRORLEVEL% NEQ 0 GOTO NO_DOCKER

echo "Building backend"
docker build -t sbt_backend:local ..\backend

echo "Building frontend"
docker build -t sbt_frontend:local ..\frontend

echo "Starting app"
docker compose -f local-compose.yml up


:NO_DOCKER
echo Please install docker from https://www.docker.com/get-started