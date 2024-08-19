package com.v6.yeogaekgi.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KioskRequestDTO {
    private String location;
    private String address;
    private int balance;
    private int amount;
}
