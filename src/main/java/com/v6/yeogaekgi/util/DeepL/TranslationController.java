package com.v6.yeogaekgi.util.DeepL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class TranslationController {

    private final String deeplApiUrl = "https://api-free.deepl.com/v2/translate";

    @Value("${deepl.auth-key}")
    private String authKey;

    @PostMapping("/translate")
    public ResponseEntity<TranslationDTOResponse> translate(@RequestBody TranslationDTORequest request) {
        String targetLang = request.getTarget_lang();

        String apiUrl = deeplApiUrl + "?target_lang=" + targetLang;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "DeepL-Auth-Key " + authKey);

        TranslationDTOResponse response = restTemplate.postForObject(apiUrl, createHttpEntity(request, headers), TranslationDTOResponse.class);

        return ResponseEntity.ok(response);
    }

    private HttpEntity<TranslationDTORequest> createHttpEntity(TranslationDTORequest request, HttpHeaders headers) {
        return new HttpEntity<>(request, headers);
    }
}