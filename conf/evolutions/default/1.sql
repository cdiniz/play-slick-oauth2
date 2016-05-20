# --- !Ups

create table "suppliers" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"desc" VARCHAR(254) NOT NULL,"created_at" TIMESTAMP DEFAULT now() NOT NULL);

create table "accounts" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"email" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"created_at" TIMESTAMP DEFAULT now() NOT NULL);

create table "oauth_clients" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"owner_id"  BIGINT NOT NULL,"grant_type" VARCHAR(254) NOT NULL,"client_id" VARCHAR(254) NOT NULL,"client_secret" VARCHAR(254) NOT NULL,"redirect_uri" VARCHAR(254),"created_at" TIMESTAMP DEFAULT now() NOT NULL);
alter table "oauth_clients" add constraint "oauth_client_account_fk" foreign key("owner_id") references "accounts"("id") on update NO ACTION on delete NO ACTION;

create table "oauth_authorization_codes" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"account_id" BIGINT NOT NULL,"oauth_client_id" BIGINT NOT NULL,"code" VARCHAR(254) NOT NULL,"redirect_uri" VARCHAR(254),"created_at" TIMESTAMP DEFAULT now() NOT NULL);
alter table "oauth_authorization_codes" add constraint "oauth_authorization_code_account_fk" foreign key("account_id") references "accounts"("id") on update NO ACTION on delete NO ACTION;
alter table "oauth_authorization_codes" add constraint "oauth_authorization_code_client_fk" foreign key("oauth_client_id") references "oauth_clients"("id") on update NO ACTION on delete NO ACTION;


create table "oauth_access_tokens" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"account_id" BIGINT NOT NULL,"oauth_client_id" BIGINT NOT NULL,"access_token" VARCHAR(254) NOT NULL,"refresh_token" VARCHAR(254) NOT NULL,"created_at" TIMESTAMP DEFAULT now() NOT NULL);
alter table "oauth_access_tokens" add constraint "oauth_access_token_account_fk" foreign key("account_id") references "accounts"("id") on update NO ACTION on delete NO ACTION;
alter table "oauth_access_tokens" add constraint "oauth_access_token_client_fk" foreign key("oauth_client_id") references "oauth_clients"("id") on update NO ACTION on delete NO ACTION;


# --- !Downs
drop table "suppliers";
drop table "oauth_authorization_codes";
drop table "oauth_access_tokens";
drop table "oauth_clients";
drop table "accounts";