
🤖 Feiyang Digital Telegram 群管机器人
===========================================

基于 **SpringBoot** 和 **Telegrambot-Api** 打造的多功能 Telegram 群管理机器人。

1️⃣ 开始之前
----------------

- **创建你的 Telegram 机器人:**
  1. 前往 [@BotFather](https://t.me/botfather) 以创建机器人。
  2. 记录下机器人的 `token` 和用户名。
  3. 不熟悉的话，可以[查阅此具体步骤](https://blog.csdn.net/whatday/article/details/113747294)。

- **准备环境:**
  - [安装 Docker 和 Docker-Compose](https://www.wxy97.com/archives/77)。

2️⃣ 一键部署
--------------

```bash
curl -o start.sh https://ghproxy.com/https://raw.githubusercontent.com/youshandefeiyang/feiyangdigital-bot/main/start.sh && chmod +x start.sh && ./start.sh
```

3️⃣ 配置机器人
----------------

- 前往 `/home/feiyangdigitalbotconf/` 目录，编辑 `conf.json` 文件：
  1. 填入你的 `username` 和 `token` 到 `botConfig` 的 `name` 和 `token` 字段。
  2. 保存更改。

4️⃣ 运行机器人
----------------

- 确保你的网络可以连接到 Telegram 服务器。如果使用软路由，请使用增强代理。
```bash
docker-compose up -d
```

🔍 查看日志
------------

在 `/home/feiyangdigitalbotconf/` 目录下执行：
```bash
docker-compose logs -f 
```

🔄 更新
--------

在 `/home/feiyangdigitalbotconf/` 目录下进行以下操作：
- 拉取最新镜像：
```bash
docker-compose pull  
```
- 使用新镜像重新启动容器：
```bash
docker-compose up -d
```
