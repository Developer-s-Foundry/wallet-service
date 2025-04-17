package com.df.wallet.dtos.request;

public record WalletWithdrawRequest(
        String amount,
        String transactionPin
) {
}
