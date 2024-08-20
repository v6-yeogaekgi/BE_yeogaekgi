package com.v6.yeogaekgi.payTrack.repository;

import java.util.List;

public interface PayTrackQueryRepository {
    List<Object[]> findPayTrackByUserCardNo(Long userCardNo, int year, int month);
}
