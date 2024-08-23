package com.v6.yeogaekgi.services.controller;

import com.v6.yeogaekgi.services.dto.ServiceResponseDTO;
import com.v6.yeogaekgi.services.service.Servicesservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/services")
@Log4j2
@RequiredArgsConstructor
public class ServicesController {
    private final Servicesservice servicesservice;

    @GetMapping("/servicesList")
    public ResponseEntity<List<ServiceResponseDTO>> getAllServices() {
        return new ResponseEntity<>(servicesservice.findAllServices(), HttpStatus.OK);
    }

}
