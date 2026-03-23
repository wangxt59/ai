package com.example.aistarter.llm;

import com.example.aistarter.config.AppConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * 一个极简的 OpenAI 兼容接口客户端。
 *
 * 为了让示例尽量纯净，这里没有引入 JSON 库，而是手工拼装和解析。
 * 这并不适合生产环境，但足够帮助你理解“模型调用层”在做什么。
 */
public class OpenAiCompatibleClient implements LlmClient {

    private final AppConfig config;
    private final HttpClient httpClient;

    public OpenAiCompatibleClient(AppConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public String generate(String prompt) {
        String requestBody = buildRequestBody(prompt);
        String url = normalizeBaseUrl(config.baseUrl()) + "/v1/chat/completions";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.apiKey())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() >= 300) {
                return "模型调用失败，HTTP 状态码: " + response.statusCode() + "\n响应体: " + response.body();
            }
            return parseContent(response.body());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "模型调用异常: " + e.getMessage();
        }
    }

    private String buildRequestBody(String prompt) {
        String escapedPrompt = escapeJson(prompt);
        return """
                {
                  "model": "%s",
                  "temperature": 0.2,
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(escapeJson(config.model()), escapedPrompt);
    }

    /**
     * 这里只做一个非常简化的解析：
     * 从 JSON 中找出第一个 "content" 字段。
     *
     * 真实项目应使用 Jackson / Gson / JSON-B 等库安全解析。
     */
    private String parseContent(String responseBody) {
        String marker = "\"content\":";
        int start = responseBody.indexOf(marker);
        if (start < 0) {
            return "未能从响应中解析到 content。原始响应:\n" + responseBody;
        }

        int firstQuote = responseBody.indexOf('"', start + marker.length());
        if (firstQuote < 0) {
            return "未能定位 content 起始位置。原始响应:\n" + responseBody;
        }

        StringBuilder content = new StringBuilder();
        boolean escaped = false;
        for (int i = firstQuote + 1; i < responseBody.length(); i++) {
            char ch = responseBody.charAt(i);
            if (escaped) {
                switch (ch) {
                    case 'n' -> content.append('\n');
                    case 't' -> content.append('\t');
                    case 'r' -> content.append('\r');
                    case '"' -> content.append('"');
                    case '\\' -> content.append('\\');
                    default -> content.append(ch);
                }
                escaped = false;
                continue;
            }
            if (ch == '\\') {
                escaped = true;
                continue;
            }
            if (ch == '"') {
                return content.toString();
            }
            content.append(ch);
        }

        return "未能完整解析 content。原始响应:\n" + responseBody;
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
