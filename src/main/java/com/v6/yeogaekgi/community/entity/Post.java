package com.v6.yeogaekgi.community.entity;

import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post extends BaseEntity {
}
