package com.v6.yeogaekgi.util.DeepL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Translation {

    private String detected_source_language;
    private String text;
}
