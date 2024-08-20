package com.v6.yeogaekgi.kiosk.service;

import com.v6.yeogaekgi.kiosk.dto.KioskRequestDTO;
import com.v6.yeogaekgi.kiosk.dto.KioskResponseDTO;
import com.v6.yeogaekgi.kiosk.entity.Kiosk;
import com.v6.yeogaekgi.kiosk.repository.KioskRepository;
import com.v6.yeogaekgi.member.entity.Member;
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
    public KioskResponseDTO addKiosk(KioskRequestDTO kioskRequestDTO, Member member){
        Kiosk kiosk = new Kiosk(
                kioskRequestDTO.getLocation(),
                kioskRequestDTO.getAddress(),
                kioskRequestDTO.getBalance(),
                kioskRequestDTO.getAmount(),
                member
        );
        kioskRepository.save(kiosk);
        return new KioskResponseDTO(kiosk);
    }

    @Transactional
    public List<String> getKioskLists(){
        List<Kiosk>kiosks = kioskRepository.findAll();

        return kiosks.stream()
                .map(Kiosk::getLocation)
                .collect(Collectors.toList());
    }
}
