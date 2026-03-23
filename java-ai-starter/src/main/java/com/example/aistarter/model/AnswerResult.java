package com.example.aistarter.model;

import java.util.List;

/**
 * AI 回答结果。
 *
 * 除了最终答案，还把引用文档带回去，
 * 这样你能更直观地看到“先检索，再回答”的链路。
 */
public record AnswerResult(String answer, List<KnowledgeDocument> references) {
}
