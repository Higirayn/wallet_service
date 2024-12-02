package com.higirayn.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "enum класс для операций снятия и пополнения баланса")
public enum OperationType {
    DEPOSIT, WITHDRAW
}
