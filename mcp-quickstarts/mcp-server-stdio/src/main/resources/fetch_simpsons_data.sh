#!/bin/bash

API_BASE_URL="https://thesimpsonsapi.com/api/episodes"
OUTPUT_FILE="data.sql"
TOTAL_PAGES=39

echo "-- Episodes data" > "$OUTPUT_FILE"

for page in $(seq 1 $TOTAL_PAGES); do
    echo "Fetching page $page..."

    curl -sk "${API_BASE_URL}?page=${page}" | jq -r '
        .results[] |
        "INSERT INTO episode (id, airdate, episode_number, image_path, name, season, synopsis) VALUES (\(.id), \(if .airdate and .airdate != "" then "'\''"+.airdate+"'\''" else "null" end), \(.episode_number // "null"), \(if .image_path then "'\''https://cdn.thesimpsonsapi.com"+.image_path+"'\''" else "null" end), '\''"+(.name | gsub("'\''"; "'\'''\''"))+"'\'', \(.season // "null"), \(if .synopsis and .synopsis != "" then "'\''"+(.synopsis | gsub("'\''"; "'\'''\''"))+"'\''" else "null" end));"
    ' >> "$OUTPUT_FILE"
done

echo "Done! SQL queries have been generated in $OUTPUT_FILE"
