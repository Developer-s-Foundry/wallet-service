package com.df.wallet.repositories;

import com.df.wallet.entities.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions,Long> {
    Page<Transactions> findByWalletId(Long walletId, Pageable pageable);

}
