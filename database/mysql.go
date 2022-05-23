package database

import (
	"fmt"
	"funtour/tool"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

var _db *gorm.DB

func init() {
	//配置MySQL连接参数
	username := "czy"  //账号
	password := "Mysql@826525" //密码
	host := "czytgc.com" //数据库地址，可以是Ip或者域名
	port := 3307 //数据库端口
	Dbname := "amusingtourdb" //数据库名
	timeout := "10s" //连接超时，10秒

	//拼接下dsn参数, dsn格式可以参考上面的语法，这里使用Sprintf动态拼接dsn参数，因为一般数据库连接参数，我们都是保存在配置文件里面，需要从配置文件加载参数，然后拼接dsn。
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?charset=utf8&parseTime=True&loc=Local&timeout=%s", username, password, host, port, Dbname, timeout)
	//连接MYSQL, 获得DB类型实例，用于后面的数据库读写操作。
	_db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		tool.Error("Mysql数据库连接失败：DSN=", dsn,";	error=", err.Error())
		panic("连接数据库失败, error=" + err.Error())
	}else {
		tool.Info("Mysql数据库连接成功：", dsn)
	}

	sqlDB, _ := _db.DB()

	sqlDB.SetMaxOpenConns(10)
	sqlDB.SetMaxIdleConns(5)

}

func GetDb() *gorm.DB {
	return _db
}