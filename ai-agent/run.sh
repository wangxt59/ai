#!/bin/bash
# 启动脚本

# 检查Python环境
if ! command -v python3 &> /dev/null; then
    echo "错误: 未找到 python3，请先安装 Python 3"
    exit 1
fi

# 检查依赖
if [ ! -d "venv" ]; then
    echo "创建虚拟环境..."
    python3 -m venv venv
fi

echo "激活虚拟环境..."
source venv/bin/activate

echo "安装依赖..."
pip install -r requirements.txt

# 检查配置文件
if [ ! -f "config.json" ]; then
    echo "警告: config.json 不存在，请从 config.example.json 复制并填写 API 密钥"
    if [ -f "config.example.json" ]; then
        cp config.example.json config.json
        echo "已创建 config.json，请填写你的 API 密钥"
    fi
    exit 1
fi

# 创建必要的目录
mkdir -p articles
mkdir -p logs

# 运行脚本
echo "开始执行文章生成任务..."
python3 wechat_article_generator.py
