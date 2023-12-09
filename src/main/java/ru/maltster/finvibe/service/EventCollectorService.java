package ru.maltster.finvibe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maltster.finvibe.client.edisclosure.EDisclosureClient;
import ru.maltster.finvibe.dto.EventInfoDto;
import ru.maltster.finvibe.model.EDisclosureFavourite;
import ru.maltster.finvibe.model.EDisclosureHistory;
import ru.maltster.finvibe.repository.EDisclosureEventsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventCollectorService {

    private final EDisclosureEventsRepository eventsRepository;
    private final EDisclosureClient eDisclosureClient;

    @Autowired
    public EventCollectorService(EDisclosureEventsRepository eventsRepository, EDisclosureClient eDisclosureClient) {
        this.eventsRepository = eventsRepository;
        this.eDisclosureClient = eDisclosureClient;
    }

    public void collectNewEventsForFavourites() {
        List<EDisclosureFavourite> favourites = eventsRepository.getAllFavourites();
        for (EDisclosureFavourite favourite : favourites) {
            List<EventInfoDto> eventsForCompany = eDisclosureClient.getAllEventsBy(favourite.getCompanyId(), LocalDateTime.now().getYear());
            List<EDisclosureHistory> history = eventsRepository.getAllHistory();
            for (EventInfoDto eventInfoDto : eventsForCompany) {
                boolean isSaved = false;
                for (EDisclosureHistory historyEvent : history) {
                    if (historyEvent.getPubDate().equals(eventInfoDto.getPubDate()) &&
                            historyEvent.getCompanyId().equals(favourite.getCompanyId()) &&
                            historyEvent.getPseudoGUID().equals(eventInfoDto.getPseudoGUID())) {
                        isSaved = true;
                        break;
                    }
                }
                if (!isSaved) {
                    eventsRepository.saveEvent(EDisclosureHistory.builder()
                                    .companyId(favourite.getCompanyId())
                                    .pseudoGUID(eventInfoDto.getPseudoGUID())
                                    .eventName(eventInfoDto.getEventName())
                                    .eventDate(eventInfoDto.getEventDate())
                                    .pubDate(eventInfoDto.getPubDate())
                                    .notification(false)
                            .build());
                }
            }
        }
    }

}
