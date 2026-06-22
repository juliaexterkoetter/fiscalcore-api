package com.fiscalcore.api.dto;

import java.math.BigDecimal;

public class TaxCalculationResponseDTO {

    private String taxName;
    private BigDecimal baseAmount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;

    public TaxCalculationResponseDTO() {}

    public TaxCalculationResponseDTO(String taxName, BigDecimal baseAmount, BigDecimal taxRate, BigDecimal taxAmount) {
        this.taxName = taxName;
        this.baseAmount = baseAmount;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
    }

    public String getTaxName() { return taxName; }
    public void setTaxName(String taxName) { this.taxName = taxName; }

    public BigDecimal getBaseAmount() { return baseAmount; }
    public void setBaseAmount(BigDecimal baseAmount) { this.baseAmount = baseAmount; }

    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
}
