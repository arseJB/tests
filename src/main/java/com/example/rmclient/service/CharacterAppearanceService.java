package com.example.rmclient.service;

import org.openapitools.model.CharacterAppearance;
import org.springframework.stereotype.Service;

@Service
public interface CharacterAppearanceService {
    CharacterAppearance getCharacterAppearance(String name);
}
