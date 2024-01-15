package ru.maltster.finvibe.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.maltster.finvibe.model.EDisclosureFavourite;
import ru.maltster.finvibe.model.EDisclosureHistory;

import java.sql.PreparedStatement;
import java.util.List;


@Repository
public class EDisclosureEventsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EDisclosureEventsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EDisclosureFavourite> getAllFavourites() {
        String query = "SELECT company_id, primary_ticker, shortname FROM favourites";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(EDisclosureFavourite.class));
    }

    public List<EDisclosureHistory> getAllHistoryByCompanyId(Long companyId) {
        String query = "SELECT id, pseudo_guid, company_id, company_name, event_name, event_date, pub_date, notification FROM history WHERE company_id = " + companyId;
        return jdbcTemplate.query(query, (rs, rowNum) -> EDisclosureHistory.builder()
                .id(rs.getLong("id"))
                .pseudoGUID(rs.getString("pseudo_guid"))
                .companyId(rs.getLong("company_id"))
                .companyName(rs.getString("company_name"))
                .eventName(rs.getString("event_name"))
                .eventDate(rs.getTimestamp("event_date"))
                .pubDate(rs.getTimestamp("pub_date"))
                .notification(rs.getBoolean("notification"))
                .build());
    }

    public void saveEvent(List<EDisclosureHistory> historyEvents) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO history (pseudo_guid, company_id, company_name, event_name, event_date, pub_date, notification) VALUES (?, ?, ?, ?, ?, ?, ?)",
                historyEvents,
                100,
                (PreparedStatement ps, EDisclosureHistory event) -> {
                    ps.setString(1, event.getPseudoGUID());
                    ps.setLong(2, event.getCompanyId());
                    ps.setString(3, event.getCompanyName());
                    ps.setString(4, event.getEventName());
                    ps.setTimestamp(5, event.getEventDate());
                    ps.setTimestamp(6, event.getPubDate());
                    ps.setBoolean(7, event.isNotification());
                }
        );
    }

    public void setNotificationFlag(long id, boolean notification) {
        jdbcTemplate.update("UPDATE history SET notification = ? WHERE id = ?",
                notification, id);
    }

    public List<EDisclosureHistory> selectEventForNotification() {
        String query = "SELECT id, pseudo_guid, company_id, company_name, event_name, event_date, pub_date, notification FROM history " +
                "WHERE notification IS NULL OR notification = false ORDER BY pub_date LIMIT 1";
        return jdbcTemplate.query(query, (rs, rowNum) -> EDisclosureHistory.builder()
            .id(rs.getLong("id"))
            .pseudoGUID(rs.getString("pseudo_guid"))
            .companyId(rs.getLong("company_id"))
            .companyName(rs.getString("company_name"))
            .eventName(rs.getString("event_name"))
            .eventDate(rs.getTimestamp("event_date"))
            .pubDate(rs.getTimestamp("pub_date"))
            .notification(rs.getBoolean("notification"))
            .build());
    }

}
