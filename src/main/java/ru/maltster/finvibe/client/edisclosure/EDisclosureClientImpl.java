package ru.maltster.finvibe.client.edisclosure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.maltster.finvibe.dto.EventInfoDto;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EDisclosureClientImpl implements EDisclosureClient {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:120.0) Gecko/20100101 Firefox/120.0";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public EDisclosureClientImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<EventInfoDto> getAllEventsBy(Long companyId, Integer year) {
        List<EventInfoDto> events;

        URI uri = UriComponentsBuilder.fromHttpUrl(String.format(TEMPLATE_ALL_EVENTS_BY_COMPANY_AND_YEAR, companyId, year))
                .build().toUri();

        ResponseEntity<String> responseEntity = restClient.get()
                .uri(uri)
                .header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .retrieve()
                .toEntity(String.class);

        String jsonString;
        if (responseEntity.getStatusCode().is3xxRedirection()) {
            List<String> cookies = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
            String cookieHeader = Objects.isNull(cookies) ? "" : String.join("; ", cookies);
            jsonString = repeatRequest(uri, cookieHeader);
        } else {
            jsonString = responseEntity.getBody();
        }
        try {
            events = objectMapper.readValue(jsonString, new TypeReference<>() {});
        } catch (Exception ex) {
            throw new RuntimeException("Cannot parse as json: " + jsonString, ex);
        }
        return events;
    }

    @Override
    public String getEventByPseudoGUID(String pseudoGUID) {
        URI uri = UriComponentsBuilder.fromHttpUrl(String.format(TEMPLATE_EVENT_BY_GUID, pseudoGUID))
                .build().toUri();

        ResponseEntity<String> responseEntity = restClient.get()
                .uri(uri)
                .header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .retrieve()
                .toEntity(String.class);
        String html;
        if (responseEntity.getStatusCode().is3xxRedirection()) {
            List<String> cookies = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
            String cookieHeader = Objects.isNull(cookies) ? "" : String.join("; ", cookies);
            html = repeatRequest(uri, cookieHeader);
        } else {
            html = responseEntity.getBody();
        }
        return html;
    }

    private String repeatRequest(URI uri, String cookieHeader) {
        return restClient.get()
                .uri(uri)
                .header(HttpHeaders.ACCEPT, MediaType.ALL_VALUE)
                .header(HttpHeaders.COOKIE, cookieHeader)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }

}
