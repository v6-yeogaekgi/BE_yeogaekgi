package com.v6.yeogaekgi.Member.service;

import com.v6.yeogaekgi.Member.entity.Country;
import com.v6.yeogaekgi.Member.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CsvService {
    private final CountryRepository countryRepository;

    @Transactional
    public void importCsvToDatabase(String filePath){
        List<Country> countries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 2) {
                    String code = values[0].trim();
                    String name = values[1].trim();
                    Country country = new Country(code, name);
                    countries.add(country);
                }
            }
            countryRepository.saveAll(countries);
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
    }
}
