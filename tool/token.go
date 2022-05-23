package tool

import (
	"crypto/rand"
	"crypto/rsa"
	"github.com/golang-jwt/jwt"
	"time"
)

func main() {
	GetToken("")
}

func GetToken(userId string) string{
	token := jwt.NewWithClaims(jwt.SigningMethodRS256, jwt.MapClaims{
		"iss": "funTour",
		"aud": userId,
		"iat": time.Now(),
	})
	privatekey, _ := rsa.GenerateKey(rand.Reader, 1024)
	t, err := token.SignedString(privatekey)
	if err != nil {
		panic(err)
	}
	return t
}