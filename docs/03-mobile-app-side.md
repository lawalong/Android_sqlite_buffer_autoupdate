# Mobile-App-Side

## File location
    Normally, I would like to put the SQLite file copy to "assets" folder in your project app/src/main.
    And copy one into App source folder after installing

## Function
### CheckDB
    Check database before App starting and do copy at the first time.
### UpdateDB
    1. Send local DB version to romote server.
    2. Get server response, it should be like "no need for update" or JSON contains all data that should be update.
    3. Update local SQLite and version number.
    





