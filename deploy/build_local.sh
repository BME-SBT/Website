#!/usr/bin/env bash

echo "Building backend"
docker build -t sbt_backend:local ../backend

echo "Building frontend"
docker build -t sbt_frontend:local ../frontend

echo "Starting app"
docker compose -f local-compose.yml up
