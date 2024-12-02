package com.higirayn.bank.service;

import com.higirayn.bank.dto.WalletRequest;
import com.higirayn.bank.entity.Wallet;
import com.higirayn.bank.exception.InsufficientBalanceException;
import com.higirayn.bank.exception.WalletNotFoundException;
import com.higirayn.bank.dto.OperationType;
import com.higirayn.bank.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service providing the logic of work with the wallet balance
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {
    private final WalletRepository walletRepository;

    /**
     * Method responsible for rebalancing
     * @param walletRequest query containing the operation data
     * @return returns the response status
     */
    @Transactional
    public ResponseEntity<Void> updateBalance(WalletRequest walletRequest) {

        synchronized (this) {
            try {
                Wallet wallet = walletRepository.findById(walletRequest.getWalletId()).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
                if (walletRequest.getOperationType() == OperationType.WITHDRAW && wallet.getBalance() < walletRequest.getAmount()) {
                    throw new InsufficientBalanceException("Insufficient funds");
                }
                if (walletRequest.getOperationType() == OperationType.DEPOSIT) {
                    wallet.setBalance(wallet.getBalance() + walletRequest.getAmount());
                } else if (walletRequest.getOperationType() == OperationType.WITHDRAW) {
                    wallet.setBalance(wallet.getBalance() - walletRequest.getAmount());
                }
                walletRepository.save(wallet);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Method responsible for rebalancing
     * @param walletId wallet identifier
     * @return returns a balance
     */
    @Transactional(readOnly = true)
    public Long getBalance(UUID walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found")).getBalance();
    }
}