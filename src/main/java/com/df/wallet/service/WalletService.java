package com.df.wallet.service;

import com.df.wallet.dtos.request.FundWalletRequestDTO;
import com.df.wallet.dtos.request.WalletCreateRequestDTO;
import com.df.wallet.dtos.request.WalletTransferRequest;
import com.df.wallet.dtos.request.WalletWithdrawRequest;
import com.df.wallet.dtos.response.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface WalletService {
    ApiResponse<?> createWallet(WalletCreateRequestDTO requestDTO);
    ApiResponse<?> enableWallet(Long walletId);
    ApiResponse<?> withdraw(Long walletId, WalletWithdrawRequest walletWithdrawRequest);
    public ApiResponse<?> getBalance(Long walletId);
    public ApiResponse<?> transfer(WalletTransferRequest walletTransferRequest);
    public ApiResponse<?> getWalletTransactions(Long walletId, Pageable pageable);
    ApiResponse<?> disableWallet(Long walletId);
    ApiResponse<?> setTransactionLimit(Long walletId, BigDecimal limit);
    ApiResponse<?> removeTransactionLimit(Long walletId);
    ApiResponse<?> fundWallet(FundWalletRequestDTO requestDTO);






}
