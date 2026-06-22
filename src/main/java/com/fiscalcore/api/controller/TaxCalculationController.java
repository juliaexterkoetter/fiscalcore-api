package com.fiscalcore.api.controller;

import com.fiscalcore.api.dto.TaxCalculationRequestDTO;
import com.fiscalcore.api.dto.TaxCalculationResponseDTO;
import com.fiscalcore.api.model.Tax;
import com.fiscalcore.api.service.TaxService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/tax-calculations")
public class TaxCalculationController {

    private final TaxService taxService;

    public TaxCalculationController(TaxService taxService) {
        this.taxService = taxService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public TaxCalculationResponseDTO calculateTax(@RequestBody @Valid TaxCalculationRequestDTO request) {
        Tax tax = taxService.findById(request.getTaxId());
        BigDecimal taxAmount = taxService.calculateTax(request.getTaxId(), request.getBaseAmount());
        return new TaxCalculationResponseDTO(
                tax.getName(),
                request.getBaseAmount(),
                tax.getRate(),
                taxAmount
        );
    }
}
