package com.df.wallet.controller;

import com.df.wallet.dtos.request.FundWalletRequestDTO;
import com.df.wallet.dtos.request.WalletCreateRequestDTO;
import com.df.wallet.dtos.request.WalletTransferRequest;
import com.df.wallet.dtos.request.WalletWithdrawRequest;
import com.df.wallet.dtos.response.ApiResponse;
import com.df.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create-wallet")
    public ApiResponse<?> createWallet(@RequestBody @Valid WalletCreateRequestDTO requestDTO) {
        return walletService.createWallet(requestDTO);
    }

    @PutMapping("/enable/{walletId}")
    public ApiResponse<?> enableWallet(@PathVariable Long walletId) {

        return walletService.enableWallet(walletId);
    }

    @PostMapping("/withdraw/{walletId}")
    public ApiResponse<?> withdraw(@PathVariable Long walletId, @RequestBody WalletWithdrawRequest walletWithdrawRequest){
        return walletService.withdraw(walletId, walletWithdrawRequest);
    }

    @GetMapping("/balance/{walletId}")
    public ApiResponse<?> balance(@PathVariable Long walletId){
        return walletService.getBalance(walletId);
    }

    @PostMapping("/transfer")
    public ApiResponse<?> withdraw( WalletTransferRequest walletTransferRequest){
        return walletService.transfer(walletTransferRequest);
    }

    @GetMapping("/get-wallet-transactions/{walletId}")
    public ApiResponse<?> getWalletTransactions( @PathVariable Long walletId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return walletService.getWalletTransactions(walletId, pageable);
    }

    @PutMapping("/disable/{walletId}")
    public ApiResponse<?> disableWallet(@PathVariable Long walletId) {

        return walletService.disableWallet(walletId);
    }

    @PutMapping("/limit/{walletId}")
    public ApiResponse<?> setTransactionLimit(
            @PathVariable Long walletId,
            @RequestParam BigDecimal limit
    ) {
        return walletService.setTransactionLimit(walletId, limit);
    }

    @PutMapping("/limit/remove/{walletId}")
    public ApiResponse<?> removeTransactionLimit(@PathVariable Long walletId) {
        return walletService.removeTransactionLimit(walletId);
    }

    @PostMapping("/fund")
    public ApiResponse<?> fundWallet(@RequestBody FundWalletRequestDTO requestDTO) {
        return walletService.fundWallet(requestDTO);
    }

}
