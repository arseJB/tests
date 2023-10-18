package com.example.rmclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class CharacterAppearanceDTO {
    @JsonProperty("results")
    private List<CharacterAppearanceResultDTO> results;
}
