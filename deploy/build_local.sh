#!/usr/bin/env bash

echo "Checking for certificates..."
if [ ! -f "cert/privkey.pem" ]; then
    echo "Certificate does not exists"
    echo "Creating NGINX certs..."
    mkdir -p cert
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout "cert/privkey.key" -out "cert/fullchain.pem" -subj "/CN=HU/CN=localhost"
fi

echo "Building backend"
docker build -t sbt_backend:local ../backend

echo "Building frontend"
docker build -t sbt_frontend:local ../frontend

echo "Starting app"
docker compose -f local-compose.yml up
