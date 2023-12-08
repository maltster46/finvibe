package ru.maltster.finvibe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventInfoDto {

    @JsonProperty("eventName")
    private String eventName;

    @JsonProperty("pseudoGUID")
    private String pseudoGUID;

    @JsonProperty("pubDate")
//    private Instant pubDate;
    private Date pubDate;

    @JsonProperty("eventDate")
//    private Instant eventDate;
    private Date eventDate;

}
