package com.v6.yeogaekgi.Member.controller;

import com.v6.yeogaekgi.Member.service.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class CsvController {
    private final CsvService csvService;

    @GetMapping("/import-csv")
    public ResponseEntity<String>importCsv(@RequestParam String filePath){
        try {
            String decodedPath = URLDecoder.decode(filePath,StandardCharsets.UTF_8.name());
            csvService.importCsvToDatabase(decodedPath);
            return ResponseEntity.ok("CSV import 성공");
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.status(500).body("디코딩 실패");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("CSV import 실패: " + e.getMessage());
        }
    }
}
