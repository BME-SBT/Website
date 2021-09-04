#!/usr/bin/env bash

# Settings

DB_NAME=deploy_mysql_1
ASSET_DIR=

echo "NOTE: The services must be running when doing the sync"

echo "Syncing..."

./create_backup.sh

mkdir -p _backup_sync

echo "Extracting..."
tar -xzf remote_backup.tar.gz -C _backup_sync
mkdir -p _backup_sync/assets_extract
tar -xzf _backup_sync/asset_backup.tar.gz -C _backup_sync/assets_extract

echo "Syncing database..."
docker exec $DB_NAME /usr/bin/mysql -u root --password=rootpass < _backup_sync/remote_backup.sql

echo "Syncing assets..."
cp -R -f _backup_sync/assets_extract/opt/www-data/assets data/volume_assets/

echo "Removing temporary files"

rm -rf _backup_sync
rm remote_backup.tar.gz

echo "Done"