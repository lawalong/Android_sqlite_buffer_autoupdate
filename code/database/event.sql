BEGIN

insert into source_need_for_update 
(SELECT null,source.*,(select max(version)from source_need_for_update)+1
FROM source
    LEFT JOIN sourcec_snapshot ON (source.id = sourcec_snapshot.id)
WHERE source.price != sourcec_snapshot.price or sourcec_snapshot.id IS NULL);
truncate sourcec_snapshot;
insert into sourcec_snapshot select * from source;


END