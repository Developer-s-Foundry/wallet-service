package com.df.wallet.controller;

import com.df.wallet.dtos.request.WalletCreateRequestDTO;
import com.df.wallet.dtos.response.ApiResponse;
import com.df.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ApiResponse<?> createWallet(@RequestBody @Valid WalletCreateRequestDTO requestDTO) {
        return walletService.createWallet(requestDTO);
    }

    @PutMapping("/enable/{walletId}")
    public ApiResponse<?> enableWallet(@RequestHeader Long walletId) {

        return walletService.enableWallet(walletId);
    }
}
