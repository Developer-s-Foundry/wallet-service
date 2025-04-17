package com.df.wallet.service;

import com.df.wallet.dtos.request.WalletCreateRequestDTO;
import com.df.wallet.dtos.request.WalletTransferRequest;
import com.df.wallet.dtos.request.WalletWithdrawRequest;
import com.df.wallet.dtos.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface WalletService {
    ApiResponse<?> createWallet(WalletCreateRequestDTO requestDTO);
    ApiResponse<?> enableWallet(Long walletId);
    ApiResponse<?> withdraw(Long walletId, WalletWithdrawRequest walletWithdrawRequest);
    public ApiResponse<?> getBalance(Long walletId);
    public ApiResponse<?> transfer(WalletTransferRequest walletTransferRequest);
    public ApiResponse<?> getWalletTransactions(Long walletId, Pageable pageable);

    }
