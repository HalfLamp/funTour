FROM candengbanzhan/maven:1.3 AS build
COPY pom.xml /usr/src/app/pom.xml
WORKDIR /usr/src/app
COPY src /usr/src/app/src
RUN mvn package

FROM openjdk:8
#定义时区
ENV TZ=Asia/Shanghai
ENV JAR public-consumer.jar
#强行创建软连接更改时间,更改时间
RUN ln -sf /usr/share/zoneinfo/$TZ \
    && echo $TZ > /etc/timezone \
    && export LOG_HOME = /app
EXPOSE 8080
WORKDIR /app
COPY --from=build /usr/src/app/target/public-consumer.jar /app/app.jar
ENTRYPOINT ["nohup","java","-jar","/app/app.jar","&"]