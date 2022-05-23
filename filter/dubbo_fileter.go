package filter

import (
	"context"
	"dubbo.apache.org/dubbo-go/v3/common/extension"
	"dubbo.apache.org/dubbo-go/v3/filter"
	"dubbo.apache.org/dubbo-go/v3/protocol"
	"fmt"
	"funtour/tool"
)

func init() {
	extension.SetFilter("traceFileter", NewMyFilter)
}

func NewMyFilter() filter.Filter {
	return &MyFilter{}
}

type MyFilter struct {
}

func (f *MyFilter) Invoke(ctx context.Context, invoker protocol.Invoker, invocation protocol.Invocation) protocol.Result {
	fmt.Println("MyClientFilter Invoke is called, method Name = ", invocation.MethodName())
	tool.TraceId = fmt.Sprint(ctx.Value("traceId"))
	return invoker.Invoke(ctx, invocation)
}

func (f *MyFilter) OnResponse(ctx context.Context, result protocol.Result, invoker protocol.Invoker, protocol protocol.Invocation) protocol.Result {
	tool.Info("响应结果：", result)
	return result
}