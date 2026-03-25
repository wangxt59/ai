# 微信公众号情感类文章自动生成器

每天自动获取微信公众号排行前十的情感类文章，基于这些文章生成一篇新的情感类文章，并生成匹配的封面图。

## 功能特点

- 📊 获取微信公众号情感类文章排行榜
- ✍️ 基于热门文章生成新的情感类文章
- 🎨 自动生成匹配文章主题的封面图
- ⏰ 支持定时任务，每天自动执行
- 💾 自动保存文章和封面图到本地

## 安装依赖

```bash
pip install -r requirements.txt
```

## 配置

1. 复制 `config.json` 文件，填写你的 API 密钥：
   - `openai_api_key`: OpenAI API 密钥（必需，用于生成文章和图片）
   - `wechat_api_key`: 微信公众号 API 密钥（可选，如果使用第三方服务）

2. 或者通过环境变量设置：
   ```bash
   export OPENAI_API_KEY="your-api-key"
   export WECHAT_API_KEY="your-wechat-api-key"  # 可选
   ```

## 使用方法

### 快速开始（使用启动脚本）

```bash
./run.sh
```

启动脚本会自动：
- 创建虚拟环境（如果不存在）
- 安装依赖
- 检查配置文件
- 执行文章生成任务

### 手动运行

```bash
python wechat_article_generator.py
```

### 定时任务（每天执行一次）

#### macOS/Linux (使用 cron)

1. 编辑 crontab：
   ```bash
   crontab -e
   ```

2. 添加以下行（每天上午9点执行）：
   ```bash
   0 9 * * * cd /Users/wangxt/Documents/project-p/ai-agent && /usr/bin/python3 wechat_article_generator.py >> logs/cron.log 2>&1
   ```

   注意：请根据你的实际路径和 Python 路径修改上述命令。

#### 使用 Python schedule 库（推荐）

运行定时任务脚本：
```bash
python scheduler.py
```

这个脚本会在每天指定时间自动执行文章生成任务。

#### macOS LaunchAgent（推荐用于 macOS）

使用系统级的定时任务，在后台运行：

```bash
./install_scheduler.sh
```

这会安装一个 LaunchAgent，每天上午 9:00 自动执行任务。

管理命令：
- 查看状态: `launchctl list | grep wechat`
- 卸载任务: `launchctl unload ~/Library/LaunchAgents/com.wechat.article.generator.plist`
- 重新加载: `launchctl unload ~/Library/LaunchAgents/com.wechat.article.generator.plist && launchctl load ~/Library/LaunchAgents/com.wechat.article.generator.plist`

## 输出文件

生成的文件会保存在 `articles/` 目录下：

- `article_YYYY-MM-DD.md`: 生成的文章（Markdown 格式）
- `cover_YYYY-MM-DD.png`: 生成的封面图
- `references_YYYY-MM-DD.json`: 参考文章信息

## 注意事项

1. **微信公众号 API**: 当前代码中使用了模拟数据。实际使用时，你需要：
   - 使用微信公众号官方 API（需要认证）
   - 或使用第三方服务（如新榜、清博指数等）
   - 或自己实现爬虫获取热门文章

2. **API 限制**: OpenAI API 有使用限制和费用，请确保：
   - 账户有足够的余额
   - 注意 API 调用频率限制

3. **图片生成**: 使用 DALL-E 3 生成图片，每次调用会产生费用。

## 自定义配置

你可以修改 `wechat_article_generator.py` 中的以下参数：

- `category`: 文章类别（默认：'情感'）
- `limit`: 获取的参考文章数量（默认：10）
- 文章生成提示词和参数
- 图片生成参数

## 故障排除

1. **API 密钥错误**: 检查 `config.json` 或环境变量中的 API 密钥是否正确
2. **网络问题**: 确保网络连接正常，可以访问 OpenAI API
3. **依赖缺失**: 运行 `pip install -r requirements.txt` 安装所有依赖

## 许可证

MIT License
