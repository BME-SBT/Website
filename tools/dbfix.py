import mysql.connector

TABLE = "achievement"
FIELD_NAME = "picture"
FIELD_NUM = 7
ID_NUM = 0
ID_NAME = "id"


def strip_junk(original: str) -> str:
    last_slash = original.rfind("/")
    return original[last_slash + 1 :]


connection = mysql.connector.connect(
    host="localhost", user="root", password="rootpass", database="db_solarboat"
)
cursor = connection.cursor()

cursor.execute(f"SELECT * FROM {TABLE}")
entries = cursor.fetchall()

for entry in entries:
    bad_url = entry[FIELD_NUM]
    fixed = strip_junk(bad_url)
    print(f"{bad_url} -> {fixed}")

    cursor.execute(
        f'UPDATE {TABLE} SET {FIELD_NAME} = "{fixed}" WHERE {ID_NAME} = {entry[0]}'
    )

connection.commit()
