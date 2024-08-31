package com.v6.yeogaekgi.util.PageDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class PageResultDTO<T>{

    private List<T> content;
    private int page;
    private int size;
    private boolean hasNext;

    public PageResultDTO(List<T> content, Pageable pageable, boolean hasNext) {
        this.content = content;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.hasNext = hasNext;

    }

}