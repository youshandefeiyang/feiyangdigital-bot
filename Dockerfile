# 使用Maven 3.8.5基础镜像进行构建
FROM maven:3.8.5-openjdk-17 AS build  # ← 修正 AS 大小写

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

# 安装中文字体（buster 已 EOL，切到 archive 源并关闭有效期校验）
RUN printf '%s\n' \
 'deb http://archive.debian.org/debian buster main' \
 'deb http://archive.debian.org/debian-security buster/updates main' \
 > /etc/apt/sources.list \
 && apt-get -o Acquire::Check-Valid-Until=false update \
 && apt-get install -y --no-install-recommends fonts-wqy-microhei \
 && rm -rf /var/lib/apt/lists/*

# 设置应用的目录结构
WORKDIR /app

# 从构建阶段复制构建的jar文件和src目录
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/src/ /app/src/

# 设置Java的Headless模式（修正为 key=value 写法）
ENV JAVA_TOOL_OPTIONS="-Djava.awt.headless=true"

# 设置启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
