package com.v6.yeogaekgi.services.controller;

import com.v6.yeogaekgi.services.dto.ServiceResponseDTO;
import com.v6.yeogaekgi.services.service.Servicesservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/services")
@Log4j2
@RequiredArgsConstructor
public class ServicesController {
    private final Servicesservice servicesservice;

    @GetMapping("/servicesList")
    public List<ServiceResponseDTO> getAllServices() {
        return servicesservice.findAllServices();
    }
}
