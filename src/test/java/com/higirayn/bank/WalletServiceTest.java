package com.higirayn.bank;

import com.higirayn.bank.dto.WalletRequest;
import com.higirayn.bank.entity.Wallet;
import com.higirayn.bank.exception.InsufficientBalanceException;
import com.higirayn.bank.exception.WalletNotFoundException;
import com.higirayn.bank.dto.OperationType;
import com.higirayn.bank.repository.WalletRepository;
import com.higirayn.bank.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @InjectMocks
    private WalletService walletService;
    private final UUID walletId = UUID.randomUUID();
    private Wallet getWallet() {
        return new Wallet(walletId, 1000L);
    }

    @Test
    void testDepositSuccessfully() {
        Wallet wallet = getWallet();
        WalletRequest  walletRequest = new WalletRequest(walletId, OperationType.DEPOSIT, 500L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        walletService.updateBalance(walletRequest);

        assertEquals(1500L, wallet.getBalance());
    }

    @Test
    void testWithdrawSuccessfully() {
        Wallet wallet = getWallet();
        WalletRequest  walletRequest = new WalletRequest(walletId, OperationType.WITHDRAW, 500L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        walletService.updateBalance(walletRequest);

        assertEquals(500L, wallet.getBalance());
    }

    @Test
    void testWalletNotFoundException() {
        WalletRequest  walletRequest = new WalletRequest(walletId, OperationType.DEPOSIT, 1000L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, ()-> walletService.updateBalance(walletRequest));
    }
//
    @Test
    void testInsufficientBalanceException() {
        Wallet wallet = getWallet();
        WalletRequest  walletRequest = new WalletRequest(walletId, OperationType.WITHDRAW, 5000L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        assertThrows(InsufficientBalanceException.class, ()-> walletService.updateBalance(walletRequest));
    }

    @Test
    void testConcurrentTransaction() throws InterruptedException {
        Wallet wallet = getWallet();
        wallet.setBalance(0L);
        WalletRequest  walletRequest = new WalletRequest(walletId, OperationType.DEPOSIT, 5L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Runnable withdraw = () -> walletService.updateBalance(walletRequest);
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        for(int i = 0; i < 1000; i++) {
            executor.submit(withdraw);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(5000L, wallet.getBalance());
        verify(walletRepository, times(1000)).save(wallet);
    }
}


























