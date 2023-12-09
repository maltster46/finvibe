package ru.maltster.finvibe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EDisclosureHistory {

    private Long id;
    private String pseudoGUID;
    private Long companyId;
    private String eventName;
    private Date eventDate;
    private Date pubDate;
    private boolean notification;

}
