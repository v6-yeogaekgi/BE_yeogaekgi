package com.v6.yeogaekgi.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    private String code;
    private String name;


}
