package com.example.rmclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class EpisodeDTO {
    
    @JsonProperty("air_date")
    private String air_date;
    
    @JsonProperty("name")
    private String name;
}
