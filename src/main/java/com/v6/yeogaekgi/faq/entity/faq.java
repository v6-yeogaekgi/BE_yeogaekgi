package com.v6.yeogaekgi.faq.entity;

import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "faq")
public class faq extends BaseEntity {
}
