package com.example.skala_ium.global.slack;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class SlackNotificationService {

    private final RestClient slackRestClient;

    public SlackNotificationService(@Value("${slack.bot.token}") String slackBotToken) {
        this.slackRestClient = RestClient.builder()
            .baseUrl("https://slack.com/api")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + slackBotToken)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public void sendDirectMessage(String slackUserId, String message) {
        if (slackUserId == null || slackUserId.isBlank()) {
            log.warn("Slack user ID가 없어 메시지를 보낼 수 없습니다.");
            return;
        }

        try {
            slackRestClient.post()
                .uri("/chat.postMessage")
                .body(Map.of(
                    "channel", slackUserId,
                    "text", message
                ))
                .retrieve()
                .toBodilessEntity();
            log.info("Slack DM 전송 완료: userId={}", slackUserId);
        } catch (Exception e) {
            log.error("Slack DM 전송 실패: userId={}", slackUserId, e);
        }
    }
}
