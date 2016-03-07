# --- !Ups

create table "suppliers" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"desc" VARCHAR(254) NOT NULL);

# --- !Downs
;
drop table "suppliers";