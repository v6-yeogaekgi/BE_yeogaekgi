package com.v6.yeogaekgi.kiosk.controller;

import com.v6.yeogaekgi.kiosk.service.KioskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class KioskController {
    private final KioskService kioskService;

    @GetMapping("/kiosk")
    public ResponseEntity<List<String>>kioskList() {
        List<String> kioskLists = kioskService.getKioskLists();
        return ResponseEntity.ok(kioskLists);
    }
}
