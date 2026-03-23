package com.example.aistarter.llm;

/**
 * 大模型客户端抽象。
 *
 * 这是 AI 工程里一个很关键的设计点：
 * 上层业务不应该直接依赖某个具体模型厂商。
 */
public interface LlmClient {

    String generate(String prompt);
}
