package com.v6.yeogaekgi.repository.kioskTest;

import com.v6.yeogaekgi.kiosk.entity.Kiosk;
import com.v6.yeogaekgi.kiosk.repository.KioskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class KioskRepositoryTests {
    @Autowired
    private KioskRepository kioskRepository;

    @Test
    void testKioskRepository() {
        Kiosk kiosk1 = new Kiosk("홍대입구역 4번출구", "서울시 마포구", 0, 0);
        Kiosk kiosk2 = new Kiosk("홍대입구역 1번출구", "서울시 마포구", 0, 0);
        Kiosk kiosk3 = new Kiosk("홍대입구역 8번출구", "서울시 마포구", 0, 0);
        Kiosk kiosk4 = new Kiosk("홍대입구역 9번출구", "서울시 마포구", 0, 0);
        Kiosk kiosk5 = new Kiosk("홍대입구역 11번출구", "서울시 마포구", 0, 0);
        Kiosk kiosk6 = new Kiosk("이태원역 2번출구", "서울시 용산구",0, 0);
        Kiosk kiosk7 = new Kiosk("이태원역 3번출구", "서울시 용산구",0, 0);
        Kiosk kiosk8 = new Kiosk("이태원역 4번출구", "서울시 용산구",0, 0);
        Kiosk kiosk9 = new Kiosk("이태원역 5번출구", "서울시 용산구",0, 0);
        Kiosk kiosk10 = new Kiosk("이태원역 6번출구", "서울시 용산구",0, 0);
        kioskRepository.save(kiosk1);
        kioskRepository.save(kiosk2);
        kioskRepository.save(kiosk3);
        kioskRepository.save(kiosk4);
        kioskRepository.save(kiosk5);
        kioskRepository.save(kiosk6);
        kioskRepository.save(kiosk7);
        kioskRepository.save(kiosk8);
        kioskRepository.save(kiosk9);
        kioskRepository.save(kiosk10);

        List<Kiosk> kiosks = kioskRepository.findAll();

    }
}
