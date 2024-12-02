package com.higirayn.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Response класс для обработки ошибок")
@Getter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "Поле отражающее статус ответа", example = "HttpStatus.NOT_FOUND")
    private int status;
    @Schema(description = "Текст ошибки", example = "Wallet not found")
    private String message;
    @Schema(description = "Время возникновения ошибки", example = "1732122552382")
    private long timestamp;
}
