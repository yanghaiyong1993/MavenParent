# 添加 java 8 镜像来源
FROM java:8

# 添加参数
ARG JAR_FILE

# 添加 SpringBoot 包
ADD target/${JAR_FILE} app.jar

# 暴露端口
EXPOSE 8082

# 执行启动命令(可以使用"-Djava.security.egd=file:/dev/./urandom"加快随机数产生过程)
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]