package com.df.wallet.dtos.response;

import com.df.wallet.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
public class TransactionDto {
    private String sourceWalletId;
    private String destinationWalletId;
    private BigDecimal amount;
    private TransactionType transactionType;
}
