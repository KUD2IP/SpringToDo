package com.emobile.springtodo.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {

    private Long id;

    private String title;
    private String description;
    private Status status;
    private LocalDateTime createdAt;

}
