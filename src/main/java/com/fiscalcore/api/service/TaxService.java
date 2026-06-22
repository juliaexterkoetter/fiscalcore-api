package com.fiscalcore.api.service;

import com.fiscalcore.api.exception.ResourceNotFoundException;
import com.fiscalcore.api.model.Tax;
import com.fiscalcore.api.repository.TaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TaxService {

    private static final Logger logger = LoggerFactory.getLogger(TaxService.class);

    private final TaxRepository taxRepository;

    public TaxService(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    public List<Tax> findAll() {
        logger.info("Listing all taxes");
        return taxRepository.findAll();
    }

    public Tax findById(Long id) {
        logger.info("Finding tax with ID: {}", id);
        return taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found with ID: " + id));
    }

    public Tax create(Tax tax) {
        logger.info("Creating tax: {}", tax.getName());
        return taxRepository.save(tax);
    }

    public void deleteById(Long id) {
        logger.info("Deleting tax with ID: {}", id);
        if (!taxRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tax not found with ID: " + id);
        }
        taxRepository.deleteById(id);
    }

    public BigDecimal calculateTax(Long taxId, BigDecimal baseAmount) {
        logger.info("Calculating tax for ID: {} with base amount: {}", taxId, baseAmount);
        Tax tax = findById(taxId);
        return baseAmount.multiply(tax.getRate())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
