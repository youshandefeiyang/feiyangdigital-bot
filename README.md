# 使用说明：
## 这是一个基于SpringBoot和Telegrambot-Api的多功能Telegram群管机器人
## 首先你需要在[@BotFather](https://t.me/botfather)创建一个自己的机器人，并记录token和机器人的用户名，就是@xxx_bot
## 然后你需要一键执行
```
```
## 下载配置文件，并在/home/feiyangdigitalbotconf/目录下编辑该conf.json，将刚才保存的username和token填入botConfig下的name和token并保存
## 最后你可以执行Docker一键运行该bot，确保你的网络能连接到telegram服务器，软路由请使用增强代理
```
docker run -d --restart unless-stopped --privileged=true --name feiyangdigital-bot -p 35442:8080 -v /Users/feiyang/Desktop/json/config.json:/app/config.json youshandefeiyang/feiyangdigital-bot
```