# 打包依赖阶段使用golang作为基础镜像
FROM golang:alpine as builder

# 启用go module
ENV GO111MODULE=on \
    CGO_ENABLE=0 \
    GOOS=linux \
    GOARCH=amd64 \
    GOPROXY="https://goproxy.cn,direct"

WORKDIR /app
COPY . /app

WORKDIR /app/cmd
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build .

FROM scratch

WORKDIR /app

COPY --from=builder /app /app

WORKDIR /app/cmd

EXPOSE 80

ENTRYPOINT ["./cmd"]


