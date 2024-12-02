package com.higirayn.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Schema(description = "DTO request класс для кошелька")
@AllArgsConstructor
@Getter
public class WalletRequest {
    @Schema(description = "Id кошелька", example ="97D253C1-111A-4BDD-8621-B6332DC958D1")
    private UUID walletId;
    @Schema(description = "Поле предоставляющее тип операции", example = "DEPOSIT")
    private OperationType operationType;
    @Schema(description = "Поле содержащее сумму для изменения баланса", example = "1000")
    private Long amount;
}
