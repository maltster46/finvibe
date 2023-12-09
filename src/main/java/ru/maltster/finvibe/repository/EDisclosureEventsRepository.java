package ru.maltster.finvibe.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.maltster.finvibe.model.EDisclosureFavourite;
import ru.maltster.finvibe.model.EDisclosureHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class EDisclosureEventsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EDisclosureEventsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EDisclosureFavourite> getAllFavourites() {
        String query = "SELECT company_id, primary_ticker, shortname FROM edisclosure_favourites";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(EDisclosureFavourite.class));
    }

    public List<EDisclosureHistory> getAllHistory() {
        String query = "SELECT id, pseudo_guid, company_id, event_name, event_date, pub_date, notification FROM edisclosure_history";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(EDisclosureHistory.class));
    }

    public void saveEvent(EDisclosureHistory EDisclosureHistory) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("edisclosure_history");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", EDisclosureHistory.getId());
        parameters.put("pseudo_guid", EDisclosureHistory.getPseudoGUID());
        parameters.put("company_id", EDisclosureHistory.getCompanyId());
        parameters.put("event_name", EDisclosureHistory.getEventName());
        parameters.put("event_date", EDisclosureHistory.getEventDate());
        parameters.put("pub_date", EDisclosureHistory.getPubDate());
        parameters.put("notification", EDisclosureHistory.isNotification());

        simpleJdbcInsert.execute(parameters);
    }

    public void setNotificationFlag(long id, boolean notification) {
        jdbcTemplate.update("UPDATE edisclosure_history SET notification = ? WHERE id = ?",
                notification, id);
    }

}
