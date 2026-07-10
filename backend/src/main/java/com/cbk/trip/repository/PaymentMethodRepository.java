package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.PaymentMethod;
import com.cbk.trip.enums.Status;

/**
 * * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>, JpaSpecificationExecutor<PaymentMethod> {

    List<PaymentMethod> findByStatus(Status status);

    Optional<PaymentMethod> findByAccountNumber(String accountNumber);

    Optional<PaymentMethod> findByAccountNumberAndIdNot(String accountNumber, Long id);

}