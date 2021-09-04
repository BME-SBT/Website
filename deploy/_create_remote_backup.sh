docker exec sb_container /usr/bin/mysqldump -u root --password=rootpass db_solarboat > remote_backup.sql
tar -czf asset_backup.tar.gz /opt/www-data/assets
tar -czf remote_backup.tar.gz remote_backup.sql asset_backup.tar.gz