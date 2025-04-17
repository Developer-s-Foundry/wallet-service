package com.df.wallet.dtos.request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class FundWalletRequestDTO {
    private Long walletId;
    private BigDecimal amount;

}
