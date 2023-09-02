# 使用说明：
## 这是一个基于SpringBoot和Telegrambot-Api的多功能Telegram群管机器人
## 首先你需要在[@BotFather](https://t.me/botfather)创建一个自己的机器人，并记录token和机器人的用户名，具体步骤：[点击查看](https://blog.csdn.net/whatday/article/details/113747294)
## 然后需要[安装Docker和Docker-Compose](https://www.wxy97.com/archives/77)
## 然后你需要一键执行
```
curl -o start.sh https://ghproxy.com/https://raw.githubusercontent.com/youshandefeiyang/feiyangdigital-bot/main/start.sh && chmod +x start.sh && ./start.sh
```
## 然后你需要在/home/feiyangdigitalbotconf/目录下编辑该conf.json，将刚才申请的username和token填入botConfig下的name和token并保存
## 最后你可以执行Docker一键运行该bot，确保你的网络能连接到telegram服务器，软路由请使用增强代理
```
docker-compose up -d
```
## 查看日志，在/home/feiyangdigitalbotconf/目录下执行：
```
docker-compose logs -f 
```
