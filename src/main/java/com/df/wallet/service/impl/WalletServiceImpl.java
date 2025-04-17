package com.df.wallet.service.impl;

import com.df.wallet.TransactionType;
import com.df.wallet.dtos.request.FundWalletRequestDTO;
import com.df.wallet.dtos.request.WalletCreateRequestDTO;
import com.df.wallet.dtos.request.WalletTransferRequest;
import com.df.wallet.dtos.request.WalletWithdrawRequest;
import com.df.wallet.dtos.response.ApiResponse;
import com.df.wallet.dtos.response.TransactionDto;
import com.df.wallet.entities.Transactions;
import com.df.wallet.entities.Wallet;
import com.df.wallet.exception.ApiException;
import com.df.wallet.repositories.TransactionsRepository;
import com.df.wallet.repositories.WalletRepository;
import com.df.wallet.service.WalletService;
import com.df.wallet.utils.AccountNumberGenerator;
import com.df.wallet.utils.MapToTransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionsRepository transactionsRepository;
    private final MapToTransactionDto mapToTransactionDto;

    @Override
    public ApiResponse<?> createWallet(WalletCreateRequestDTO requestDTO) {
        log.info("Attempting to create wallet for userId: {}", requestDTO.getUserId());
        Optional<Wallet> existingTag = walletRepository.findByWalletTag(requestDTO.getWalletTag());

        if (existingTag.isPresent()) {
            log.info("Wallet tag '{}' already exists for userId: {}", requestDTO.getWalletTag(), requestDTO.getUserId());
            throw new ApiException("Wallet tag already exists","05",400);
        }


        Wallet wallet = Wallet.builder()
                .walletName(requestDTO.getWalletName())
                .userId(requestDTO.getUserId())
                .walletTag(requestDTO.getWalletTag())
                .currency(requestDTO.getCurrency())
                .dailyTransactionLimit(requestDTO.getDailyTransactionLimit())
                .transactionPin(requestDTO.getTransactionPin())
                .virtualAccountNumber(AccountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.valueOf(0.00))
                .isEnabled(false)
                .isActive(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        walletRepository.save(wallet);
        log.info("Wallet created successfully for userId: {}", requestDTO.getUserId());
        return ApiResponse.builder()
                .statusCode(201)
                .message("Wallet created successfully")
                .responseCode("01")
                .success(true)
                .data(wallet)
                .build();
    }

    @Override
    public ApiResponse<?> enableWallet(Long walletId) {
        log.info("Attempting to enable wallet with walletId: {}", walletId);
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow( ()-> new ApiException("Wallet not found","05",404));

        if (wallet.isEnabled()) {
            log.info("Wallet with walletId: {} is already enabled", walletId);
            throw new ApiException("Wallet already enabled","05",400);
        }

        wallet.setEnabled(true);
        walletRepository.save(wallet);

        log.info("Wallet with walletId: {} enabled successfully", walletId);
        return ApiResponse.builder()
                .statusCode(200)
                .message("Wallet enabled successfully")
                .responseCode("01")
                .success(true)
                .data(wallet)
                .build();
    }
@Override
    public ApiResponse<?> withdraw(Long walletId, WalletWithdrawRequest withdrawRequest){

    Map<String, Object> responseData = new HashMap<>();

        Wallet walletFromDb = walletRepository.findById(walletId)
                .orElseThrow( ()-> new ApiException("Wallet not found","05",404));

    BigDecimal amountToBigDecimal = BigDecimal.valueOf(Long.parseLong(withdrawRequest.amount()));

    if(!walletFromDb.isEnabled()){
            throw new ApiException("Wallet not enabled for transactions","06",400);
        }
    
    if(walletFromDb.getBalance().compareTo(amountToBigDecimal) < 0){
        throw new ApiException("Insufficient balance","06",400);
    }
    
    if(!Objects.equals(walletFromDb.getTransactionPin(), withdrawRequest.transactionPin())){
        throw new ApiException("Incorrect pin","06",400);
    }
    BigDecimal amountInBigDecimal = amountToBigDecimal;
    BigDecimal walletBalance = walletFromDb.getBalance().subtract(amountInBigDecimal);
        walletFromDb.setBalance(walletBalance);

   Transactions transactions = Transactions.builder()
            .amount(amountInBigDecimal)
            .transactionType(TransactionType.WITHDRAWAL)
            .wallet(walletFromDb)
            .build();

   try {
       walletRepository.save(walletFromDb);
       transactionsRepository.save(transactions);
   } catch (Exception e) {
       throw new ApiException("Withdrawal failed","08",400);
   }

        responseData.put("balance", walletBalance);

        return ApiResponse.builder().
                statusCode(200)
                .message(String.format("%s withdrawn from your wallet", withdrawRequest.amount()))
                .responseCode("01")
                .data(responseData)
                .success(true)
                .build();
    }

    @Override
    public ApiResponse<?> getBalance(Long walletId){

    Map<String, Object> balance = new HashMap<>();
        Wallet walletFromDb = walletRepository.findById(walletId)
                .orElseThrow( ()-> new ApiException("Wallet not found","05",404));

        balance.put("balance", walletFromDb.getBalance());
        return ApiResponse.builder()
                .statusCode(200)
                .message("Data retrieved successfully")
                .responseCode("01")
                .success(true)
                .data(balance)
                .build();
}
@Override
public ApiResponse<?> transfer(WalletTransferRequest walletTransferRequest){
        Map<String, Object> response = new HashMap<>();

    Wallet wallet1FromDb = walletRepository.findById(Long.valueOf(walletTransferRequest.sourceWalletId())).orElseThrow( ()-> new ApiException("Source wallet not found","05",404));
    Wallet wallet2FromDb = walletRepository.findById(Long.valueOf(walletTransferRequest.destinationWalletId())).orElseThrow( ()-> new ApiException("Destination wallet not found","05",404));
    BigDecimal amountToBigDecimal = BigDecimal.valueOf(Long.parseLong(walletTransferRequest.amount()));
    if(wallet1FromDb.getBalance().compareTo(amountToBigDecimal) < 0){
        throw new ApiException("Insufficient balance","06",400);
    }
    if(!Objects.equals(wallet1FromDb.getTransactionPin(), walletTransferRequest.transactionPin())){
        throw new ApiException("Incorrect pin","06",400);
    }
    if(wallet1FromDb.isEnabled()){
        throw new ApiException("Wallet not enabled for transactions","06",400);
    }
    wallet2FromDb.setBalance(amountToBigDecimal);
    Transactions transaction1 = Transactions
            .builder()
            .wallet(wallet1FromDb)
            .transactionType(TransactionType.TRANSFER)
            .sourceWalletId(walletTransferRequest.sourceWalletId())
            .destinationWalletId(walletTransferRequest.destinationWalletId())
            .amount(amountToBigDecimal)
            .build();
    Transactions transaction2 = Transactions
            .builder()
            .wallet(wallet2FromDb)
            .transactionType(TransactionType.TRANSFER)
            .sourceWalletId(walletTransferRequest.sourceWalletId())
            .destinationWalletId(walletTransferRequest.destinationWalletId())
            .amount(amountToBigDecimal)
            .build();
    try {
        walletRepository.save(wallet2FromDb);
        transactionsRepository.save(transaction1);
        transactionsRepository.save(transaction2);

    } catch (Exception e) {
        throw new ApiException("Transaction failed","07",400);
    }

    response.put("amount", walletTransferRequest.amount());
    return new ApiResponse<>(200, "Transfer successful", "01", true,response);
}
@Override
public ApiResponse<?> getWalletTransactions(Long walletId, Pageable pageable){
    Wallet walletFromDb = walletRepository.findById(walletId).orElseThrow( ()-> new ApiException("Wallet not found","05",404));
    Page<Transactions> transactions = transactionsRepository.findByWalletId(walletId, pageable);
    Page<TransactionDto> transactionDtos = transactions.map(mapToTransactionDto::transactionDto);

    return new ApiResponse<>(200, "Successful","01", true,transactionDtos);

}


    @Override
    public ApiResponse<?> disableWallet(Long walletId) {
        log.info("Attempting to disable wallet with walletId: {}", walletId);

        Wallet wallet = walletRepository.findById(walletId)
                .orElse(null);

        if (wallet == null) {
            log.info("Wallet with walletId: {} not found", walletId);
            return ApiResponse.builder()
                    .statusCode(404)
                    .message("Wallet not found")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        if (!wallet.isEnabled()) {
            log.info("Wallet with walletId: {} is already disabled", walletId);
            return ApiResponse.builder()
                    .statusCode(400)
                    .message("Wallet is already disabled")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        wallet.setEnabled(false);
        walletRepository.save(wallet);

        log.info("Wallet with walletId: {} disabled successfully", walletId);
        return ApiResponse.builder()
                .statusCode(200)
                .message("Wallet disabled successfully")
                .responseCode("01")
                .success(true)
                .data(wallet)
                .build();
    }

    @Override
    public ApiResponse<?> setTransactionLimit(Long walletId, BigDecimal newLimit) {
        log.info("Setting new transaction limit for walletId: {} to {}", walletId, newLimit);

        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet == null) {
            return ApiResponse.builder()
                    .statusCode(404)
                    .message("Wallet not found")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        wallet.setDailyTransactionLimit(newLimit);
        walletRepository.save(wallet);

        return ApiResponse.builder()
                .statusCode(200)
                .message("Transaction limit updated successfully")
                .responseCode("00")
                .success(true)
                .data(wallet)
                .build();
    }

    @Override
    public ApiResponse<?> removeTransactionLimit(Long walletId) {
        log.info("Removing transaction limit for walletId: {}", walletId);

        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet == null) {
            return ApiResponse.builder()
                    .statusCode(404)
                    .message("Wallet not found")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        wallet.setDailyTransactionLimit(null); // Remove the limit
        walletRepository.save(wallet);

        return ApiResponse.builder()
                .statusCode(200)
                .message("Transaction limit removed successfully")
                .responseCode("00")
                .success(true)
                .data(wallet)
                .build();
    }

    @Override
    public ApiResponse<?> fundWallet(FundWalletRequestDTO requestDTO) {
        log.info("Funding walletId: {} with amount: {}", requestDTO.getWalletId(), requestDTO.getAmount());

        Wallet wallet = walletRepository.findById(requestDTO.getWalletId()).orElse(null);
        if (wallet == null) {
            return ApiResponse.builder()
                    .statusCode(404)
                    .message("Wallet not found")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        if (!wallet.isEnabled()) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .message("Wallet is not enabled")
                    .responseCode("05")
                    .success(false)
                    .build();
        }

        BigDecimal currentBalance = wallet.getBalance() != null ? wallet.getBalance() : BigDecimal.ZERO;
        wallet.setBalance(currentBalance.add(requestDTO.getAmount()));
        walletRepository.save(wallet);

        return ApiResponse.builder()
                .statusCode(200)
                .message("Wallet funded successfully")
                .responseCode("00")
                .success(true)
                .data(wallet)
                .build();
    }



}
