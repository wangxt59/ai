#!/bin/bash
# 安装定时任务脚本（macOS LaunchAgent）

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PLIST_FILE="com.wechat.article.generator.plist"
PLIST_PATH="$HOME/Library/LaunchAgents/$PLIST_FILE"

echo "安装定时任务..."

# 更新 plist 文件中的路径
sed "s|/Users/wangxt/Documents/project-p/ai-agent|$SCRIPT_DIR|g" "$SCRIPT_DIR/$PLIST_FILE" > "$PLIST_PATH"

# 加载定时任务
launchctl load "$PLIST_PATH" 2>/dev/null || launchctl load -w "$PLIST_PATH"

echo "定时任务已安装！"
echo "任务将在每天上午 9:00 自动执行"
echo ""
echo "管理命令："
echo "  查看状态: launchctl list | grep wechat"
echo "  卸载任务: launchctl unload $PLIST_PATH"
echo "  重新加载: launchctl unload $PLIST_PATH && launchctl load $PLIST_PATH"
