package service

import (
	"context"
	"fmt"
	"funtour/database"
	. "funtour/model"
	"funtour/query"
	"funtour/tool"
)

type UserService struct {
}

func (*UserService) Login(account string, password string) (*Result, error) {
	tool.Info("请求参数:", account, password)

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

func (*UserService) Register(user User) (*Result, error) {
	if len(user.Password) == 0 || (len(user.Email) == 0 && len(user.Phone) == 0) {
		return Error(202, "信息不完整"), nil
	}
	userid := tool.GetUUID()
	user.UserID = userid

	u := query.Use(database.GetDb()).User
	err := u.WithContext(context.TODO()).Select(u.ALL).Create(&user)
	if err != nil {
		return Error(200, "注册失败"), nil
	}

	return ToDataWithName("user", user), nil
}

// 校验token有效期并刷新有效期
func (*UserService) CheckToken(key string) (*Result, error) {
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

// 获取系统参数
func (*UserService) GetSystemParams(key string) (*Result, error) {
	// 从缓存获取参数
	cache, err := database.GetSystemCache(key)
	if err != nil {
		return Error(501, "获取参数出错"), err
	}
	return ToData(cache), nil
}

// MethodMapper 定义方法名映射，从 Go 的方法名映射到 Java 小写方法名，只有 dubbo 协议服务接口才需要使用
func (s *UserService) MethodMapper() map[string]string {
	return map[string]string{
		"Login":           "login",
		"Register":        "register",
		"CheckToken":      "checkToken",
		"GetSystemParams": "getSystemParams",
	}
}
