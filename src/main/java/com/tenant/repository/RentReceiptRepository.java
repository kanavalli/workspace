package com.tenant.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tenant.model.RentReceipt;

public interface RentReceiptRepository extends JpaRepository<RentReceipt, Long> {

	@Query(value = "select id,amount,tenant,createdDate from RentReceipt b where b.tenant=?1 ")
	List<RentReceipt> findByTenant(Integer tenant);

	@Query(value = "select id,amount,tenant,createdDate from RentReceipt b where b.createdDate>?1 and b.createdDate<?2")
	List<RentReceipt> findTenantByTime(LocalDateTime time1, LocalDateTime time2);

}
