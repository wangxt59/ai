# Java 工程师转 AI 学习路线 + 入门工程

这个项目不是为了做一个“完整产品”，而是为了让你先看懂 AI 应用在工程上的基本分层：

1. 用户问题从哪里进入
2. 知识库如何检索
3. Prompt 如何组织
4. 大模型接口如何调用
5. 为什么 AI 项目通常要把“业务逻辑”和“模型能力”解耦

## 一、2026 年面向 Java 工程师的转 AI 路线

建议按下面 4 个阶段推进，每个阶段都要“学一点就做一点”。

### 阶段 1：补齐 AI 应用基础（2-4 周）

目标：先从“会用 AI 做应用”开始，而不是先冲算法研究。

你需要掌握：

- 大模型基础概念：Prompt、Token、Context Window、Temperature、Embedding、RAG、Function Calling、Agent
- Python 基础：语法、虚拟环境、包管理、FastAPI
- 向量检索基础：什么是 Embedding，为什么相似度检索能帮助问答
- OpenAI 兼容接口调用方式：HTTP、鉴权、模型参数

建议输出：

- 一个简单聊天 Demo
- 一个“文档问答” Demo
- 一个“结构化输出” Demo

### 阶段 2：用工程视角做 AI 应用（4-8 周）

目标：把你已有的 Java 工程经验迁移过来。

你需要掌握：

- Prompt 分层设计：system prompt、user prompt、few-shot 示例
- RAG 基本链路：切片、Embedding、召回、重排、回答
- 工程分层：Controller / Service / Retrieval / LLM Client / Config
- 日志、重试、限流、降级、缓存
- AI 输出不稳定时的治理方法：约束格式、校验、兜底

建议输出：

- 一个客服知识库问答系统
- 一个简历分析器
- 一个 SQL 生成与解释工具

### 阶段 3：进入 AI 工程化（2-3 个月）

目标：从“能跑 Demo”提升到“能做企业级项目”。

你需要掌握：

- 向量数据库：pgvector、Milvus、Weaviate、Elasticsearch 向量检索
- 工作流编排：LangChain / LangGraph / Spring AI / Dify 的定位和差异
- 模型评估：准确率、幻觉率、延迟、成本
- Agent 设计：工具调用、记忆、任务拆解、状态管理
- 线上治理：Prompt 版本管理、A/B 测试、观测与评估

建议输出：

- 一个带检索和工具调用的企业知识助手
- 一个多步骤表单处理 Agent

### 阶段 4：补模型和数据基础（长期）

目标：知道 AI 系统底层在做什么，但不要求你先转算法岗。

你需要掌握：

- Transformer 基础
- 训练 / 微调 / 推理的区别
- LoRA、SFT、RLHF 的概念
- 数据清洗、标注、评估集构造

## 二、你的技术路线建议

你本身是 Java 工程师，不建议一上来就硬转“算法工程师”。更现实的路线是：

`Java 后端工程师 -> AI 应用工程师 -> AI 平台/智能体工程师`

原因很直接：

- 你的工程经验已经很强，分层、接口、稳定性、可维护性这些在 AI 项目里仍然是核心竞争力
- 企业真正缺的是“把模型接进业务系统的人”，不只是会调 API 的人
- 先把 AI 应用做深，再决定要不要补训练、微调、算法研究

## 三、这个入门工程能学到什么

这个工程演示一个最小版“RAG 问答助手”：

- `Main`：程序入口，模拟命令行问答
- `KnowledgeBase`：本地知识库
- `RetrievalService`：根据问题召回相关文档
- `PromptTemplate`：把上下文和问题拼成 Prompt
- `LlmClient`：抽象模型调用接口
- `FakeLlmClient`：不依赖外部模型的本地假实现，帮助你理解链路
- `OpenAiCompatibleClient`：预留的真实模型调用实现
- `AiAssistantService`：串起完整问答流程

## 四、如何运行

由于当前机器没有 `mvn`，这里先给出纯 JDK 的运行方式。

### 编译

```bash
cd /Users/wangxt/Documents/ai/java-ai-starter
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
```

### 运行

```bash
cd /Users/wangxt/Documents/ai/java-ai-starter
java -cp out com.example.aistarter.Main
```

### 切换到真实模型

先设置环境变量：

```bash
export OPENAI_API_KEY=你的Key
export OPENAI_BASE_URL=https://api.openai.com
export OPENAI_MODEL=gpt-4.1-mini
```

然后再次运行程序。程序会优先使用真实接口；如果没有配置，就自动回退到本地 `FakeLlmClient`。

## 五、下一步建议

你把这个项目跑通并读懂后，下一步建议按这个顺序继续做：

1. 把本地知识库改成从文件读取
2. 把召回算法改成真正的 Embedding + 向量检索
3. 给回答加结构化输出
4. 再把这个工程改成 Spring Boot Web 服务

如果你愿意，下一轮我可以继续直接帮你把这个项目升级成：

- `Spring Boot + OpenAI 接口 + REST API` 版本
- 或者 `Java 后端 + Python AI 服务` 的双服务版本
