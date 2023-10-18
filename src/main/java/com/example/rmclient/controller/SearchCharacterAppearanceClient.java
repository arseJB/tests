package com.example.rmclient.controller;

import com.example.rmclient.service.CharacterAppearanceService;
import com.example.rmclient.utils.DataNotFoundException;
import org.openapitools.api.SearchCharacterAppearanceApi;
import org.openapitools.model.CharacterAppearance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchCharacterAppearanceClient implements SearchCharacterAppearanceApi{

    private final CharacterAppearanceService service;

    public SearchCharacterAppearanceClient(@Qualifier("characterAppearanceService") CharacterAppearanceService service) {
        this.service = service;
    }

    /***
     * Get character appearance by name
     * @param name Character&#39;s name (required)
     */
    @Override
    public ResponseEntity<CharacterAppearance> searchCharacterAppearanceGet(String name) {
        try{
            CharacterAppearance characterAppearance = service.getCharacterAppearance(name);
            return ResponseEntity.ok(characterAppearance);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}