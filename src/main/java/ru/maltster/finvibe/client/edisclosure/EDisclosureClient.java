package ru.maltster.finvibe.client.edisclosure;

import ru.maltster.finvibe.dto.EventInfoDto;

import java.util.List;

public interface EDisclosureClient {
    String TEMPLATE_EVENT_BY_GUID = "https://www.e-disclosure.ru/portal/event.aspx?EventId=%s";
    String TEMPLATE_ALL_EVENTS_BY_COMPANY_AND_YEAR = "https://www.e-disclosure.ru/api/events/page?companyId=%s&year=%s";

    List<EventInfoDto> getAllEventsBy(Long companyId, Integer year);

    String getEventByPseudoGUID(String pseudoGUID);
}
