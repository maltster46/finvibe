package ru.maltster.finvibe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.maltster.finvibe.client.edisclosure.EDisclosureClient;
import ru.maltster.finvibe.client.edisclosure.EDisclosureClientImpl;
import ru.maltster.finvibe.notification.TelegramNotificationService;
import ru.maltster.finvibe.service.EventCollectorService;

@SpringBootApplication
@Slf4j
public class FinVibeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FinVibeApplication.class, args);
		EDisclosureClient client = context.getBean(EDisclosureClientImpl.class);
		EventCollectorService collectorService = context.getBean(EventCollectorService.class);
		TelegramNotificationService telegramNotificationService = context.getBean(TelegramNotificationService.class);


		Long companyId = 347L;
		int year = 2023;

		try {
		/*
			List<EventInfoDto> allEvents = client.getAllEventsBy(companyId, year);
			log.debug("All events by companyId: {} and year: {}", companyId, year);

			if (!allEvents.isEmpty()) {
				EventInfoDto eventInfo = allEvents.get(0);
				String html = client.getEventByPseudoGUID(eventInfo.getPseudoGUID());
				log.debug("Event: {}\nHTML:\n{}", eventInfo.getPseudoGUID(), html);
			}
		 */
			collectorService.collectNewEventsForFavourites();
		} catch (Exception ex) {
			log.error("e-disclosure failed", ex);
		}

		SpringApplication.exit(context);
	}

}
