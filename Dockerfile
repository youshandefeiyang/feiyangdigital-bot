# 构建阶段（修正 as/AS 大小写）
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
# 利用缓存将依赖先拉下（可选，需 BuildKit）
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -DskipTests dependency:go-offline
COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -DskipTests clean package

# 运行阶段：使用受支持的 Debian 12（bookworm）slim
FROM openjdk:17-jre-slim-bookworm

# 安装中文字体（WenQuanYi 与 Noto 二选一或都装）
RUN apt-get update \
 && apt-get install -y --no-install-recommends fonts-wqy-microhei fonts-wqy-zenhei fonts-noto-cjk \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
# 只拷贝 jar，去掉 src/（减小镜像）
COPY --from=build /app/target/*.jar /app/app.jar

# 设置Java的Headless模式
ENV JAVA_TOOL_OPTIONS -Djava.awt.headless=true -Dfile.encoding=UTF-8

# 设置启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
