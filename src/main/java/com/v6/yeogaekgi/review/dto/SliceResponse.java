package com.v6.yeogaekgi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;

import java.sql.Timestamp;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class SliceResponse<T>{

    private List<T> content;
    private int page;
    private int size;
    private boolean hasNext;

    public SliceResponse(List<T> content, Pageable pageable, boolean hasNext) {
        this.content = content;
        //0부터 보여지므로
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();
        this.hasNext = hasNext;
    }

}
