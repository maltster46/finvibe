package ru.maltster.finvibe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EDisclosureFavourite {

    private Long companyId;
    private String primaryTicker;
    private String shortname;

}
