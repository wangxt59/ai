package com.example.aistarter.model;

/**
 * 知识库中的一条文档。
 *
 * RAG 场景里，文档通常来自数据库、文件、Wiki 或切片后的段落。
 * 这里先用最小结构表示：id、标题、正文。
 */
public record KnowledgeDocument(String id, String title, String content) {
}
