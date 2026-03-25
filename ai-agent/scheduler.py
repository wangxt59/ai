#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
定时任务调度器
每天在指定时间执行文章生成任务
"""

import schedule
import time
from datetime import datetime
from wechat_article_generator import WeChatArticleGenerator
import logging

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('logs/scheduler.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)

logger = logging.getLogger(__name__)

def job():
    """执行文章生成任务"""
    try:
        logger.info("开始执行定时任务...")
        generator = WeChatArticleGenerator()
        generator.run()
        logger.info("定时任务执行完成")
    except Exception as e:
        logger.error(f"定时任务执行失败: {e}", exc_info=True)

def main():
    """主函数"""
    # 创建日志目录
    import os
    os.makedirs('logs', exist_ok=True)
    
    # 设置定时任务 - 每天上午9点执行
    schedule.every().day.at("09:00").do(job)
    
    # 也可以设置为其他时间，例如：
    # schedule.every().day.at("08:00").do(job)  # 每天上午8点
    # schedule.every().day.at("20:00").do(job)  # 每天晚上8点
    
    logger.info("定时任务调度器已启动")
    logger.info("任务将在每天 09:00 执行")
    logger.info("按 Ctrl+C 停止")
    
    # 立即执行一次（可选）
    # job()
    
    # 保持运行
    while True:
        schedule.run_pending()
        time.sleep(60)  # 每分钟检查一次

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        logger.info("定时任务调度器已停止")
