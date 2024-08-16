package com.v6.yeogaekgi.kiosk.repository;

import com.v6.yeogaekgi.kiosk.entity.Kiosk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KioskRepository extends JpaRepository<Kiosk,Long> {
}
