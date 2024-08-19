package com.v6.yeogaekgi.kiosk.service;

import com.v6.yeogaekgi.kiosk.entity.Kiosk;
import com.v6.yeogaekgi.kiosk.repository.KioskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KioskService {
    private final KioskRepository kioskRepository;

    @Transactional
    public List<String> getKioskLists(){
        List<Kiosk>kiosks = kioskRepository.findAll();

        return kiosks.stream()
                .map(Kiosk::getLocation)
                .collect(Collectors.toList());
    }
}
