package com.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tenant.model.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

}