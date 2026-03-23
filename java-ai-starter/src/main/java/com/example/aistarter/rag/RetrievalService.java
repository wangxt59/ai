package com.example.aistarter.rag;

import com.example.aistarter.model.KnowledgeDocument;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 一个极简版检索器。
 *
 * 为了避免引入第三方库，这里使用“关键词命中数”做粗糙召回。
 * 真实项目里，这一层通常会升级为 Embedding + 向量检索 + 重排。
 */
public class RetrievalService {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[^\\p{IsAlphabetic}\\p{IsDigit}\\p{IsIdeographic}]+");

    private final KnowledgeBase knowledgeBase;

    public RetrievalService(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public List<KnowledgeDocument> retrieve(String question, int topK) {
        Set<String> keywords = splitToKeywords(question);
        String normalizedQuestion = normalize(question);

        return knowledgeBase.allDocuments().stream()
                .map(doc -> new RetrievalHit(doc, score(doc, keywords, normalizedQuestion)))
                .filter(hit -> hit.score() > 0)
                .sorted(Comparator.comparingInt(RetrievalHit::score).reversed())
                .limit(topK)
                .map(RetrievalHit::document)
                .toList();
    }

    private int score(KnowledgeDocument document, Set<String> keywords, String normalizedQuestion) {
        String documentText = document.title() + " " + document.content();
        String normalizedDocument = normalize(documentText);
        Set<String> docTerms = splitToKeywords(documentText);
        int score = 0;

        for (String keyword : keywords) {
            if (docTerms.contains(keyword)) {
                score++;
            }
        }

        // 中文场景下，很多问题没有空格分词，因此再补一层“子串命中”。
        // 这不是向量检索，但足够帮助你理解召回优化为什么重要。
        if (!normalizedQuestion.isBlank()) {
            if (normalizedDocument.contains(normalizedQuestion)) {
                score += 3;
            }

            for (String fragment : extractChineseFragments(normalizedQuestion)) {
                if (fragment.length() >= 2 && normalizedDocument.contains(fragment)) {
                    score += 2;
                }
            }

            for (String term : extractAsciiTerms(normalizedQuestion)) {
                if (term.length() >= 2 && normalizedDocument.contains(term)) {
                    score += 2;
                }
            }
        }

        return score;
    }

    private Set<String> splitToKeywords(String text) {
        return SPLIT_PATTERN.splitAsStream(text.toLowerCase())
                .map(String::trim)
                .filter(token -> !token.isBlank())
                .filter(token -> token.length() > 1)
                .collect(java.util.stream.Collectors.toSet());
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\p{IsIdeographic}]+", "");
    }

    /**
     * 从中文连续片段中生成 2 到 4 个字的短语，
     * 用来模拟最粗粒度的中文召回增强。
     */
    private Set<String> extractChineseFragments(String text) {
        java.util.Set<String> fragments = new java.util.HashSet<>();
        String chineseOnly = text.replaceAll("[^\\p{IsIdeographic}]", "");
        for (int size = 2; size <= 4; size++) {
            for (int i = 0; i + size <= chineseOnly.length(); i++) {
                fragments.add(chineseOnly.substring(i, i + size));
            }
        }
        return fragments;
    }

    private Set<String> extractAsciiTerms(String text) {
        return Pattern.compile("[a-z0-9]{2,}")
                .matcher(text)
                .results()
                .map(match -> match.group())
                .collect(java.util.stream.Collectors.toSet());
    }

    private record RetrievalHit(KnowledgeDocument document, int score) {
    }
}
