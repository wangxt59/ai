package com.example.aistarter.rag;

import com.example.aistarter.model.KnowledgeDocument;

import java.util.List;

/**
 * 模拟一个本地知识库。
 *
 * 真实系统里，这层通常负责从数据库、搜索引擎、向量库中取数据。
 * 这里先放几条固定文档，便于你理解整体流程。
 */
public class KnowledgeBase {

    private final List<KnowledgeDocument> documents = List.of(
            new KnowledgeDocument(
                    "doc-1",
                    "什么是 Prompt Engineering",
                    "Prompt Engineering 是围绕提示词设计、约束和迭代的方法。目标是让大模型在给定上下文中更稳定地产生想要的输出。"
            ),
            new KnowledgeDocument(
                    "doc-2",
                    "什么是 RAG",
                    "RAG 是 Retrieval-Augmented Generation 的缩写。它先检索外部知识，再把检索结果连同用户问题一起交给大模型生成回答。"
            ),
            new KnowledgeDocument(
                    "doc-3",
                    "Java 工程师如何转 AI",
                    "Java 工程师转 AI 的高性价比路线通常是先做 AI 应用工程。重点补齐 Python、Prompt、RAG、模型接口调用、评估与治理。"
            ),
            new KnowledgeDocument(
                    "doc-4",
                    "为什么 AI 系统需要结构化分层",
                    "AI 输出具有概率性，因此工程上通常要把检索、Prompt、模型调用、结果校验、监控拆开，便于替换和治理。"
            )
    );

    public List<KnowledgeDocument> allDocuments() {
        return documents;
    }
}
