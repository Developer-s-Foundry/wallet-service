package com.df.wallet.service.impl;

import com.df.wallet.dtos.request.WalletCreateRequestDTO;
import com.df.wallet.dtos.response.ApiResponse;
import com.df.wallet.entities.Wallet;
import com.df.wallet.repositories.WalletRepository;
import com.df.wallet.service.WalletService;
import com.df.wallet.utils.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public ApiResponse<?> createWallet(WalletCreateRequestDTO requestDTO) {
        Optional<Wallet> existingTag = walletRepository.findByWalletTag(requestDTO.getWalletTag());
        if (existingTag.isPresent()) {
            return ApiResponse.builder()
                    .statusCode(409)
                    .message("Wallet tag already exists")
                    .responseCode("05")
                    .success(false)
                    .build();
        }


        Wallet wallet = Wallet.builder()
                .walletName(requestDTO.getWalletName())
                .userId(requestDTO.getUserId())
                .walletTag(requestDTO.getWalletTag())
                .currency(requestDTO.getCurrency())
                .dailyTransactionLimit(requestDTO.getDailyTransactionLimit())
                .transactionPin(requestDTO.getTransactionPin())
                .virtualAccountNumber(AccountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.valueOf(0.00))
                .isEnabled(false)
                .isActive(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        walletRepository.save(wallet);

        return ApiResponse.builder()
                .statusCode(201)
                .message("Wallet created successfully")
                .responseCode("01")
                .success(true)
                .data(wallet)
                .build();
    }

    @Override
    public ApiResponse<?> enableWallet(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElse(null);

        if (wallet == null) {
            return ApiResponse.builder()
                    .statusCode(404)
                    .message("Wallet not found")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        if (wallet.isEnabled()) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .message("Wallet is already enabled")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        wallet.setEnabled(true);
        walletRepository.save(wallet);

        return ApiResponse.builder()
                .statusCode(200)
                .message("Wallet enabled successfully")
                .responseCode("01")
                .success(true)
                .data(wallet)
                .build();
    }
}
