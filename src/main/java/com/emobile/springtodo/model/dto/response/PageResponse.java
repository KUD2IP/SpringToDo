package com.emobile.springtodo.model.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {

    @Schema(description = "Список задач.")
    private List<T> tasks;

    @Schema(description = "Номер страницы.", example = "1", minimum = "1")
    private int page;

    @Schema(description = "Число задач на 1 странице.", example = "1", minimum = "1")
    private int size;

}
