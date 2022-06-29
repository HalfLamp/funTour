package filters

import (
	"context"
	"encoding/json"
	"fmt"
	"funtour/model"
	"funtour/tool"

	"dubbo.apache.org/dubbo-go/v3/common/constant"
	"dubbo.apache.org/dubbo-go/v3/common/extension"
	"dubbo.apache.org/dubbo-go/v3/filter"
	"dubbo.apache.org/dubbo-go/v3/protocol"
)

func init() {
	extension.SetFilter("traceFilter", NewMyFilter)
}

func NewMyFilter() filter.Filter {
	return &MyFilter{}
}

type MyFilter struct {
}

func (f *MyFilter) Invoke(ctx context.Context, invoker protocol.Invoker, invocation protocol.Invocation) protocol.Result {
	m := ctx.Value(constant.DubboCtxKey("attachment")).(map[string]interface{})
	fmt.Println(m)
	tool.TraceId = fmt.Sprint(m["traceId"])
	tool.Info("dubbo调用方法名称：", invocation.MethodName())
	marshal, _ := json.Marshal(invocation.Arguments())
	tool.Info("方法参数: ", string(marshal))
	return invoker.Invoke(ctx, invocation)
}

func (f *MyFilter) OnResponse(ctx context.Context, result protocol.Result, invoker protocol.Invoker, protocol protocol.Invocation) protocol.Result {
	if result.Result() == nil {
		result.SetResult(model.Error(501, "服务提供者异常：未知错误"))
	}
	res, _ := json.Marshal(result)
	tool.Info("响应结果：", string(res))
	return result
}

// 此方法仅用于打印初始化过滤器日志，并调用filters包下的init方法
func Log() {
	tool.Info("初始化过滤器")
}
