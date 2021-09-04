#!/usr/bin/env bash

echo "Connecting to remote..."
echo -n "SSH password: " 
read -s password
echo
echo "Creating backup..."
sshpass -p $password ssh root@165.22.80.189 'bash -s' < _create_remote_backup.sh
echo "Downloading backup..."
sshpass -p $password scp root@165.22.80.189:remote_backup.tar.gz .

echo "Deleting remote backup..."
sshpass -p $password ssh root@165.22.80.189 'bash -s' < _remove_remote_backup.sh

echo "Done"