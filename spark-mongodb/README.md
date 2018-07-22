下载
	https://www.mongodb.com/download-center
	选取社区版，找历史版本
	spark 版本对比
	https://docs.mongodb.com/spark-connector/current/
mongodb
	mkdir /opt/mongodb
	cd /opt/mongodb
	wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-3.2.20.tgz
	或是 
	curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-3.2.20.tgz
    以下为3.4 3.6版本
    curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-3.4.16.tgz
    curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-3.6.6.tgz
==========================================================================
####################################################################################################################
## 单机安装mongodb
####################################################################################################################
	参考：https://www.runoob.com/mongodb/mongodb-linux-install.html
	
	1， tar xvf mongodb-linux-x86_64-rhel70-3.2.20.tgz
		ln -s /opt/mongodb/mongodb-linux-x86_64-rhel70-3.2.20 /usr/local/mongodb
		echo 'MONGODB_HOME=/usr/local/mongodb' >> ~/.bashrc
		echo 'PATH=$PATH:$MONGODB_HOME/bin' >> ~/.bashrc
		source ~/.bashrc
		检查 which mongod
	
	2,  配置
		mkdir ${MONGODB_HOME}/db ${MONGODB_HOME}/conf ${MONGODB_HOME}/logs
		
		配置文件如下：
		[root@ht1.r1.n11 mongodb]# cat ${MONGODB_HOME}/conf/mongodb.conf 
		dbpath=/usr/local/mongodb/db
		logpath=/usr/local/mongodb/logs/mongodb.log
		port=27017
		fork=true
	3,  启动
		mongod -f ${MONGODB_HOME}/conf/mongodb.conf
		检查 ps -ef|grep mongod

	4, 使用
		mongo
		> 2+2
		4
		> db.runoob.insert({x:10})
		WriteResult({ "nInserted" : 1 })
		> db.runoob.find()
		{ "_id" : ObjectId("5b53ec0909621d565dcb466f"), "x" : 10 }
		> 

	备注： 该命令启动可以通过网页查看
		mongod -f ${MONGODB_HOME}/conf/mongodb.conf --rest
		http://192.168.1.XXX:28017/  #其使用具体ip或是 localhost
	
####################################################################################################################
## mongodb SQL参考：
####################################################################################################################
	# mongo
	> show dbs;
	> use wotung;	# 创建了数据库 wotung
	> db
	> show dbs  	# 查看所有数据库
	> db.wotung.insert({"name":"和同软件"})
	> db.wotung.insert({engineer: [{'name': '田圃森',}, {'name': '陈溪',}] } )
	> show dbs
	> db.wotung.find();  # 查询数据库
	> db.wotung.find().pretty();  
	> db.wotung.findOne();  
	> db.wotung.count(); # 查询数据库条数
	
	# 在数据库下创建集合
	> # 创建集合 db.createCollection(name, options)
	> db.createCollection("wotungSet") # "wotung"的话会报已存在的错误  创建集合。当你插入一些文档时，MongoDB 会自动创建集合
	> show collections;
	> db.wotungSet.insert({"公司":"和同软件"});
	> db.wotungSet.find();
	
	> use wotung
	> db.dropDatabase()  # 删除数据库 wotung
	> show collections    
	> db.wutongSet.drop()   
	
	# 直接insert默认使用的是集群
	> document = ({"a":"b", "1":{"x":"y"} } ); # 标准的json串
	> db.collections.insert(document)
	> show collections;

mongodb集群环境
参考: https://blog.csdn.net/wangshuang1631/article/details/53857319
	https://blog.csdn.net/xiaomage510/article/details/70174056
	https://www.cnblogs.com/littleatp/p/8563273.html
####################################################################################################################
## mongodb 副本集 Replica Set
####################################################################################################################
	在集群机器上分别执行以下1-3步（与单机安装类似，只有3 启动时添加一个启动参数 --replSet ）
	以 192.168.1.11;		 192.168.1.12;	 	192.168.1.13 三台机器部署为例
	分别在三台机器上执行 1-3步
	1， tar xvf mongodb-linux-x86_64-rhel70-3.2.20.tgz
		ln -s /opt/mongodb/mongodb-linux-x86_64-rhel70-3.2.20 /usr/local/mongodb
		echo 'MONGODB_HOME=/usr/local/mongodb' >> ~/.bashrc
		echo 'PATH=$PATH:$MONGODB_HOME/bin' >> ~/.bashrc
		source ~/.bashrc
		检查 which mongod
	
	2,  配置
		mkdir ${MONGODB_HOME}/db ${MONGODB_HOME}/conf ${MONGODB_HOME}/logs
		
		配置文件如下：
		[root@ht1.r1.n11 mongodb]# cat ${MONGODB_HOME}/conf/mongodb.conf 
		dbpath=/usr/local/mongodb/db
		logpath=/usr/local/mongodb/logs/mongodb.log
		port=27017
		fork=true

	3,  启动
		mongod -f ${MONGODB_HOME}/conf/mongodb.conf --replSet wotungSet

	4, 配置副本集
		在其中一台机器上执行  _id需要跟启动中参数相同
	    mongo -port 27017 或是 mongo
		> use admin     # switched to db admin
		> cfg = {	
			_id:"wotungSet", 
			members:[
				{_id:0, host:'192.168.1.11:27017'},
				{_id:1, host:'192.168.1.12:27017'}, 
				{_id:2, host:'192.168.1.13:27017'}
				]
			};
		> rs.initiate(cfg);    # { "ok" : 1 }
		wotungSet:OTHER> rs.status()               
		wotungSet:PRIMAR> db.isMaster();
		
		执行完后，在另外2台机器上可以看以到提示符不一样
		mongo
		wotungSet:SECONDARY> db.isMaster();
		
		怎么mongodb://192.168.1.XXX 连如何确保连接的是master没有做过了解， 只能在mongodb master这台机器上。
		在三台机器上分别执行 mongo 可以看到提示符不一样。 
		执行语句要在PRIMAR这台上执行
		
####################################################################################################################
## mongodb 分片
####################################################################################################################
	后续补充
####################################################################################################################
## mongodb 主备 aster-Slaver  官方已经不推荐这种方式
####################################################################################################################
	不做介绍

####################################################################################################################
## spark mongdb
####################################################################################################################
参考： https://blog.csdn.net/soul_code/article/details/78523140
	https://blog.csdn.net/chenguohong88/article/details/78044215

####################################################################################################################
## spark 环境开发参考：https://www.cnblogs.com/wonglu/p/5901356.html
####################################################################################################################

集群
centos7
mongodb  3.2.20
机器 192.168.1.11-13
1, Replica Set
	见 "mongodb 副本集 Replica Set" 这段
		
2, spark使用yarn来运行配置文档如下
[root@ht1.r1.n11 ~]# cat /usr/hadoop-parafs/spark-2.0.1/conf/spark-defaults.conf
spark.master                     yarn
spark.executor.memory            1G
spark.executor.cores             1

3, 启动yarn
# start-yarn.sh
# spark-shell   \
	--conf "spark.mongodb.input.uri=mongodb://192.168.1.11:27017/dbName.collectionName?authSource=admin" \
	--conf "spark.mongodb.output.uri=mongodb://192.168.1.11:27017/dbName.collectionName?authSource=admin" \
	--packages org.mongodb.spark:mongo-spark-connector_2.11:2.1.0

可以看到spark正常成功启动
注意：
	启动spark 注意此处mongodb连接需要使用 Master的ip
	需要注意 packages org.mongodb.spark:引用版本号。  可以查看 /root/.ivy2/jars/org.mongodb*


本地代码可见
	maven工程，导入idea中
	分别运行ReadMongodb， WriteMongodb
	登陆查看数据库数据变更

### 这里暂时没有验证
### 打包在 spark环境执行
# spark-submit --class com.wotung.ReadMongodb --executor-memory 1G --total-executor-cores 2 /opt/mongdb.jar