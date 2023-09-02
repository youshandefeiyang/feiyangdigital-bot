#!/bin/sh

TARGET_DIR="/home/feiyangdigitalbotconf"
CONFIG_URL="https://www.sss.sss/config.json"

if [ -d "$TARGET_DIR" ]; then
    rm -rf "$TARGET_DIR"
fi
mkdir -p "$TARGET_DIR"

echo "开始从$CONFIG_URL下载config.json文件..."
curl -o "$TARGET_DIR/config.json" "$CONFIG_URL"

if [ $? -ne 0 ]; then
    echo "错误: 无法从$CONFIG_URL下载文件。"
    exit 1
else
    echo "成功下载config.json至$TARGET_DIR。"
fi