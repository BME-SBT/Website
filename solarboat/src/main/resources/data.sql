
# INSERT INTO db_solarboat.member (first_name, last_name, picture) VALUE ("Czibere", "Szilárd", "czibere-szilard.jpg");
# INSERT INTO db_solarboat.team (description_en, description_hu, name_hu, name_en, team_type, leader_id) VALUES ("angol leiras", "magyar leiras", "Csapat 1 neve magyarul","Csapat 1 neve angolul", 1,1),("angol leiras", "magyar leiras", "Csapat 2 neve magyarul","Csapat 2 neve angolul", 2,1), ("angol leiras", "magyar leiras", "Csapat 3 neve magyarul","Csapat 3 neve angolul", 3,1), ("angol leiras", "magyar leiras", "Csapat 4 neve magyarul","Csapat 4 neve angolul", 4,1),("angol leiras", "magyar leiras", "Csapat 5 neve magyarul","Csapat 5 neve angolul", 5,1),("angol leiras", "magyar leiras", "Csapat 6 neve magyarul","Csapat 6 neve angolul", 6,1);
 ALTER TABLE db_solarboat.news CONVERT TO CHARACTER SET utf8 COLLATE utf8_hungarian_ci;
 ALTER TABLE db_solarboat.team CONVERT TO CHARACTER SET utf8 COLLATE utf8_hungarian_ci;
 ALTER TABLE db_solarboat.member CONVERT TO CHARACTER SET utf8 COLLATE utf8_hungarian_ci;
 ALTER TABLE db_solarboat.achievement CONVERT TO CHARACTER SET utf8 COLLATE utf8_hungarian_ci;

#INSERT INTO db_solarboat.roles (name) VALUE ("ROLE_EDITOR");
# ALTER TABLE db_solarboat.news DROP picture;
ALTER TABLE db_solarboat.member MODIFY job_hu NVARCHAR(50) NULL;
# DROP TABLE db_solarboat.team_members;
# DROP TABLE db_solarboat.team;
#  DROP TABLE db_solarboat.news;

SET SQL_SAFE_UPDATES = 0;
UPDATE db_solarboat.sponsor
SET order_number = 2
WHERE group_id = 4;
sET SQL_SAFE_UPDATES = 1;