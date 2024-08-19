package com.v6.yeogaekgi.kiosk.dto;

import com.v6.yeogaekgi.kiosk.entity.Kiosk;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KioskResponseDTO {
    private String location;

    public KioskResponseDTO(Kiosk kiosk){
        this.location = kiosk.getLocation();
    }
}