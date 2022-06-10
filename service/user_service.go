package service

import (
	"context"
	"encoding/json"
	"fmt"
	"funtour/database"
	. "funtour/model"
	"funtour/query"
	"funtour/tool"
	"github.com/garyburd/redigo/redis"
	"github.com/zouyx/agollo/v3/component/log"
	"strconv"
)

type UserService struct {
}

func (*UserService) Login(account string, password string) (*Result, error) {
	defer tool.CatchPanic()

	u := query.Use(database.GetDb()).User
	user, _ := u.WithContext(context.TODO()).
		Where(u.Phone.Eq(account), u.Password.Eq(password)).
		Or(u.Email.Eq(account), u.Password.Eq(password)).
		First()
	if user != nil {
		return ToDataWithName("user", user), nil
	} else {
		return Error(200, "账号或密码错误"), nil
	}
}

func (*UserService) Register(user *User) (*Result, error) {
	defer tool.CatchPanic()

	if len(user.Password) == 0 || (len(user.Email) == 0 && len(user.Phone) == 0) {
		return Error(202, "信息不完整"), nil
	}
	userid := tool.GetUUID()
	user.UserId = userid

	u := query.Use(database.GetDb()).User
	err := u.WithContext(context.TODO()).Select(u.ALL).Create(user)
	if err != nil {
		return Error(200, "注册失败"), nil
	}

	return ToDataWithName("user", user), nil
}

// 校验token有效期并刷新有效期
func (*UserService) CheckToken(key string) (*Result, error) {
	defer tool.CatchPanic()

	redis := database.GetConnect()
	reply, _ := redis.Do("get", key)
	result := fmt.Sprintln(reply)
	if result == "" {
		return ToData(""), nil
	} else {
		// 设置过期时间为15分钟
		redis.Do("expire", key, 900)
		return ToData(result), nil
	}
}

// 设置缓存,存在则刷新时间，不存在则新建
func (*UserService) SetCache(key string, value string, time string) (*Result, error) {
	defer tool.CatchPanic()

	key = tool.CACHE_USER_TOKEN + key
	time_int, err := strconv.ParseInt(time, 10, 64)
	if err != nil {
		log.Error("字符串转int64出错", err)
		return Error(206, "时间字段为只包含数字的字符串"), err
	}

	_, err = database.GetConnect().Do("setex", key, time_int, value)
	if err != nil {
		tool.Error("更新redis失败", err)
		return Error(506, "操作redis失败"), err
	}

	return Ok(), nil
}

// 获取系统参数
func (*UserService) GetSystemParams(key string) (*Result, error) {
	defer tool.CatchPanic()

	// 从缓存获取参数
	cache, err := database.GetSystemCache(key)
	if err != nil {
		return Error(501, "获取参数出错"), err
	}
	return ToData(cache), nil
}

// 更改用户信息
func (*UserService) ChangeUserMessage(user *User) (*Result, error) {
	defer tool.CatchPanic()

	if user.UserId == "" {
		return Error(301, "用户id不正确"), nil
	}

	query := query.Use(database.GetDb()).User
	update, err := query.WithContext(context.TODO()).Where(query.UserID.Eq(user.UserId)).Updates(user)
	if err != nil {
		tool.Error("更新用户数据失败", err)
		return Error(503, "更新用户数据报错"), err
	}
	if update.RowsAffected < 1 {
		tool.Info("没有数据被更新")
		return Error(200, "无被更新的数据"), err
	}

	// 更新redis中的用户数据
	userInfo, err := query.WithContext(context.TODO()).Where(query.UserID.Eq(user.UserId)).First()
	key := tool.GetTokenCacheKey(user.UserId)
	// 模糊查询key
	keys, err := redis.Strings(database.GetConnect().Do("keys", key+"*"))
	if err != nil {
		log.Error("更新缓存的用户信息失败:", err)
	}else if len(key) < 1 {
		log.Info("无用户登录信息的缓存，可能是登陆超时或非法操作")
	}
	// 更新
	value, _ := json.Marshal(userInfo)
	database.GetConnect().Do("setex", keys[0], 60*30, string(value))

	return Ok(), nil
}

// MethodMapper 定义方法名映射，从 Go 的方法名映射到 Java 小写方法名，只有 dubbo 协议服务接口才需要使用
func (s *UserService) MethodMapper() map[string]string {
	return map[string]string{
		"Login":             "login",
		"Register":          "register",
		"CheckToken":        "checkToken",
		"GetSystemParams":   "getSystemParams",
		"ChangeUserMessage": "changeUserMessage",
		"SetCache":          "setCache",
	}
}

