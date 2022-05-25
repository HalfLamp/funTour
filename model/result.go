package model

import (
	"encoding/json"
	"fmt"
	"funtour/tool"
	hessian "github.com/apache/dubbo-go-hessian2"
)

type Result struct {
	Code   int32  `json:"code"`
	ErrMes string `json:"errMes"`
	Data   string `json:"data"`
}

type Data struct {
	result string `json:"data"`
}

func (d *Data) JavaClassName() string {
	return "java.lang.Object"
}

func init() {
	hessian.RegisterPOJO(&Result{})
}

//hessian序列化实现接口
func (u *Result) JavaClassName() string {
	return "com.cai.funtour.pojo.Result"
}

func Ok() *Result {
	r := &Result{
		Code:   200,
		ErrMes: "",
		Data:   "",
	}
	tool.Info("封装结果:", r)
	return r
}

func Error(code int32, errorMes string) *Result {
	r := &Result{
		Code:   code,
		ErrMes: errorMes,
		Data:   "",
	}
	tool.Info("封装结果:", r)
	return r
}

func ToData(result interface{}) *Result {
	var data string
	switch result.(type) {
	case string:
		data = fmt.Sprintln(result)
	default:
		result, _ := json.Marshal(&result)
		data = string(result)
	}

	r := &Result{
		Code:   200,
		ErrMes: "",
		Data:   data,
	}
	tool.Info("封装结果:", result)
	return r
}

func ToDataWithName(name string, result interface{}) *Result {
	mapData := make(map[string]*interface{})
	mapData[name] = &result
	data, _ := json.Marshal(&mapData)
	r := &Result{
		Code:   200,
		ErrMes: "",
		Data:   string(data),
	}
	tool.Info("封装结果:", result)
	return r
}