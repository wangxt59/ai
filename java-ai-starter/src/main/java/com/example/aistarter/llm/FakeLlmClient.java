package com.example.aistarter.llm;

/**
 * 本地假模型。
 *
 * 它不是真的大模型，只是为了在没有外部 API 的情况下，
 * 让你先把调用链路跑通并理解每一层的职责。
 */
public class FakeLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {
        return """
                结论：
                这是一个基于本地知识库生成的演示回答，重点是帮助你理解 AI 应用工程结构。

                关键解释：
                当前程序已经模拟了“检索 -> 组装 Prompt -> 调用模型 -> 返回结果”的完整链路。
                在真实项目中，FakeLlmClient 会被替换成调用 OpenAI 兼容接口的实现。

                下一步建议：
                先读懂 RetrievalService、PromptTemplate、AiAssistantService 三层之间的关系，
                再把知识库替换成文件或数据库来源。
                """;
    }
}
