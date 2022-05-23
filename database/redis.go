package database

import (
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
