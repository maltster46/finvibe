package ru.maltster.finvibe.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class TelegramNotificationService {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:120.0) Gecko/20100101 Firefox/120.0";
    private static final String TEMPLATE_SEND_MESSAGE = "https://api.telegram.org/bot%s/sendMessage";
    private final RestClient restClient;

    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.chat-id}")
    private String chatId;

    @Autowired
    public TelegramNotificationService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void send(String message) {
        URI uri = UriComponentsBuilder.fromHttpUrl(String.format(TEMPLATE_SEND_MESSAGE, token))
                .queryParam("chat_id", chatId)
                .queryParam("text", message)
                .build().toUri();
        System.out.println(uri);
        restClient.post()
                .uri(uri)
                .header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .retrieve()
                .toBodilessEntity();
    }

}
