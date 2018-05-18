# Server-Side

## Database
### data table
    1. source_snapshot  (clone source table)
    2. source   (source table, may modify by other services)
    3. source_need_for_update   (include all necessary data and version number)
    4. source_version   (include version number, and update date)

### database event/trigger
    1. check different between 'source' and 'source_snapshot' (e.g. different price or new row)
    2. clean 'source_snapshot'
    3. clone 'source' to 'source_sanpshot'
    4. insert different row to 'source_need_for_update'
    5. (trigger -> source_control)
    
## Web Server
### controller
    1. Listening request
    2. Check cache
    3. Query DB if necessary
    4. Send response







