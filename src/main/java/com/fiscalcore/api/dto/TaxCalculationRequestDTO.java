package com.fiscalcore.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TaxCalculationRequestDTO {

    @NotNull(message = "Tax ID is required")
    private Long taxId;

    @NotNull(message = "Base amount is required")
    @DecimalMin(value = "0.01", message = "Base amount must be greater than or equal to 0.01")
    private BigDecimal baseAmount;

    public TaxCalculationRequestDTO() {}

    public TaxCalculationRequestDTO(Long taxId, BigDecimal baseAmount) {
        this.taxId = taxId;
        this.baseAmount = baseAmount;
    }

    public Long getTaxId() { return taxId; }
    public void setTaxId(Long taxId) { this.taxId = taxId; }

    public BigDecimal getBaseAmount() { return baseAmount; }
    public void setBaseAmount(BigDecimal baseAmount) { this.baseAmount = baseAmount; }
}
