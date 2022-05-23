package model

import (
	"encoding/json"
	"funtour/tool"
)

type Result struct {
	Code   int16  `json:"code"`
	ErrMes string `json:"errMes"`
	Data   string `json:"data"`
}

func Ok() *Result {
	r := &Result{
		Code:   200,
		ErrMes: "",
		Data:   "",
	}
	tool.Info("响应结果:", r)
	return r
}

func Error(code int16, errorMes string) *Result {
	r := &Result{
		Code:   code,
		ErrMes: errorMes,
		Data:   "",
	}
	tool.Info("响应结果:", r)
	return r
}

func ToData(result interface{}) *Result {
	data, _ := json.Marshal(&result)
	r := &Result{
		Code:   200,
		ErrMes: "",
		Data:   string(data),
	}
	tool.Info("响应结果:", result)
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
	tool.Info("响应结果:", result)
	return r
}
