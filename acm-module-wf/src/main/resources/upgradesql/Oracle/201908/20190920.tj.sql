--流程全局接口配置URL
insert into wsd_base_set (ID, BO_CODE, BS_KEY, BS_VALUE, LAST_UPD_TIME, LAST_UPD_USER, CREAT_TIME, CREATOR, LAST_UPD_IP, WSDVER, SORT_NUM)
values (0, 'wf', 'wfListenerConfigure', 'http://127.0.0.1:8765/api/cftask/task', to_date('10-08-2019 20:30:00', 'dd-mm-yyyy hh24:mi:ss'), 1, to_date('10-08-2019 20:30:00', 'dd-mm-yyyy hh24:mi:ss'), 1, '127.0.0.1', 0, null);

