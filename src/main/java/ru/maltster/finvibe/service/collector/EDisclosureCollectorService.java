package ru.maltster.finvibe.service.collector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.maltster.finvibe.client.edisclosure.EDisclosureClient;
import ru.maltster.finvibe.dto.EventInfoDto;
import ru.maltster.finvibe.model.EDisclosureFavourite;
import ru.maltster.finvibe.model.EDisclosureHistory;
import ru.maltster.finvibe.repository.EDisclosureEventsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EDisclosureCollectorService {

    private final EDisclosureEventsRepository eventsRepository;
    private final EDisclosureClient eDisclosureClient;

    @Autowired
    public EDisclosureCollectorService(EDisclosureEventsRepository eventsRepository, EDisclosureClient eDisclosureClient) {
        this.eventsRepository = eventsRepository;
        this.eDisclosureClient = eDisclosureClient;
    }

//    @Scheduled(cron = "* */2 * * * *")
    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void collectNewEventsForFavourites() {
        log.debug("Сборщик новостей edisclosure запущен");
        List<EDisclosureFavourite> favourites = eventsRepository.getAllFavourites();

        for (EDisclosureFavourite favourite : favourites) {
            List<EventInfoDto> eventsForCompany = eDisclosureClient.getAllEventsBy(favourite.getCompanyId(), LocalDateTime.now().getYear());
            List<EDisclosureHistory> history = eventsRepository.getAllHistoryByCompanyId(favourite.getCompanyId());

            ArrayList<EDisclosureHistory> prepareToSave = new ArrayList<>();
            for (EventInfoDto eventInfoDto : eventsForCompany) {
                boolean isSaved = false;
                for (EDisclosureHistory historyEvent : history) {
                    if (historyEvent.getPubDate().equals(eventInfoDto.getPubDate()) &&
                            historyEvent.getPseudoGUID().equals(eventInfoDto.getPseudoGUID())) {
                        isSaved = true;
                        break;
                    }
                }
                if (!isSaved) {
                    prepareToSave.add(EDisclosureHistory.builder()
                            .companyId(favourite.getCompanyId())
                            .companyName(favourite.getShortname())
                            .pseudoGUID(eventInfoDto.getPseudoGUID())
                            .eventName(eventInfoDto.getEventName())
                            .eventDate(eventInfoDto.getEventDate())
                            .pubDate(eventInfoDto.getPubDate())
                            .notification(false)
                            .build());
                }
            }
            eventsRepository.saveEvent(prepareToSave);
        }
        log.debug("Сборщик новостей edisclosure завершил работу");
    }

}
