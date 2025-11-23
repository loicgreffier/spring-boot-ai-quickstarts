#!/bin/bash

API_BASE_URL="https://thesimpsonsapi.com/api/characters"
OUTPUT_FILE="data.sql"
TOTAL_PAGES=60

echo "-- Characters data" > "$OUTPUT_FILE"

for page in $(seq 1 $TOTAL_PAGES); do
    echo "Fetching page $page..."

    curl -sk "${API_BASE_URL}?page=${page}" | jq -r '
        .results[] |
        .id as $id |
        "INSERT INTO character (id, age, birth_date, gender, name, occupation, portrait_url, status) VALUES (\($id), \(.age // "null"), \(if .birthdate then "'\''" +.birthdate+"'\''" else "null" end), \(if .gender then "'\''"+.gender+"'\''" else "null" end), '\''"+(.name | gsub("'\''"; "'\'''\''"))+"'\'', \(if .occupation then "'\''"+(.occupation | gsub("'\''"; "'\'''\''"))+"'\''" else "null" end), \(if .portrait_path then "'\''https://cdn.thesimpsonsapi.com/500"+.portrait_path+"'\''" else "null" end), \(if .status then "'\''"+.status+"'\''" else "null" end));",
        (if .phrases then .phrases[] | "INSERT INTO phrase (text, character_id) VALUES ('\''"+(.| gsub("'\''"; "'\'''\''"))+"'\'', \($id));" else empty end)
    ' >> "$OUTPUT_FILE"
done

echo "Done! SQL queries have been generated in $OUTPUT_FILE"
