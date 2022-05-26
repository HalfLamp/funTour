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

func (*UserService) Register(user *User) (*Result, error) {
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

//TODO 设置缓存
func (*UserService) SetCache(key string, value string) (*Result, error) {
	return Ok(), nil
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

// 更改用户信息
func (*UserService) ChangeUserMessage(user *User) (*Result, error) {
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
		tool.Info("更新数据条数小于1条!!!")
		return Error(502, "更新数据条数小于1"), err
	}

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
	}
}
