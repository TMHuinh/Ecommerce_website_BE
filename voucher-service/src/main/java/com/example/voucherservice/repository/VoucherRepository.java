package com.example.voucherservice.repository;

import com.example.voucherservice.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    List<Voucher> findByFreeShipTrue();

    List<Voucher> findAllByQuantityGreaterThanAndExpirationDateGreaterThanEqual(int quantity, LocalDate expirationDate);
}
