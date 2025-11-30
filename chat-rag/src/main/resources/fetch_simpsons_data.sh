#!/bin/bash

API_BASE_URL="https://thesimpsonsapi.com/api/episodes"
OUTPUT_FILE="episodes.csv"
TOTAL_PAGES=39

echo "id,airdate,episode_number,image_path,name,season,synopsis" > "$OUTPUT_FILE"

for page in $(seq 1 $TOTAL_PAGES); do
    echo "Fetching page $page..."

    curl -sk "${API_BASE_URL}?page=${page}" | jq -r '
        .results[] |
        [.id, .airdate, .episode_number, (if .image_path then "https://cdn.thesimpsonsapi.com"+.image_path else "" end), (.name | gsub("\""; "\"\"") | gsub(","; ",")), .season, (.synopsis | gsub("\""; "\"\"") | gsub("\n"; " "))] |
        @csv
    ' >> "$OUTPUT_FILE"
done

echo "Done! Episodes data has been saved to $OUTPUT_FILE"
