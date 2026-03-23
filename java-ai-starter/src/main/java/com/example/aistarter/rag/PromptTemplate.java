package com.example.aistarter.rag;

import com.example.aistarter.model.KnowledgeDocument;

import java.util.List;

/**
 * 负责构造 Prompt。
 *
 * 很多 AI 应用并不是“用户问题直接发给模型”，
 * 而是会先拼出一个更完整的 system/user prompt。
 */
public class PromptTemplate {

    public String buildPrompt(String question, List<KnowledgeDocument> references) {
        StringBuilder builder = new StringBuilder();
        builder.append("你是一名面向 Java 工程师的 AI 学习助手。\n");
        builder.append("请严格基于已提供的参考资料回答。\n");
        builder.append("如果参考资料不足，就明确说明“根据当前知识库，我只能给出基础建议”。\n\n");

        builder.append("参考资料：\n");
        if (references.isEmpty()) {
            builder.append("当前没有检索到参考资料。\n");
        } else {
            for (KnowledgeDocument document : references) {
                builder.append("[").append(document.id()).append("] ")
                        .append(document.title()).append("\n")
                        .append(document.content()).append("\n\n");
            }
        }

        builder.append("用户问题：\n");
        builder.append(question).append("\n\n");

        builder.append("请给出：\n");
        builder.append("1. 简洁结论\n");
        builder.append("2. 关键解释\n");
        builder.append("3. 下一步建议\n");
        return builder.toString();
    }
}
