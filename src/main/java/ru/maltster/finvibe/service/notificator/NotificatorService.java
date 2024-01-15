package ru.maltster.finvibe.service.notificator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maltster.finvibe.client.telegram.TelegramClientImpl;
import ru.maltster.finvibe.model.EDisclosureHistory;
import ru.maltster.finvibe.repository.EDisclosureEventsRepository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NotificatorService {

    private final EDisclosureEventsRepository eventsRepository;
    private final TelegramClientImpl telegramClient;

    @Autowired
    public NotificatorService(EDisclosureEventsRepository eventsRepository, TelegramClientImpl telegramClient) {
        this.eventsRepository = eventsRepository;
        this.telegramClient = telegramClient;
    }


    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void sendNotification() {
        // TODO: методы scheduled работают последовательно (sendNotification не может работать одновременно с collectNewEventsForFavourites)
        List<EDisclosureHistory> events = eventsRepository.selectEventForNotification();
        if (Objects.nonNull(events) && !events.isEmpty()) {
            EDisclosureHistory singleEvent = events.get(0);
            telegramClient.send(singleEvent.getNotificationString());
            eventsRepository.setNotificationFlag(singleEvent.getId(), true);
        }
    }

}
