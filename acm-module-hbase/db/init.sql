
#database
CREATE SCHEMA IF NOT EXISTS acm_cloud;

#table
create table acm_cloud.wsd_logger(id varchar primary key, module varchar, title varchar, content varchar, creat_time date, creator varchar, ipaddress varchar, user_type varchar, status varchar, error varchar);

#index
CREATE INDEX MODULE_CREATOR_CREATTIME_IDX ON acm_cloud.wsd_logger(module,creator,creat_time);

