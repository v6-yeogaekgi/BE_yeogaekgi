package com.v6.yeogaekgi.kiosk.controller;


import com.v6.yeogaekgi.kiosk.dto.KioskRequestDTO;
import com.v6.yeogaekgi.kiosk.dto.KioskResponseDTO;
import com.v6.yeogaekgi.kiosk.service.KioskService;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KioskController {
    private final KioskService kioskService;

    @PostMapping("/kiosk")
    public ResponseEntity<KioskResponseDTO>addKiosk(@RequestBody KioskRequestDTO kioskRequestDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        KioskResponseDTO kioskResponeDTO = kioskService.addKiosk(kioskRequestDTO, memberDetails.getMember());
        return ResponseEntity.ok(kioskResponeDTO);
    }

    @GetMapping("/kiosk")
    public ResponseEntity<List<String>>kioskList() {
        List<String> kioskLists = kioskService.getKioskLists();
        return ResponseEntity.ok(kioskLists);
    }
}
