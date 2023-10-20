
🤖 Feiyang Digital Telegram 群管机器人
===========================================

基于 **SpringBoot** 和 **Telegrambot-Api** 打造的多功能 Telegram 群管理机器人，**Powered By OpenAI And Google Cloud Vision**。

1️⃣ 开始之前
----------------
# 强烈推荐观看保姆级部署视频教程：https://www.youtube.com/watch?v=QgrPRgR5tek
# 使用文档：https://pan.v1.mk/%E6%AF%8F%E6%9C%9F%E8%A7%86%E9%A2%91%E4%B8%AD%E7%94%A8%E5%88%B0%E7%9A%84%E6%96%87%E4%BB%B6%E5%88%86%E4%BA%AB/%E5%AF%86%E7%A0%81123-2023.09.25%E6%9C%9F.zip
- **创建你的 Telegram 机器人:**
  1. 前往 [@BotFather](https://t.me/botfather) 以创建机器人。
  2. 记录下机器人的 `token` 和用户名。
  3. 不熟悉的话，可以[查阅此具体步骤](https://blog.csdn.net/whatday/article/details/113747294)。

- **准备环境:**
  - [安装 Docker 和 Docker-Compose](https://www.wxy97.com/archives/77)。

2️⃣ 终端运行
--------------

```bash
curl -o start.sh https://ghproxy.com/https://raw.githubusercontent.com/youshandefeiyang/feiyangdigital-bot/main/start.sh && chmod +x start.sh && ./start.sh
```

3️⃣ 配置机器人
----------------

- 前往 `/home/feiyangdigitalbotconf/` 目录，编辑 `conf.json` 文件：
  1. 填入你的 `username` 和 `token` 到 `botConfig` 的 `name` 和 `token` 字段。
  2. 保存更改。

▶️ 运行机器人
----------------

- 确保你的网络可以连接到 Telegram 服务器。如果使用软路由，请使用增强代理。
- 在 `/home/feiyangdigitalbotconf/` 目录下执行：
```bash
docker-compose up -d
```

⏸️ 暂停容器
------------

- 在 `/home/feiyangdigitalbotconf/` 目录下执行：
```bash
docker-compose stop
```

🔥 重启容器
------------

- 在 `/home/feiyangdigitalbotconf/` 目录下执行：
```bash
docker-compose restart
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
1.停止并移除卷：
```bash
docker-compose down
```
2.删除数据库持久卷（❗️危险操作，你需要对比本仓库里的数据库文件是否更新过，否则不要执行，删除之前请备份各种关键词文档）：
- 首先备份数据库至`/home/`目录下
```bash
docker exec -it feiyangdigitalbotconf-mysql-1 mysqldump -uroot -ppassword bot  > /home/bot.sql
```
- 删除数据库持久卷
```bash
docker volume rm feiyangdigitalbotconf_mysql-data
```
3.拉取最新镜像：
```bash
docker-compose pull  
```
4.使用新镜像重新启动容器：
```bash
docker-compose up -d
```
5.在宿主机的`/etc/sysctl.conf`文件中添加或修改以下行并重启：
```bash
vm.overcommit_memory = 1
```
