package tool

import (
	"fmt"
	"github.com/88250/gulu"
	"os"
)

var info *gulu.Logger
var debug *gulu.Logger
var error *gulu.Logger
var TraceId string

func init() {
	file := "./log/info.log"
	infoOut, err := os.OpenFile(file, os.O_RDWR, 0)
	if err != nil {
		panic(err)
	}
	info = gulu.Log.NewLogger(infoOut)
	info.SetLevel("info")

	debugFile := "./log/debug.log"
	debugOut, err := os.OpenFile(debugFile, os.O_RDWR, 0)
	if err != nil {
		panic(err)
	}
	debug = gulu.Log.NewLogger(debugOut)
	debug.SetLevel("debug")

	errorFile := "./log/debug.log"
	errorOut, err := os.OpenFile(errorFile, os.O_RDWR, 0)
	if err != nil {
		panic(err)
	}
	error = gulu.Log.NewLogger(errorOut)
	error.SetLevel("error")

}

func Info(message ...interface{}) {
	fmt.Printf("info traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	v := fmt.Sprintf("traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	info.Info(v)
}

func Debug(message ...interface{}) {
	fmt.Printf("Debug traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))

	v := fmt.Sprintf("traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	debug.Info(v)
}

func Error(message ...interface{}) {
	fmt.Printf("ERROR! traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	v := fmt.Sprintf("traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	error.Info(v)
}
