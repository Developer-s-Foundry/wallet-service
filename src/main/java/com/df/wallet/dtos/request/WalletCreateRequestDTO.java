package com.df.wallet.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletCreateRequestDTO {


    @NotBlank(message = "Wallet name is required")
    private String walletName;

    @NotBlank(message = "Wallet tag is required")
    private String walletTag;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "Daily transaction limit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily transaction limit must be positive")
    private BigDecimal dailyTransactionLimit;

    @NotEmpty(message = "Pin is mandatory")
    @NotBlank(message = "Pin is mandatory")
    @Size(min = 4, max = 4,message = "Pin should be 4 characters long")
    private String transactionPin;
}
