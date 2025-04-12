package com.df.wallet.service;

import com.df.wallet.dtos.request.WalletCreateRequestDTO;
import com.df.wallet.dtos.response.ApiResponse;

public interface WalletService {
    ApiResponse<?> createWallet(WalletCreateRequestDTO requestDTO);
    ApiResponse<?> enableWallet(Long walletId);
}
