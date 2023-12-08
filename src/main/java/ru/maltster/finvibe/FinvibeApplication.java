package ru.maltster.finvibe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.maltster.finvibe.client.edisclosure.EDisclosure;
import ru.maltster.finvibe.client.edisclosure.EDisclosureImpl;
import ru.maltster.finvibe.dto.EventInfoDto;

import java.util.List;

@SpringBootApplication
@Slf4j
public class FinVibeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FinVibeApplication.class, args);
		EDisclosure client = context.getBean(EDisclosureImpl.class);

		String companyId = "347";
		int year = 2023;

		try {
			List<EventInfoDto> allEvents = client.getAllEventsBy(companyId, year);
			log.debug("All events by companyId: {} and year: {}", companyId, year);

			if (!allEvents.isEmpty()) {
				EventInfoDto eventInfo = allEvents.get(0);
				String html = client.getEventByPseudoGUID(eventInfo.getPseudoGUID());
				log.debug("Event: {}\nHTML:\n{}", eventInfo.getPseudoGUID(), html);
			}

		} catch (Exception ex) {
			log.error("e-disclosure failed", ex);
		}
		SpringApplication.exit(context);
	}

}
