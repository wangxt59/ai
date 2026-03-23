package com.example.aistarter.config;

/**
 * 统一管理运行配置。
 *
 * 在真实项目里，这一层通常会对接 Spring Boot 的配置系统。
 * 这里先用环境变量，目的是把“配置”和“业务代码”分开。
 */
public record AppConfig(String apiKey, String baseUrl, String model) {

    public static AppConfig fromEnv() {
        String apiKey = readEnv("OPENAI_API_KEY");
        String baseUrl = readEnvOrDefault("OPENAI_BASE_URL", "https://api.openai.com");
        String model = readEnvOrDefault("OPENAI_MODEL", "gpt-4.1-mini");
        return new AppConfig(apiKey, baseUrl, model);
    }

    public boolean isOpenAiConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    private static String readEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static String readEnvOrDefault(String key, String defaultValue) {
        String value = readEnv(key);
        return value == null ? defaultValue : value;
    }
}
