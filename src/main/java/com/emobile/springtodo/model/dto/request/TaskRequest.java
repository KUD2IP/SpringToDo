package com.emobile.springtodo.model.dto.request;

import com.emobile.springtodo.model.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest implements Serializable {

    @Size(max = 50, message = "Длина названия должна составлять не более 50 символов")
    @Schema(description = "Название задачи.", example = "Сделать зарядку", maxLength = 25)
    private String title;

    @Size(max = 1000, message = "Длина описания должна составлять не более 1000 символов")
    @Schema(description = "Описание задачи.", example = "Растяжка, медитация и приседания", maxLength = 1000)
    private String description;

    @Schema(description = "Статус задачи.")
    private Status status;
}
