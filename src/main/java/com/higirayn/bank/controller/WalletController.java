package com.higirayn.bank.controller;

import com.higirayn.bank.dto.WalletRequest;
import com.higirayn.bank.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "WalletController",
        description = "Класс контроллер предоставляющий операции пополнения, снятия и получения баланса кошелька")
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @Operation(summary = "Пополнить баланс, или снять деньги с кошелька",
            parameters = {
                @Parameter(name = "walletRequest",
                        description = "Объект содержащий id кошелька, требуемую операцию, сумму для изменения",
                        required = true,
                        example = "{\n" +
                                "    \"walletId\":\"97D253C1-111A-4BDD-8621-B6332DC958D1\",\n" +
                                "    \"operationType\": \"WITHDRAW\",\n" +
                                "    \"amount\": 500\n" +
                                "}")
            })
    @PostMapping
    public ResponseEntity<Void> updateBalance(@RequestBody WalletRequest walletRequest) {
        return walletService.updateBalance(walletRequest);

    }

    @Operation(summary = "Получить баланс кошелька",
            parameters = {
                @Parameter(name = "walletId",
                        description = "id кошелька",
                        required = true,
                        example = "97D253C1-111A-4BDD-8621-B6332DC958D1")
            })
    @GetMapping("/{walletId}")
    public ResponseEntity<Long> getBalance(@PathVariable UUID walletId) {
        return ResponseEntity.ok(walletService.getBalance(walletId));
    }
}
