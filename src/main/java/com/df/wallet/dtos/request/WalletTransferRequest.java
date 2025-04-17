package com.df.wallet.dtos.request;

public record WalletTransferRequest(
        String sourceWalletId,
        String destinationWalletId,
        String amount,
        String transactionPin
) {
}
