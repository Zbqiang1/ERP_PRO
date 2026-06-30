# ===========================================
# Stage 1: Maven 构建阶段
# ===========================================
FROM maven:3.8-openjdk-8-slim AS builder

WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .
# 下载依赖（利用Docker缓存）
RUN mvn dependency:go-offline -B -q || true

# 复制源代码
COPY src ./src

# 构建项目（跳过测试）
RUN mvn clean package -DskipTests -B -q

# ===========================================
# Stage 2: 运行阶段
# ===========================================
FROM openjdk:8-jre-alpine

# 设置时区为亚洲/上海
RUN apk add --no-cache tzdata curl \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

# 设置工作目录
WORKDIR /app

# 创建日志目录
RUN mkdir -p /app/logs

# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# JVM参数配置
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs/"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT exec java ${JAVA_OPTS} -jar /app/app.jar
