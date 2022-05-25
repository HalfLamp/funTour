package database

import (
	"context"
	"funtour/query"
	"funtour/tool"
	"github.com/garyburd/redigo/redis"
)

var pool *redis.Pool

func init() {
	pool = &redis.Pool{
		MaxIdle:     8,   //最大空闲链接数量
		MaxActive:   0,   //表示和数据库最大链接数，0表示，并发不限制数量
		IdleTimeout: 100, //最大空闲时间，用完链接后100秒后就回收到链接池
		Dial: func() (redis.Conn, error) { //初始化链接池的代码
			dial, err := redis.Dial("tcp", "czytgc.com:6379")
			dial.Do("AUTH", "caizhiyuan")
			if err == nil {
				tool.Info("redis连接并认证成功：czytgc.com:6379")
			}
			return dial, err
		},
	}
}

func GetConnect() redis.Conn {
	return pool.Get()
}

// 查询系统参数缓存
func GetSystemCache(key string) (string, error) {
	conn := GetConnect()

	data, err := conn.Do("get", key)
	if err != nil {
		tool.Error("获取缓存出错", err)
		return "", err
	}

	var reply string
	// 若缓存中有值
	if data != nil {
		reply, err = redis.String(data, err)
		if err != nil {
			tool.Error("转化字符串出错", err)
			return "", err
		}
		return reply, nil
	}

	// 若缓存中无值
	db := GetDb()
	param := query.Use(db).Param
	result, err := param.WithContext(context.TODO()).
		Where(param.Key.Eq(key)).
		First()
	if err != nil {
		tool.Error("查询数据库出错", err)
		return "", err
	}
	// 设置缓存
	conn.Do("setex", key, 43200, result.Value)
	reply, _ = redis.String(conn.Do("get", key))
	return reply, nil
}
