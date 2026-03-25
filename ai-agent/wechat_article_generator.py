#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
微信公众号情感类文章生成器
每天自动获取排行前十的情感类文章，生成新文章和封面图
"""

import os
import json
import requests
from datetime import datetime
from pathlib import Path
import openai
from openai import OpenAI
import time

class WeChatArticleGenerator:
    def __init__(self, config_file='config.json'):
        """初始化生成器"""
        self.config = self.load_config(config_file)
        self.client = OpenAI(api_key=self.config.get('openai_api_key'))
        self.output_dir = Path('articles')
        self.output_dir.mkdir(exist_ok=True)
        
    def load_config(self, config_file):
        """加载配置文件"""
        if os.path.exists(config_file):
            with open(config_file, 'r', encoding='utf-8') as f:
                return json.load(f)
        else:
            # 返回默认配置，需要用户填写
            return {
                'openai_api_key': os.getenv('OPENAI_API_KEY', ''),
                'wechat_api_key': os.getenv('WECHAT_API_KEY', ''),
                'wechat_api_url': 'https://api.weixin.qq.com'
            }
    
    def get_top_wechat_articles(self, category='情感', limit=10):
        """
        获取微信公众号排行前十的情感类文章
        
        注意：微信公众号官方API需要认证，这里提供一个模拟实现
        实际使用时需要替换为真实的API调用或爬虫方法
        """
        print(f"正在获取{category}类排行前十的文章...")
        
        # 模拟数据 - 实际使用时需要替换为真实API调用
        # 可以使用第三方服务如：新榜、清博指数等，或自己爬取
        articles = []
        
        # 示例：如果使用新榜API或其他第三方服务
        # try:
        #     response = requests.get(
        #         'https://api.newrank.cn/api/wechat/article/list',
        #         params={
        #             'key': self.config.get('wechat_api_key'),
        #             'category': category,
        #             'limit': limit
        #         }
        #     )
        #     articles = response.json().get('data', [])
        # except Exception as e:
        #     print(f"获取文章列表失败: {e}")
        
        # 由于微信公众号API需要认证，这里提供一个备用方案：
        # 使用网络搜索或爬虫获取热门情感类文章标题和摘要
        # 或者使用公开的微信公众号排行榜网站
        
        # 模拟返回文章信息
        print("注意：当前使用模拟数据，请配置真实的API或爬虫方法")
        articles = [
            {'title': '那些年，我们错过的爱情', 'summary': '关于青春和遗憾的故事'},
            {'title': '最好的感情，是彼此成就', 'summary': '探讨健康关系的本质'},
            {'title': '一个人爱不爱你，看这三点就知道', 'summary': '情感识别的实用指南'},
            {'title': '真正的放下，是不再提起', 'summary': '关于释怀和成长'},
            {'title': '余生，和相处舒服的人在一起', 'summary': '选择合适的人生伴侣'},
            {'title': '最好的关系，是相互独立又相互依赖', 'summary': '成熟关系的平衡'},
            {'title': '爱一个人，就是给他自由', 'summary': '关于爱情的理解'},
            {'title': '时间会告诉你，谁值得珍惜', 'summary': '时间见证真情'},
            {'title': '有些路，只能一个人走', 'summary': '独立成长的重要性'},
            {'title': '愿你被世界温柔以待', 'summary': '温暖人心的祝福'}
        ]
        
        return articles[:limit]
    
    def generate_article(self, reference_articles):
        """
        基于参考文章生成新的情感类文章
        """
        print("正在生成文章...")
        
        # 提取参考文章的主题和关键词
        topics = [article.get('title', '') + ': ' + article.get('summary', '') 
                 for article in reference_articles]
        topics_text = '\n'.join([f"{i+1}. {topic}" for i, topic in enumerate(topics)])
        
        prompt = f"""请参考以下微信公众号情感类热门文章的主题和风格，创作一篇新的情感类文章。

参考文章：
{topics_text}

要求：
1. 文章要有深度，能够引起读者共鸣
2. 语言优美，情感真挚
3. 字数在1500-2000字左右
4. 包含一个吸引人的标题
5. 文章结构清晰，有开头、主体和结尾
6. 主题要与参考文章相关但要有新意

请直接输出文章内容，格式如下：
标题：[文章标题]

[文章正文内容]
"""
        
        try:
            response = self.client.chat.completions.create(
                model="gpt-4",
                messages=[
                    {"role": "system", "content": "你是一位擅长写作情感类文章的作家，文笔优美，情感真挚。"},
                    {"role": "user", "content": prompt}
                ],
                temperature=0.8,
                max_tokens=2000
            )
            
            article_content = response.choices[0].message.content
            return article_content
        except Exception as e:
            print(f"生成文章失败: {e}")
            return None
    
    def generate_cover_image(self, article_title, article_summary):
        """
        生成匹配文章主题的封面图
        """
        print("正在生成封面图...")
        
        # 从文章标题和摘要中提取关键词用于生成图片
        prompt = f"""为这篇情感类文章生成一张封面图：
标题：{article_title}
主题：{article_summary}

要求：
1. 风格温馨、温暖，符合情感类文章的调性
2. 色彩柔和，有艺术感
3. 不要包含文字
4. 能够传达文章的情感氛围
"""
        
        try:
            response = self.client.images.generate(
                model="dall-e-3",
                prompt=prompt,
                size="1024x1024",
                quality="standard",
                n=1
            )
            
            image_url = response.data[0].url
            return image_url
        except Exception as e:
            print(f"生成封面图失败: {e}")
            return None
    
    def download_image(self, image_url, save_path):
        """下载图片到本地"""
        try:
            response = requests.get(image_url, timeout=30)
            response.raise_for_status()
            with open(save_path, 'wb') as f:
                f.write(response.content)
            print(f"封面图已保存到: {save_path}")
            return True
        except Exception as e:
            print(f"下载图片失败: {e}")
            return False
    
    def save_article(self, article_content, image_path=None):
        """保存文章到文件"""
        today = datetime.now().strftime('%Y-%m-%d')
        article_file = self.output_dir / f'article_{today}.md'
        
        # 保存文章内容
        with open(article_file, 'w', encoding='utf-8') as f:
            f.write(f"# 生成日期: {today}\n\n")
            if image_path:
                f.write(f"![封面图]({image_path})\n\n")
            f.write(article_content)
        
        print(f"文章已保存到: {article_file}")
        return article_file
    
    def run(self):
        """执行主流程"""
        print("=" * 50)
        print(f"开始执行文章生成任务 - {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print("=" * 50)
        
        # 1. 获取排行前十的文章
        reference_articles = self.get_top_wechat_articles()
        if not reference_articles:
            print("未能获取参考文章，任务终止")
            return
        
        print(f"成功获取 {len(reference_articles)} 篇参考文章")
        
        # 2. 生成新文章
        article_content = self.generate_article(reference_articles)
        if not article_content:
            print("文章生成失败，任务终止")
            return
        
        # 提取标题和摘要用于生成封面图
        lines = article_content.split('\n')
        title = ''
        summary = ''
        for line in lines:
            if line.startswith('标题：') or line.startswith('标题:'):
                title = line.replace('标题：', '').replace('标题:', '').strip()
            elif len(line) > 10 and not summary:
                summary = line[:100]  # 取前100字作为摘要
        
        if not title:
            title = lines[0].replace('#', '').strip()
        
        # 3. 生成封面图
        image_url = self.generate_cover_image(title, summary)
        image_path = None
        
        if image_url:
            today = datetime.now().strftime('%Y-%m-%d')
            image_filename = f'cover_{today}.png'
            image_path = self.output_dir / image_filename
            self.download_image(image_url, image_path)
            image_path = image_filename  # 相对路径用于markdown
        
        # 4. 保存文章
        article_file = self.save_article(article_content, image_path)
        
        # 5. 保存参考文章信息
        ref_file = self.output_dir / f'references_{datetime.now().strftime("%Y-%m-%d")}.json'
        with open(ref_file, 'w', encoding='utf-8') as f:
            json.dump(reference_articles, f, ensure_ascii=False, indent=2)
        
        print("=" * 50)
        print("任务完成！")
        print(f"文章文件: {article_file}")
        if image_path:
            print(f"封面图: {self.output_dir / image_path}")
        print("=" * 50)


def main():
    """主函数"""
    generator = WeChatArticleGenerator()
    generator.run()


if __name__ == '__main__':
    main()
