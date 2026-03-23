package com.example.aistarter;

import com.example.aistarter.config.AppConfig;
import com.example.aistarter.llm.FakeLlmClient;
import com.example.aistarter.llm.LlmClient;
import com.example.aistarter.llm.OpenAiCompatibleClient;
import com.example.aistarter.model.AnswerResult;
import com.example.aistarter.rag.KnowledgeBase;
import com.example.aistarter.rag.PromptTemplate;
import com.example.aistarter.rag.RetrievalService;
import com.example.aistarter.service.AiAssistantService;

import java.util.Scanner;

/**
 * 程序入口。
 *
 * 这个类故意保持得很薄，只负责组装依赖和读取用户输入。
 * 你可以把它理解为没有引入 Spring 时，最简单的“启动类 + 手工装配”示例。
 */
public class Main {

    public static void main(String[] args) {
        AppConfig config = AppConfig.fromEnv();
        LlmClient llmClient = buildLlmClient(config);

        KnowledgeBase knowledgeBase = new KnowledgeBase();
        RetrievalService retrievalService = new RetrievalService(knowledgeBase);
        PromptTemplate promptTemplate = new PromptTemplate();
        AiAssistantService assistantService =
                new AiAssistantService(retrievalService, promptTemplate, llmClient);

        System.out.println("Java AI Starter 已启动。");
        System.out.println("输入你的问题，输入 exit 退出。");
        System.out.println();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("你> ");
                String question = scanner.nextLine();
                if ("exit".equalsIgnoreCase(question.trim())) {
                    System.out.println("程序结束。");
                    break;
                }

                AnswerResult result = assistantService.ask(question);

                System.out.println();
                System.out.println("系统检索到的知识：");
                result.references().forEach(doc ->
                        System.out.println("- [" + doc.id() + "] " + doc.title()));

                System.out.println();
                System.out.println("AI 回答：");
                System.out.println(result.answer());
                System.out.println();
            }
        }
    }

    /**
     * 如果环境变量齐全，则使用真实大模型客户端。
     * 否则回退到本地假实现，便于在没有 API Key 的情况下先理解工程结构。
     */
    private static LlmClient buildLlmClient(AppConfig config) {
        if (config.isOpenAiConfigured()) {
            return new OpenAiCompatibleClient(config);
        }
        return new FakeLlmClient();
    }
}
