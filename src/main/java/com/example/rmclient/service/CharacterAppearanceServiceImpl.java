package com.example.rmclient.service;

import com.example.rmclient.utils.DataNotFoundException;
import com.example.rmclient.dto.CharacterAppearanceDTO;
import com.example.rmclient.dto.EpisodeDTO;
import org.openapitools.model.CharacterAppearance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Qualifier("characterAppearanceService")
public class CharacterAppearanceServiceImpl implements CharacterAppearanceService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiPath;
    private final DateTimeFormatter externalServiceDateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
    private final DateTimeFormatter clientDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int HTTP_OK = HttpStatus.OK.value();

    public CharacterAppearanceServiceImpl(RestTemplate restTemplate,
                                          @Value("${rickmorty.url}") String baseUrl,
                                          @Value("${rickmorty.path}") String apiPath) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiPath = apiPath;
    }

    /***
     * Get character appearance by name
     * @param name character name (required)
     */
    @Override
    public CharacterAppearance getCharacterAppearance(String name) {
        String externalApiUrl = baseUrl + apiPath + "?name=" + name;
        try {
            ResponseEntity<CharacterAppearanceDTO> responseEntity = restTemplate.getForEntity(externalApiUrl, CharacterAppearanceDTO.class);

            if (responseEntity.getStatusCodeValue() == HTTP_OK && responseEntity.getBody()!=null) {
                return buildCharacterAppearance(name, responseEntity.getBody());
            } else {
                throw new DataNotFoundException("Character not found");
            }
        }
        catch(RestClientException e){
                throw new DataNotFoundException("Character not found");
        }
    }

    /**
     * Build character appearance mapping and transforming fields
     * @param name character name
     * @param characterDTO character appearance dto
     */
    private CharacterAppearance buildCharacterAppearance(String name, CharacterAppearanceDTO characterDTO) {
        CharacterAppearance characterAppearance = new CharacterAppearance();
        characterAppearance.setName(name);
        characterAppearance.setEpisodes(new ArrayList<>());

        characterDTO.getResults().forEach(character -> processEpisodeList(characterAppearance, character.getEpisode()));
        return characterAppearance;
    }

    /***
     * Process episode list, getting info from external api
     * @param characterAppearance
     * @param episodeList
     */
    private void processEpisodeList(CharacterAppearance characterAppearance, List<String> episodeList) {
        episodeList.forEach(episodeUrl -> {
            ResponseEntity<EpisodeDTO> responseEntity = restTemplate.getForEntity(episodeUrl, EpisodeDTO.class);
            if (responseEntity.getStatusCodeValue() == HTTP_OK) {
                updateCharacterAppearanceWithEpisode(characterAppearance, responseEntity.getBody());
            } else {
                throw new DataNotFoundException("Error getting episode name");
            }
        });
        //service can repeat episode names, so we need to remove duplicates
        characterAppearance.setEpisodes(
                characterAppearance.getEpisodes().stream().distinct().collect(Collectors.toList()));
    }

    /***
     * Update character appearance with episode name and first appearance
     * @param characterAppearance
     * @param episode
     */
    private void updateCharacterAppearanceWithEpisode(CharacterAppearance characterAppearance, EpisodeDTO episode) {
        List<String> episodeNames = characterAppearance.getEpisodes();
        episodeNames.add(episode.getName());
        characterAppearance.setFirstAppearance(
                compareFirstAppearance(characterAppearance.getFirstAppearance(), episode.getAir_date()));
    }

    /***
     * Compare first appearance of character in episode and choose the one that is earlier
     * @param firstAppearance
     * @param airDate
     * @return
     */
    private String compareFirstAppearance(String firstAppearance, String airDate) {
        if (firstAppearance == null) {
            return formatClientDate(airDate);
        }

        LocalDate serviceDateTime = LocalDate.parse(airDate, externalServiceDateTimeFormatter);
        LocalDate clientDateTime = LocalDate.parse(firstAppearance, clientDateTimeFormatter);

        if (clientDateTime.compareTo(serviceDateTime) > 0) {
            return formatClientDate(airDate);
        } else {
            return firstAppearance;
        }
    }

    /***
     * Format date from external service to client format
     * @param date
     * @return
     */
    private String formatClientDate(String date) {
        LocalDate serviceDateTime = LocalDate.parse(date, externalServiceDateTimeFormatter);
        return serviceDateTime.format(clientDateTimeFormatter);
    }
}
