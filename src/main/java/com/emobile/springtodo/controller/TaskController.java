package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.ExceptionResponse;
import com.emobile.springtodo.dto.PageResponse;
import com.emobile.springtodo.dto.TaskDtoRequest;
import com.emobile.springtodo.dto.TaskDtoResponse;
import com.emobile.springtodo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Создать задачу.",
            description = "Создает новую задачу."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Задача успешно добавлена."),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                                    examples = @ExampleObject(
                                            value = "{" +
                                                    "\"status\": 400," +
                                                    " \"message\": \"Validation failed for argument\"," +
                                                    " \"timestamp\": \"2025-03-22T18:22:08.727092900Z" +
                                                    "\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/add")
    public ResponseEntity<TaskDtoResponse> addTask(
           @Valid @RequestBody TaskDtoRequest taskDtoRequest
    ) {

        TaskDtoResponse taskDtoResponse = taskService.addTask(taskDtoRequest);

        return ResponseEntity.status(201).body(taskDtoResponse);
    }

    @Operation(
            summary = "Получить все задачи.",
            description = "Возвращает все задачи с пагинацией."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Список задач успешно получен."),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных (page или size меньше 1).",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                                    examples = @ExampleObject(
                                            value = "{" +
                                                    "\"status\": 400," +
                                                    " \"message\": \"Page and size must be greater than 0\"," +
                                                    " \"timestamp\": \"2025-03-22T18:22:08.727092900Z" +
                                                    "\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<PageResponse<TaskDtoResponse>> getAllTasks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Page and size must be greater than 0");
        }

        return ResponseEntity.ok(taskService.getAllTasks(page, size));
    }

    @Operation(
            summary = "Получить задачу по ID.",
            description = "Возвращает задачу по нужному ID."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно получена."),
                    @ApiResponse(responseCode = "404", description = "Задача с переданным ID отсутствует.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDtoResponse> getTaskById(
            @PathVariable Long taskId
    ){
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @Operation(
            summary = "Обновить задачу по ID.",
            description = "Обновляет поля задачи на переданные по ID."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена."),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                                    examples = @ExampleObject(
                                            value = "{" +
                                                    "\"status\": 400," +
                                                    " \"message\": \"Validation failed for argument\"," +
                                                    " \"timestamp\": \"2025-03-22T18:22:08.727092900Z" +
                                                    "\"}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Задача с переданным ID отсутствует.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDtoResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskDtoRequest taskDtoRequest
    ){
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDtoRequest));
    }

    @Operation(
            summary = "Удалить задачу по ID.",
            description = "Удаляет задачу по переданному ID."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Задача успешно удалена."),
                    @ApiResponse(responseCode = "404", description = "Задача с переданным ID отсутствует.",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @DeleteMapping("/{taskId}")
    public ResponseEntity<TaskDtoResponse> deleteTask(
            @PathVariable Long taskId
    ){
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

}
