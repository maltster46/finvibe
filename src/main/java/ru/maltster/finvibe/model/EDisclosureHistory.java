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

    private Long id;
    private String pseudoGUID;
    private Long companyId;
    private String eventName;
    private Timestamp eventDate;
    private Timestamp pubDate;
    private boolean notification;

}
