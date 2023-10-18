# 使用Maven 3.8.5基础镜像进行构建
FROM maven:3.8.5-openjdk-17 as build

# 设置工作目录
WORKDIR /app

# 复制pom.xml
COPY pom.xml .

# 复制源代码
COPY src/ src/

# 执行Maven构建
RUN mvn clean package

# 使用OpenJDK 17官方提供的buster镜像
FROM openjdk:17-jdk-buster

# 安装中文字体
RUN apt-get update && apt-get install -y fonts-wqy-microhei

# 设置应用的目录结构
WORKDIR /app

# 从构建阶段复制构建的jar文件和src目录
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/src/ /app/src/

# 设置Java的Headless模式
ENV JAVA_TOOL_OPTIONS -Djava.awt.headless=true

# 设置启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
