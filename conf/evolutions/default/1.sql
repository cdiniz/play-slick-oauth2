# --- !Ups

create table "accounts" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"email" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"created_at" TIMESTAMP DEFAULT now() NOT NULL,"field5"  BIGINT,"field6"  BIGINT,"field7"  BIGINT,"field8"  BIGINT,"field9"  BIGINT,"field10"  BIGINT,"field11"  BIGINT,"field12"  BIGINT,"field13"  BIGINT,"field14"  BIGINT,"field15"  BIGINT,"field16"  BIGINT,"field17"  BIGINT,"field18"  BIGINT,"field19"  BIGINT,"field20"  BIGINT,"field21"  BIGINT,"field22"  BIGINT,"field23"  BIGINT);

create table "oauth_clients" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"owner_id"  BIGINT NOT NULL,"grant_type" VARCHAR(254) NOT NULL,"client_id" VARCHAR(254) NOT NULL,"client_secret" VARCHAR(254) NOT NULL,"redirect_uri" VARCHAR(254),"created_at" TIMESTAMP DEFAULT now() NOT NULL);
alter table "oauth_clients" add constraint "oauth_client_account_fk" foreign key("owner_id") references "accounts"("id") on update NO ACTION on delete NO ACTION;

create table "oauth_authorization_codes" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"account_id" BIGINT NOT NULL,"oauth_client_id" BIGINT NOT NULL,"code" VARCHAR(254) NOT NULL,"redirect_uri" VARCHAR(254),"created_at" TIMESTAMP DEFAULT now() NOT NULL);
alter table "oauth_authorization_codes" add constraint "oauth_authorization_code_account_fk" foreign key("account_id") references "accounts"("id") on update NO ACTION on delete NO ACTION;
alter table "oauth_authorization_codes" add constraint "oauth_authorization_code_client_fk" foreign key("oauth_client_id") references "oauth_clients"("id") on update NO ACTION on delete NO ACTION;


create table "oauth_access_tokens" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"account_id" BIGINT NOT NULL,"oauth_client_id" BIGINT NOT NULL,"access_token" VARCHAR(254) NOT NULL,"refresh_token" VARCHAR(254) NOT NULL,"created_at" TIMESTAMP DEFAULT now() NOT NULL);
alter table "oauth_access_tokens" add constraint "oauth_access_token_account_fk" foreign key("account_id") references "accounts"("id") on update NO ACTION on delete NO ACTION;
alter table "oauth_access_tokens" add constraint "oauth_access_token_client_fk" foreign key("oauth_client_id") references "oauth_clients"("id") on update NO ACTION on delete NO ACTION;

insert into "accounts"("email", "password", "field5", "field6", "field7", "field8", "field9","field10", "field11", "field12", "field13", "field14", "field15", "field16", "field17", "field18", "field19", "field20", "field21", "field22", "field23") values ('bob@example.com', '48181acd22b3edaebc8a447868a7df7ce629920a', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1); -- password:bob
insert into "accounts"("email", "password", "field5", "field6", "field7", "field8", "field9", "field10", "field11", "field12", "field13", "field14", "field15", "field16", "field17", "field18", "field19", "field20", "field21", "field22", "field23") values ('alice@example.com', '522b276a356bdf39013dfabea2cd43e141ecc9e8', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1); -- password:alice

insert into "oauth_clients"("owner_id", "grant_type", "client_id", "client_secret")
  values (1, 'client_credentials', 'bob_client_id', 'bob_client_secret');
insert into "oauth_clients"("owner_id", "grant_type", "client_id", "client_secret", "redirect_uri")
  values (2, 'authorization_code', 'alice_client_id', 'alice_client_secret', 'http://localhost:3000/callback');
insert into "oauth_clients"("owner_id", "grant_type", "client_id", "client_secret")
  values (2, 'password', 'alice_client_id2', 'alice_client_secret2');

insert into "oauth_authorization_codes"("account_id", "oauth_client_id", "code", "redirect_uri")
    values (1, 2, 'bob_code', 'http://localhost:3000/callback');


# --- !Downs
drop table "oauth_authorization_codes";
drop table "oauth_access_tokens";
drop table "oauth_clients";
drop table "accounts";