package com.emobile.springtodo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "ToDo Spring API",
                version = "1.0",
                description = "API для управления задачами",
                contact = @Contact(
                        name = "Сторожев Иван",
                        url = "https://t.me/kudziP"
                )
        ),
        servers = @Server(
                url = "http://localhost:8080",
                description = "локальный сервер"
        )


)
public class OpenApiConfig {
}
