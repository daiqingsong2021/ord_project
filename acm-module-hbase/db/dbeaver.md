#### phoenix客户端
https://dbeaver.io/

#### Apache Phoenix下载，必须要与wsd-hbase hbase版本号匹配
http://www.apache.org/dyn/closer.lua/phoenix/apache-phoenix-4.14.1-HBase-1.3/bin/apache-phoenix-4.14.1-HBase-1.3-bin.tar.gz

#### 
db/phoenix-4.14.1-HBase-1.3-client.jar替换下，主要添加hbase-site.xml文件

#### dbeaver配置Apache Phoenix驱动
1）Host: 47.92.71.117
2）Edit Driver Settings/Libraries，添加驱动jar包
phoenix-4.14.1-HBase-1.3-client.jar （jar里面必须包含hbase-site.xml文件）
phoenix-4.14.1-HBase-1.3-queryserver.jar
phoenix-4.14.1-HBase-1.3-server.jar
phoenix-4.14.1-HBase-1.3-thin-client.jar
phoenix-core-4.14.1-HBase-1.3.jar

### Window/System32/drivers/etc/hosts或者/etc/hosts
47.92.71.117	hbase

#### init.sql
db/init.sql  初始化acm_cloud.wsd_logger日志表
phoenix语法 https://phoenix.apache.org/language/index.html#create_schema

