package com.df.wallet.utils;

import com.df.wallet.dtos.response.TransactionDto;
import com.df.wallet.entities.Transactions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MapToTransactionDto {
    public TransactionDto transactionDto(Transactions transactions){
        return TransactionDto.builder()
                .transactionType(transactions.getTransactionType())
                .amount(transactions.getAmount())
                .sourceWalletId(transactions.getSourceWalletId())
                .destinationWalletId(transactions.getDestinationWalletId())
                .build();
    }
}
