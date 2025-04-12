package com.df.wallet.repositories;

import com.df.wallet.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByWalletTag(String walletTag);
    boolean existsByVirtualAccountNumber(String virtualAccountNumber);
}
