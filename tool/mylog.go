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

func inits() {
	file := "./log/info.log"
	infoOut, err := os.Open(file)
	if err != nil {
		panic(err)
	}
	gulu.Log.SetLevel("info")
	info = gulu.Log.NewLogger(infoOut)

	debugFile := "./log/debug.log"
	debugOut, err := os.Open(debugFile)
	if err != nil {
		panic(err)
	}
	gulu.Log.SetLevel("debug")
	debug = gulu.Log.NewLogger(debugOut)

	errorFile := "./log/debug.log"
	errorOut, err := os.Open(errorFile)
	if err != nil {
		panic(err)
	}
	gulu.Log.SetLevel("error")
	error = gulu.Log.NewLogger(errorOut)
}

func Info(message ...interface{}) {
	v := fmt.Sprintf("traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	info.Info(v)
}

func Debug(message ...interface{}) {
	v := fmt.Sprintf("traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	debug.Info(v)
}

func Error(message ...interface{}) {
	v := fmt.Sprintf("traceId:%s message:%s ", TraceId, fmt.Sprintln(message...))
	error.Info(v)
}
