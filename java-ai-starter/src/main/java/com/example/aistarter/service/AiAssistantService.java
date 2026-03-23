package com.example.aistarter.service;

import com.example.aistarter.llm.LlmClient;
import com.example.aistarter.model.AnswerResult;
import com.example.aistarter.model.KnowledgeDocument;
import com.example.aistarter.rag.PromptTemplate;
import com.example.aistarter.rag.RetrievalService;

import java.util.List;

/**
 * 业务编排层。
 *
 * 这层最重要的职责不是“实现模型”，
 * 而是把检索、Prompt 组装、模型调用这些能力串起来。
 */
public class AiAssistantService {

    private final RetrievalService retrievalService;
    private final PromptTemplate promptTemplate;
    private final LlmClient llmClient;

    public AiAssistantService(RetrievalService retrievalService,
                              PromptTemplate promptTemplate,
                              LlmClient llmClient) {
        this.retrievalService = retrievalService;
        this.promptTemplate = promptTemplate;
        this.llmClient = llmClient;
    }

    public AnswerResult ask(String question) {
        List<KnowledgeDocument> references = retrievalService.retrieve(question, 3);
        String prompt = promptTemplate.buildPrompt(question, references);
        String answer = llmClient.generate(prompt);
        return new AnswerResult(answer, references);
    }
}
