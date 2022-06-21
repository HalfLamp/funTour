package tool

import (
	"crypto/rand"
	"fmt"
	"io"
)

const (
	CACHE_USER_TOKEN   = "FUNTOUR:USER:TOKEN_"
	CACHE_SYSTEM_PARAM = "FUNTOUR:SYSTEM:PARAM_"
)

func GetUUID() string {
	b := make([]byte, 16)
	io.ReadFull(rand.Reader, b)
	b[6] = (b[6] & 0x0f) | 0x40
	b[8] = (b[8] & 0x3f) | 0x80
	return fmt.Sprintf("%x-%x-%x-%x-%x", b[0:4], b[4:6], b[6:8], b[8:10], b[10:])
}

func GetTokenCacheKey(userId string) string {
	return CACHE_USER_TOKEN + userId
}

func CatchPanic() {
	if err := recover(); err != nil {
		Error("异常捕获：未知异常：", err)
	}
}
