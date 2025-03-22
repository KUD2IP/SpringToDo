package com.emobile.springtodo.dto;

import com.emobile.springtodo.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoResponse implements Serializable {

    @Schema(description = "Уникальный идентификатор задачи.", example = "1")
    private Long id;

    @Schema(description = "Название задачи.", example = "Сделать уборку")
    private String title;

    @Schema(description = "Описание задачи.", example = "Растяжка, медитация и приседания")
    private String description;

    @Schema(description = "Статус задачи.")
    private Status status;

    @Schema(description = "Дата создания задачи.", example = "2025-03-22T18:22:08.727092900")
    private String createdAt;
}
