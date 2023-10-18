package com.example.rmclient.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.rmclient.dto.CharacterAppearanceDTO;
import com.example.rmclient.dto.CharacterAppearanceResultDTO;
import com.example.rmclient.dto.EpisodeDTO;
import com.example.rmclient.utils.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.openapitools.model.CharacterAppearance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles({"standalone"})
@ExtendWith(SpringExtension.class)
class CharacterAppearanceServiceImplTest {
    @Autowired
    private CharacterAppearanceServiceImpl characterAppearanceServiceImpl;

    @MockBean
    private RestTemplate restTemplate;

    /**
     * Method under test: {@link CharacterAppearanceServiceImpl#getCharacterAppearance(String)}
     */
    @Test
    void testGetCharacterAppearanceFound() throws RestClientException {
        ResponseEntity<CharacterAppearanceDTO> responseEntity = getCharacterAppearanceDTOResponseEntity();
        ResponseEntity<EpisodeDTO> responseEpisodeEntity = getEpisodesDTOResponseEntity();
        when(restTemplate.getForEntity(Mockito.<String>any(), eq(CharacterAppearanceDTO.class))).thenReturn(responseEntity);
        when(restTemplate.getForEntity(Mockito.<String>any(), eq(EpisodeDTO.class))).thenReturn(responseEpisodeEntity);
        //then
        CharacterAppearance actualSearchCharacterAppearanceGetResult = 
                characterAppearanceServiceImpl.getCharacterAppearance("Rick Sanchez");
        
        assertEquals(actualSearchCharacterAppearanceGetResult.getName(), "Rick Sanchez");
        assertEquals(actualSearchCharacterAppearanceGetResult.getFirstAppearance(),"2013-12-02");
        assertFalse(actualSearchCharacterAppearanceGetResult.getEpisodes().isEmpty());
        assertEquals(actualSearchCharacterAppearanceGetResult.getEpisodes().get(0), "Test");

    }

    /**
     * Method under test: {@link CharacterAppearanceServiceImpl#getCharacterAppearance(String)}
     */
    @Test
    void testGetCharacterAppearanceNotFound() throws RestClientException {
        ResponseEntity<CharacterAppearanceDTO> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenThrow(new RestClientException("Msg"));
        when(restTemplate.getForEntity(Mockito.any(), Mockito.<Class<CharacterAppearanceDTO>>any(),
                (Object[]) any())).thenReturn(responseEntity);
        assertThrows(DataNotFoundException.class,
                () -> characterAppearanceServiceImpl.getCharacterAppearance("Name"));
        verify(restTemplate).getForEntity(Mockito.any(), Mockito.any(),
                (Object[]) any());
        verify(responseEntity).getStatusCodeValue();
    }

    private ResponseEntity<EpisodeDTO> getEpisodesDTOResponseEntity() {
        EpisodeDTO episode = new EpisodeDTO();
        episode.setName("Test");
        episode.setAir_date("December 2, 2013");
        ResponseEntity<EpisodeDTO> responseEntityEpisode = new ResponseEntity<>(episode,HttpStatus.OK);
        return responseEntityEpisode;
    }

    private static ResponseEntity<CharacterAppearanceDTO> getCharacterAppearanceDTOResponseEntity() {
        CharacterAppearanceDTO dto = new CharacterAppearanceDTO();
        List<CharacterAppearanceResultDTO> result = new ArrayList<>();
        CharacterAppearanceResultDTO character = new CharacterAppearanceResultDTO();
        List<String> episodes = new ArrayList<>();
        episodes.add("Test");
        episodes.add("Test 2");
        character.setEpisode(episodes);
        result.add(character);
        dto.setResults(result);
        ResponseEntity<CharacterAppearanceDTO> responseEntity = new ResponseEntity<>(dto, HttpStatus.OK);
        return responseEntity;
    }
}

