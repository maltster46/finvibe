package ru.maltster.finvibe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EDisclosureHistory {

    private static final String TEMPLATE_EVENT_URL = "https://www.e-disclosure.ru/portal/event.aspx?EventId=";

    private Long id;
    private String pseudoGUID;
    private Long companyId;
    private String eventName;
    private Timestamp eventDate;
    private Timestamp pubDate;
    private boolean notification;

    public String getNotificationString() {
        return String.format("id: %s\nid компании: %s\nДата публикации: %s\nСобытие: %s\nСсылка: %s",
                id,
                companyId,
                pubDate,
                eventName,
                TEMPLATE_EVENT_URL + pseudoGUID);
    }

}
