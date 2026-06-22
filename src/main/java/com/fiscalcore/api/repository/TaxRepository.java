package com.fiscalcore.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiscalcore.api.model.Tax;

public interface TaxRepository extends JpaRepository<Tax, Long> {
}
