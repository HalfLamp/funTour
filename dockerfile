FROM candengbanzhan/maven:1.2 AS build
COPY pom.xml /usr/src/app/pom.xml
WORKDIR /usr/src/app
#缓存依赖
RUN mvn dependency:go-offline
COPY src /usr/src/app/src
RUN mvn package

FROM openjdk:8
#定义时区
ENV TZ=Asia/Shanghai
ENV JAR gateway.jar
#强行创建软连接更改时间,更改时间
RUN ln -sf /usr/share/zoneinfo/$TZ \
    && echo $TZ > /etc/timezone
EXPOSE 8080
WORKDIR /app
COPY --from=build /usr/src/app/target/gateway.jar /app/app.jar
ENTRYPOINT ["nohup","java","-jar","/app/app.jar","&"]