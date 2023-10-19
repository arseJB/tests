package com.example.rmclient.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.rmclient.service.CharacterAppearanceService;
import com.example.rmclient.utils.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.openapitools.model.CharacterAppearance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SearchCharacterAppearanceClient.class})
@ActiveProfiles({"standalone"})
@ExtendWith(SpringExtension.class)
class SearchCharacterAppearanceClientTest {
    @MockBean(name = "characterAppearanceService")
    private CharacterAppearanceService characterAppearanceService;

    @Autowired
    private SearchCharacterAppearanceClient searchCharacterAppearanceClient;

    /**
     * Method under test: {@link SearchCharacterAppearanceClient#searchCharacterAppearanceGet(String)}
     */
    @Test
    void testSearchCharacterAppearanceGetFound() {
        when(characterAppearanceService.getCharacterAppearance(Mockito.any()))
                .thenReturn(new CharacterAppearance());
        ResponseEntity<CharacterAppearance> actualSearchCharacterAppearanceGetResult = searchCharacterAppearanceClient
                .searchCharacterAppearanceGet("Name");
        assertTrue(actualSearchCharacterAppearanceGetResult.hasBody());
        assertTrue(actualSearchCharacterAppearanceGetResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualSearchCharacterAppearanceGetResult.getStatusCode());
        verify(characterAppearanceService).getCharacterAppearance(Mockito.any());
    }

    /**
     * Method under test: {@link SearchCharacterAppearanceClient#searchCharacterAppearanceGet(String)}
     */
    @Test
    void testSearchCharacterAppearanceGet2NotFound() {
        when(characterAppearanceService.getCharacterAppearance(Mockito.any()))
                .thenThrow(new DataNotFoundException("An error occurred"));
        ResponseEntity<CharacterAppearance> actualSearchCharacterAppearanceGetResult = searchCharacterAppearanceClient
                .searchCharacterAppearanceGet("Name");
        assertNull(actualSearchCharacterAppearanceGetResult.getBody());
        assertEquals(HttpStatus.NOT_FOUND, actualSearchCharacterAppearanceGetResult.getStatusCode());
        assertTrue(actualSearchCharacterAppearanceGetResult.getHeaders().isEmpty());
        verify(characterAppearanceService).getCharacterAppearance(Mockito.any());
    }
}

